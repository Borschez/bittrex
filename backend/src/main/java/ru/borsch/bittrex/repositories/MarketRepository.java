package ru.borsch.bittrex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.borsch.bittrex.model.Market;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {

    List<Market> findByName(String name);
}
