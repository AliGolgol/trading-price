package com.solactive.codechallenge.tradingprice.serevice;

import com.solactive.codechallenge.tradingprice.repository.StatisticsRepository;
import com.solactive.codechallenge.tradingprice.repository.TickRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticsScheduleTest {

    @Mock
    private TickRepository tickRepository;
    @Mock
    private StatisticsAggregatorService statisticsAggregatorService;
    @Mock
    private StatisticsRepository statisticsRepository;

    @SpyBean
    StatisticsSchedule statisticsSchedule;

    @Test
    public void should_updateTotalAndTickStatistics_when_thereAre(){

    }

}