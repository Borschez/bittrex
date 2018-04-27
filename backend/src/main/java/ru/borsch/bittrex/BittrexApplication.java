package ru.borsch.bittrex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.borsch.bittrex.config.JpaConfiguration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Import(JpaConfiguration.class)
@SpringBootApplication(scanBasePackages={"ru.borsch.bittrex"})
@EnableScheduling
public class BittrexApplication {
    private static final Logger logger = LoggerFactory.getLogger(BittrexApplication.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        SpringApplication.run(BittrexApplication.class, args);
    }
}
