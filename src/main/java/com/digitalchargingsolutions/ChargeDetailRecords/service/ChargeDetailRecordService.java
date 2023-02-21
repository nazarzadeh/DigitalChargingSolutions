package com.digitalchargingsolutions.ChargeDetailRecords.service;

import com.digitalchargingsolutions.ChargeDetailRecords.model.ChargeDetailRecord;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Sort;

public interface ChargeDetailRecordService {

    CompletableFuture<ChargeDetailRecord> createChargeDetailRecord(ChargeDetailRecord cdr);

    CompletableFuture<ChargeDetailRecord> getChargeDetailRecordById(String id);

    CompletableFuture<List<ChargeDetailRecord>> getChargeDetailRecordsByVehicleId(String vehicleId,
                                                                                  String sortBy,
                                                                                  Sort.Direction sortDirection);

}

