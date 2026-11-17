package com.wms.backend.modules.accessory.entity;

import java.time.LocalDateTime;

public class AccessoryWarehouse {
    private String id;
    private String code;
    private String name;
    private Integer zoneCount;
    private String manager;
    private Integer stockTotal;
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
    public Integer getZoneCount() { return zoneCount; }
    public void setZoneCount(Integer zoneCount) { this.zoneCount = zoneCount; }
    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }
    public Integer getStockTotal() { return stockTotal; }
    public void setStockTotal(Integer stockTotal) { this.stockTotal = stockTotal; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
