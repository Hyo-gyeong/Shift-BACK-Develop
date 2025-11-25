package com.project.shift.product.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SequenceGenerator(
	    name = "SEQ_PT_GEN",
	    sequenceName = "seq_point_transactions",  
	    allocationSize = 1
	)
	@Entity
	@Table(name = "POINT_TRANSACTIONS")
	@Getter @Setter
	@NoArgsConstructor @AllArgsConstructor @Builder
	public class PointTransaction {

	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PT_GEN")
	    @Column(name = "POINT_TRANSACTION_ID")
	    private Long id;

	    @Column(name = "USER_ID", nullable = false)
	    private Long userId;

	    @Column(name = "ORDER_ID")
	    private Long orderId;

	    @Column(name = "TRANSACTION_TYPE", nullable = false, columnDefinition = "CHAR(1)")
	    private String type;

	    @Column(name = "AMOUNT", nullable = false)
	    private Integer amount;

	    @Column(name = "CREATED_AT", nullable = false)
	    private LocalDateTime createdAt;
	}
