package com.wms.backend.modules.repair.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.repair.dto.RepairProcessSaveRequest;
import com.wms.backend.modules.repair.entity.RepairProcess;
import com.wms.backend.modules.repair.service.RepairProcessService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/repair/processes")
public class RepairProcessController {
    private final RepairProcessService service;
    public RepairProcessController(RepairProcessService service) { this.service = service; }
    @GetMapping
    public ApiResponse<ApiPageResponse<RepairProcess>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                            @RequestParam(value = "status", required = false) String status) {
        List<RepairProcess> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<RepairProcess>(records, records.size()));
    }
    @GetMapping("/{id}")
    public ApiResponse<RepairProcess> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }
    @PostMapping
    public ApiResponse<RepairProcess> create(@Valid @RequestBody RepairProcessSaveRequest request) { return ApiResponse.success("新增成功", service.create(request)); }
    @PutMapping("/{id}")
    public ApiResponse<RepairProcess> update(@PathVariable("id") String id, @Valid @RequestBody RepairProcessSaveRequest request) { return ApiResponse.success("修改成功", service.update(id, request)); }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("删除成功", null); }
}
