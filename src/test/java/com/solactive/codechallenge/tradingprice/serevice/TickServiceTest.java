package com.solactive.codechallenge.tradingprice.serevice;

import com.solactive.codechallenge.tradingprice.model.Instrument;
import com.solactive.codechallenge.tradingprice.model.Statistics;
import com.solactive.codechallenge.tradingprice.model.Tick;
import com.solactive.codechallenge.tradingprice.repository.StatisticsRepository;
import com.solactive.codechallenge.tradingprice.repository.TickRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TickServiceTest {

    @Mock
    private TickRepository mockTickRepository;

    @Mock
    StatisticsRepository mockStatisticsRepository;

    @Mock
    StatisticsAggregatorService mockStatisticsAggregatorService;

    @InjectMocks
    TickServiceImpl tickService;

    @Before
    public void setup() {
    }

    @Test
    public void should_persistTick_when_itIsInTimeSliding() {
        long timestamp = System.currentTimeMillis() / 1000;
        Tick tick = Tick.builder().timestamp(timestamp).price(12000l).instrument("IBM").build();
        List<Tick> ticks = Arrays.asList(tick);
        Mono<Tick> tickMono = Mono.just(tick);

        when(mockTickRepository.save(tick)).thenReturn(tickMono);
        when(mockTickRepository.findAll()).thenReturn(Flux.just(ticks));

        try {
            tickService.save(tick);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    public void should_throwException_when_tickIsNotInTimeSliding(){
        long timestamp = 12341583;
        Tick tick = Tick.builder().timestamp(timestamp).price(12000l).instrument("IBM").build();
        List<Tick> ticks = Arrays.asList(tick);
        Mono<Tick> tickMono = Mono.just(tick);

        when(mockTickRepository.save(tick)).thenReturn(tickMono);
        when(mockTickRepository.findAll()).thenReturn(Flux.just(ticks));

        assertThrows(Exception.class,()->{
            tickService.save(tick);
        });
    }

    @Test
    public void should_returnStatistics_when_providingInstrument() throws Exception {
        Statistics statistics = Statistics.builder().max(120).min(20).avg(70).count(2).build();
        when(mockStatisticsRepository.findByInstrument("IBM")).thenReturn(Mono.just(statistics));

        assertEquals(Mono.just(statistics).block().getCount(),tickService.getStatisticsByInstrument("IBM").block().getCount());
        assertEquals(null,tickService.getStatisticsByInstrument("NETFLIX"));
    }

    @Test
    public void should_throwException_when_instrumentIsEmpty() throws Exception {
        Statistics statistics = Statistics.builder().max(120).min(20).avg(70).count(2).build();

        when(mockStatisticsRepository.findByInstrument("IBM")).thenReturn(Mono.just(statistics));

        assertThrows(Exception.class,()->{
            tickService.getStatisticsByInstrument("");
        });
    }

    @Test
    public void should_totalStatistics_when_statisticsIsReady() throws Exception {
        Statistics statistics = Statistics.builder().max(120).min(20).avg(70).count(2).build();
        when(mockStatisticsRepository.getTotalByInstrument()).thenReturn(Mono.just(statistics));

        assertEquals(Mono.just(statistics).block().getCount(),tickService.getAllStatistics().block().getCount());
    }

    @Test
    public void should_throwException_when_thereIsNotStatisticsInSlidingTime() {
        Statistics statistics = Statistics.builder().max(120).min(20).avg(70).count(0).build();
        when(mockStatisticsRepository.getTotalByInstrument()).thenReturn(Mono.just(statistics));

        assertThrows(Exception.class,()->{
            tickService.getAllStatistics();
        });
    }
}