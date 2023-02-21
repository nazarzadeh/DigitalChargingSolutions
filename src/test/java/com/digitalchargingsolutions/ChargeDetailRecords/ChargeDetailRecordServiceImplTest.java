package com.digitalchargingsolutions.ChargeDetailRecords;

import com.digitalchargingsolutions.ChargeDetailRecords.model.ChargeDetailRecord;
import com.digitalchargingsolutions.ChargeDetailRecords.repo.ChargeDetailRecordRepository;
import com.digitalchargingsolutions.ChargeDetailRecords.service.ChargeDetailRecordService;
import com.digitalchargingsolutions.ChargeDetailRecords.service.ChargeDetailRecordServiceImpl;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class ChargeDetailRecordServiceImplTest {

    private ChargeDetailRecordService service;

    @Mock
    private ChargeDetailRecordRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        //    private Executor cdrExecutor;
        ExecutorService executor = Executors.newFixedThreadPool(3);
        service = new ChargeDetailRecordServiceImpl(repository, executor);
    }

    @Test
    void testCreateChargeDetailRecordSuccess() throws ExecutionException, InterruptedException {
        ChargeDetailRecord cdr = new ChargeDetailRecord();
        cdr.setSessionId("session-001");
        cdr.setVehicleId("vehicle-001");
        cdr.setStartTime(LocalDateTime.now());
        cdr.setEndTime(LocalDateTime.now().plusHours(1));
        cdr.setTotalCost(BigDecimal.TEN);

        when(repository.findByVehicleId(any())).thenReturn(new ArrayList<>());
        when(repository.save(any(ChargeDetailRecord.class))).thenReturn(cdr);

        CompletableFuture<ChargeDetailRecord> result = service.createChargeDetailRecord(cdr);
        assertNotNull(result.get());
    }

    @Test
    void testCreateChargeDetailRecordEndTimeBeforeStartTime() {
        ChargeDetailRecord cdr = new ChargeDetailRecord();
        cdr.setSessionId("session-001");
        cdr.setVehicleId("vehicle-001");
        cdr.setStartTime(LocalDateTime.now().plusHours(1));
        cdr.setEndTime(LocalDateTime.now());
        cdr.setTotalCost(BigDecimal.TEN);

        CompletableFuture<ChargeDetailRecord> chargeDetailRecord = service.createChargeDetailRecord(cdr);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                chargeDetailRecord.join();
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void testCreateChargeDetailRecordStartTimeBeforePreviousEndTime() {
        List<ChargeDetailRecord> previousCdrs = new ArrayList<>();
        ChargeDetailRecord previousCdr = new ChargeDetailRecord();
        previousCdr.setSessionId("session-001");
        previousCdr.setVehicleId("vehicle-001");
        previousCdr.setStartTime(LocalDateTime.now().minusHours(1));
        previousCdr.setEndTime(LocalDateTime.now());
        previousCdrs.add(previousCdr);

        ChargeDetailRecord cdr = new ChargeDetailRecord();
        cdr.setSessionId("session-002");
        cdr.setVehicleId("vehicle-001");
        cdr.setStartTime(LocalDateTime.now().minusMinutes(1));
        cdr.setEndTime(LocalDateTime.now().plusHours(1));
        cdr.setTotalCost(BigDecimal.TEN);

        when(repository.findByVehicleId(any())).thenReturn(previousCdrs);

        CompletableFuture<ChargeDetailRecord> chargeDetailRecord = service.createChargeDetailRecord(cdr);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                chargeDetailRecord.join();
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void testCreateChargeDetailRecordTotalCostLessThanOrEqualToZero() {
        ChargeDetailRecord cdr = new ChargeDetailRecord();
        cdr.setSessionId("session-001");
        cdr.setVehicleId("vehicle-001");
        cdr.setStartTime(LocalDateTime.now());
        cdr.setEndTime(LocalDateTime.now().plusHours(1));
        cdr.setTotalCost(BigDecimal.ZERO);

        CompletableFuture<ChargeDetailRecord> chargeDetailRecord = service.createChargeDetailRecord(cdr);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                chargeDetailRecord.join();
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void testGetChargeDetailRecordByIdSuccess() throws ExecutionException, InterruptedException {
        String id = "12345";
        ChargeDetailRecord cdr = new ChargeDetailRecord();
        cdr.setSessionId("session-001");
        cdr.setVehicleId("vehicle-001");
        cdr.setStartTime(LocalDateTime.now());
        cdr.setEndTime(LocalDateTime.now().plusHours(1));
        cdr.setTotalCost(BigDecimal.TEN);

        when(repository.findById(id)).thenReturn(Optional.of(cdr));

        CompletableFuture<ChargeDetailRecord> result = service.getChargeDetailRecordById(id);
        assertEquals(cdr, result.get());
    }
}
