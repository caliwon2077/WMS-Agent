package com.wms.backend.modules.accessory.dto;

import javax.validation.constraints.NotBlank;

public class AccessorySupplierSaveRequest {
    @NotBlank(message = "Code is required")
    private String code;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Contact is required")
    private String contact;
    @NotBlank(message = "Delivery cycle is required")
    private String deliveryCycle;
    @NotBlank(message = "Grade is required")
    private String grade;
    @NotBlank(message = "Status is required")
    private String status;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getDeliveryCycle() { return deliveryCycle; }
    public void setDeliveryCycle(String deliveryCycle) { this.deliveryCycle = deliveryCycle; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
