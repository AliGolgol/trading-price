package com.solactive.codechallenge.tradingprice.serevice;

import com.solactive.codechallenge.tradingprice.model.Statistics;
import com.solactive.codechallenge.tradingprice.model.Tick;
import reactor.core.publisher.Mono;

public interface TickService {
    Mono<Tick> save(Tick tick) throws Exception;
    Mono<Statistics> getAllStatistics() throws Exception;
    Mono<Statistics> getStatisticsByInstrument(String instrument) throws Exception;
}
