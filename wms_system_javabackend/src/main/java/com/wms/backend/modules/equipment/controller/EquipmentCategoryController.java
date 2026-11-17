package com.wms.backend.modules.equipment.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.equipment.dto.EquipmentCategorySaveRequest;
import com.wms.backend.modules.equipment.entity.EquipmentCategory;
import com.wms.backend.modules.equipment.service.EquipmentCategoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/equipment/categories")
public class EquipmentCategoryController {
    private final EquipmentCategoryService service;
    public EquipmentCategoryController(EquipmentCategoryService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<EquipmentCategory>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                                @RequestParam(value = "status", required = false) String status) {
        List<EquipmentCategory> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<EquipmentCategory>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<EquipmentCategory> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<EquipmentCategory> create(@Valid @RequestBody EquipmentCategorySaveRequest request) {
        return ApiResponse.success("新增成功", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<EquipmentCategory> update(@PathVariable("id") String id, @Valid @RequestBody EquipmentCategorySaveRequest request) {
        return ApiResponse.success("修改成功", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("删除成功", null);
    }
}
