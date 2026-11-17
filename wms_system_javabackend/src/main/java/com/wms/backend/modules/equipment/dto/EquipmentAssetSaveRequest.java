package com.wms.backend.modules.equipment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class EquipmentAssetSaveRequest {

    @NotBlank(message = "设备编码不能为空")
    private String code;

    @NotBlank(message = "设备名称不能为空")
    private String name;

    @NotBlank(message = "设备分类不能为空")
    private String category;

    @NotBlank(message = "所属车间不能为空")
    private String workshop;

    @NotBlank(message = "负责人不能为空")
    private String owner;

    @NotBlank(message = "设备状态不能为空")
    private String status;

    @NotNull(message = "购置日期不能为空")
    private LocalDate purchaseDate;

    private String model;
    private String supplier;
    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWorkshop() {
        return workshop;
    }

    public void setWorkshop(String workshop) {
        this.workshop = workshop;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
