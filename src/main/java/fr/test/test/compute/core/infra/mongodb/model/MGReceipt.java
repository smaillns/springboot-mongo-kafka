package fr.test.test.compute.core.infra.mongodb.model;

import fr.test.test.compute.core.domain.model.Receipt;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;


@Getter
@Builder
public class MGReceipt {

    @Id
    private Integer id;

    public static MGReceipt fromModel(Receipt receipt) {

        return MGReceipt.builder()
                        .id(receipt.getId())
                        .build();

    }

    public Receipt toModel() {

        return Receipt.builder()
                      .id(id)
                      .build();

    }
}
