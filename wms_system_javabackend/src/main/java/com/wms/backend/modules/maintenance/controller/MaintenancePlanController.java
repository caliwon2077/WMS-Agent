package com.wms.backend.modules.maintenance.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.maintenance.dto.MaintenancePlanSaveRequest;
import com.wms.backend.modules.maintenance.entity.MaintenancePlan;
import com.wms.backend.modules.maintenance.service.MaintenancePlanService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/maintenance/plans")
public class MaintenancePlanController {
    private final MaintenancePlanService service;
    public MaintenancePlanController(MaintenancePlanService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<MaintenancePlan>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                              @RequestParam(value = "status", required = false) String status) {
        List<MaintenancePlan> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<MaintenancePlan>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<MaintenancePlan> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<MaintenancePlan> create(@Valid @RequestBody MaintenancePlanSaveRequest request) { return ApiResponse.success("新增成功", service.create(request)); }

    @PutMapping("/{id}")
    public ApiResponse<MaintenancePlan> update(@PathVariable("id") String id, @Valid @RequestBody MaintenancePlanSaveRequest request) { return ApiResponse.success("修改成功", service.update(id, request)); }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("删除成功", null); }
}
