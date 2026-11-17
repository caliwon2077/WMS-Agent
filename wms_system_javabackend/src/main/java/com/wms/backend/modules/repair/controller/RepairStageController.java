package com.wms.backend.modules.repair.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.repair.dto.RepairStageSaveRequest;
import com.wms.backend.modules.repair.entity.RepairStage;
import com.wms.backend.modules.repair.service.RepairStageService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/repair/stages")
public class RepairStageController {
    private final RepairStageService service;
    public RepairStageController(RepairStageService service) { this.service = service; }
    @GetMapping
    public ApiResponse<ApiPageResponse<RepairStage>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                          @RequestParam(value = "status", required = false) String status) {
        List<RepairStage> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<RepairStage>(records, records.size()));
    }
    @GetMapping("/{id}")
    public ApiResponse<RepairStage> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }
    @PostMapping
    public ApiResponse<RepairStage> create(@Valid @RequestBody RepairStageSaveRequest request) { return ApiResponse.success("新增成功", service.create(request)); }
    @PutMapping("/{id}")
    public ApiResponse<RepairStage> update(@PathVariable("id") String id, @Valid @RequestBody RepairStageSaveRequest request) { return ApiResponse.success("修改成功", service.update(id, request)); }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("删除成功", null); }
}
