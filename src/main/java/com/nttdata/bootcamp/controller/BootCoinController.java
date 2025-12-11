package com.nttdata.bootcamp.controller;

import com.nttdata.bootcamp.entity.BootCoin;
import com.nttdata.bootcamp.service.BootCoinService;
import com.nttdata.bootcamp.entity.dto.VirtualCoinDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Date;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/virtual-coin")
public class BootCoinController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootCoinController.class);

    @Autowired
    private BootCoinService bootCoinService;

    // All Virtual-coin Registered
    @GetMapping("/findAllVirtualCoin")
    public Flux<BootCoin> findAllVirtualCoin() {
        return bootCoinService.findAllBootCoin()
                .doOnSubscribe(sub -> LOGGER.info("Fetching all registered virtual coins"))
                .doOnTerminate(() -> LOGGER.info("All virtual coins fetched"))
                .log(); // Optionally log the Flux for debugging
    }

    // Virtual-coin registered by customer
    @GetMapping("/findVirtualCoinByCellNumber/{cellNumber}")
    public Mono<BootCoin> findVirtualCoinByCellNumber(@PathVariable("cellNumber") String cellNumber) {
        return bootCoinService.findBootCoinByCellNumber(cellNumber)
                .doOnSubscribe(sub -> LOGGER.info("Fetching virtual coin by cell number: {}", cellNumber))
                .doOnTerminate(() -> LOGGER.info("Virtual coin fetch completed for cell number: {}", cellNumber))
                .log(); // Optionally log the Mono for debugging
    }

    // Save Virtual-coin
    @PostMapping(value = "/saveVirtualCoin")
    public Mono<BootCoin> saveVirtualCoin(@RequestBody VirtualCoinDto virtualCoinDto) {

        BootCoin bootCoin = new BootCoin();
        bootCoin.setDni(virtualCoinDto.getDni());
        bootCoin.setEmail(virtualCoinDto.getEmail());
        bootCoin.setCellNumber(virtualCoinDto.getCellNumber());
        bootCoin.setMount(0.00);

        // Usar el servicio reactivo de forma directa
        return bootCoinService.saveBootCoin(bootCoin)
                .doOnSubscribe(sub -> LOGGER.info("Saving virtual coin with cell number: {}", virtualCoinDto.getCellNumber()))
                .doOnSuccess(savedCoin -> LOGGER.info("Virtual coin saved successfully: {}", savedCoin))
                .doOnError(e -> LOGGER.error("Error saving virtual coin", e))
                .log(); // Optionally log the Mono for debugging
    }
}
