package com.nttdata.bootcamp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
    @Document(collection = "bootcoin")
public class BootCoin {
    @Id
    private String id;

    private String dni;
    private String cellNumber;
    private String email;
    private Number mount;
    private String numberTransaction;
}
