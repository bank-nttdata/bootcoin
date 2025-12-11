package com.nttdata.bootcamp.repository;

import com.nttdata.bootcamp.entity.BootCoin;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BootCoinRepository extends ReactiveCrudRepository<BootCoin, String> {
}
