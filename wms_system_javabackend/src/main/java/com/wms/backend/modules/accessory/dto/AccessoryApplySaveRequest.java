package com.wms.backend.modules.accessory.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AccessoryApplySaveRequest {
    @NotBlank(message = "申请单号不能为空")
    private String applyNo;
    @NotBlank(message = "申请人不能为空")
    private String applicant;
    @NotBlank(message = "配件名称不能为空")
    private String accessoryName;
    @NotNull(message = "申请数量不能为空")
    @Min(value = 1, message = "申请数量必须大于 0")
    private Integer quantity;
    @NotBlank(message = "审批状态不能为空")
    private String approvalStatus;
    @NotBlank(message = "出库状态不能为空")
    private String deliveryStatus;
    @NotBlank(message = "仓库不能为空")
    private String warehouse;
    @NotNull(message = "申请日期不能为空")
    private LocalDate applyDate;
    private String purpose;
    private String receiver;
    private String remark;
    public String getApplyNo() { return applyNo; }
    public void setApplyNo(String applyNo) { this.applyNo = applyNo; }
    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }
    public String getAccessoryName() { return accessoryName; }
    public void setAccessoryName(String accessoryName) { this.accessoryName = accessoryName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }
    public String getWarehouse() { return warehouse; }
    public void setWarehouse(String warehouse) { this.warehouse = warehouse; }
    public LocalDate getApplyDate() { return applyDate; }
    public void setApplyDate(LocalDate applyDate) { this.applyDate = applyDate; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
