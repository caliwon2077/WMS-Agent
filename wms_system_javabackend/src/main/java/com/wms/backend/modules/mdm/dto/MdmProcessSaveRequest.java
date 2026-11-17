package com.wms.backend.modules.mdm.dto;

import javax.validation.constraints.NotBlank;

public class MdmProcessSaveRequest {
    @NotBlank(message = "Process code is required")
    private String code;
    @NotBlank(message = "Process name is required")
    private String name;
    @NotBlank(message = "Standard hours is required")
    private String standardHours;
    @NotBlank(message = "Job type is required")
    private String jobType;
    @NotBlank(message = "Equipment name is required")
    private String equipmentName;
    @NotBlank(message = "Status is required")
    private String status;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStandardHours() { return standardHours; }
    public void setStandardHours(String standardHours) { this.standardHours = standardHours; }
    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
