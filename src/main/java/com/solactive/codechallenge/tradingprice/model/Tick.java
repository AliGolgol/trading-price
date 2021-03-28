package com.solactive.codechallenge.tradingprice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tick  {
    private String instrument;
    private double price;
    private long timestamp;

    @Override
    public String toString() {
        return "Tick{" +
                "instrument='" + instrument + '\'' +
                ", price=" + price +
                ", timestamp=" + timestamp +
                '}';
    }
}
