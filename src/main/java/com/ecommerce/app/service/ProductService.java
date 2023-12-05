package com.ecommerce.app.service;

import com.ecommerce.app.entities.Product;
import com.ecommerce.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product create(Product product){
        return productRepository.save(product);
    }
    public Optional<Product> loadProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
    public Product update(Product product) {
        return productRepository.save(product);
    }


}
