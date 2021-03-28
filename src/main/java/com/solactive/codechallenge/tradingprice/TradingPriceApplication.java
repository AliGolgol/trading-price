package com.solactive.codechallenge.tradingprice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@SpringBootApplication
@EnableSwagger2
public class TradingPriceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradingPriceApplication.class, args);
    }
}
