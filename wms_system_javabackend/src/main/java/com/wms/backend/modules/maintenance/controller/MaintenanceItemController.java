package com.wms.backend.modules.maintenance.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.maintenance.dto.MaintenanceItemSaveRequest;
import com.wms.backend.modules.maintenance.entity.MaintenanceItem;
import com.wms.backend.modules.maintenance.service.MaintenanceItemService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/maintenance/items")
public class MaintenanceItemController {
    private final MaintenanceItemService service;
    public MaintenanceItemController(MaintenanceItemService service) { this.service = service; }
    @GetMapping
    public ApiResponse<ApiPageResponse<MaintenanceItem>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                              @RequestParam(value = "status", required = false) String status) {
        List<MaintenanceItem> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<MaintenanceItem>(records, records.size()));
    }
    @GetMapping("/{id}")
    public ApiResponse<MaintenanceItem> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }
    @PostMapping
    public ApiResponse<MaintenanceItem> create(@Valid @RequestBody MaintenanceItemSaveRequest request) { return ApiResponse.success("新增成功", service.create(request)); }
    @PutMapping("/{id}")
    public ApiResponse<MaintenanceItem> update(@PathVariable("id") String id, @Valid @RequestBody MaintenanceItemSaveRequest request) { return ApiResponse.success("修改成功", service.update(id, request)); }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("删除成功", null); }
}
