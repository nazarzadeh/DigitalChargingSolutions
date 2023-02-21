package com.digitalchargingsolutions.ChargeDetailRecords.repo;

import com.digitalchargingsolutions.ChargeDetailRecords.model.ChargeDetailRecord;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface ChargeDetailRecordRepository extends MongoRepository<ChargeDetailRecord, String> {
    List<ChargeDetailRecord> findByVehicleId(String vehicleId, Sort sort);
    List<ChargeDetailRecord> findByVehicleId(String vehicleId);
}
