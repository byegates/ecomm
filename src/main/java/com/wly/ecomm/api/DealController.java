package com.wly.ecomm.api;

import com.wly.ecomm.model.Deal;
import com.wly.ecomm.service.DealService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deals")
@AllArgsConstructor
@Slf4j
public class DealController {
    private final DealService service;

    @GetMapping()
    public List<Deal> findAll() {
        return service.findAll();
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Deal create(@Valid @RequestBody Deal deal) {
        log.info("Deal info received: {}", deal);
        return service.save(deal);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        service.deleteById(id);
    }

    @GetMapping("/{id}")
    public Deal findById(@PathVariable Integer id) {
        return service.findById(id);
    }
}
