
package com.ecommerce.app.controller;
import com.ecommerce.app.dto.ResponseMessageDto;
import com.ecommerce.app.entities.Product;
import com.ecommerce.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAll(){
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> GetById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.loadProductById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessageDto> delete(@PathVariable Long id) {
        if (productService.loadProductById(id) != null) {
            productService.delete(id);
            return ResponseEntity.ok(new ResponseMessageDto("Eliminado"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping()
    public ResponseEntity<Product> update(@RequestBody Product product) {
        Optional<Product> userDetails = productService.loadProductById(product.getId());
        if (userDetails != null) {
            return ResponseEntity.ok(productService.update(product));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Product product)
    {
        var productSaved = productService.create(product);
        if (productSaved != null){
            return ResponseEntity.ok(productSaved);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
