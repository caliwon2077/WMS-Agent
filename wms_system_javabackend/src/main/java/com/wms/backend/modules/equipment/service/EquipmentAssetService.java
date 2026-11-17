package com.wms.backend.modules.equipment.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.equipment.dto.EquipmentAssetSaveRequest;
import com.wms.backend.modules.equipment.entity.EquipmentAsset;
import com.wms.backend.modules.equipment.repository.EquipmentAssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentAssetService {

    private final EquipmentAssetRepository repository;

    public EquipmentAssetService(EquipmentAssetRepository repository) {
        this.repository = repository;
    }

    public List<EquipmentAsset> list(String keyword, String status) {
        return repository.findAll(keyword, status);
    }

    public EquipmentAsset detail(String id) {
        EquipmentAsset record = repository.findById(id);
        if (record == null) {
            throw new BusinessException("NOT_FOUND", "设备不存在");
        }
        return record;
    }

    public EquipmentAsset create(EquipmentAssetSaveRequest request) {
        return repository.insert(request);
    }

    public EquipmentAsset update(String id, EquipmentAssetSaveRequest request) {
        detail(id);
        return repository.update(id, request);
    }

    public void delete(String id) {
        detail(id);
        repository.deleteById(id);
    }
}
