package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.Deal;
import com.wly.ecomm.repository.DealRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class DealService {
    private final DealRepository repository;

    public List<Deal> findAll() {
        return repository.findAll();
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public Deal findById(Integer id) {
        Optional<Deal> maybeDeal = repository.findById(id);
        if (maybeDeal.isEmpty()) {
            throw new UserDefinedException(String.format("Deal not found with id: %d", id));
        }
        return maybeDeal.get();
    }

    public Deal save(Deal deal) {
        return repository.save(deal);
    }

    public List<Deal> initDeal() {
        return repository.saveAll(List.of(
                new Deal("BOGO50", "BUY ONE GET ONE 50% OFF"),
                new Deal("BOGO100", "BUY ONE GET ONE FREE"),
                new Deal("OFF35", "35% OFF"),
                new Deal("OFF20", "20% OFF")
        ));
    }
}
