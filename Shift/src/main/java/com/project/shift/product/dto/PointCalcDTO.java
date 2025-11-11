package com.project.shift.product.dto;

/**
 * 금액권 합계 계산 요청 및 응답에 사용되는 DTO 클래스
 * - 입력금액과 추가금액을 합산하고, 범위 검증 결과를 반환한다.
 */
public class PointCalcDTO {
    private int inputAmount;   // 사용자가 입력한 금액
    private int addAmount;     // 버튼 클릭으로 추가된 금액
    private int totalAmount;   // 최종 합산 금액
    private boolean withinRange; // 유효 범위 여부
    private int minPrice;      // 최소 허용 금액
    private int maxPrice;      // 최대 허용 금액

    // Getter & Setter
    public int getInputAmount() { 
    	return inputAmount;
    }
    public void setInputAmount(int inputAmount) { 
    	this.inputAmount = inputAmount;
    }

    public int getAddAmount() { 
    	return addAmount;
    }  
    public void setAddAmount(int addAmount) { 
    	this.addAmount = addAmount;
    }

    public int getTotalAmount() { 
    	return totalAmount;
    }
    public void setTotalAmount(int totalAmount) { 
    	this.totalAmount = totalAmount;
    }

    public boolean isWithinRange() { 
    	return withinRange;
    }
    public void setWithinRange(boolean withinRange) { 
    	this.withinRange = withinRange;
    }

    public int getMinPrice() { 
    	return minPrice;
    }
    public void setMinPrice(int minPrice) { 
    	this.minPrice = minPrice;
    }

    public int getMaxPrice() { 
    	return maxPrice;
    }
    public void setMaxPrice(int maxPrice) { 
    	this.maxPrice = maxPrice;
    }
}
