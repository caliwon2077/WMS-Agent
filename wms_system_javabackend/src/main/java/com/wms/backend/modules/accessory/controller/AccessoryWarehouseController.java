package com.wms.backend.modules.accessory.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.accessory.dto.AccessoryWarehouseSaveRequest;
import com.wms.backend.modules.accessory.entity.AccessoryWarehouse;
import com.wms.backend.modules.accessory.service.AccessoryWarehouseService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/accessory/warehouses")
public class AccessoryWarehouseController {
    private final AccessoryWarehouseService service;
    public AccessoryWarehouseController(AccessoryWarehouseService service) { this.service = service; }
    @GetMapping
    public ApiResponse<ApiPageResponse<AccessoryWarehouse>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                                 @RequestParam(value = "status", required = false) String status) {
        List<AccessoryWarehouse> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<AccessoryWarehouse>(records, records.size()));
    }
    @GetMapping("/{id}")
    public ApiResponse<AccessoryWarehouse> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }
    @PostMapping
    public ApiResponse<AccessoryWarehouse> create(@Valid @RequestBody AccessoryWarehouseSaveRequest request) { return ApiResponse.success("Created successfully", service.create(request)); }
    @PutMapping("/{id}")
    public ApiResponse<AccessoryWarehouse> update(@PathVariable("id") String id, @Valid @RequestBody AccessoryWarehouseSaveRequest request) { return ApiResponse.success("Updated successfully", service.update(id, request)); }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("Deleted successfully", null); }
}
