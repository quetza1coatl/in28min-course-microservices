package com.quetzalcoatl.microservices.currencyexchangeservice;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {
    private final Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api")
//    @Retry(name = "default")
//    @Retry(name = "sample-api", fallbackMethod = "hardcodedResponse")
    @CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponse")
    public String sampleApi(){
        logger.info("sample api is called");
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("localhost:8080/dummy-api", String.class);
        return forEntity.getBody();
    }

    public String hardcodedResponse(Exception e){
        logger.info("Fallback method is called after exception - {}", e.getClass());
        return "fallback-response";
    }

    @GetMapping("/limit")
    @RateLimiter(name = "default")
    public String limitedCalls(){
        return "Limited calls";
    }

    @GetMapping("/bulkhead")
    @RateLimiter(name = "default")
    public String bulkHead(){
        return "bulkhead";
    }
}
