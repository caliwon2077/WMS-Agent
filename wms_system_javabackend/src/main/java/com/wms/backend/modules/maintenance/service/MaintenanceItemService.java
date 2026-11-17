package com.wms.backend.modules.maintenance.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.maintenance.dto.MaintenanceItemSaveRequest;
import com.wms.backend.modules.maintenance.entity.MaintenanceItem;
import com.wms.backend.modules.maintenance.repository.MaintenanceItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceItemService {
    private final MaintenanceItemRepository repository;
    public MaintenanceItemService(MaintenanceItemRepository repository) { this.repository = repository; }
    public List<MaintenanceItem> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public MaintenanceItem detail(String id) {
        MaintenanceItem record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "保养项目不存在");
        return record;
    }
    public MaintenanceItem create(MaintenanceItemSaveRequest request) { return repository.insert(request); }
    public MaintenanceItem update(String id, MaintenanceItemSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
