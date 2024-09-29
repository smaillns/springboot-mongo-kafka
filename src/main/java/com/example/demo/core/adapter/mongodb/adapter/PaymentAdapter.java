package com.example.demo.core.adapter.mongodb.adapter;

import com.example.demo.common.exception.NotFoundException;
import com.example.demo.core.domain.model.Payment;
import com.example.demo.core.port.infra.PaymentDataGateway;
import com.example.demo.core.adapter.mongodb.respository.PaymentRepository;
import com.example.demo.core.adapter.mongodb.model.MGPayment;
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

	@Override
	public Payment findPaymentById(String id) {
		return paymentRepository.findById(id)
				.map(MGPayment::toModel)
				.orElseThrow(() -> new NotFoundException(
						String.format("Payment with id %s not found !", id)));
	}
}
