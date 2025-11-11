package com.project.shift.shop.service;

import com.project.shift.shop.dto.OrderDTO;
import com.project.shift.shop.dto.OrderListResponseDTO;
import com.project.shift.shop.dto.OrderDetailResponseDTO;



public interface IOrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderListResponseDTO getOrdersByUser(Long userId);
    
 // SHOP-008
    OrderDetailResponseDTO getOrderDetail(Long orderId);

}
