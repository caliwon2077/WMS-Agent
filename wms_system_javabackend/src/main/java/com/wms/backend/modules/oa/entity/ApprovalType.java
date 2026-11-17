package com.wms.backend.modules.oa.entity;

import java.time.LocalDateTime;

public class ApprovalType {
    private String id;
    private String code;
    private String name;
    private String bindFlow;
    private String strategy;
    private Integer modifyCount;
    private String status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
