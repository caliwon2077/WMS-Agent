package com.wms.backend.modules.inspection.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.inspection.dto.CheckRecordSaveRequest;
import com.wms.backend.modules.inspection.entity.CheckRecord;
import com.wms.backend.modules.inspection.service.CheckRecordService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/inspection/records")
public class CheckRecordController {
    private final CheckRecordService service;
    public CheckRecordController(CheckRecordService service) { this.service = service; }

    @GetMapping
    public ApiResponse<ApiPageResponse<CheckRecord>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                          @RequestParam(value = "status", required = false) String status) {
        List<CheckRecord> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<CheckRecord>(records, records.size()));
    }

    @GetMapping("/{id}")
    public ApiResponse<CheckRecord> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }

    @PostMapping
    public ApiResponse<CheckRecord> create(@Valid @RequestBody CheckRecordSaveRequest request) { return ApiResponse.success("新增成功", service.create(request)); }

    @PutMapping("/{id}")
    public ApiResponse<CheckRecord> update(@PathVariable("id") String id, @Valid @RequestBody CheckRecordSaveRequest request) { return ApiResponse.success("修改成功", service.update(id, request)); }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("删除成功", null); }
}
