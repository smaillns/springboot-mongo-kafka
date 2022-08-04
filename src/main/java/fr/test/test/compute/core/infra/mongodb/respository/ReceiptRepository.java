package fr.test.test.compute.core.infra.mongodb.respository;

import fr.test.test.compute.core.infra.mongodb.model.MGReceipt;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReceiptRepository extends MongoRepository<MGReceipt, String> {
}
