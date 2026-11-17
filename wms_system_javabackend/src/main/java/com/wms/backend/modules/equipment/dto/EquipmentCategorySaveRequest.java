package com.wms.backend.modules.equipment.dto;

import javax.validation.constraints.NotBlank;

public class EquipmentCategorySaveRequest {
    @NotBlank(message = "分类编码不能为空")
    private String code;
    @NotBlank(message = "分类名称不能为空")
    private String name;
    @NotBlank(message = "上级分类不能为空")
    private String parentCategory;
    @NotBlank(message = "点检策略不能为空")
    private String checkStrategy;
    @NotBlank(message = "保养策略不能为空")
    private String maintenanceStrategy;
    @NotBlank(message = "状态不能为空")
    private String status;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getParentCategory() { return parentCategory; }
    public void setParentCategory(String parentCategory) { this.parentCategory = parentCategory; }
    public String getCheckStrategy() { return checkStrategy; }
    public void setCheckStrategy(String checkStrategy) { this.checkStrategy = checkStrategy; }
    public String getMaintenanceStrategy() { return maintenanceStrategy; }
    public void setMaintenanceStrategy(String maintenanceStrategy) { this.maintenanceStrategy = maintenanceStrategy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
