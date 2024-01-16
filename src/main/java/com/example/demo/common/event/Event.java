package com.example.demo.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event<K> {

    protected K payload;

    public K getPayload() {
        return payload;
    }

    public void setPayload(K payload) {
        this.payload = payload;
    }

}
