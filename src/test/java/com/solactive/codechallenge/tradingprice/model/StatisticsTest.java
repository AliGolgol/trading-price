package com.solactive.codechallenge.tradingprice.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class StatisticsTest {

    @Test
    public void should_returnStatistics_when_itIsProvided(){
        Statistics statistics = Statistics.builder().max(10).min(1).avg(5.5).count(2).build();
        assertEquals(2,statistics.getCount());
    }
}