package com.nttdata.bootcamp.events;

import com.nttdata.bootcamp.entity.dto.VirtualCoinKafkaDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BootCoinCreatedEventKafka extends EventKafka<VirtualCoinKafkaDto> {

}
