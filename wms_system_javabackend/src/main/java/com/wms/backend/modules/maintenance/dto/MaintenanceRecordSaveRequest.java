package com.wms.backend.modules.maintenance.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MaintenanceRecordSaveRequest {
    @NotBlank(message = "记录编号不能为空")
    private String code;

    @NotBlank(message = "保养对象不能为空")
    private String target;

    @NotBlank(message = "执行人不能为空")
    private String executor;

    @NotNull(message = "保养日期不能为空")
    private LocalDate maintainDate;

    @NotBlank(message = "保养结果不能为空")
    private String result;

    @NotBlank(message = "状态不能为空")
    private String status;

    private String planName;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }

    public String getExecutor() { return executor; }
    public void setExecutor(String executor) { this.executor = executor; }

    public LocalDate getMaintainDate() { return maintainDate; }
    public void setMaintainDate(LocalDate maintainDate) { this.maintainDate = maintainDate; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
