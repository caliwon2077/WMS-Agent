package com.wms.backend.modules.maintenance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MaintenanceRecord {
    private String id;
    private String code;
    private String target;
    private String executor;
    private LocalDate maintainDate;
    private String result;
    private String status;
    private String planName;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public String getExecutor() { return executor; }
    public void setExecutor(String executor) { this.executor = executor; }
    public LocalDate getMaintainDate() { return maintainDate; }
    public void setMaintainDate(LocalDate maintainDate) { this.maintainDate = maintainDate; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
