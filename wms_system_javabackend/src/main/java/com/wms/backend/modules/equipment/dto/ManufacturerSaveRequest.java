package com.wms.backend.modules.equipment.dto;

import javax.validation.constraints.NotBlank;

public class ManufacturerSaveRequest {
    @NotBlank(message = "厂商编码不能为空")
    private String code;
    @NotBlank(message = "厂商名称不能为空")
    private String name;
    @NotBlank(message = "联系人不能为空")
    private String contact;
    @NotBlank(message = "联系电话不能为空")
    private String mobile;
    private String email;
    private String address;
    @NotBlank(message = "状态不能为空")
    private String status;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
