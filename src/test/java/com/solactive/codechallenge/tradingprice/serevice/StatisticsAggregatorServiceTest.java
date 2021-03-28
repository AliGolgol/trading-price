package com.solactive.codechallenge.tradingprice.serevice;

import com.solactive.codechallenge.tradingprice.model.Instrument;
import com.solactive.codechallenge.tradingprice.model.Statistics;
import com.solactive.codechallenge.tradingprice.model.Tick;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class StatisticsAggregatorServiceTest {

    StatisticsAggregatorService statisticsAggregatorService;

    @Before
    public void setup(){
        statisticsAggregatorService = new StatisticsAggregatorServiceImpl();
    }
    @Test
    public void should_returnStatistics_when_tickListIsProvider(){
        Instrument instrument = Instrument.builder().name("IBM").build();
        List<Tick> ticks = Arrays.asList(
                Tick.builder().instrument("IBM").price(1200l).timestamp(16490000).build(),
                Tick.builder().instrument("IBM").price(1300l).timestamp(16490000).build(),
                Tick.builder().instrument("IBM").price(1400l).timestamp(16490000).build(),
                Tick.builder().instrument("IBM").price(1500l).timestamp(16490000).build(),
                Tick.builder().instrument("IBM").price(1600l).timestamp(16490000).build(),
                Tick.builder().instrument("AMAZON").price(1600l).timestamp(16490000).build(),
                Tick.builder().instrument("AMAZON").price(1600l).timestamp(16490000).build());
        Statistics statistics = Statistics.builder().avg(1457.142857142857).count(7).min(1200l).max(1600l).build();

        assertEquals(statistics,statisticsAggregatorService.calculateStatistics(ticks));
    }
}