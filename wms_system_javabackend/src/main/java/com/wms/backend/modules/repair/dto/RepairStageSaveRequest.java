package com.wms.backend.modules.repair.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RepairStageSaveRequest {
    @NotBlank(message = "阶段编码不能为空")
    private String code;
    @NotBlank(message = "阶段名称不能为空")
    private String name;
    @NotNull(message = "顺序不能为空")
    @Min(value = 1, message = "顺序必须大于 0")
    private Integer sortOrder;
    @NotBlank(message = "是否可回退不能为空")
    private String allowRollback;
    @NotBlank(message = "SLA 不能为空")
    private String slaHours;
    @NotBlank(message = "状态不能为空")
    private String status;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getAllowRollback() { return allowRollback; }
    public void setAllowRollback(String allowRollback) { this.allowRollback = allowRollback; }
    public String getSlaHours() { return slaHours; }
    public void setSlaHours(String slaHours) { this.slaHours = slaHours; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
