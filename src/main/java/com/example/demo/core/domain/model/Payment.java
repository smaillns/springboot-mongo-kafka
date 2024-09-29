package com.example.demo.core.domain.model;

import com.example.demo.common.exception.FunctionalException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;


@Getter
@Builder
@AllArgsConstructor
public class Payment {

    private Integer id;
    private BigDecimal amount;

    public boolean isValid() throws FunctionalException {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new FunctionalException("invalid payment amount");
        }
        return true;
    }
}
