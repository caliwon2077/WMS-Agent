package com.wms.backend.modules.inspection.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.inspection.dto.CheckPlanSaveRequest;
import com.wms.backend.modules.inspection.entity.CheckPlan;
import com.wms.backend.modules.inspection.service.CheckPlanService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/inspection/plans")
public class CheckPlanController {
    private final CheckPlanService service;
    public CheckPlanController(CheckPlanService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<CheckPlan>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "status", required = false) String status) {
        List<CheckPlan> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<CheckPlan>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<CheckPlan> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<CheckPlan> create(@Valid @RequestBody CheckPlanSaveRequest request) {
        return ApiResponse.success("新增成功", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<CheckPlan> update(@PathVariable("id") String id, @Valid @RequestBody CheckPlanSaveRequest request) {
        return ApiResponse.success("修改成功", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ApiResponse.success("删除成功", null);
    }
}
