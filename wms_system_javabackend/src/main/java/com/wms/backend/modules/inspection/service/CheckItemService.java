package com.wms.backend.modules.inspection.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.inspection.dto.CheckItemSaveRequest;
import com.wms.backend.modules.inspection.entity.CheckItem;
import com.wms.backend.modules.inspection.repository.CheckItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckItemService {
    private final CheckItemRepository repository;
    public CheckItemService(CheckItemRepository repository) { this.repository = repository; }
    public List<CheckItem> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public CheckItem detail(String id) {
        CheckItem record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "点检项目不存在");
        return record;
    }
    public CheckItem create(CheckItemSaveRequest request) { return repository.insert(request); }
    public CheckItem update(String id, CheckItemSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
