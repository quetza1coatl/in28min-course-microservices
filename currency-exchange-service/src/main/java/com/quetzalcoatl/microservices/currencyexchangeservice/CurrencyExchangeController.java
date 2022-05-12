package com.quetzalcoatl.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
public class CurrencyExchangeController {
    private final Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
    private final Environment environment;
    private final CurrencyExchangeRepository repository;

    public CurrencyExchangeController(Environment environment, CurrencyExchangeRepository repository) {
        this.environment = environment;
        this.repository = repository;
    }

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(
            @PathVariable String from,
            @PathVariable String to
    ){
        logger.info("retrieve exchange values with params from={}, to={}", from, to);
        CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);
        if(currencyExchange == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        String port = environment.getProperty("local.server.port");
        //CHANGE-KUBERNETES
        String host = environment.getProperty("HOSTNAME");
        String version = "v0.0.11";
        currencyExchange.setEnvironment(port + " " + version + " " + host);
        return currencyExchange;
    }


}
