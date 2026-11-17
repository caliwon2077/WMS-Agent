package com.wms.backend.modules.accessory.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.accessory.dto.AccessoryApplySaveRequest;
import com.wms.backend.modules.accessory.entity.AccessoryApply;
import com.wms.backend.modules.accessory.repository.AccessoryApplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessoryApplyService {
    private final AccessoryApplyRepository repository;
    public AccessoryApplyService(AccessoryApplyRepository repository) { this.repository = repository; }
    public List<AccessoryApply> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public AccessoryApply detail(String id) {
        AccessoryApply record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "配件申请不存在");
        return record;
    }
    public AccessoryApply create(AccessoryApplySaveRequest request) { return repository.insert(request); }
    public AccessoryApply update(String id, AccessoryApplySaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
