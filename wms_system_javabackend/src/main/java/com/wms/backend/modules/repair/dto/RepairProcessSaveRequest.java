package com.wms.backend.modules.repair.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RepairProcessSaveRequest {
    @NotBlank(message = "流程编码不能为空")
    private String code;
    @NotBlank(message = "流程名称不能为空")
    private String name;
    @NotBlank(message = "适用范围不能为空")
    private String scope;
    @NotNull(message = "节点数不能为空")
    @Min(value = 1, message = "节点数必须大于 0")
    private Integer nodeCount;
    @NotBlank(message = "审批方式不能为空")
    private String approvalMode;
    @NotBlank(message = "状态不能为空")
    private String status;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }
    public Integer getNodeCount() { return nodeCount; }
    public void setNodeCount(Integer nodeCount) { this.nodeCount = nodeCount; }
    public String getApprovalMode() { return approvalMode; }
    public void setApprovalMode(String approvalMode) { this.approvalMode = approvalMode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
