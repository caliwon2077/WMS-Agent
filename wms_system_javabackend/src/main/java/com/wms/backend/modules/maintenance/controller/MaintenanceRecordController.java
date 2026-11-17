package com.wms.backend.modules.maintenance.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.maintenance.dto.MaintenanceRecordSaveRequest;
import com.wms.backend.modules.maintenance.entity.MaintenanceRecord;
import com.wms.backend.modules.maintenance.service.MaintenanceRecordService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/maintenance/records")
public class MaintenanceRecordController {
    private final MaintenanceRecordService service;
    public MaintenanceRecordController(MaintenanceRecordService service) { this.service = service; }
    @GetMapping
    public ApiResponse<ApiPageResponse<MaintenanceRecord>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                                @RequestParam(value = "status", required = false) String status) {
        List<MaintenanceRecord> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<MaintenanceRecord>(records, records.size()));
    }
    @GetMapping("/{id}")
    public ApiResponse<MaintenanceRecord> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }
    @PostMapping
    public ApiResponse<MaintenanceRecord> create(@Valid @RequestBody MaintenanceRecordSaveRequest request) { return ApiResponse.success("新增成功", service.create(request)); }
    @PutMapping("/{id}")
    public ApiResponse<MaintenanceRecord> update(@PathVariable("id") String id, @Valid @RequestBody MaintenanceRecordSaveRequest request) { return ApiResponse.success("修改成功", service.update(id, request)); }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("删除成功", null); }
}
