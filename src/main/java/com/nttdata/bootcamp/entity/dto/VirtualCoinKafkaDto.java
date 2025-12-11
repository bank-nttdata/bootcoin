package com.nttdata.bootcamp.entity.dto;

import lombok.Data;

@Data
public class VirtualCoinKafkaDto {
    private String cellNumberSend;
    private String cellNumberReceive;
    private Number mount;
}
