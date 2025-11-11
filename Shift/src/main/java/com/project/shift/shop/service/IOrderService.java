package com.project.shift.shop.service;

import com.project.shift.shop.dto.OrderDTO;

public interface IOrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
}
