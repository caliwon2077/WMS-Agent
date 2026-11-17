package com.wms.backend.modules.equipment.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.equipment.dto.EquipmentAssetSaveRequest;
import com.wms.backend.modules.equipment.entity.EquipmentAsset;
import com.wms.backend.modules.equipment.service.EquipmentAssetService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/equipment/assets")
public class EquipmentAssetController {

    private final EquipmentAssetService service;

    public EquipmentAssetController(EquipmentAssetService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<ApiPageResponse<EquipmentAsset>> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status) {
        List<EquipmentAsset> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<EquipmentAsset>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<EquipmentAsset> detail(@PathVariable("id") String id) {
        return ApiResponse.success(service.detail(id));
    }

    @PostMapping
    public ApiResponse<EquipmentAsset> create(@Valid @RequestBody EquipmentAssetSaveRequest request) {
        return ApiResponse.success("新增成功", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<EquipmentAsset> update(@PathVariable("id") String id,
                                              @Valid @RequestBody EquipmentAssetSaveRequest request) {
        return ApiResponse.success("修改成功", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("删除成功", null);
    }
}
