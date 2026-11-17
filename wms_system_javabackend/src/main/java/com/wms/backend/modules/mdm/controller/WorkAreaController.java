package com.wms.backend.modules.mdm.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.mdm.dto.WorkAreaSaveRequest;
import com.wms.backend.modules.mdm.entity.WorkArea;
import com.wms.backend.modules.mdm.service.WorkAreaService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/mdm/work-areas")
public class WorkAreaController {
    private final WorkAreaService service;
    public WorkAreaController(WorkAreaService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<WorkArea>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                       @RequestParam(value = "status", required = false) String status) {
        List<WorkArea> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<WorkArea>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<WorkArea> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<WorkArea> create(@Valid @RequestBody WorkAreaSaveRequest request) {
        return ApiResponse.success("Created successfully", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<WorkArea> update(@PathVariable("id") String id, @Valid @RequestBody WorkAreaSaveRequest request) {
        return ApiResponse.success("Updated successfully", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("Deleted successfully", null);
    }
}
