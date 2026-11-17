package com.wms.backend.modules.mdm.dto;

import javax.validation.constraints.NotBlank;

public class MaterialSaveRequest {
    @NotBlank(message = "Material code is required")
    private String code;
    @NotBlank(message = "Material name is required")
    private String name;
    @NotBlank(message = "Specification is required")
    private String spec;
    @NotBlank(message = "Unit is required")
    private String unit;
    @NotBlank(message = "Category is required")
    private String category;
    @NotBlank(message = "Status is required")
    private String status;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
