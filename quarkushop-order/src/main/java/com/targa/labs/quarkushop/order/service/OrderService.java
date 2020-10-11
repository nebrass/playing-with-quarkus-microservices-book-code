package com.targa.labs.quarkushop.order.service;

import com.targa.labs.quarkushop.commons.dto.OrderDto;
import com.targa.labs.quarkushop.order.domain.Order;
import com.targa.labs.quarkushop.order.domain.enums.OrderStatus;
import com.targa.labs.quarkushop.order.repository.CartRepository;
import com.targa.labs.quarkushop.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
public class OrderService {

    @Inject
    OrderRepository orderRepository;

    @Inject
    CartRepository cartRepository;

    public static OrderDto mapToDto(Order order) {
        var orderItems = order
                .getOrderItems()
                .stream()
                .map(OrderItemService::mapToDto)
                .collect(Collectors.toSet());

        return new OrderDto(
                order.getId(),
                order.getPrice(),
                order.getStatus().name(),
                order.getShipped(),
                order.getPaymentId() != null ? order.getPaymentId() : null,
                AddressService.mapToDto(order.getShipmentAddress()),
                orderItems,
                CartService.mapToDto(order.getCart())
        );
    }

    public List<OrderDto> findAll() {
        log.debug("Request to get all Orders");
        return this.orderRepository.findAll()
                .stream()
                .map(OrderService::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto findById(Long id) {
        log.debug("Request to get Order : {}", id);
        return this.orderRepository.findById(id)
                .map(OrderService::mapToDto)
                .orElse(null);
    }

    public List<OrderDto> findAllByUser(Long id) {
        return this.orderRepository.findByCartCustomerId(id)
                .stream()
                .map(OrderService::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto create(OrderDto orderDto) {
        log.debug("Request to create Order : {}", orderDto);

        var cartId = orderDto.getCart().getId();
        var cart = this.cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new IllegalStateException("The Cart with ID[" + cartId + "] was not found !"));

        return mapToDto(
                this.orderRepository.save(
                        new Order(
                                BigDecimal.ZERO,
                                OrderStatus.CREATION,
                                null,
                                null,
                                AddressService.createFromDto(orderDto.getShipmentAddress()),
                                Collections.emptySet(),
                                cart
                        )
                )
        );
    }

    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);

        var order = this.orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Order with ID[" + id + "] cannot be found!"));

        //Optional.ofNullable(order.getPayment()).ifPresent(paymentRepository::delete);

        orderRepository.delete(order);
    }

    public boolean existsById(Long id) {
        return this.orderRepository.existsById(id);
    }

    public OrderDto findByPaymentId(Long id) {
        return mapToDto(
                this.orderRepository.findByPaymentId(id)
                        .orElseThrow(() -> new IllegalStateException("Order with ID[" + id + "] cannot be found!"))
        );
    }
}
