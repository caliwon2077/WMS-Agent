package com.wms.backend.modules.mdm.entity;

import java.time.LocalDateTime;

public class WorkArea {
    private String id;
    private String code;
    private String name;
    private String parentArea;
    private String owner;
    private Integer equipmentCount;
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
    public String getParentArea() { return parentArea; }
    public void setParentArea(String parentArea) { this.parentArea = parentArea; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public Integer getEquipmentCount() { return equipmentCount; }
    public void setEquipmentCount(Integer equipmentCount) { this.equipmentCount = equipmentCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
