package com.example.demo.core.infra.mongodb.respository;

import com.example.demo.core.infra.mongodb.model.MGPayment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<MGPayment, String> {
}
