package com.wms.backend.modules.mdm.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.mdm.dto.WorkshopSaveRequest;
import com.wms.backend.modules.mdm.entity.Workshop;
import com.wms.backend.modules.mdm.service.WorkshopService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/mdm/workshops")
public class WorkshopController {
    private final WorkshopService service;
    public WorkshopController(WorkshopService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<Workshop>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                       @RequestParam(value = "status", required = false) String status) {
        List<Workshop> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<Workshop>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<Workshop> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<Workshop> create(@Valid @RequestBody WorkshopSaveRequest request) {
        return ApiResponse.success("Created successfully", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Workshop> update(@PathVariable("id") String id, @Valid @RequestBody WorkshopSaveRequest request) {
        return ApiResponse.success("Updated successfully", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("Deleted successfully", null);
    }
}
