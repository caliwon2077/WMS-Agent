package com.wms.backend.modules.iot.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DeviceModelSaveRequest {
    @NotBlank(message = "模型编码不能为空")
    private String code;
    @NotBlank(message = "模型名称不能为空")
    private String name;
    @NotNull(message = "采集点数不能为空")
    @Min(value = 0, message = "采集点数不能小于 0")
    private Integer pointCount;
    @NotNull(message = "告警规则数不能为空")
    @Min(value = 0, message = "告警规则数不能小于 0")
    private Integer alarmRuleCount;
    @NotBlank(message = "接入协议不能为空")
    private String protocol;
    @NotBlank(message = "状态不能为空")
    private String status;
    private String remark;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getPointCount() { return pointCount; }
    public void setPointCount(Integer pointCount) { this.pointCount = pointCount; }
    public Integer getAlarmRuleCount() { return alarmRuleCount; }
    public void setAlarmRuleCount(Integer alarmRuleCount) { this.alarmRuleCount = alarmRuleCount; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
