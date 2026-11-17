package com.wms.backend.modules.equipment.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.equipment.dto.ManufacturerSaveRequest;
import com.wms.backend.modules.equipment.entity.Manufacturer;
import com.wms.backend.modules.equipment.service.ManufacturerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/equipment/manufacturers")
public class ManufacturerController {
    private final ManufacturerService service;
    public ManufacturerController(ManufacturerService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<Manufacturer>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                           @RequestParam(value = "status", required = false) String status) {
        List<Manufacturer> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<Manufacturer>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<Manufacturer> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<Manufacturer> create(@Valid @RequestBody ManufacturerSaveRequest request) {
        return ApiResponse.success("新增成功", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Manufacturer> update(@PathVariable("id") String id, @Valid @RequestBody ManufacturerSaveRequest request) {
        return ApiResponse.success("修改成功", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("删除成功", null);
    }
}
