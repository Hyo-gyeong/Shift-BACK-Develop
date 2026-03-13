package com.project.shift.shop.dto;

import java.util.List;

public class OrderListResponseDTO {

    private List<OrderListDTO> orders;

    public OrderListResponseDTO(List<OrderListDTO> orders) {
        this.orders = orders;
    }

    public List<OrderListDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderListDTO> orders) {
        this.orders = orders;
    }
}
