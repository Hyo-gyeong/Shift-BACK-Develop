package com.project.shift.shop.controller;

import com.project.shift.shop.dto.OrderDTO;
import com.project.shift.shop.dto.OrderListResponseDTO;
import com.project.shift.shop.dto.OrderDetailResponseDTO;
import com.project.shift.shop.service.IOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO request) {
        OrderDTO result = orderService.createOrder(request);
        return ResponseEntity.ok(result);
    }
    
    //SHOP-007
    @GetMapping("/orders")
    public ResponseEntity<OrderListResponseDTO> getOrders(@RequestParam("userId") Long userId) {
        OrderListResponseDTO response = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(response);
    }
    
    // SHOP-008
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDetailResponseDTO> getOrderDetail(@PathVariable Long orderId) {
        OrderDetailResponseDTO response = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok(response);
    }
}
