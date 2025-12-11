package com.nttdata.bootcamp.service;

import com.nttdata.bootcamp.events.EventKafka;
import reactor.core.publisher.Mono;

public interface KafkaService {
    Mono<Void> consumerSave(EventKafka<?> eventKafka);
}
