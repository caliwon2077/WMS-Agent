package com.wms.backend.modules.repair.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RepairApply {
    private String id;
    private String ticketNo;
    private String equipmentName;
    private String level;
    private String stage;
    private String team;
    private String status;
    private String applicant;
    private LocalDate applyDate;
    private String faultType;
    private String contact;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTicketNo() { return ticketNo; }
    public void setTicketNo(String ticketNo) { this.ticketNo = ticketNo; }
    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }
    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }
    public LocalDate getApplyDate() { return applyDate; }
    public void setApplyDate(LocalDate applyDate) { this.applyDate = applyDate; }
    public String getFaultType() { return faultType; }
    public void setFaultType(String faultType) { this.faultType = faultType; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
