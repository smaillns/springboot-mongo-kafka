package fr.test.test.compute.core.api.messaging.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.test.test.compute.core.domain.model.Receipt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KReceipt {

    @JsonProperty("receipt_id")
    private Integer receiptId;


    public static KReceipt fromModel(Receipt receipt) {
        return KReceipt.builder().receiptId(receipt.getId()).build();
    }

    public Receipt toModel() {
        return Receipt.builder()
                      .id(receiptId)
                      .build();
    }

}
