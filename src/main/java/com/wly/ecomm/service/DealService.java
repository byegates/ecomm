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
@AllArgsConstructor
public class DealService {
    private final DealRepository repository;

    public List<Deal> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public Deal findById(Integer id) {
        Optional<Deal> maybeDeal = repository.findById(id);
        if (maybeDeal.isEmpty()) {
            throw new UserDefinedException(String.format("Deal not found with id: %d", id));
        }
        return maybeDeal.get();
    }

    @Transactional
    public Deal save(Deal deal) {
        return repository.save(deal);
    }

    @Transactional
    public List<Deal> saveAll(List<Deal> dealList) {
        return repository.saveAll(dealList);
    }

}
