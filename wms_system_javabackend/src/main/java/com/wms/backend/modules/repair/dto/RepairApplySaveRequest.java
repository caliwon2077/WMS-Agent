package com.wms.backend.modules.repair.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class RepairApplySaveRequest {
    @NotBlank(message = "工单编号不能为空")
    private String ticketNo;
    @NotBlank(message = "设备名称不能为空")
    private String equipmentName;
    @NotBlank(message = "故障等级不能为空")
    private String level;
    @NotBlank(message = "当前阶段不能为空")
    private String stage;
    @NotBlank(message = "责任班组不能为空")
    private String team;
    @NotBlank(message = "状态不能为空")
    private String status;
    @NotBlank(message = "报修人不能为空")
    private String applicant;
    @NotNull(message = "报修日期不能为空")
    private LocalDate applyDate;
    private String faultType;
    private String contact;
    @NotBlank(message = "故障描述不能为空")
    private String description;
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
}
