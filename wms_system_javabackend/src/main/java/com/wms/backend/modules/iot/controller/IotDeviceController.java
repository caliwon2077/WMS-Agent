package com.wms.backend.modules.iot.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.iot.dto.IotDeviceSaveRequest;
import com.wms.backend.modules.iot.entity.IotDevice;
import com.wms.backend.modules.iot.service.IotDeviceService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/iot/device-instances")
public class IotDeviceController {
    private final IotDeviceService service;
    public IotDeviceController(IotDeviceService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<IotDevice>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "status", required = false) String status) {
        List<IotDevice> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<IotDevice>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<IotDevice> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<IotDevice> create(@Valid @RequestBody IotDeviceSaveRequest request) {
        return ApiResponse.success("新增成功", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<IotDevice> update(@PathVariable("id") String id, @Valid @RequestBody IotDeviceSaveRequest request) {
        return ApiResponse.success("修改成功", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("删除成功", null);
    }
}
