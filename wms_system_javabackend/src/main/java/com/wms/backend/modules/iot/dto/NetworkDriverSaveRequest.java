package com.wms.backend.modules.iot.dto;

import javax.validation.constraints.NotBlank;

public class NetworkDriverSaveRequest {
    @NotBlank(message = "驱动编码不能为空")
    private String code;
    @NotBlank(message = "驱动名称不能为空")
    private String name;
    @NotBlank(message = "协议不能为空")
    private String protocol;
    @NotBlank(message = "版本不能为空")
    private String version;
    @NotBlank(message = "供应商不能为空")
    private String vendor;
    @NotBlank(message = "状态不能为空")
    private String status;
    private String configTemplate;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getConfigTemplate() { return configTemplate; }
    public void setConfigTemplate(String configTemplate) { this.configTemplate = configTemplate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
