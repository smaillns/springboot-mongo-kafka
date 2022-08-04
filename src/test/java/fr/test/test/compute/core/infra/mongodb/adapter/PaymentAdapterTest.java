package fr.test.test.compute.core.infra.mongodb.adapter;

import fr.test.test.compute.core.domain.model.Payment;
import fr.test.test.compute.core.infra.mongodb.respository.PaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@SpringBootTest
class PaymentAdapterTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PaymentAdapter paymentAdapter;

    @Autowired
    private PaymentRepository paymentRepository;

    private final String collectionName = "Payments";

    @AfterEach
    void afterEach() {
        mongoTemplate.dropCollection(collectionName);
    }

    @Test
    void saveDisbursement() {
        var payment = new Payment(999);
        paymentAdapter.savePayment(payment);

        var savedPayment = paymentRepository.findAll().get(0);
        assertEquals(payment.getId(), savedPayment.getId());
    }

}
