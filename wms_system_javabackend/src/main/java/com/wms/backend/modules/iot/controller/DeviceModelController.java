package com.wms.backend.modules.iot.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.iot.dto.DeviceModelSaveRequest;
import com.wms.backend.modules.iot.entity.DeviceModel;
import com.wms.backend.modules.iot.service.DeviceModelService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/iot/device-models")
public class DeviceModelController {
    private final DeviceModelService service;
    public DeviceModelController(DeviceModelService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<DeviceModel>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                          @RequestParam(value = "status", required = false) String status) {
        List<DeviceModel> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<DeviceModel>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<DeviceModel> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<DeviceModel> create(@Valid @RequestBody DeviceModelSaveRequest request) {
        return ApiResponse.success("新增成功", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<DeviceModel> update(@PathVariable("id") String id, @Valid @RequestBody DeviceModelSaveRequest request) {
        return ApiResponse.success("修改成功", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("删除成功", null);
    }
}
