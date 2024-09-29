package com.example.demo.core.domain.service;

import com.example.demo.core.domain.model.Payment;
import com.example.demo.core.domain.port.infra.PaymentDataGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentDataGateway paymentDataGateway;

    private PaymentService paymentService;

    @BeforeEach
    void setup() {
        paymentService = new PaymentService(paymentDataGateway);
    }

    @Test
    void handlePaymentReceivedEvent() {
        Payment payment = Mockito.mock(Payment.class);

        doReturn(true).when(payment).isValid();
        paymentService.handlePaymentReceivedEvent(payment);

        verify(paymentDataGateway, times(1)).savePayment(payment);
    }
}
