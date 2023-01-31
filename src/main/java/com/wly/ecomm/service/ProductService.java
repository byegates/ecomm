package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.Deal;
import com.wly.ecomm.model.Product;
import com.wly.ecomm.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final DealService dealService;

    public List<Product> findAll() {
        return repository.findAll();
    }

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

    public Product save(Product product) {
        return repository.save(product);
    }

    public List<Product> saveALl(List<Product> productList) {
        return repository.saveAll(productList);
    }

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

    public List<Product> initProduct() {
        List<Deal> deals = dealService.initDeal();

        var BOGO50 = deals.get(0);
        var OFF35 = deals.get(2);

        // products with deal buy one, get one 50% off
        var products1 = List.of(
                new Product("iPhone 14 128GB", 799.00),
                new Product("iPhone 14 Plus 128GB", 899.00)
        );
        repository.saveAll(products1);
        BOGO50.addProducts(products1);
        dealService.save(BOGO50);

        // products with deal 35% off
        var products2 = List.of(
                new Product("iPhone 14 Pro 128GB", 999.00),
                new Product("iPhone 14 Pro Max 128GB", 1099.00)
        );
        repository.saveAll(products2);
        OFF35.addProducts(products2);
        dealService.save(OFF35);

        Product product5 = new Product("iPhone 14 256GB", 899.00);
        var BOGO100 = deals.get(1);
        product5.addDeal(BOGO100);
        repository.save(product5);
        Product product6 = new Product("iPhone 14 Plus 256GB", 999.00);
        var OFF20 = deals.get(3);
        product6.addDeal(OFF20);
        repository.save(product6);

        // products without deals
        repository.saveAll(List.of(
                new Product("iPhone 14 Pro 256GB", 1099.00),
                new Product("iPhone 14 Pro Max 256GB", 1199.00)
        ));

        return repository.findAll();
    }
}
