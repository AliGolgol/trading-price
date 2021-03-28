package com.solactive.codechallenge.tradingprice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statistics  {
    private double avg;
    private double max;
    private double min;
    private long count;
}
