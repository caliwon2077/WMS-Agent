package com.wms.backend.modules.iot.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.iot.dto.NetworkDriverSaveRequest;
import com.wms.backend.modules.iot.entity.NetworkDriver;
import com.wms.backend.modules.iot.service.NetworkDriverService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/iot/network-drivers")
public class NetworkDriverController {
    private final NetworkDriverService service;
    public NetworkDriverController(NetworkDriverService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<NetworkDriver>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                            @RequestParam(value = "status", required = false) String status) {
        List<NetworkDriver> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<NetworkDriver>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<NetworkDriver> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<NetworkDriver> create(@Valid @RequestBody NetworkDriverSaveRequest request) {
        return ApiResponse.success("新增成功", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<NetworkDriver> update(@PathVariable("id") String id, @Valid @RequestBody NetworkDriverSaveRequest request) {
        return ApiResponse.success("修改成功", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("删除成功", null);
    }
}
