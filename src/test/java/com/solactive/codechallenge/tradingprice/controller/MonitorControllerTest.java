package com.solactive.codechallenge.tradingprice.controller;

import com.solactive.codechallenge.tradingprice.model.Statistics;
import com.solactive.codechallenge.tradingprice.model.Tick;
import com.solactive.codechallenge.tradingprice.serevice.TickService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@RunWith(SpringRunner.class)
public class MonitorControllerTest {

    @Mock
    TickService service;

    private MockMvc mockMvc;

    @InjectMocks
    private MonitorController controller;

    @Before
    public void setup() {
        mockMvc = standaloneSetup(this.controller).build();
    }

    @Test
    public void should_persistTick_when_itIsInSlidingTime() throws Exception {
        Tick tick = Tick.builder().timestamp(System.currentTimeMillis() / 100).instrument("IBM").price(12000).build();
        when(service.save(tick)).thenReturn(Mono.just(tick));
        mockMvc.perform(post("/ticks").
                contentType(MediaType.APPLICATION_JSON).
                content("{\n" +
                        "    \"instrument\":\"IBM\",\n" +
                        "    \"price\":200.0,\n" +
                        "    \"timestamp\":1616933326\n" +
                        "}")).
                andExpect(status().isCreated());
    }

    @Test
    public void should_returnStatusCode204_when_itIsNotInSlidingTime() throws Exception {
        Tick tick = Tick.builder().timestamp(8780988).instrument("IBM").price(12000).build();
        when(service.save(tick)).thenReturn(Mono.just(tick));
        mockMvc.perform(post("/ticks").
                contentType(MediaType.APPLICATION_JSON).
                content("{\n" +
                        "    \"instrument\":\"IBM\",\n" +
                        "    \"price\":200.0,\n" +
                        "    \"timestamp\":1616933326\n" +
                        "}")).
                andExpect(status().isCreated());
    }

    @Test
    public void should_returnTotalStatistics_when_itIsInSlidingTime() throws Exception {
        Statistics statistics = Statistics.builder().avg(200.0).count(1).min(200.0).max(200.0).build();

        when(service.getAllStatistics()).thenReturn(Mono.just(statistics));
        mockMvc.perform(MockMvcRequestBuilders.get("/statistics")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"avg\": 200.0,\n" +
                        "    \"max\": 200.0,\n" +
                        "    \"min\": 200.0,\n" +
                        "    \"count\": 1\n" +
                        "}"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void should_returnTickStatistics_when_itIsInSlidingTime() throws Exception {
        Statistics statistics = Statistics.builder().avg(200.0).count(1).min(200.0).max(200.0).build();

        when(service.getStatisticsByInstrument("IBM")).thenReturn(Mono.just(statistics));
        mockMvc.perform(MockMvcRequestBuilders.get("/statistics")
                .param("instrument_identifier", "IBM")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"avg\": 200.0,\n" +
                        "    \"max\": 200.0,\n" +
                        "    \"min\": 200.0,\n" +
                        "    \"count\": 1\n" +
                        "}"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void should_throwException_when_itIsInSlidingTime() throws Exception {
        Tick tick = Tick.builder().timestamp(8780988).instrument("IBM").price(12000).build();

        when(service.save(tick)).thenThrow(Exception.class);
        mockMvc.perform(post("/ticks").
                contentType(MediaType.APPLICATION_JSON).
                content("{\n" +
                        "    \"instrument\":\"IBM\",\n" +
                        "    \"price\":200.0,\n" +
                        "    \"timestamp\":1616933326\n" +
                        "}")).
                andExpect(status().isNoContent());
    }
}
