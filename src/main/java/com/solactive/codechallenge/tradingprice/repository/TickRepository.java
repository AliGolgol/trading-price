package com.solactive.codechallenge.tradingprice.repository;

import com.solactive.codechallenge.tradingprice.model.Tick;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TickRepository  {

    private static Map<String, List<Tick>> tickMap = new HashMap<>();

    /**
     *  Persist the tick in a Map object which has constant time and space complexity (O(1))
     * @param tick is a {@link Tick} object to be persisted
     * @return a tick {@link Tick} which is upon a Mono
     */
    public Mono<Tick> save(Tick tick) {
        Mono<Tick> tickMono = Mono.just(tick);

        tickMono.subscribe(t->{
            List<Tick> tickList = tickMap.getOrDefault(t.getInstrument(),new ArrayList<>());
            tickList.add(t);
            tickMap.put(tick.getInstrument(),tickList);
        });
        return tickMono;
    }

    /**
     * Find the all ticks
     * @return a list of {@link List<Tick>} which is based upon Flux
     */
    public Flux<List<Tick>> findAll() {
        List<Tick> tickList = tickMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
        Flux<List<Tick>> flux = Flux.just(tickList);
        return flux;
    }

    /**
     * Find an instrument`statistics by its name
     * @param name of instrument
     * @return the list of {@link List<Tick>} which is based upon Flux
     */
    public Flux<List<Tick>> findByInstrument(String name) {
        Flux<List<Tick>> tickList = Flux.just(tickMap.get(name));
        return tickList;
    }

    /**
     * Get all ticks which are persisted so far
     * @return a {@link Map<String, List<Tick>>} which is based upon Mono
     */
    public Mono<Map<String,List<Tick>>> getAllTicks(){
        return Mono.just(tickMap);
    }
}
