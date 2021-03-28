package com.solactive.codechallenge.tradingprice.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TickTest {

    @Test
    public void should_returnTick_when_itIsProvided(){
        Tick tick = Tick.builder().instrument("IBM").price(1299).timestamp(System.currentTimeMillis()/1000).build();
        assertEquals("IBM",tick.getInstrument());
    }
}