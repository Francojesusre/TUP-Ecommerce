package com.ecommerce.app.service;

import com.ecommerce.app.entities.Order;
import com.ecommerce.app.repository.OrderRepository;
import com.ecommerce.app.security.JwtUtilService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private UserService userService;

    public Order create(Order order, HttpServletRequest request) throws Exception {
        orderValidations(order);
        discountStock(order);
        var username = jwtUtilService.extractUsername(jwtUtilService.getToken(request));

        if (!StringUtils.isBlank(username)){
            var userLogged = userService.loadUserByEmail(username);
            order.setUserEntity(userLogged);
            order.setDate(new Date());
            order.setTotalPrice((double) order.getOrderItems().stream().mapToDouble(o -> (o.getAmount() * o.getQuantity())).sum());
            return orderRepository.save(order);
        }else{
            throw new Exception("Ocurrio un error al momento de recuperar el usuario");
        }
    }

    public Optional<Order> loadOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAll(){
        return orderRepository.findAll();
    }

    public List<Order> getAllByUserId(HttpServletRequest request) throws Exception {
        var username = jwtUtilService.extractUsername(jwtUtilService.getToken(request));
        if (!StringUtils.isBlank(username)){
            var userLogged = userService.loadUserByEmail(username);
            return orderRepository.findAllByUserEntity_Id(userLogged.getId());
        }else{
            throw new Exception("Ocurrio un error al momento de recuperar el usuario");
        }
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
    public Order update(Order order) {
        return orderRepository.save(order);
    }

    private boolean orderValidations(Order order) throws Exception {
        for (var orderItem: order.getOrderItems()) {
            var productId = orderItem.getProduct().getId();
            if (productId != null){
                var product = productService.loadProductById(productId);
                orderItem.setAmount(product.get().getPrice());
                if (product.get().getStock() < orderItem.getQuantity()){
                    throw new Exception("El producto " + product.get().getName() + " no tiene stock suficiente.");
                }
            }else {
                throw new Exception("Ocurrio un error al momento de recuperar los productos");
            }
        }
        return true;
    }
    private void discountStock(Order order) throws Exception {
        for (var orderItem: order.getOrderItems()) {
            var productId = orderItem.getProduct().getId();
            if (productId != null){
                var product = productService.loadProductById(productId);
                var newStock = product.get().getStock() - orderItem.getQuantity();
                product.get().setStock(newStock);
                productService.update(product.get());
            }
        }
    }
}
