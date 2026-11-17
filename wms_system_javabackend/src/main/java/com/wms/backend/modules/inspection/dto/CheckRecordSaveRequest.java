package com.wms.backend.modules.inspection.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CheckRecordSaveRequest {
    @NotBlank(message = "记录编号不能为空")
    private String code;
    @NotBlank(message = "设备名称不能为空")
    private String equipmentName;
    @NotBlank(message = "执行人不能为空")
    private String executor;
    @NotNull(message = "执行时间不能为空")
    private LocalDateTime executeTime;
    @NotBlank(message = "结果不能为空")
    private String result;
    @NotBlank(message = "状态不能为空")
    private String status;
    private String planName;
    private String abnormalDesc;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }
    public String getExecutor() { return executor; }
    public void setExecutor(String executor) { this.executor = executor; }
    public LocalDateTime getExecuteTime() { return executeTime; }
    public void setExecuteTime(LocalDateTime executeTime) { this.executeTime = executeTime; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public String getAbnormalDesc() { return abnormalDesc; }
    public void setAbnormalDesc(String abnormalDesc) { this.abnormalDesc = abnormalDesc; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
