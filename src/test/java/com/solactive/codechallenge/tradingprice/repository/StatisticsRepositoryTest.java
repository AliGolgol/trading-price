package com.solactive.codechallenge.tradingprice.repository;

import com.solactive.codechallenge.tradingprice.model.Statistics;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class StatisticsRepositoryTest {

    StatisticsRepository repository;

    @Before
    public void setup(){
        repository = new StatisticsRepository();
    }

    @Test
    public void should_returnTickStatistics_when_thereIs(){
        Statistics statistics = Statistics.builder().avg(1457.142857142857).count(7).min(1200l).max(1600l).build();
        Mono<Statistics> statisticsMono = repository.saveTick("IBM",statistics);
        statisticsMono.subscribe(s->{
            assertEquals(7,s.getCount());
        });
    }

    @Test
    public void should_returnTotalStatistics_when_thereIs(){
        Statistics statistics = Statistics.builder().avg(1457.142857142857).count(7).min(1200l).max(1600l).build();
        Mono<Statistics> statisticsMono = repository.saveTotal(statistics);
        statisticsMono.subscribe(s->{
            assertEquals(7,s.getCount());
        });
    }

    @Test
    public void should_returnTickStatistics_when_instrumentIsProvided(){
        Statistics statistics = Statistics.builder().avg(1457.142857142857).count(7).min(1200l).max(1600l).build();
         repository.saveTick("IBM",statistics);
        Mono<Statistics> statisticsMono =repository.findByInstrument("IBM");
        statisticsMono.subscribe(s->{
            assertEquals(7,s.getCount());
        });
    }

    @Test
    public void should_returnTotalByInstrument_when_instrumentIsProvided(){
        Statistics statistics = Statistics.builder().avg(1457.142857142857).count(7).min(1200l).max(1600l).build();
         repository.saveTotal(statistics);
        Mono<Statistics> statisticsMono =repository.getTotalByInstrument();
        statisticsMono.subscribe(s->{
            assertEquals(7,s.getCount());
        });
    }

}