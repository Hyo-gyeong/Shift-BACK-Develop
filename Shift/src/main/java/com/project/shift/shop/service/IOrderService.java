package com.project.shift.shop.service;

import com.project.shift.shop.dto.OrderDTO;
import com.project.shift.shop.dto.OrderListResponseDTO;


public interface IOrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderListResponseDTO getOrdersByUser(Long userId);

}
