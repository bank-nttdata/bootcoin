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

    private final KafkaSender<String, BootCoin> kafkaSender;

    @Autowired
    public KafkaServiceImpl(KafkaSender<String, BootCoin> kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    @KafkaListener(
            topics = "${topic.bootCoin.name:topic_bootCoin}",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "grupo1")
    public Mono<Void> consumerSave(EventKafka<?> eventKafka) {
        if (eventKafka instanceof BootCoinCreatedEventKafka) {
            BootCoinCreatedEventKafka createdEvent = (BootCoinCreatedEventKafka) eventKafka;
            log.info("Received Data created event .... with Id={}, data={}",
                    createdEvent.getId(),
                    createdEvent.getData());

            VirtualCoinKafkaDto virtualCoinKafkaDto = createdEvent.getData();
            BootCoin bootCoin = new BootCoin();
            bootCoin.setMount(virtualCoinKafkaDto.getMount());
            bootCoin.setCellNumber(virtualCoinKafkaDto.getCellNumberReceive());
            bootCoin.setNumberTransaction(UUID.randomUUID().toString());

            // Crear el ProducerRecord
            ProducerRecord<String, BootCoin> producerRecord = new ProducerRecord<>("your-topic", bootCoin.getNumberTransaction(), bootCoin);

            // Crear el SenderRecord usando el ProducerRecord
            SenderRecord<String, BootCoin, String> senderRecord = SenderRecord.create(producerRecord, bootCoin.getNumberTransaction());

            // Enviar el evento a Kafka de forma reactiva
            return kafkaSender.send(Mono.just(senderRecord))
                    .doOnTerminate(() -> log.info("Message sent to Kafka"))
                    .doOnError(error -> log.error("Error sending message to Kafka", error))
                    .then();  // Mono<Void> para indicar que el proceso ha terminado
        }
        return Mono.empty();  // Si no es el tipo esperado, no hacer nada
    }
}

