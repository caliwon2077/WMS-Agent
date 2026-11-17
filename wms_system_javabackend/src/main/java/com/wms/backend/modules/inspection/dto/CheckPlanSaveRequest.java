package com.wms.backend.modules.inspection.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class CheckPlanSaveRequest {
    @NotBlank(message = "计划编号不能为空")
    private String code;
    @NotBlank(message = "计划名称不能为空")
    private String name;
    @NotBlank(message = "点检对象不能为空")
    private String target;
    @NotBlank(message = "频率不能为空")
    private String frequency;
    @NotBlank(message = "执行人不能为空")
    private String executor;
    @NotBlank(message = "状态不能为空")
    private String status;
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;
    private String cycleRule;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public String getExecutor() { return executor; }
    public void setExecutor(String executor) { this.executor = executor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public String getCycleRule() { return cycleRule; }
    public void setCycleRule(String cycleRule) { this.cycleRule = cycleRule; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
