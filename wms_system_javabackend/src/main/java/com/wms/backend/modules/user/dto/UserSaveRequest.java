package com.wms.backend.modules.user.dto;

import javax.validation.constraints.NotBlank;

public class UserSaveRequest {
    @NotBlank(message = "账号不能为空")
    private String account;
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotBlank(message = "部门不能为空")
    private String dept;
    @NotBlank(message = "角色不能为空")
    private String role;
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    @NotBlank(message = "状态不能为空")
    private String status;
    private String email;
    private String syncStatus;
    private String remark;
    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSyncStatus() { return syncStatus; }
    public void setSyncStatus(String syncStatus) { this.syncStatus = syncStatus; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
