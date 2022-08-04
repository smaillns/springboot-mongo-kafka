package fr.test.test.compute.core.infra.mongodb.respository;

import fr.test.test.compute.core.infra.mongodb.model.MGPayment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<MGPayment, String> {
}
