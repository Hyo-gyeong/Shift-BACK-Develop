package com.project.shift.product.dto;

import java.util.Date;

public interface UserReviewDetailProjection {
    Long getReviewId();
    Long getRating();
    String getContent();
    Date getCreatedDate();
    Integer getQuantity();
    Integer getItemPrice();
    Long getOrderItemId();
    Long getProductId();
    String getProductName();
    Integer getPrice();
    String getSeller();
    String getImageUrl();
}
