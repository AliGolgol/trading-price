package com.solactive.codechallenge.tradingprice.controller;

import com.solactive.codechallenge.tradingprice.model.Statistics;
import com.solactive.codechallenge.tradingprice.model.Tick;
import com.solactive.codechallenge.tradingprice.serevice.TickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class MonitorController {

    @Autowired
    TickService tickService;

    @PostMapping("/ticks")
    public ResponseEntity tick(@RequestBody Tick tick) {
        try {
            tickService.save(tick);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Mono<Statistics>> getStatistics() {
        try {
            return new ResponseEntity(tickService.getAllStatistics(),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/statistics/{instrument_identifier}")
    public ResponseEntity<Mono<Statistics>> getStatisticByInstrumentIdentifier(@PathVariable String instrument) {
        try {
            Mono<Statistics> statisticsMono = tickService.getStatisticsByInstrument(instrument);
            return new ResponseEntity(statisticsMono,HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}
