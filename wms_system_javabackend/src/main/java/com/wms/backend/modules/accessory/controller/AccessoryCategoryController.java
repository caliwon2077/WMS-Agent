package com.wms.backend.modules.accessory.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.accessory.dto.AccessoryCategorySaveRequest;
import com.wms.backend.modules.accessory.entity.AccessoryCategory;
import com.wms.backend.modules.accessory.service.AccessoryCategoryService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/accessory/categories")
public class AccessoryCategoryController {
    private final AccessoryCategoryService service;
    public AccessoryCategoryController(AccessoryCategoryService service) { this.service = service; }
    @GetMapping
    public ApiResponse<ApiPageResponse<AccessoryCategory>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                                @RequestParam(value = "status", required = false) String status) {
        List<AccessoryCategory> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<AccessoryCategory>(records, records.size()));
    }
    @GetMapping("/{id}")
    public ApiResponse<AccessoryCategory> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }
    @PostMapping
    public ApiResponse<AccessoryCategory> create(@Valid @RequestBody AccessoryCategorySaveRequest request) { return ApiResponse.success("Created successfully", service.create(request)); }
    @PutMapping("/{id}")
    public ApiResponse<AccessoryCategory> update(@PathVariable("id") String id, @Valid @RequestBody AccessoryCategorySaveRequest request) { return ApiResponse.success("Updated successfully", service.update(id, request)); }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("Deleted successfully", null); }
}
