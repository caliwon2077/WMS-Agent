package com.wms.backend.modules.system.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.system.service.SystemResourceService;
import java.util.List;
import java.util.Map;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/system/resources/{resourceKey}")
public class SystemResourceController {
    private final SystemResourceService service;

    public SystemResourceController(SystemResourceService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<ApiPageResponse<Map<String, Object>>> list(@PathVariable("resourceKey") String resourceKey,
                                                                  @RequestParam(value = "keyword", required = false) String keyword,
                                                                  @RequestParam(value = "status", required = false) String status) {
        List<Map<String, Object>> records = service.list(resourceKey, keyword, status);
        return ApiResponse.success(new ApiPageResponse<Map<String, Object>>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable("resourceKey") String resourceKey,
                                                   @PathVariable("id") String id) {
        return ApiResponse.success(service.detail(resourceKey, id));
    }

    @PostMapping
    public ApiResponse<Map<String, Object>> create(@PathVariable("resourceKey") String resourceKey,
                                                   @RequestBody Map<String, Object> payload) {
        return ApiResponse.success("Created successfully", service.create(resourceKey, payload));
    }

    @PutMapping("/{id}")
    public ApiResponse<Map<String, Object>> update(@PathVariable("resourceKey") String resourceKey,
                                                   @PathVariable("id") String id,
                                                   @RequestBody Map<String, Object> payload) {
        return ApiResponse.success("Updated successfully", service.update(resourceKey, id, payload));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("resourceKey") String resourceKey,
                                    @PathVariable("id") String id) {
        service.delete(resourceKey, id);
        return ApiResponse.success("Deleted successfully", null);
    }
}
