package com.example.demo.core.adapter.mongodb.respository;

import com.example.demo.core.adapter.mongodb.model.MGPayment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<MGPayment, String> {
}
