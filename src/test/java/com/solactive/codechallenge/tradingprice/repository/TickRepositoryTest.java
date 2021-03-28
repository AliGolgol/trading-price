package com.solactive.codechallenge.tradingprice.repository;

import com.solactive.codechallenge.tradingprice.model.Tick;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class TickRepositoryTest {

    TickRepository tickRepository;
    List<Tick> tickList = new ArrayList<>();
    @Before
    public void setup() {
        tickRepository = new TickRepository();
        tickList = Arrays.asList(
                Tick.builder().instrument("IBM").price(1200l).timestamp(16490000).build(),
                Tick.builder().instrument("IBM").price(1300l).timestamp(16490000).build(),
                Tick.builder().instrument("IBM").price(1400l).timestamp(16490000).build(),
                Tick.builder().instrument("IBM").price(1500l).timestamp(16490000).build(),
                Tick.builder().instrument("IBM").price(1600l).timestamp(16490000).build(),
                Tick.builder().instrument("AMAZON").price(1600l).timestamp(16490000).build(),
                Tick.builder().instrument("AMAZON").price(1600l).timestamp(16490000).build());
    }

    @Test
    public void should_returnTick_when_tickIsInSlidingTime() {
        Tick tick = Tick.builder().instrument("IBM").price(12000l).timestamp(System.currentTimeMillis() / 1000).build();
        Mono<Tick> tickMono = tickRepository.save(tick);
        tickMono.subscribe(t->{
            assertEquals(tick,t);
        });
    }

    @Test
    public void should_returnAllTicks_when_thereIsTick(){
        tickList.forEach(tick->{
            tickRepository.save(tick);
        });
        Flux<List<Tick>> tickFlux = tickRepository.findAll();
        tickFlux.subscribe(s-> assertEquals(7, s.size()));
    }

    @Test
    public void should_returnTick_when_instrumentIsProvided(){
        tickList.forEach(tick->{
            tickRepository.save(tick);
        });

        Flux<List<Tick>> tickFlux = tickRepository.findByInstrument("IBM");
        tickFlux.subscribe(ticks -> {
            assertEquals(5,ticks.size());
        });
    }

    @Test
    public void should_returnAllTicks_when_itIsPersisted(){
        tickList.forEach(tick->{
            tickRepository.save(tick);
        });

        Mono<Map<String,List<Tick>>> mapMono = tickRepository.getAllTicks();
        mapMono.subscribe(tickList->{
           assertEquals(5,tickList.get("IBM"));
        });
    }
}