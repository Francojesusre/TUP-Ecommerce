package com.ecommerce.app.controller;
import com.ecommerce.app.dto.ResponseMessageDto;
import com.ecommerce.app.dto.ResponseMessageDto;
import com.ecommerce.app.entities.Order;
import com.ecommerce.app.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAll(){
        return orderService.getAll();
    }

    @GetMapping("/mis-pedidos")
    public ResponseEntity getAllByUser(HttpServletRequest request){
        try {
            return ResponseEntity.ok(orderService.getAllByUserId(request));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto(exception.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Order>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.loadOrderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessageDto> delete(@PathVariable Long id) {
        if (orderService.loadOrderById(id) != null) {
            orderService.delete(id);
            return ResponseEntity.ok(new ResponseMessageDto("Eliminado"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping()
    public ResponseEntity<Order> update(@RequestBody Order order) {
        Optional<Order> userDetails = orderService.loadOrderById(order.getId());
        if (userDetails != null) {
            return ResponseEntity.ok(orderService.update(order));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PostMapping("/create")
    @Operation(summary = "Crear una orden", description = "Ejemplo de request: \n {\n" +
            "  \"id\": 0,\n" +
            "  \"firstName\": \"\",\n" +
            "  \"lastName\": \"\",\n" +
            "  \"city\": \"\",\n" +
            "  \"address\": \"\",\n" +
            "  \"email\": \"\",\n" +
            "  \"orderItems\": [\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"quantity\": 1,\n" +
            "      \"product\": {\n" +
            "        \"id\": [productId]\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}")
    public ResponseEntity create(HttpServletRequest request, @RequestBody Order order)
    {
        try {
            var productSaved = orderService.create(order, request);
            if (productSaved != null){
                return ResponseEntity.ok(productSaved);
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto(exception.getMessage()));
        }
    }
}
