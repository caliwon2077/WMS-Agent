package com.wms.backend.modules.repair.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.repair.dto.RepairApplySaveRequest;
import com.wms.backend.modules.repair.entity.RepairApply;
import com.wms.backend.modules.repair.service.RepairApplyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/repair/applies")
public class RepairApplyController {
    private final RepairApplyService service;
    public RepairApplyController(RepairApplyService service) { this.service = service; }
    @GetMapping
    public ApiResponse<ApiPageResponse<RepairApply>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                          @RequestParam(value = "status", required = false) String status) {
        List<RepairApply> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<RepairApply>(records, records.size()));
    }
    @GetMapping("/{id}")
    public ApiResponse<RepairApply> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }
    @PostMapping
    public ApiResponse<RepairApply> create(@Valid @RequestBody RepairApplySaveRequest request) { return ApiResponse.success("新增成功", service.create(request)); }
    @PutMapping("/{id}")
    public ApiResponse<RepairApply> update(@PathVariable("id") String id, @Valid @RequestBody RepairApplySaveRequest request) { return ApiResponse.success("修改成功", service.update(id, request)); }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("删除成功", null); }
}
