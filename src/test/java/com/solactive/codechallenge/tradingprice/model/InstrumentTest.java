package com.solactive.codechallenge.tradingprice.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class InstrumentTest {

    @Test
    public void should_returnName_when_itIsProvided(){
        Instrument instrument = Instrument.builder().name("IBM").build();
        assertEquals("IBM",instrument.getName());
    }
}