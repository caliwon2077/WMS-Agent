package com.wms.backend.modules.accessory.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.accessory.dto.AccessoryItemSaveRequest;
import com.wms.backend.modules.accessory.entity.AccessoryItem;
import com.wms.backend.modules.accessory.repository.AccessoryItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccessoryItemService {
    private final AccessoryItemRepository repository;
    public AccessoryItemService(AccessoryItemRepository repository) { this.repository = repository; }
    public List<AccessoryItem> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public AccessoryItem detail(String id) {
        AccessoryItem record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "Accessory item not found");
        return record;
    }
    public AccessoryItem create(AccessoryItemSaveRequest request) { return repository.insert(request); }
    public AccessoryItem update(String id, AccessoryItemSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
