package com.wms.backend.modules.oa.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ApprovalTypeSaveRequest {
    @NotBlank(message = "Code is required")
    private String code;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Bind flow is required")
    private String bindFlow;
    @NotBlank(message = "Strategy is required")
    private String strategy;
    @NotNull(message = "Modify count is required")
    @Min(value = 0, message = "Modify count cannot be less than 0")
    private Integer modifyCount;
    @NotBlank(message = "Status is required")
    private String status;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBindFlow() { return bindFlow; }
    public void setBindFlow(String bindFlow) { this.bindFlow = bindFlow; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public Integer getModifyCount() { return modifyCount; }
    public void setModifyCount(Integer modifyCount) { this.modifyCount = modifyCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
