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
    public List<Product> saveALl(Collection<Product> productList) {
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

    @Transactional
    public List<Product> initProduct() {
        var BOGO50 = new Deal("BOGO50", "BUY ONE GET ONE 50% OFF");
        var BOGO100 = new Deal("BOGO100", "BUY ONE GET ONE FREE");
        var OFF35 = new Deal("OFF35", "35% OFF");
        var OFF20 = new Deal("OFF20", "20% OFF");


        // products with deal buy one, get one 50% off
        var products1 = List.of(
                new Product("iPhone 14 128GB", 799.00),
                new Product("iPhone 14 Plus 128GB", 899.00)
        );
        BOGO50.addProducts(products1);
        dealService.save(BOGO50);

        // products with deal 35% off
        var products2 = List.of(
                new Product("iPhone 14 Pro 128GB", 999.00),
                new Product("iPhone 14 Pro Max 128GB", 1099.00)
        );
        OFF35.addProducts(products2);
        dealService.save(OFF35);

        Product product5 = new Product("iPhone 14 256GB", 899.00);
        product5.addDeal(BOGO100);
        repository.save(product5);
        Product product6 = new Product("iPhone 14 Plus 256GB", 999.00);
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
