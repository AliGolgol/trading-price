package com.solactive.codechallenge.tradingprice.serevice;

import com.solactive.codechallenge.tradingprice.model.Statistics;
import com.solactive.codechallenge.tradingprice.model.Tick;
import com.solactive.codechallenge.tradingprice.repository.StatisticsRepository;
import com.solactive.codechallenge.tradingprice.repository.TickRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TickServiceImpl implements TickService {

    @Autowired
    private TickRepository tickRepository;

    @Autowired
    StatisticsRepository statisticsRepository;

    @Autowired
    StatisticsAggregatorService statisticsAggregatorService;

    /**
     * Persist a Tick
     *
     * @param tick is a {@link Tick}
     * @throws Exception be thrown if the tick`timestamp is not in the sliding time
     */
    @Override
    public Mono<Tick> save(Tick tick) throws Exception {
        if (!isInSlidingTime(tick)) {
            log.error("The tick is not in sliding time!");
            throw new Exception("The tick is not in sliding time!");
        }

        tickRepository.save(tick);
        Flux<List<Tick>> ticksFlux = tickRepository.findAll();
        ticksFlux.subscribe(ticks -> {
            updateTickStatistics(tick, ticks);
            updateTotalStatistics(ticks);
        });
        return Mono.just(tick);
    }

    /**
     * Retrieve a tick's statistics
     *
     * @param instrument is a {@link String}
     * @return a {@link Statistics} which is based upon a Mono
     */
    @Override
    public Mono<Statistics> getStatisticsByInstrument(String instrument) throws Exception {
        if (instrument.isEmpty()) {
            throw new Exception("The instrument can not be empty");
        }
        return statisticsRepository.findByInstrument(instrument);
    }

    /**
     * Retrieve total statistics
     *
     * @return a {@link Statistics} which is based upon a Mono
     */
    @Override
    public Mono<Statistics> getAllStatistics() throws Exception{
        Mono<Statistics> statisticsMono = statisticsRepository.getTotalByInstrument();
        statisticsMono.subscribe(statistics -> {
            if (statistics.getCount() == 0) {
                try {
                    throw new Exception("There is not statistics in lass 60!");
                } catch (Exception e) {
                    log.error(e);
                }
            }
        });
        return statisticsMono;
    }

    /**
     * Update total statistics base on Tick`list
     *
     * @param ticks is a {@link Tick}
     */
    private void updateTotalStatistics(List<Tick> ticks) {
        List<Tick> totalTicks = ticks
                .stream()
                .filter(t -> !isInSlidingTime(t))
                .collect(Collectors.toList());
        statisticsRepository.saveTotal(statisticsAggregatorService.calculateStatistics(totalTicks));
    }

    /**
     * Update the Tick`s statistics
     *
     * @param tick  is a {@link Tick}
     * @param ticks is a list of {@link Tick}
     */
    private void updateTickStatistics(Tick tick, List<Tick> ticks) {
        List<Tick> tickList = ticks
                .stream()
                .filter(t -> t.getInstrument().equals(tick.getInstrument()) && !isInSlidingTime(tick))
                .collect(Collectors.toList());
        statisticsRepository.saveTick(tick.getInstrument(), statisticsAggregatorService.calculateStatistics(tickList));
    }


    /**
     * Validate a timestamp whether or not in sliding time
     *
     * @param tick is a {@link Tick}
     * @return True or False
     */
    private boolean isInSlidingTime(Tick tick) {
        long now = System.currentTimeMillis() / 1000;
        long diff = now - tick.getTimestamp();
        if (diff > 60) {
            return false;
        }
        return true;
    }

}