package com.digitalchargingsolutions.ChargeDetailRecords.controller;

import com.digitalchargingsolutions.ChargeDetailRecords.model.ChargeDetailRecord;
import com.digitalchargingsolutions.ChargeDetailRecords.service.ChargeDetailRecordService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 This class serves as the REST API for managing Charge Detail Records (CDR) in real time to a network of Charge Point Operators (CPO).
 */
@RestController
@RequestMapping("/cdr")
public class ChargeDetailRecordController {

    private final ChargeDetailRecordService cdrService;

    public ChargeDetailRecordController(ChargeDetailRecordService cdrService) {
        this.cdrService = cdrService;
    }
    /**
     Creates a new charge detail record in the system.
     @param cdr The charge detail record to create.
     @return A CompletableFuture that resolves to the response entity.
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<ChargeDetailRecord>> createChargeDetailRecord(
            @Validated @RequestBody ChargeDetailRecord cdr) {
        return cdrService.createChargeDetailRecord(cdr)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    if (throwable.getCause() instanceof IllegalArgumentException) {
                        return ResponseEntity.badRequest().body(new ChargeDetailRecord(throwable.getMessage()));
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ChargeDetailRecord("INTERNAL SERVER ERROR"));
                    }
                });
    }

    /**
     Retrieves the charge detail record with the given ID.
     @param id The ID of the charge detail record to retrieve.
     @return A CompletableFuture that resolves to the response entity.
     */
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ChargeDetailRecord>> getChargeDetailRecordById(@PathVariable String id) {
        return cdrService.getChargeDetailRecordById(id)
                .thenApply(cdr -> ResponseEntity.ok().body(cdr))
                .exceptionally(throwable -> {
                    if (throwable.getCause() instanceof RuntimeException) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ChargeDetailRecord(throwable.getMessage()));
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                    }
                });
    }

    /**
     Retrieves all charge detail records for the given vehicle ID.
     @param id The ID of the vehicle to retrieve charge detail records for.
     @param sortBy The field to sort the charge detail records by.
     @param sortDirection The direction to sort the charge detail records in.
     @return A CompletableFuture that resolves to the response entity.
     */
    @GetMapping("/vehicle/{id}")
    public CompletableFuture<ResponseEntity<List<ChargeDetailRecord>>> getChargeDetailRecordsByVehicleId(
            @PathVariable String id,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection) {
        return cdrService.getChargeDetailRecordsByVehicleId(id, sortBy, sortDirection)
                .thenApply(cdrs -> ResponseEntity.ok().body(cdrs))
                .exceptionally(throwable -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
    }
}
