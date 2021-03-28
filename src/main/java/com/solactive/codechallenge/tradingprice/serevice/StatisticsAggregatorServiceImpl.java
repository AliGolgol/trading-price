package com.solactive.codechallenge.tradingprice.serevice;

import com.solactive.codechallenge.tradingprice.model.Statistics;
import com.solactive.codechallenge.tradingprice.model.Tick;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsAggregatorServiceImpl implements StatisticsAggregatorService {
    /**
     * Calculate Avg, Min, Max, and Count as a Statistics
     * @param ticks is a list of {@link Tick}
     * @return a {@link Statistics}
     */
    public Statistics calculateStatistics(List<Tick> ticks) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        int count = ticks.size();
        double average = 0;
        for (Tick tick : ticks) {
            min = Math.min(min, tick.getPrice());
            max = Math.max(max, tick.getPrice());
            average += tick.getPrice();
        }
        average = average / count;

        return Statistics.builder()
                .max(max)
                .min(min)
                .avg(average)
                .count(count)
                .build();
    }
}
