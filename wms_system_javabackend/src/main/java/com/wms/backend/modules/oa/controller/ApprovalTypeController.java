package com.wms.backend.modules.oa.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.oa.dto.ApprovalTypeSaveRequest;
import com.wms.backend.modules.oa.entity.ApprovalType;
import com.wms.backend.modules.oa.service.ApprovalTypeService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/oa/approval-types")
public class ApprovalTypeController {
    private final ApprovalTypeService service;
    public ApprovalTypeController(ApprovalTypeService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<ApprovalType>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                           @RequestParam(value = "status", required = false) String status) {
        List<ApprovalType> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<ApprovalType>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<ApprovalType> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<ApprovalType> create(@Valid @RequestBody ApprovalTypeSaveRequest request) {
        return ApiResponse.success("Created successfully", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<ApprovalType> update(@PathVariable("id") String id, @Valid @RequestBody ApprovalTypeSaveRequest request) {
        return ApiResponse.success("Updated successfully", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("Deleted successfully", null);
    }
}
