package com.wms.backend.modules.mdm.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class WorkAreaSaveRequest {
    @NotBlank(message = "Work area code is required")
    private String code;
    @NotBlank(message = "Work area name is required")
    private String name;
    @NotBlank(message = "Parent area is required")
    private String parentArea;
    @NotBlank(message = "Owner is required")
    private String owner;
    @NotNull(message = "Equipment count is required")
    @Min(value = 0, message = "Equipment count cannot be less than 0")
    private Integer equipmentCount;
    @NotBlank(message = "Status is required")
    private String status;
    private String remark;

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
}
