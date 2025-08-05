package com.example.oms.controller;

import com.example.oms.model.Order;
import com.example.oms.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost:3000") // Allow React frontend
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<Order> createOrder(
            @RequestParam("customerName") String customerName,
            @RequestParam("product") String product,
            @RequestParam("quantity") int quantity,
            @RequestParam("price") Double price,
            @RequestParam("invoiceFile") MultipartFile invoiceFile) {
        Order order = orderService.createOrder(customerName, product, quantity, price, invoiceFile);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id);
    }
}
