package com.solactive.codechallenge.tradingprice.serevice;

import com.solactive.codechallenge.tradingprice.model.Statistics;
import com.solactive.codechallenge.tradingprice.model.Tick;
import com.solactive.codechallenge.tradingprice.repository.StatisticsRepository;
import com.solactive.codechallenge.tradingprice.repository.TickRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Log4j2
public class StatisticsSchedule {
    @Autowired
    private TickRepository tickRepository;
    @Autowired
    private StatisticsAggregatorService statisticsAggregatorService;

    @Autowired
    StatisticsRepository statisticsRepository;

    /**
     * Update the total statistics every second
     */
    @Scheduled(cron = "0/1 * * * * ?")
    public void updateStatistics() {
        Flux<List<Tick>> ticksFlux = tickRepository.findAll();
        ticksFlux.subscribe(ticks -> {
            List<Tick> totalTicks = ticks
                    .stream()
                    .filter(t -> ((System.currentTimeMillis() / 1000) - t.getTimestamp()) <= 60)
                    .collect(Collectors.toList());

            statisticsRepository.saveTotal(statisticsAggregatorService.calculateStatistics(totalTicks));
            statisticsRepository.getTotalByInstrument().subscribe(x -> log.info("Statistics " + x));
        });
    }

    /**
     * Update the Tick's statistics every second
     */
    @Scheduled(cron = "0/1 * * * * ?")
    public void updateTicksStatistics() {
        Mono<Map<String, List<Tick>>> ticks = tickRepository.getAllTicks();
        ticks.subscribe(x -> {

            for (Map.Entry<String, List<Tick>> tick : x.entrySet()) {
                log.info("List of ticks to update :" + tick.getKey());
                statisticsRepository.saveTick(tick.getKey(),
                        statisticsAggregatorService.calculateStatistics(
                                tick.getValue()
                                .stream()
                                .filter(t -> ((System.currentTimeMillis() / 1000) - t.getTimestamp()) <= 60).collect(Collectors.toList())));

                statisticsRepository.findByInstrument(tick.getKey()).subscribe(s -> {
                    log.info("List of updated tick :" + s);
                });
            }
        });
    }

}
