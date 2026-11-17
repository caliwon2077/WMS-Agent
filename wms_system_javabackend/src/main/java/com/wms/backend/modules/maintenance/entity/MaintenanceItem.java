package com.wms.backend.modules.maintenance.entity;

import java.time.LocalDateTime;

public class MaintenanceItem {
    private String id;
    private String code;
    private String name;
    private String standardHours;
    private String targetType;
    private String riskLevel;
    private String status;
    private String executeStandard;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStandardHours() { return standardHours; }
    public void setStandardHours(String standardHours) { this.standardHours = standardHours; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getExecuteStandard() { return executeStandard; }
    public void setExecuteStandard(String executeStandard) { this.executeStandard = executeStandard; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
