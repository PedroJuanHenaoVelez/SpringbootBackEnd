package com.example.farmacia.service;

import com.example.farmacia.model.Purchase;
import com.example.farmacia.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }

    public Optional<Purchase> findById(Long id) {
        return purchaseRepository.findById(id);
    }

    public Purchase save(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public void deleteById(Long id) {
        purchaseRepository.deleteById(id);
    }
}
