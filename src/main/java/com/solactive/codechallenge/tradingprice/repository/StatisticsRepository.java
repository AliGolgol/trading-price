package com.solactive.codechallenge.tradingprice.repository;

import com.solactive.codechallenge.tradingprice.model.Statistics;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
public class StatisticsRepository {
    private final String TOTAL_STATISTICS = "total";
    private static Map<String, Statistics> statisticsMap = new HashMap<>();

    /**
     *  Persist a Tick and its Statistics
     * @param instrument is a {@link String}
     * @param statistic is a {@link Statistics} object
     * @return a {@link Statistics} which is upon a Mono
     */
    public Mono<Statistics> saveTick(String instrument, Statistics statistic) {
        statisticsMap.put(instrument,statistic);
        return Mono.just(statistic);
    }

    /**
     * Persist total Statistics
     * @param statistics is {@link Statistics} object
     * @return a {@link Statistics} which is based upon a Mono
     */
    public Mono<Statistics> saveTotal(Statistics statistics) {
        statisticsMap.put(TOTAL_STATISTICS,statistics);
        return Mono.just(statistics);
    }

    /**
     *  Find an instrument by its name
     * @param instrument is {@link com.solactive.codechallenge.tradingprice.model.Instrument}
     * @return a {@link Statistics} which is based upon Mono
     */
    public Mono<Statistics> findByInstrument(String instrument) {
        return Mono.just(statisticsMap.get(instrument));
    }

    /**
     * Retrieve the total statistics
     * @return a {@link Statistics} which is based upon a Mono
     */
    public Mono<Statistics> getTotalByInstrument() {
        return Mono.just(statisticsMap.get(TOTAL_STATISTICS));
    }
}
