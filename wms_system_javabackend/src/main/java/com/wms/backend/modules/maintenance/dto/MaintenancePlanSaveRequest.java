package com.wms.backend.modules.maintenance.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class MaintenancePlanSaveRequest {
    @NotBlank(message = "计划编号不能为空")
    private String code;
    @NotBlank(message = "计划名称不能为空")
    private String name;
    @NotBlank(message = "保养对象不能为空")
    private String target;
    @NotBlank(message = "周期不能为空")
    private String cycle;
    @NotBlank(message = "负责人不能为空")
    private String owner;
    @NotBlank(message = "状态不能为空")
    private String status;
    @NotNull(message = "下次保养日期不能为空")
    private LocalDate nextDate;
    @NotBlank(message = "保养策略不能为空")
    private String strategy;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public String getCycle() { return cycle; }
    public void setCycle(String cycle) { this.cycle = cycle; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getNextDate() { return nextDate; }
    public void setNextDate(LocalDate nextDate) { this.nextDate = nextDate; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
