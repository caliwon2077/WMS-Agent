package com.wms.backend.modules.inspection.dto;

import javax.validation.constraints.NotBlank;

public class CheckItemSaveRequest {
    @NotBlank(message = "项目编码不能为空")
    private String code;
    @NotBlank(message = "项目名称不能为空")
    private String name;
    @NotBlank(message = "判定方式不能为空")
    private String judgeType;
    @NotBlank(message = "标准值不能为空")
    private String standardValue;
    @NotBlank(message = "适用设备不能为空")
    private String equipmentType;
    @NotBlank(message = "风险等级不能为空")
    private String riskLevel;
    @NotBlank(message = "状态不能为空")
    private String status;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getJudgeType() { return judgeType; }
    public void setJudgeType(String judgeType) { this.judgeType = judgeType; }
    public String getStandardValue() { return standardValue; }
    public void setStandardValue(String standardValue) { this.standardValue = standardValue; }
    public String getEquipmentType() { return equipmentType; }
    public void setEquipmentType(String equipmentType) { this.equipmentType = equipmentType; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
