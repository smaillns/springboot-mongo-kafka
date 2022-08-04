package fr.test.test.compute.core.infra.mongodb.adapter;

import fr.test.test.compute.core.domain.model.Payment;
import fr.test.test.compute.core.domain.port.infra.PaymentDataGateway;
import fr.test.test.compute.core.infra.mongodb.model.MGPayment;
import fr.test.test.compute.core.infra.mongodb.respository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PaymentAdapter implements PaymentDataGateway {

	private final PaymentRepository paymentRepository;

	@Override
	public void savePayment(Payment payment) {
		paymentRepository.save(MGPayment.fromModel(payment));
	}
}
