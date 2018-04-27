package ru.borsch.bittrex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.borsch.bittrex.model.Currency;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    List<Currency> findByName(String name);
}
