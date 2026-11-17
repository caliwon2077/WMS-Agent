package com.wms.backend.modules.accessory.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.accessory.dto.AccessorySupplierSaveRequest;
import com.wms.backend.modules.accessory.entity.AccessorySupplier;
import com.wms.backend.modules.accessory.service.AccessorySupplierService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/accessory/suppliers")
public class AccessorySupplierController {
    private final AccessorySupplierService service;
    public AccessorySupplierController(AccessorySupplierService service) { this.service = service; }
    @GetMapping
    public ApiResponse<ApiPageResponse<AccessorySupplier>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                                @RequestParam(value = "status", required = false) String status) {
        List<AccessorySupplier> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<AccessorySupplier>(records, records.size()));
    }
    @GetMapping("/{id}")
    public ApiResponse<AccessorySupplier> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }
    @PostMapping
    public ApiResponse<AccessorySupplier> create(@Valid @RequestBody AccessorySupplierSaveRequest request) { return ApiResponse.success("Created successfully", service.create(request)); }
    @PutMapping("/{id}")
    public ApiResponse<AccessorySupplier> update(@PathVariable("id") String id, @Valid @RequestBody AccessorySupplierSaveRequest request) { return ApiResponse.success("Updated successfully", service.update(id, request)); }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("Deleted successfully", null); }
}
