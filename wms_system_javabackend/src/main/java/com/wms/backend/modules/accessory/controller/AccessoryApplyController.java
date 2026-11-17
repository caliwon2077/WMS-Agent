package com.wms.backend.modules.accessory.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.accessory.dto.AccessoryApplySaveRequest;
import com.wms.backend.modules.accessory.entity.AccessoryApply;
import com.wms.backend.modules.accessory.service.AccessoryApplyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/accessory/applies")
public class AccessoryApplyController {
    private final AccessoryApplyService service;
    public AccessoryApplyController(AccessoryApplyService service) { this.service = service; }
    @GetMapping
    public ApiResponse<ApiPageResponse<AccessoryApply>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                             @RequestParam(value = "status", required = false) String status) {
        List<AccessoryApply> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<AccessoryApply>(records, records.size()));
    }
    @GetMapping("/{id}")
    public ApiResponse<AccessoryApply> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }
    @PostMapping
    public ApiResponse<AccessoryApply> create(@Valid @RequestBody AccessoryApplySaveRequest request) { return ApiResponse.success("新增成功", service.create(request)); }
    @PutMapping("/{id}")
    public ApiResponse<AccessoryApply> update(@PathVariable("id") String id, @Valid @RequestBody AccessoryApplySaveRequest request) { return ApiResponse.success("修改成功", service.update(id, request)); }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("删除成功", null); }
}
