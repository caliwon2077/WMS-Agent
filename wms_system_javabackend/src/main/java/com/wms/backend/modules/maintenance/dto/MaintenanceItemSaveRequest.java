package com.wms.backend.modules.maintenance.dto;

import javax.validation.constraints.NotBlank;

public class MaintenanceItemSaveRequest {
    @NotBlank(message = "项目编号不能为空")
    private String code;

    @NotBlank(message = "项目名称不能为空")
    private String name;

    @NotBlank(message = "标准工时不能为空")
    private String standardHours;

    @NotBlank(message = "适用对象不能为空")
    private String targetType;

    @NotBlank(message = "风险等级不能为空")
    private String riskLevel;

    @NotBlank(message = "状态不能为空")
    private String status;

    private String executeStandard;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStandardHours() { return standardHours; }
    public void setStandardHours(String standardHours) { this.standardHours = standardHours; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getExecuteStandard() { return executeStandard; }
    public void setExecuteStandard(String executeStandard) { this.executeStandard = executeStandard; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
