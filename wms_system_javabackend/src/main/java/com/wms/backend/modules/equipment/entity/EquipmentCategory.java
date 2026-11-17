package com.wms.backend.modules.equipment.entity;

import java.time.LocalDateTime;

public class EquipmentCategory {
    private String id;
    private String code;
    private String name;
    private String parentCategory;
    private String checkStrategy;
    private String maintenanceStrategy;
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
    public String getParentCategory() { return parentCategory; }
    public void setParentCategory(String parentCategory) { this.parentCategory = parentCategory; }
    public String getCheckStrategy() { return checkStrategy; }
    public void setCheckStrategy(String checkStrategy) { this.checkStrategy = checkStrategy; }
    public String getMaintenanceStrategy() { return maintenanceStrategy; }
    public void setMaintenanceStrategy(String maintenanceStrategy) { this.maintenanceStrategy = maintenanceStrategy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
