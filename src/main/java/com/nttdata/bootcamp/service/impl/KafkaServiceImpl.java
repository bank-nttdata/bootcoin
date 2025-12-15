package com.nttdata.bootcamp.service.impl;

import com.nttdata.bootcamp.entity.BootCoin;
import com.nttdata.bootcamp.entity.dto.VirtualCoinKafkaDto;
import com.nttdata.bootcamp.events.BootCoinCreatedEventKafka;
import com.nttdata.bootcamp.events.EventKafka;
import com.nttdata.bootcamp.repository.BootCoinRepository;
import com.nttdata.bootcamp.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.UUID;

@Slf4j
@Service
public class KafkaServiceImpl implements KafkaService {

    private final BootCoinRepository bootCoinRepository;

    @Autowired
    public KafkaServiceImpl(BootCoinRepository bootCoinRepository) {
        this.bootCoinRepository = bootCoinRepository;
    }

    @KafkaListener(
            topics = "${topic.bootCoin.name:topic_bootCoin}",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "grupo1"
    )
    @Override
    public void consumerSave(EventKafka<?> eventKafka) {

        if (eventKafka instanceof BootCoinCreatedEventKafka) {

            BootCoinCreatedEventKafka createdEvent =
                    (BootCoinCreatedEventKafka) eventKafka;

            VirtualCoinKafkaDto dto = createdEvent.getData();

            BootCoin bootCoin = new BootCoin();
            bootCoin.setMount(dto.getMount());
            bootCoin.setCellNumber(dto.getCellNumberReceive());
            bootCoin.setNumberTransaction(UUID.randomUUID().toString());

            bootCoinRepository.save(bootCoin)
                    .doOnSuccess(b ->
                            log.info("BootCoin guardado correctamente: {}", b))
                    .doOnError(e ->
                            log.error("Error guardando BootCoin", e))
                    .subscribe();
        }
    }

}
