package com.wly.ecomm.api;

import com.wly.ecomm.model.Product;
import com.wly.ecomm.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping()
    public List<Product> findAll() {
        return service.findAll();
    }

    @PostMapping(consumes = "application/json")
    public Product create(@Valid @RequestBody Product product) {
        product.clearDeal();
        return service.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        service.deleteById(id);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping("/{id}/{action}/{dealId}")
    public Product updateDeals(@PathVariable Integer id, @PathVariable String action, @PathVariable Integer dealId) {
        return service.updateDeals(id, action, dealId);
    }
}
