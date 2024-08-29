package com.example.farmacia.controller;

import com.example.farmacia.model.Product;
import com.example.farmacia.model.Purchase;
import com.example.farmacia.service.ProductService;
import com.example.farmacia.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Purchase> getAllPurchases() {
        return purchaseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable Long id) {
        return purchaseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Purchase createPurchase(@RequestBody Purchase purchase) {
        Optional<Product> optionalProduct = productService.findById(purchase.getProduct().getId());
        if (!optionalProduct.isPresent()) {
            throw new RuntimeException("Product not found");
        }

        Product product = optionalProduct.get();

        // Verificar suficiente stock
        if (product.getQuantityInStock() < purchase.getQuantity()) {
            throw new RuntimeException("Not enough stock available");
        }

        // Crear compra
        Purchase createdPurchase = purchaseService.save(purchase);

        // Actualizar stock del producto
        product.setQuantityInStock(product.getQuantityInStock() - purchase.getQuantity());
        productService.save(product);

        return purchaseService.save(purchase);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Purchase> updatePurchase(@PathVariable Long id, @RequestBody Purchase purchaseDetails) {
        return purchaseService.findById(id)
                .map(purchase -> {
                    purchase.setPurchaseDate(purchaseDetails.getPurchaseDate());
                    purchase.setQuantity(purchaseDetails.getQuantity());
                    purchase.setClient(purchaseDetails.getClient());
                    purchase.setProduct(purchaseDetails.getProduct());
                    return ResponseEntity.ok(purchaseService.save(purchase));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        if (purchaseService.findById(id).isPresent()) {
            purchaseService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
