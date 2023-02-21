package com.digitalchargingsolutions.ChargeDetailRecords.service;

import com.digitalchargingsolutions.ChargeDetailRecords.model.ChargeDetailRecord;
import com.digitalchargingsolutions.ChargeDetailRecords.repo.ChargeDetailRecordRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
@Service
public class ChargeDetailRecordServiceImpl implements ChargeDetailRecordService {

    private static final Logger logger = LoggerFactory.getLogger(ChargeDetailRecordServiceImpl.class);

    private final ChargeDetailRecordRepository repository;
    private final Executor cdrExecutor;

    public ChargeDetailRecordServiceImpl(
            ChargeDetailRecordRepository repository,
            @Qualifier("cdrExecutor") Executor cdrExecutor) {
        this.repository = repository;
        this.cdrExecutor = cdrExecutor;
    }

    /**
     * Creates a new Charge Detail Record in the system.
     *
     * @param cdr The Charge Detail Record to create.
     * @return A CompletableFuture that will eventually contain the newly created Charge Detail Record.
     * @throws IllegalArgumentException If any of the input parameters are invalid.
     */
    public CompletableFuture<ChargeDetailRecord> createChargeDetailRecord(ChargeDetailRecord cdr) {
        logger.info("Creating charge detail record: {}", cdr);
        return CompletableFuture.supplyAsync(() -> {
            // validate input
            if (cdr.getEndTime().isBefore(cdr.getStartTime())) {
                logger.error("End time is before start time: {}", cdr);
                throw new IllegalArgumentException("End time cannot be before start time");
            }
            List<ChargeDetailRecord> previousCdrs = repository.findByVehicleId(cdr.getVehicleId());
            if (!previousCdrs.isEmpty() && (cdr.getStartTime().isBefore(previousCdrs.get(previousCdrs.size() - 1).getEndTime())
                    || cdr.getStartTime().isEqual(previousCdrs.get(previousCdrs.size() - 1).getEndTime()))) {
                logger.error("Start time is before previous end time: {}", cdr);
                throw new IllegalArgumentException("Start time must be after previous end time");
            }
            if (cdr.getTotalCost().compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("Total cost is less than or equal to 0: {}", cdr);
                throw new IllegalArgumentException("Total cost must be greater than 0");
            }
            // save to repository and return saved object
            ChargeDetailRecord savedCdr = repository.save(cdr);
            logger.info("Charge detail record created: {}", savedCdr);
            return savedCdr;
        }, cdrExecutor);
    }

    /**
     * Retrieves a Charge Detail Record from the system by its unique identifier.
     *
     * @param id The unique identifier of the Charge Detail Record to retrieve.
     * @return A CompletableFuture that will eventually contain the retrieved Charge Detail Record.
     * @throws RuntimeException If no Charge Detail Record with the specified identifier could be found.
     */
    public CompletableFuture<ChargeDetailRecord> getChargeDetailRecordById(String id) {
        logger.info("Getting charge detail record by ID: {}", id);
        return CompletableFuture.supplyAsync(() -> {
            Optional<ChargeDetailRecord> cdr = repository.findById(id);
            if (!cdr.isPresent()) {
                logger.error("Charge detail record not found with ID: {}", id);
                throw new RuntimeException("Charge detail record not found with id: " + id);
            }
            logger.info("Charge detail record found by ID: {}", cdr.get());
            return cdr.get();
        });
    }

    /**
     * Retrieves all Charge Detail Records associated with a particular vehicle.
     *
     * @param vehicleId The unique identifier of the vehicle to retrieve Charge Detail Records for.
     * @param sortBy The name of the field to sort the results by.
     * @param sortDirection The direction to sort the results in (ascending or descending).
     * @return A CompletableFuture that will eventually contain a List of all Charge Detail Records for the specified vehicle.
     */
    public CompletableFuture<List<ChargeDetailRecord>> getChargeDetailRecordsByVehicleId(String vehicleId,
                                                                                         String sortBy,
                                                                                         Sort.Direction sortDirection) {
        logger.info("Getting charge detail records by vehicle ID: {}, sortBy: {}, sortDirection: {}", vehicleId, sortBy, sortDirection);
        return CompletableFuture.supplyAsync(() -> {
            // filter CDRs by vehicle ID and time range
            Sort sort = Sort.by(sortDirection, sortBy);
            List<ChargeDetailRecord> cdrs = repository.findByVehicleId(vehicleId, sort);
            logger.info("Charge detail records found by vehicle ID: {}", cdrs);
            return cdrs;
        }, cdrExecutor);
    }
}
