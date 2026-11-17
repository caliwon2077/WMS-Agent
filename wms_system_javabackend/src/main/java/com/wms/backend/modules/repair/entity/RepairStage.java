package com.wms.backend.modules.repair.entity;

import java.time.LocalDateTime;

public class RepairStage {
    private String id;
    private String code;
    private String name;
    private Integer sortOrder;
    private String allowRollback;
    private String slaHours;
    private String status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getAllowRollback() { return allowRollback; }
    public void setAllowRollback(String allowRollback) { this.allowRollback = allowRollback; }
    public String getSlaHours() { return slaHours; }
    public void setSlaHours(String slaHours) { this.slaHours = slaHours; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
