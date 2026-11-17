package com.wms.backend.modules.mdm.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.mdm.dto.MaterialSaveRequest;
import com.wms.backend.modules.mdm.entity.Material;
import com.wms.backend.modules.mdm.service.MaterialService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/mdm/materials")
public class MaterialController {
    private final MaterialService service;
    public MaterialController(MaterialService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<Material>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                       @RequestParam(value = "status", required = false) String status) {
        List<Material> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<Material>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<Material> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<Material> create(@Valid @RequestBody MaterialSaveRequest request) {
        return ApiResponse.success("Created successfully", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Material> update(@PathVariable("id") String id, @Valid @RequestBody MaterialSaveRequest request) {
        return ApiResponse.success("Updated successfully", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("Deleted successfully", null);
    }
}
