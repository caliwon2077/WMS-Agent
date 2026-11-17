package com.wms.backend.modules.oa.dto;

import javax.validation.constraints.NotBlank;

public class OaSettingSaveRequest {
    @NotBlank(message = "Code is required")
    private String code;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Current value is required")
    private String currentValue;
    @NotBlank(message = "Updated date is required")
    private String updatedAtLabel;
    @NotBlank(message = "Maintainer is required")
    private String maintainer;
    @NotBlank(message = "Status is required")
    private String status;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCurrentValue() { return currentValue; }
    public void setCurrentValue(String currentValue) { this.currentValue = currentValue; }
    public String getUpdatedAtLabel() { return updatedAtLabel; }
    public void setUpdatedAtLabel(String updatedAtLabel) { this.updatedAtLabel = updatedAtLabel; }
    public String getMaintainer() { return maintainer; }
    public void setMaintainer(String maintainer) { this.maintainer = maintainer; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
