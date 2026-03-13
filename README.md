# 📊 Shift 채팅 시스템 성능 개선 프로젝트

## 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [현재 상태 분석](#현재-상태-분석)
3. [성능 개선 목표](#성능-개선-목표)
4. [개선 전략](#개선-전략)
5. [단계별 개선 계획](#단계별-개선-계획)
6. [예상 효과](#예상-효과)

---

## 프로젝트 개요

실시간 채팅 기능을 핵심으로 하는 이커머스 플랫폼 **Shift**의 채팅 시스템 성능을 최적화하는 프로젝트입니다.

**기술 스택:**
- Backend: Spring Boot, JPA/Hibernate, WebSocket (STOMP)
- Database: Oracle
- Cache: Redis

---

## 현재 상태 분석

### 🔍 발견된 주요 문제

#### 1. N+1 쿼리 문제 (심각)

**발생 위치:** 채팅방 목록 조회 시 안읽은 메시지 개수를 각각 조회

**영향:**
- 채팅방 10개 조회 → **11개 쿼리 실행** (1개 목록 + 10개 카운트)
- 채팅방이 많을수록 성능 급격히 저하

<details>
<summary>문제 코드 위치</summary>

```
ChatroomService.java - chatroomListDTOBuilder()
ChatroomService.java - MessageSearchResultDTOBuilder()
ChatroomUserService.java - getChatroomListView()
```
</details>

#### 2. JPA 연관관계 미설정

**현재 상태:**
- 모든 FK를 Long 타입으로만 저장
- 연관 엔티티 조회 시 별도 쿼리 필요

**문제점:**
- Fetch Join 불가능
- Lazy Loading 불가능
- 객체 지향적 설계 미활용

#### 3. 복잡한 Native Query 사용

**현황:**
- 대부분의 Repository 쿼리가 Native Query
- 복잡한 JOIN과 서브쿼리 남발

**문제점:**
- 유지보수 어려움
- 데이터베이스 종속성
- 가독성 저하

---

## 성능 개선 목표

| 항목 | 현재 | 목표 | 개선율 |
|------|------|------|--------|
| **채팅방 목록 조회 쿼리** | 11개 (10개 채팅방) | 1개 | **90%↓** |
| **응답 시간 (목록)** | 300ms+ | 5ms (캐시) | **98%↓** |
| **응답 시간 (메시지)** | 100ms | 50ms | **50%↓** |
| **DB 커넥션 사용** | 높음 | 매우 낮음 | **80%↓** |

---

## 개선 전략

### 4단계 접근법

```
1단계: JPA 연관관계 설정
   ↓
2단계: Repository 쿼리 최적화  
   ↓
3단계: 인덱스 추가
   ↓
4단계: Redis 캐싱 적용
```

### 핵심 원칙

1. **점진적 개선** - 한 단계씩 테스트 후 다음 단계 진행
2. **하위 호환성 유지** - 기존 API 스펙 변경 최소화
3. **성능 측정** - 각 단계별 성능 개선 수치 확인

---

## 단계별 개선 계획

### 1단계: JPA 연관관계 설정

#### 목표
모든 Entity에 적절한 JPA 연관관계를 추가하여 Fetch Join과 Lazy Loading 활용 기반 마련

#### 주요 작업

**연관관계 매핑:**
- Message ↔ User: `@ManyToOne`
- Message ↔ Chatroom: `@ManyToOne`
- Chatroom ↔ ChatroomUser: `@OneToMany` (양방향)
- ChatroomUser ↔ User: `@ManyToOne`
- ChatroomUser ↔ Chatroom: `@ManyToOne`
- Friend ↔ User: `@ManyToOne` (두 개 - user, friend)

#### 수정 파일
- `MessageEntity.java`
- `ChatroomEntity.java`
- `ChatroomUserEntity.java`
- `ChatUserEntity.java`
- `FriendEntity.java`
- `ReplyEmoticonEntity.java`

#### 주의사항
- 모든 연관관계는 `FetchType.LAZY` 설정
- 양방향 매핑 시 `mappedBy` 명확히 지정
- 순환 참조 방지를 위한 `@JsonIgnore` 고려

---

### 2단계: Repository 쿼리 최적화

#### 2-1. 안읽은 메시지 카운트 Batch 조회

**현재 문제:**
- 각 채팅방마다 개별 쿼리로 카운트 조회
- 채팅방 N개 → N번 쿼리 실행

**개선 방안:**
- 한 번의 GROUP BY 쿼리로 모든 채팅방 카운트 일괄 조회
- Map으로 결과 저장 후 매핑

**수정 파일:**
- `MessageRepository.java` - Batch 카운트 메서드 추가
- `ChatroomDAO.java` - Batch 조회 메서드 추가
- `ChatroomService.java` - DTO 빌더 로직 수정

**효과:**
- 10개 쿼리 → **1개 쿼리**

#### 2-2. Native Query → JPQL 전환

**개선 대상:**
- `ChatroomRepository.findChatroomsByUserId()`
- `ChatroomRepository.findChatroomUsersBySearchInput()`
- 기타 복잡한 Native Query들

**개선 방향:**
- JPQL + Fetch Join 활용
- Projection Interface 대신 DTO Projection 고려

**수정 파일:**
- `ChatroomRepository.java`
- `ChatroomUserRepository.java`
- `MessageRepository.java`

**효과:**
- 가독성 향상
- 유지보수 용이
- 데이터베이스 독립성

---

### 3단계: 인덱스 추가

#### 추가할 인덱스

```sql
-- 메시지 조회 최적화
CREATE INDEX idx_message_chatroom_date 
ON MESSAGES(CHATROOM_ID, SEND_DATE);

-- 안읽은 메시지 카운트 최적화  
CREATE INDEX idx_message_user_date
ON MESSAGES(CHATROOM_ID, USER_ID, SEND_DATE);

-- 채팅방 유저 조회 최적화
CREATE INDEX idx_chatroom_user_status 
ON CHATROOM_USERS(USER_ID, CONNECTION_STATUS);

-- 친구 목록 조회 최적화
CREATE INDEX idx_friend_user
ON FRIENDS(USER_ID, FRIEND_ID);
```

#### 효과
- 쿼리 실행 속도 30-50% 향상
- DB CPU 사용량 감소

---

### 4단계: Redis 캐싱 적용

#### 캐싱 전략

**1. 채팅방 목록 (Spring Cache)**
- Key: `chatroomList:{userId}`
- TTL: 5분
- 무효화: 새 메시지 도착 시

**2. 사용자 프로필 (Spring Cache)**
- Key: `userProfile:{userId}`
- TTL: 1시간
- 무효화: 프로필 수정 시

**3. 접속 중인 사용자 (Redis Set)**
- Key: `online-users`
- 자료구조: Set
- 용도: 실시간 접속자 관리

**4. 최근 메시지 임시 저장 (Redis List)**
- Key: `recent-messages:{chatroomId}`
- 자료구조: List (최대 50개)
- 용도: DB 부하 분산

#### 설정 파일
- `RedisConfig.java` (신규 생성)
- `application.properties` - Redis 연결 정보 추가

#### 수정 파일
- `ChatroomService.java` - `@Cacheable`, `@CacheEvict` 추가
- `ChatUserService.java` - 프로필 캐싱 추가
- `MessageService.java` - 온라인 사용자 관리 로직 추가

#### 의존성 추가
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

---

## 예상 효과

### 성능 개선 효과

| 시나리오 | 개선 전 | 1-3단계 후 | 4단계(Redis) 후 |
|---------|---------|-----------|----------------|
| **채팅방 목록 조회** | 11개 쿼리, 300ms | 1개 쿼리, 30ms | 0개 쿼리, 5ms |
| **메시지 50개 조회** | 1개 쿼리, 100ms | 1개 쿼리, 50ms | 1개 쿼리, 50ms |
| **안읽은 메시지 카운트** | 10개 쿼리, 200ms | 1개 쿼리, 20ms | 캐시됨, 0ms |
| **DB 커넥션 사용** | 높음 | 보통 | 낮음 |

### 시스템 개선 효과

**성능:**
- 응답 속도 **90% 이상 개선**
- DB 부하 **80% 감소**
- 동시 접속자 처리 능력 **3배 증가**

**코드 품질:**
- 연관관계 활용으로 객체 지향적 코드 작성
- Native Query 제거로 가독성 향상
- 유지보수 시간 **50% 단축**

**비즈니스:**
- 서버 비용 **30% 절감** 가능
- 안정적인 실시간 채팅 서비스 제공
- 확장성 확보

---

## 구현 순서

1. **1단계 완료 → 테스트**
2. **2단계 완료 → 성능 측정**
3. **3단계 완료 → 부하 테스트**
4. **4단계 완료 → 최종 성능 검증**

각 단계마다 기존 기능이 정상 작동하는지 확인 후 다음 단계 진행
