package com.wms.backend.modules.user.controller;

import com.wms.backend.common.api.ApiPageResponse;
import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.modules.user.dto.UserSaveRequest;
import com.wms.backend.modules.user.entity.SysUser;
import com.wms.backend.modules.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/system/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }
    @GetMapping
    public ApiResponse<ApiPageResponse<SysUser>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                      @RequestParam(value = "status", required = false) String status) {
        List<SysUser> records = service.list(keyword, status);
        return ApiResponse.success(new ApiPageResponse<SysUser>(records, records.size()));
    }
    @GetMapping("/{id}")
    public ApiResponse<SysUser> detail(@PathVariable("id") String id) { return ApiResponse.success(service.detail(id)); }
    @PostMapping
    public ApiResponse<SysUser> create(@Valid @RequestBody UserSaveRequest request) { return ApiResponse.success("新增成功", service.create(request)); }
    @PutMapping("/{id}")
    public ApiResponse<SysUser> update(@PathVariable("id") String id, @Valid @RequestBody UserSaveRequest request) { return ApiResponse.success("修改成功", service.update(id, request)); }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") String id) { service.delete(id); return ApiResponse.success("删除成功", null); }
}
