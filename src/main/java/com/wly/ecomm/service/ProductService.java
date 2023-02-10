package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.Deal;
import com.wly.ecomm.model.Product;
import com.wly.ecomm.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final DealService dealService;

    public List<Product> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public Product findById(Integer id) {
        Optional<Product> maybeProduct = repository.findById(id);
        if (maybeProduct.isEmpty()) {
            throw  new UserDefinedException(String.format("Product not found with id: %d", id));
        }
        return maybeProduct.get();
    }

    @Transactional
    public Product save(Product product) {
        return repository.save(product);
    }

    @Transactional
    public List<Product> saveAll(Collection<Product> productList) {
        return repository.saveAll(productList);
    }

    @Transactional
    public Product updateDeals(Integer id, String op, Integer dealId) {
        Deal deal = dealService.findById(dealId);
        Product product = findById(id);
        if (op.equalsIgnoreCase("add")) {
            product.addDeal(deal);
        } else if (op.equalsIgnoreCase("remove")) {
            product.removeDeal(deal);
        } else {
            throw new UserDefinedException(String.format("---'%s' operation for update deals not yet implemented!---", op.toUpperCase()));
        }
        repository.save(product);
        return product;
    }

}
