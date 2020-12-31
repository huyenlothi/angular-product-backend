package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService productService;
    @GetMapping
    public ResponseEntity<Iterable<Product>> getAll(){
        return  new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product){
        productService.save(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        Optional<Product> optionalProduct = productService.findById(id);
        return optionalProduct.map(product -> new ResponseEntity<>(product,HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> edit(@PathVariable Long id,@RequestBody Product product){
        Optional<Product> productOptional= productService.findById(id);
        return productOptional.map(product1 -> {
            product.setId(product1.getId());
            if (product.getName().equalsIgnoreCase("")){
                product.setName(product1.getName());
            }
            if (product.getPrice() <= 0){
                product.setPrice(product1.getPrice());
            }
            if (product.getDescription().equalsIgnoreCase("")){
                product.setDescription(product1.getDescription());
            }
            return new ResponseEntity<>(productService.save(product),HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id){
        Optional<Product> productOptional = productService.findById(id);
        return productOptional.map(product -> {
            productService.delete(id);
            return  new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
