package com.digitalchargingsolutions.ChargeDetailRecords.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "charge_detail_record")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargeDetailRecord {

    @Id
    private String id;

    @Field("session_id")
    @Indexed(name = "sessionIdIndex", unique = true)
    @NotNull
    private String sessionId;

    @Field("vehicle_id")
    @NotNull
    private String vehicleId;

    @Field("start_time")
    @NotNull
    private LocalDateTime startTime;

    @Field("end_time")
    @NotNull
    private LocalDateTime endTime;

    @Field("total_cost")
    @NotNull
    private BigDecimal totalCost;

    @Transient
    private String error;

    public ChargeDetailRecord() {
    }

    public ChargeDetailRecord(String error) {
        this.error = error;
    }

    public ChargeDetailRecord(String sessionId, String vehicleId, LocalDateTime startTime,
                              LocalDateTime endTime, BigDecimal totalCost) {
        this.sessionId = sessionId;
        this.vehicleId = vehicleId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ChargeDetailRecord{" +
                "id=" + id +
                ", sessionId='" + sessionId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", totalCost=" + totalCost +
                '}';
    }
}
