package com.solactive.codechallenge.tradingprice.serevice;

import com.solactive.codechallenge.tradingprice.model.Statistics;
import com.solactive.codechallenge.tradingprice.model.Tick;

import java.util.List;

public interface StatisticsAggregatorService {
    Statistics calculateStatistics(List<Tick> ticks);
}
