package com.wms.backend.modules.iot.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class IotDeviceSaveRequest {
    @NotBlank(message = "实例编码不能为空")
    private String code;
    @NotBlank(message = "设备名称不能为空")
    private String name;
    @NotBlank(message = "模型不能为空")
    private String modelName;
    @NotBlank(message = "连接状态不能为空")
    private String connectionStatus;
    @NotNull(message = "最后上报时间不能为空")
    private LocalDateTime lastReportTime;
    @NotBlank(message = "负责人不能为空")
    private String owner;
    private String ipAddress;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public String getConnectionStatus() { return connectionStatus; }
    public void setConnectionStatus(String connectionStatus) { this.connectionStatus = connectionStatus; }
    public LocalDateTime getLastReportTime() { return lastReportTime; }
    public void setLastReportTime(LocalDateTime lastReportTime) { this.lastReportTime = lastReportTime; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
