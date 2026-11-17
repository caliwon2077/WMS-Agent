package com.wms.backend.modules.user.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.user.dto.UserSaveRequest;
import com.wms.backend.modules.user.entity.SysUser;
import com.wms.backend.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    public UserService(UserRepository repository) { this.repository = repository; }
    public List<SysUser> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public SysUser detail(String id) {
        SysUser record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "用户不存在");
        return record;
    }
    public SysUser create(UserSaveRequest request) { return repository.insert(request); }
    public SysUser update(String id, UserSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
