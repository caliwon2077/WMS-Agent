package com.wms.backend.modules.accessory.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AccessoryWarehouseSaveRequest {
    @NotBlank(message = "Code is required")
    private String code;
    @NotBlank(message = "Name is required")
    private String name;
    @NotNull(message = "Zone count is required")
    @Min(value = 0, message = "Zone count cannot be less than 0")
    private Integer zoneCount;
    @NotBlank(message = "Manager is required")
    private String manager;
    @NotNull(message = "Stock total is required")
    @Min(value = 0, message = "Stock total cannot be less than 0")
    private Integer stockTotal;
    @NotBlank(message = "Status is required")
    private String status;
    private String remark;

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
}
