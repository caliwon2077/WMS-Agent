package com.wms.backend.modules.mdm.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class WorkshopSaveRequest {
    @NotBlank(message = "Workshop code is required")
    private String code;
    @NotBlank(message = "Workshop name is required")
    private String name;
    @NotBlank(message = "Area name is required")
    private String areaName;
    @NotBlank(message = "Owner is required")
    private String owner;
    @NotNull(message = "Team count is required")
    @Min(value = 0, message = "Team count cannot be less than 0")
    private Integer teamCount;
    @NotBlank(message = "Status is required")
    private String status;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public Integer getTeamCount() { return teamCount; }
    public void setTeamCount(Integer teamCount) { this.teamCount = teamCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
