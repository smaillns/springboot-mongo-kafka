package fr.test.test.compute.core.domain.service;

import fr.test.test.compute.core.domain.model.Payment;
import fr.test.test.compute.core.domain.port.infra.PaymentDataGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {


    @Mock
    private PaymentDataGateway paymentDataGateway;


    @Test
    void handleDisbursementPaymentDueEvent_shouldSaveDisbursement()  {
        var payment = Mockito.mock(Payment.class);
        doNothing().when(paymentDataGateway).savePayment(payment);

        PaymentService disbursementService = new PaymentService(paymentDataGateway);
        disbursementService.handlePaymentReceivedEvent(payment);
        verify(paymentDataGateway, times(1)).savePayment(any());
    }
}
