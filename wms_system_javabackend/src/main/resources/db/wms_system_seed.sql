INSERT INTO equipment_assets (id, code, name, category, workshop, owner, status, purchaseDate, model, supplier, remark)
SELECT 'eq-1', 'EQ-1001', 'CNC Press A01', 'Press Equipment', 'Press Workshop', 'Zhang Lei', 'Running', '2024-05-12', 'JH21-125', 'Huachuang Equipment', 'Main production line asset'
WHERE NOT EXISTS (SELECT 1 FROM equipment_assets WHERE id = 'eq-1');

INSERT INTO equipment_assets (id, code, name, category, workshop, owner, status, purchaseDate, model, supplier, remark)
SELECT 'eq-2', 'EQ-1002', 'Welding Robot B07', 'Welding Equipment', 'Welding Workshop', 'Li Shan', 'Pending Maintenance', '2023-11-08', 'RX160', 'Qiyuan Automation', 'Three-shift usage'
WHERE NOT EXISTS (SELECT 1 FROM equipment_assets WHERE id = 'eq-2');

INSERT INTO equipment_assets (id, code, name, category, workshop, owner, status, purchaseDate, model, supplier, remark)
SELECT 'eq-3', 'EQ-1003', 'Air Compressor C02', 'Power Equipment', 'Power Workshop', 'Wang Kai', 'In Repair', '2022-08-18', 'GA37', 'Bolian Machinery', 'High temperature alarm pending'
WHERE NOT EXISTS (SELECT 1 FROM equipment_assets WHERE id = 'eq-3');

INSERT INTO manufacturer (id, code, name, contact, mobile, email, address, status, remark)
SELECT 'mf-1', 'M001', 'Huachuang Equipment', 'Liu Haiyang', '00000000000', 'demo@example.invalid', 'Zhengzhou High-tech Zone 88', 'Enabled', 'Long-term supplier for press equipment'
WHERE NOT EXISTS (SELECT 1 FROM manufacturer WHERE id = 'mf-1');

INSERT INTO manufacturer (id, code, name, contact, mobile, email, address, status, remark)
SELECT 'mf-2', 'M002', 'Qiyuan Automation', 'Zhou Jie', '00000000000', 'demo@example.invalid', 'Suzhou Industrial Park 16', 'Enabled', 'Robot and automation integrator'
WHERE NOT EXISTS (SELECT 1 FROM manufacturer WHERE id = 'mf-2');

INSERT INTO manufacturer (id, code, name, contact, mobile, email, address, status, remark)
SELECT 'mf-3', 'M003', 'Bolian Machinery', 'Feng Lei', '00000000000', 'demo@example.invalid', 'Qingdao Equipment Road 6', 'Disabled', 'Original supplier of compressor equipment'
WHERE NOT EXISTS (SELECT 1 FROM manufacturer WHERE id = 'mf-3');

INSERT INTO equipment_category (id, code, name, parentCategory, checkStrategy, maintenanceStrategy, status, remark)
SELECT 'ec-1', 'EC001', 'Press Equipment', '-', 'Weekly Check', 'Semiannual Maintenance', 'Enabled', 'Includes press line and auxiliary machines'
WHERE NOT EXISTS (SELECT 1 FROM equipment_category WHERE id = 'ec-1');

INSERT INTO equipment_category (id, code, name, parentCategory, checkStrategy, maintenanceStrategy, status, remark)
SELECT 'ec-2', 'EC002', 'Welding Equipment', '-', 'Daily Check', 'Quarterly Maintenance', 'Enabled', 'Includes robot and manual welding equipment'
WHERE NOT EXISTS (SELECT 1 FROM equipment_category WHERE id = 'ec-2');

INSERT INTO equipment_category (id, code, name, parentCategory, checkStrategy, maintenanceStrategy, status, remark)
SELECT 'ec-3', 'EC003', 'Power Equipment', '-', 'Monthly Patrol', 'Monthly Maintenance', 'Enabled', 'Includes compressor and power systems'
WHERE NOT EXISTS (SELECT 1 FROM equipment_category WHERE id = 'ec-3');

INSERT INTO check_plan (id, code, name, target, frequency, executor, status, startDate, cycleRule, remark)
SELECT 'cp-1', 'CP-001', 'Press Daily Check', 'Press Equipment', 'Daily', 'Zhang Lei', 'Running', '2026-03-01', 'Every day 08:00', 'Covers core press assets'
WHERE NOT EXISTS (SELECT 1 FROM check_plan WHERE id = 'cp-1');

INSERT INTO check_plan (id, code, name, target, frequency, executor, status, startDate, cycleRule, remark)
SELECT 'cp-2', 'CP-002', 'Welding Weekly Check', 'Welding Equipment', 'Weekly', 'Li Shan', 'Running', '2026-03-03', 'Every Tuesday 09:00', 'Focus on robot welding cells'
WHERE NOT EXISTS (SELECT 1 FROM check_plan WHERE id = 'cp-2');

INSERT INTO check_plan (id, code, name, target, frequency, executor, status, startDate, cycleRule, remark)
SELECT 'cp-3', 'CP-003', 'Power Monthly Check', 'Power Equipment', 'Monthly', 'Wang Kai', 'Draft', '2026-03-10', 'Every month day 10 10:00', 'Routine inspection for power systems'
WHERE NOT EXISTS (SELECT 1 FROM check_plan WHERE id = 'cp-3');

INSERT INTO check_item (id, code, name, judgeType, standardValue, equipmentType, riskLevel, status, remark)
SELECT 'ci-1', 'CI-001', 'Oil Pressure', 'Range', '4.5-5.5MPa', 'Press Equipment', 'Medium', 'Enabled', 'Check pressure fluctuation'
WHERE NOT EXISTS (SELECT 1 FROM check_item WHERE id = 'ci-1');

INSERT INTO check_item (id, code, name, judgeType, standardValue, equipmentType, riskLevel, status, remark)
SELECT 'ci-2', 'CI-002', 'Torch Temperature', 'Threshold', '<= 75C', 'Welding Equipment', 'High', 'Enabled', 'Report immediately if overheated'
WHERE NOT EXISTS (SELECT 1 FROM check_item WHERE id = 'ci-2');

INSERT INTO check_item (id, code, name, judgeType, standardValue, equipmentType, riskLevel, status, remark)
SELECT 'ci-3', 'CI-003', 'Noise Check', 'Manual', 'No abnormal noise', 'Power Equipment', 'Low', 'Enabled', 'Perform with on-site listening'
WHERE NOT EXISTS (SELECT 1 FROM check_item WHERE id = 'ci-3');

INSERT INTO check_record (id, code, equipmentName, executor, executeTime, result, status, planName, abnormalDesc, remark)
SELECT 'cr-1', 'CR-202603-001', 'CNC Press A01', 'Zhang Lei', '2026-03-19 08:20:00', 'Normal', 'Completed', 'Press Daily Check', '', 'Stable status'
WHERE NOT EXISTS (SELECT 1 FROM check_record WHERE id = 'cr-1');

INSERT INTO check_record (id, code, equipmentName, executor, executeTime, result, status, planName, abnormalDesc, remark)
SELECT 'cr-2', 'CR-202603-002', 'Welding Robot B07', 'Li Shan', '2026-03-19 09:00:00', 'Abnormal', 'To Repair', 'Welding Weekly Check', 'Weld point offset and repeated path error', 'Maintenance team notified'
WHERE NOT EXISTS (SELECT 1 FROM check_record WHERE id = 'cr-2');

INSERT INTO check_record (id, code, equipmentName, executor, executeTime, result, status, planName, abnormalDesc, remark)
SELECT 'cr-3', 'CR-202603-003', 'Air Compressor C02', 'Wang Kai', '2026-03-18 15:30:00', 'Normal', 'Completed', 'Power Monthly Check', '', 'Filter pressure normal'
WHERE NOT EXISTS (SELECT 1 FROM check_record WHERE id = 'cr-3');

INSERT INTO maintenance_plan (id, code, name, target, cycle, owner, status, nextDate, strategy, remark)
SELECT 'mp-1', 'MP-001', 'Compressor Monthly PM', 'Air Compressor C02', '30 Days', 'Wang Kai', 'Running', '2026-04-18', 'Monthly Maintenance', 'Check filter and drain valve'
WHERE NOT EXISTS (SELECT 1 FROM maintenance_plan WHERE id = 'mp-1');

INSERT INTO maintenance_plan (id, code, name, target, cycle, owner, status, nextDate, strategy, remark)
SELECT 'mp-2', 'MP-002', 'Robot Quarterly PM', 'Welding Robot B07', '90 Days', 'Li Shan', 'Running', '2026-06-16', 'Quarterly Maintenance', 'Check servo and torch assembly'
WHERE NOT EXISTS (SELECT 1 FROM maintenance_plan WHERE id = 'mp-2');

INSERT INTO maintenance_plan (id, code, name, target, cycle, owner, status, nextDate, strategy, remark)
SELECT 'mp-3', 'MP-003', 'Press Semiannual PM', 'CNC Press A01', '180 Days', 'Zhang Lei', 'Draft', '2026-09-15', 'Semiannual Maintenance', 'Prepare machine shutdown window'
WHERE NOT EXISTS (SELECT 1 FROM maintenance_plan WHERE id = 'mp-3');

INSERT INTO maintenance_item (id, code, name, standardHours, targetType, riskLevel, status, executeStandard, remark)
SELECT 'mi-1', 'MI-001', 'Filter Replacement', '2 Hours', 'Power Equipment', 'Medium', 'Enabled', 'Replace monthly and record pressure difference', 'Compressor specific item'
WHERE NOT EXISTS (SELECT 1 FROM maintenance_item WHERE id = 'mi-1');

INSERT INTO maintenance_item (id, code, name, standardHours, targetType, riskLevel, status, executeStandard, remark)
SELECT 'mi-2', 'MI-002', 'Lubrication Refill', '1 Hour', 'Press Equipment', 'Low', 'Enabled', 'Refill grease by point', 'Photo evidence required'
WHERE NOT EXISTS (SELECT 1 FROM maintenance_item WHERE id = 'mi-2');

INSERT INTO maintenance_item (id, code, name, standardHours, targetType, riskLevel, status, executeStandard, remark)
SELECT 'mi-3', 'MI-003', 'Servo Calibration', '4 Hours', 'Welding Equipment', 'High', 'Enabled', 'Run idle linkage test after calibration', 'Engineer review required'
WHERE NOT EXISTS (SELECT 1 FROM maintenance_item WHERE id = 'mi-3');

INSERT INTO maintenance_record (id, code, target, executor, maintainDate, result, status, planName, remark)
SELECT 'mr-1', 'MR-001', 'Air Compressor C02', 'Wang Kai', '2026-03-18', 'Done', 'Archived', 'Compressor Monthly PM', 'Pressure difference restored to normal'
WHERE NOT EXISTS (SELECT 1 FROM maintenance_record WHERE id = 'mr-1');

INSERT INTO maintenance_record (id, code, target, executor, maintainDate, result, status, planName, remark)
SELECT 'mr-2', 'MR-002', 'Welding Robot B07', 'Li Shan', '2026-03-17', 'Delayed', 'Pending', 'Robot Quarterly PM', 'Waiting for shutdown slot'
WHERE NOT EXISTS (SELECT 1 FROM maintenance_record WHERE id = 'mr-2');

INSERT INTO maintenance_record (id, code, target, executor, maintainDate, result, status, planName, remark)
SELECT 'mr-3', 'MR-003', 'CNC Press A01', 'Zhang Lei', '2026-03-15', 'Done', 'Archived', 'Press Semiannual PM', 'Lubrication and tightening finished'
WHERE NOT EXISTS (SELECT 1 FROM maintenance_record WHERE id = 'mr-3');

INSERT INTO device_model (id, code, name, pointCount, alarmRuleCount, protocol, status, remark)
SELECT 'dm-1', 'DM-001', 'Press Base Model', 18, 5, 'Modbus TCP', 'Enabled', 'Covers press runtime pressure and temperature points'
WHERE NOT EXISTS (SELECT 1 FROM device_model WHERE id = 'dm-1');

INSERT INTO device_model (id, code, name, pointCount, alarmRuleCount, protocol, status, remark)
SELECT 'dm-2', 'DM-002', 'Robot Model', 26, 8, 'OPC UA', 'Enabled', 'Includes track welding and servo status points'
WHERE NOT EXISTS (SELECT 1 FROM device_model WHERE id = 'dm-2');

INSERT INTO device_model (id, code, name, pointCount, alarmRuleCount, protocol, status, remark)
SELECT 'dm-3', 'DM-003', 'Compressor Model', 14, 4, 'MQTT', 'Enabled', 'Used by edge gateway for device data forwarding'
WHERE NOT EXISTS (SELECT 1 FROM device_model WHERE id = 'dm-3');

INSERT INTO iot_device (id, code, name, modelName, connectionStatus, lastReportTime, owner, ipAddress, remark)
SELECT 'id-1', 'ID-001', 'CNC Press A01', 'Press Base Model', 'Online', '2026-03-19 09:41:00', 'Zhang Lei', '192.168.10.21', 'Stable reporting device'
WHERE NOT EXISTS (SELECT 1 FROM iot_device WHERE id = 'id-1');

INSERT INTO iot_device (id, code, name, modelName, connectionStatus, lastReportTime, owner, ipAddress, remark)
SELECT 'id-2', 'ID-002', 'Welding Robot B07', 'Robot Model', 'Online', '2026-03-19 09:39:00', 'Li Shan', '192.168.10.37', 'Connected through OPC UA controller'
WHERE NOT EXISTS (SELECT 1 FROM iot_device WHERE id = 'id-2');

INSERT INTO iot_device (id, code, name, modelName, connectionStatus, lastReportTime, owner, ipAddress, remark)
SELECT 'id-3', 'ID-003', 'Air Compressor C02', 'Compressor Model', 'Offline', '2026-03-18 20:13:00', 'Wang Kai', '192.168.10.52', 'Check gateway power when offline'
WHERE NOT EXISTS (SELECT 1 FROM iot_device WHERE id = 'id-3');

INSERT INTO network_driver (id, code, name, protocol, version, vendor, status, configTemplate, remark)
SELECT 'nd-1', 'ND-001', 'Modbus TCP Driver', 'Modbus TCP', '1.2.0', 'Jianxin Control', 'Enabled', 'modbus-tcp-default', 'Used for press and power data collection'
WHERE NOT EXISTS (SELECT 1 FROM network_driver WHERE id = 'nd-1');

INSERT INTO network_driver (id, code, name, protocol, version, vendor, status, configTemplate, remark)
SELECT 'nd-2', 'ND-002', 'OPC UA Driver', 'OPC UA', '2.0.1', 'Jianxin Control', 'Enabled', 'opcua-standard', 'Supports certificate and namespace settings'
WHERE NOT EXISTS (SELECT 1 FROM network_driver WHERE id = 'nd-2');

INSERT INTO network_driver (id, code, name, protocol, version, vendor, status, configTemplate, remark)
SELECT 'nd-3', 'ND-003', 'MQTT Edge Gateway', 'MQTT', '1.9.8', 'Jianxin Control', 'Testing', 'mqtt-edge-gateway', 'Used for cross-network data aggregation'
WHERE NOT EXISTS (SELECT 1 FROM network_driver WHERE id = 'nd-3');

INSERT INTO repair_stage (id, code, name, sortOrder, allowRollback, slaHours, status, remark)
SELECT 'rs-1', 'RS-01', 'Pending Dispatch', 1, 'No', '2 Hours', 'Enabled', 'Initial stage after repair request submission'
WHERE NOT EXISTS (SELECT 1 FROM repair_stage WHERE id = 'rs-1');

INSERT INTO repair_stage (id, code, name, sortOrder, allowRollback, slaHours, status, remark)
SELECT 'rs-2', 'RS-02', 'Processing', 2, 'Yes', '8 Hours', 'Enabled', 'Repair team is handling the ticket'
WHERE NOT EXISTS (SELECT 1 FROM repair_stage WHERE id = 'rs-2');

INSERT INTO repair_stage (id, code, name, sortOrder, allowRollback, slaHours, status, remark)
SELECT 'rs-3', 'RS-03', 'Pending Acceptance', 3, 'Yes', '4 Hours', 'Enabled', 'Waiting for applicant acceptance after repair'
WHERE NOT EXISTS (SELECT 1 FROM repair_stage WHERE id = 'rs-3');

INSERT INTO repair_process (id, code, name, scope, nodeCount, approvalMode, status, remark)
SELECT 'rp-1', 'RP-001', 'Emergency Repair Flow', 'Critical Equipment', 6, 'Serial', 'Enabled', 'Used for high-severity or line-stop faults'
WHERE NOT EXISTS (SELECT 1 FROM repair_process WHERE id = 'rp-1');

INSERT INTO repair_process (id, code, name, scope, nodeCount, approvalMode, status, remark)
SELECT 'rp-2', 'RP-002', 'Standard Repair Flow', 'General Equipment', 4, 'Serial', 'Enabled', 'Default flow for daily repair tickets'
WHERE NOT EXISTS (SELECT 1 FROM repair_process WHERE id = 'rp-2');

INSERT INTO repair_process (id, code, name, scope, nodeCount, approvalMode, status, remark)
SELECT 'rp-3', 'RP-003', 'Outsource Repair Flow', 'External Service', 5, 'Mixed', 'Draft', 'For tickets involving external vendors'
WHERE NOT EXISTS (SELECT 1 FROM repair_process WHERE id = 'rp-3');

INSERT INTO material (id, code, name, spec, unit, category, status, remark)
SELECT 'mat-1', 'MAT-001', 'Cabinet Assembly', 'Type A', 'pcs', 'Assembly', 'Enabled', 'Main output product for line 1'
WHERE NOT EXISTS (SELECT 1 FROM material WHERE id = 'mat-1');

INSERT INTO material (id, code, name, spec, unit, category, status, remark)
SELECT 'mat-2', 'MAT-002', 'Bracket Module', 'Type B', 'pcs', 'Structural Part', 'Enabled', 'Used by welding and machining stations'
WHERE NOT EXISTS (SELECT 1 FROM material WHERE id = 'mat-2');

INSERT INTO material (id, code, name, spec, unit, category, status, remark)
SELECT 'mat-3', 'MAT-003', 'Guide Rail Seat', 'Type C', 'pcs', 'Machined Part', 'Enabled', 'Shared component across two workshops'
WHERE NOT EXISTS (SELECT 1 FROM material WHERE id = 'mat-3');

INSERT INTO work_area (id, code, name, parentArea, owner, equipmentCount, status, remark)
SELECT 'wa-1', 'WA-001', 'Plant 1', '-', 'Liu Haiyang', 28, 'Enabled', 'Primary production building'
WHERE NOT EXISTS (SELECT 1 FROM work_area WHERE id = 'wa-1');

INSERT INTO work_area (id, code, name, parentArea, owner, equipmentCount, status, remark)
SELECT 'wa-2', 'WA-002', 'Welding Line', 'Plant 1', 'Li Shan', 12, 'Enabled', 'Includes robot and manual welding zones'
WHERE NOT EXISTS (SELECT 1 FROM work_area WHERE id = 'wa-2');

INSERT INTO work_area (id, code, name, parentArea, owner, equipmentCount, status, remark)
SELECT 'wa-3', 'WA-003', 'Power Room', 'Plant 1', 'Wang Kai', 6, 'Enabled', 'Compressor and utility supply area'
WHERE NOT EXISTS (SELECT 1 FROM work_area WHERE id = 'wa-3');

INSERT INTO workshop (id, code, name, areaName, owner, teamCount, status, remark)
SELECT 'ws-1', 'WS-001', 'Press Workshop', 'Plant 1', 'Zhang Lei', 3, 'Enabled', 'Main press production area'
WHERE NOT EXISTS (SELECT 1 FROM workshop WHERE id = 'ws-1');

INSERT INTO workshop (id, code, name, areaName, owner, teamCount, status, remark)
SELECT 'ws-2', 'WS-002', 'Welding Workshop', 'Welding Line', 'Li Shan', 2, 'Enabled', 'Robot and fixture welding area'
WHERE NOT EXISTS (SELECT 1 FROM workshop WHERE id = 'ws-2');

INSERT INTO workshop (id, code, name, areaName, owner, teamCount, status, remark)
SELECT 'ws-3', 'WS-003', 'Power Workshop', 'Power Room', 'Wang Kai', 1, 'Enabled', 'Utility and compressor maintenance area'
WHERE NOT EXISTS (SELECT 1 FROM workshop WHERE id = 'ws-3');

INSERT INTO mdm_process (id, code, name, standardHours, jobType, equipmentName, status, remark)
SELECT 'pr-1', 'PR-001', 'Blanking', '18 min', 'Operator', 'CNC Press A01', 'Enabled', 'Front-end cutting process'
WHERE NOT EXISTS (SELECT 1 FROM mdm_process WHERE id = 'pr-1');

INSERT INTO mdm_process (id, code, name, standardHours, jobType, equipmentName, status, remark)
SELECT 'pr-2', 'PR-002', 'Welding', '24 min', 'Welder', 'Welding Robot B07', 'Enabled', 'Robot welding standard process'
WHERE NOT EXISTS (SELECT 1 FROM mdm_process WHERE id = 'pr-2');

INSERT INTO mdm_process (id, code, name, standardHours, jobType, equipmentName, status, remark)
SELECT 'pr-3', 'PR-003', 'Leak Test', '10 min', 'Inspector', 'Air Compressor C02', 'Enabled', 'Quality verification before packing'
WHERE NOT EXISTS (SELECT 1 FROM mdm_process WHERE id = 'pr-3');

INSERT INTO oa_setting (id, code, name, description, currentValue, updatedAtLabel, maintainer, status, remark)
SELECT 'oas-1', 'OA-001', 'Approval Timeout Reminder', 'Auto remind after SLA timeout', 'Enabled', '2026-03-16', 'Admin', 'Enabled', 'Notify process owner and assignee'
WHERE NOT EXISTS (SELECT 1 FROM oa_setting WHERE id = 'oas-1');

INSERT INTO oa_setting (id, code, name, description, currentValue, updatedAtLabel, maintainer, status, remark)
SELECT 'oas-2', 'OA-002', 'Countersign Mode', 'Rule for multi-person approval nodes', 'All Approve', '2026-03-14', 'Admin', 'Enabled', 'Can be adjusted by business type later'
WHERE NOT EXISTS (SELECT 1 FROM oa_setting WHERE id = 'oas-2');

INSERT INTO oa_setting (id, code, name, description, currentValue, updatedAtLabel, maintainer, status, remark)
SELECT 'oas-3', 'OA-003', 'Cc Notification', 'Auto notify after process completion', 'Enabled', '2026-03-12', 'Admin', 'Enabled', 'Push to message center and email'
WHERE NOT EXISTS (SELECT 1 FROM oa_setting WHERE id = 'oas-3');

INSERT INTO oa_flow (id, code, name, bizType, nodeCount, version, status, remark)
SELECT 'flow-1', 'FL-001', 'Repair Approval Flow', 'Equipment Repair', 5, 'v1.4', 'Published', 'Default approval flow for repair tickets'
WHERE NOT EXISTS (SELECT 1 FROM oa_flow WHERE id = 'flow-1');

INSERT INTO oa_flow (id, code, name, bizType, nodeCount, version, status, remark)
SELECT 'flow-2', 'FL-002', 'Accessory Apply Flow', 'Accessory Apply', 4, 'v1.1', 'Published', 'Standard warehouse approval flow'
WHERE NOT EXISTS (SELECT 1 FROM oa_flow WHERE id = 'flow-2');

INSERT INTO oa_flow (id, code, name, bizType, nodeCount, version, status, remark)
SELECT 'flow-3', 'FL-003', 'Maintenance Delay Flow', 'Maintenance', 3, 'v0.9', 'Draft', 'Used for delayed maintenance approvals'
WHERE NOT EXISTS (SELECT 1 FROM oa_flow WHERE id = 'flow-3');

INSERT INTO oa_approval_type (id, code, name, bindFlow, strategy, modifyCount, status, remark)
SELECT 'at-1', 'AT-001', 'Repair Approval', 'Repair Approval Flow', 'By Role', 3, 'Enabled', 'For equipment repair ticket approval'
WHERE NOT EXISTS (SELECT 1 FROM oa_approval_type WHERE id = 'at-1');

INSERT INTO oa_approval_type (id, code, name, bindFlow, strategy, modifyCount, status, remark)
SELECT 'at-2', 'AT-002', 'Accessory Approval', 'Accessory Apply Flow', 'By Department', 2, 'Enabled', 'For accessory receiving and warehouse release'
WHERE NOT EXISTS (SELECT 1 FROM oa_approval_type WHERE id = 'at-2');

INSERT INTO oa_approval_type (id, code, name, bindFlow, strategy, modifyCount, status, remark)
SELECT 'at-3', 'AT-003', 'Maintenance Delay Approval', 'Maintenance Delay Flow', 'Assigned User', 1, 'Enabled', 'For delayed PM plan approval'
WHERE NOT EXISTS (SELECT 1 FROM oa_approval_type WHERE id = 'at-3');

INSERT INTO accessory_item (id, code, name, spec, warehouse, stockQty, status, remark)
SELECT 'ac-1', 'AC-001', 'Bearing 6208', 'Standard Part', 'Warehouse A', 126, 'Sufficient', 'Common rotating spare part'
WHERE NOT EXISTS (SELECT 1 FROM accessory_item WHERE id = 'ac-1');

INSERT INTO accessory_item (id, code, name, spec, warehouse, stockQty, status, remark)
SELECT 'ac-2', 'AC-002', 'Servo Belt', 'B-300', 'Warehouse A', 16, 'Warning', 'Used by robot servo module'
WHERE NOT EXISTS (SELECT 1 FROM accessory_item WHERE id = 'ac-2');

INSERT INTO accessory_item (id, code, name, spec, warehouse, stockQty, status, remark)
SELECT 'ac-3', 'AC-003', 'Filter Core', 'F-20', 'Warehouse Power', 52, 'Sufficient', 'For compressor monthly maintenance'
WHERE NOT EXISTS (SELECT 1 FROM accessory_item WHERE id = 'ac-3');

INSERT INTO accessory_category (id, code, name, parentCategory, itemCount, status, remark)
SELECT 'acc-1', 'ACC-001', 'Standard Part', '-', 168, 'Enabled', 'General mechanical parts'
WHERE NOT EXISTS (SELECT 1 FROM accessory_category WHERE id = 'acc-1');

INSERT INTO accessory_category (id, code, name, parentCategory, itemCount, status, remark)
SELECT 'acc-2', 'ACC-002', 'Electrical Part', '-', 94, 'Enabled', 'Electrical and control modules'
WHERE NOT EXISTS (SELECT 1 FROM accessory_category WHERE id = 'acc-2');

INSERT INTO accessory_category (id, code, name, parentCategory, itemCount, status, remark)
SELECT 'acc-3', 'ACC-003', 'Consumable', '-', 73, 'Enabled', 'Periodically replaced supplies'
WHERE NOT EXISTS (SELECT 1 FROM accessory_category WHERE id = 'acc-3');

INSERT INTO accessory_supplier (id, code, name, contact, deliveryCycle, grade, status, remark)
SELECT 'sup-1', 'SUP-001', 'Hongda Industrial', 'Zhao Xin', '3 Days', 'A', 'Active', 'Core supplier for common parts'
WHERE NOT EXISTS (SELECT 1 FROM accessory_supplier WHERE id = 'sup-1');

INSERT INTO accessory_supplier (id, code, name, contact, deliveryCycle, grade, status, remark)
SELECT 'sup-2', 'SUP-002', 'Beijing Electric', 'Han Mei', '5 Days', 'A', 'Active', 'Main electrical accessory supplier'
WHERE NOT EXISTS (SELECT 1 FROM accessory_supplier WHERE id = 'sup-2');

INSERT INTO accessory_supplier (id, code, name, contact, deliveryCycle, grade, status, remark)
SELECT 'sup-3', 'SUP-003', 'Anrun Consumables', 'Sun Liang', '2 Days', 'B', 'Active', 'Fast delivery for consumables'
WHERE NOT EXISTS (SELECT 1 FROM accessory_supplier WHERE id = 'sup-3');

INSERT INTO accessory_warehouse (id, code, name, zoneCount, manager, stockTotal, status, remark)
SELECT 'wh-1', 'WH-001', 'Warehouse A', 6, 'Liu Haiyang', 3840, 'Enabled', 'Main accessory warehouse'
WHERE NOT EXISTS (SELECT 1 FROM accessory_warehouse WHERE id = 'wh-1');

INSERT INTO accessory_warehouse (id, code, name, zoneCount, manager, stockTotal, status, remark)
SELECT 'wh-2', 'WH-002', 'Warehouse Power', 3, 'Wang Kai', 1240, 'Enabled', 'Dedicated power equipment spare warehouse'
WHERE NOT EXISTS (SELECT 1 FROM accessory_warehouse WHERE id = 'wh-2');

INSERT INTO accessory_warehouse (id, code, name, zoneCount, manager, stockTotal, status, remark)
SELECT 'wh-3', 'WH-003', 'Emergency Warehouse', 2, 'Li Shan', 620, 'Enabled', 'Emergency spare parts reserve'
WHERE NOT EXISTS (SELECT 1 FROM accessory_warehouse WHERE id = 'wh-3');

INSERT INTO repair_apply (id, ticketNo, equipmentName, level, stage, team, status, applicant, applyDate, faultType, contact, description)
SELECT 'ra-1', 'RA-001', 'Air Compressor C02', 'High', 'Processing', 'Power Team', 'In Repair', 'Wang Kai', '2026-03-19', 'High Temperature Alarm', '00000000000', 'Continuous high temperature and noise after 30 minutes of running'
WHERE NOT EXISTS (SELECT 1 FROM repair_apply WHERE id = 'ra-1');

INSERT INTO repair_apply (id, ticketNo, equipmentName, level, stage, team, status, applicant, applyDate, faultType, contact, description)
SELECT 'ra-2', 'RA-002', 'Welding Robot B07', 'Medium', 'Pending Acceptance', 'Robot Team', 'Pending Close', 'Li Shan', '2026-03-18', 'Track Deviation', '00000000000', 'Servo calibration completed and waiting for on-site acceptance'
WHERE NOT EXISTS (SELECT 1 FROM repair_apply WHERE id = 'ra-2');

INSERT INTO repair_apply (id, ticketNo, equipmentName, level, stage, team, status, applicant, applyDate, faultType, contact, description)
SELECT 'ra-3', 'RA-003', 'CNC Press A01', 'Low', 'Pending Dispatch', 'Press Team', 'New', 'Zhang Lei', '2026-03-19', 'Light Curtain Alarm', '00000000000', 'Intermittent stop and waiting for maintenance check'
WHERE NOT EXISTS (SELECT 1 FROM repair_apply WHERE id = 'ra-3');

INSERT INTO accessory_apply (id, applyNo, applicant, accessoryName, quantity, approvalStatus, deliveryStatus, warehouse, applyDate, purpose, receiver, remark)
SELECT 'aa-1', 'AA-001', 'Zhang Lei', 'Bearing 6208', 12, 'Approved', 'Pending Delivery', 'Warehouse A', '2026-03-19', 'PM spare parts for A01', 'Zhang Lei', 'Needed before Friday'
WHERE NOT EXISTS (SELECT 1 FROM accessory_apply WHERE id = 'aa-1');

INSERT INTO accessory_apply (id, applyNo, applicant, accessoryName, quantity, approvalStatus, deliveryStatus, warehouse, applyDate, purpose, receiver, remark)
SELECT 'aa-2', 'AA-002', 'Li Shan', 'Servo Belt', 4, 'Pending Approval', 'Not Delivered', 'Warehouse A', '2026-03-18', 'Repair parts for B07', 'Li Shan', 'Deliver immediately after approval'
WHERE NOT EXISTS (SELECT 1 FROM accessory_apply WHERE id = 'aa-2');

INSERT INTO accessory_apply (id, applyNo, applicant, accessoryName, quantity, approvalStatus, deliveryStatus, warehouse, applyDate, purpose, receiver, remark)
SELECT 'aa-3', 'AA-003', 'Wang Kai', 'Filter Core', 10, 'Completed', 'Delivered', 'Warehouse Power', '2026-03-17', 'Monthly PM for compressor', 'Wang Kai', 'Signed by warehouse'
WHERE NOT EXISTS (SELECT 1 FROM accessory_apply WHERE id = 'aa-3');

INSERT INTO sys_menu (id, name, type, route, component, sortOrder, status, remark)
SELECT 'menu-1', 'Equipment Asset', 'Directory', '/ems', '-', 2, 'Visible', 'Equipment asset root menu'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE id = 'menu-1');
INSERT INTO sys_menu (id, name, type, route, component, sortOrder, status, remark)
SELECT 'menu-2', 'Equipment List', 'Menu', '/ems/equipmentAssets', '/ems/equipmentAssets/index', 3, 'Visible', 'Equipment asset list page'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE id = 'menu-2');
INSERT INTO sys_menu (id, name, type, route, component, sortOrder, status, remark)
SELECT 'menu-3', 'Add Button', 'Button', 'equipmentAssets:add', '-', 1, 'Visible', 'Add action permission button'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE id = 'menu-3');

INSERT INTO sys_dict_tree (id, code, name, level, detailCount, modifyCount, status, remark)
SELECT 'dt-1', 'DT-001', 'Equipment Fault Tree', 3, 42, 5, 'Enabled', 'Hierarchical fault code tree'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_tree WHERE id = 'dt-1');
INSERT INTO sys_dict_tree (id, code, name, level, detailCount, modifyCount, status, remark)
SELECT 'dt-2', 'DT-002', 'Area Hierarchy', 4, 31, 2, 'Enabled', 'Factory and workshop area hierarchy'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_tree WHERE id = 'dt-2');
INSERT INTO sys_dict_tree (id, code, name, level, detailCount, modifyCount, status, remark)
SELECT 'dt-3', 'DT-003', 'Safety Grade', 2, 8, 1, 'Enabled', 'Safety classification tree dictionary'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_tree WHERE id = 'dt-3');

INSERT INTO sys_setting (id, configKey, name, currentValue, category, modifyCount, status, remark)
SELECT 'set-1', 'repair.timeout', 'Repair Timeout Hours', '8', 'Repair', 4, 'Enabled', 'Repair timeout threshold in hours'
WHERE NOT EXISTS (SELECT 1 FROM sys_setting WHERE id = 'set-1');
INSERT INTO sys_setting (id, configKey, name, currentValue, category, modifyCount, status, remark)
SELECT 'set-2', 'check.autoCreate', 'Auto Generate Check Tasks', 'true', 'Inspection', 2, 'Enabled', 'Whether check tasks are auto generated'
WHERE NOT EXISTS (SELECT 1 FROM sys_setting WHERE id = 'set-2');
INSERT INTO sys_setting (id, configKey, name, currentValue, category, modifyCount, status, remark)
SELECT 'set-3', 'inventory.warnDays', 'Inventory Warning Days', '15', 'Accessory', 3, 'Enabled', 'Days before inventory shortage warning'
WHERE NOT EXISTS (SELECT 1 FROM sys_setting WHERE id = 'set-3');

INSERT INTO sys_api_log (id, logNo, apiPath, operator, duration, result, logTime, remark)
SELECT 'log-1', 'LOG-001', '/api/auth/menus', 'admin', '86ms', 'Success', '2026-03-19 09:42', 'Menu query success'
WHERE NOT EXISTS (SELECT 1 FROM sys_api_log WHERE id = 'log-1');
INSERT INTO sys_api_log (id, logNo, apiPath, operator, duration, result, logTime, remark)
SELECT 'log-2', 'LOG-002', '/api/repair/apply/list', 'admin', '102ms', 'Success', '2026-03-19 09:40', 'Repair list loaded successfully'
WHERE NOT EXISTS (SELECT 1 FROM sys_api_log WHERE id = 'log-2');
INSERT INTO sys_api_log (id, logNo, apiPath, operator, duration, result, logTime, remark)
SELECT 'log-3', 'LOG-003', '/api/iot/device/report', 'gateway-01', '188ms', 'Failed', '2026-03-19 08:58', 'Gateway upload exception'
WHERE NOT EXISTS (SELECT 1 FROM sys_api_log WHERE id = 'log-3');

INSERT INTO sys_quartz (id, code, name, cron, lastExecute, result, status, remark)
SELECT 'job-1', 'JOB-001', 'Generate Inspection Tasks', '0 0 6 * * ?', '2026-03-19 06:00', 'Success', 'Running', 'Daily auto-generate inspection tasks'
WHERE NOT EXISTS (SELECT 1 FROM sys_quartz WHERE id = 'job-1');
INSERT INTO sys_quartz (id, code, name, cron, lastExecute, result, status, remark)
SELECT 'job-2', 'JOB-002', 'Inventory Warning Scan', '0 0/30 * * * ?', '2026-03-19 09:30', 'Success', 'Running', 'Half-hour inventory warning scan'
WHERE NOT EXISTS (SELECT 1 FROM sys_quartz WHERE id = 'job-2');
INSERT INTO sys_quartz (id, code, name, cron, lastExecute, result, status, remark)
SELECT 'job-3', 'JOB-003', 'IoT Offline Scan', '0 */10 * * * ?', '2026-03-19 09:40', 'Success', 'Running', 'Ten-minute device offline detection'
WHERE NOT EXISTS (SELECT 1 FROM sys_quartz WHERE id = 'job-3');

INSERT INTO sys_org (id, code, name, parentOrg, owner, userCount, status, remark)
SELECT 'org-1', 'ORG-001', 'Group HQ', '-', 'Chen Tao', 24, 'Enabled', 'Group head office organization'
WHERE NOT EXISTS (SELECT 1 FROM sys_org WHERE id = 'org-1');
INSERT INTO sys_org (id, code, name, parentOrg, owner, userCount, status, remark)
SELECT 'org-2', 'ORG-002', 'Factory 1', 'Group HQ', 'Liu Haiyang', 168, 'Enabled', 'Primary plant organization'
WHERE NOT EXISTS (SELECT 1 FROM sys_org WHERE id = 'org-2');
INSERT INTO sys_org (id, code, name, parentOrg, owner, userCount, status, remark)
SELECT 'org-3', 'ORG-003', 'Maintenance Dept', 'Factory 1', 'Wang Kai', 24, 'Enabled', 'Equipment maintenance organization'
WHERE NOT EXISTS (SELECT 1 FROM sys_org WHERE id = 'org-3');

INSERT INTO sys_city (id, code, name, province, region, warehouseCount, status, remark)
SELECT 'city-1', 'CITY-001', 'Zhengzhou', 'Henan', 'Central China', 2, 'Enabled', 'Main factory city'
WHERE NOT EXISTS (SELECT 1 FROM sys_city WHERE id = 'city-1');
INSERT INTO sys_city (id, code, name, province, region, warehouseCount, status, remark)
SELECT 'city-2', 'CITY-002', 'Luoyang', 'Henan', 'Central China', 1, 'Enabled', 'Support warehouse city'
WHERE NOT EXISTS (SELECT 1 FROM sys_city WHERE id = 'city-2');
INSERT INTO sys_city (id, code, name, province, region, warehouseCount, status, remark)
SELECT 'city-3', 'CITY-003', 'Xinxiang', 'Henan', 'Central China', 1, 'Enabled', 'Spare storage city'
WHERE NOT EXISTS (SELECT 1 FROM sys_city WHERE id = 'city-3');

INSERT INTO sys_dict (id, code, name, category, itemCount, modifyCount, status, remark)
SELECT 'ds-1', 'DS-001', 'Equipment Status', 'System', 6, 3, 'Enabled', 'System-level equipment statuses'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE id = 'ds-1');
INSERT INTO sys_dict (id, code, name, category, itemCount, modifyCount, status, remark)
SELECT 'ds-2', 'DS-002', 'Repair Level', 'System', 4, 2, 'Enabled', 'Repair severity levels'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE id = 'ds-2');
INSERT INTO sys_dict (id, code, name, category, itemCount, modifyCount, status, remark)
SELECT 'ds-3', 'DS-003', 'Approval Result', 'System', 5, 2, 'Enabled', 'Approval result dictionary'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE id = 'ds-3');

INSERT INTO sys_auto_code (id, code, bizObject, prefix, ruleExample, resetCycle, status, remark)
SELECT 'acode-1', 'CODE-001', 'Equipment', 'EQ', 'EQ-202603-001', 'Monthly', 'Enabled', 'Equipment code generation rule'
WHERE NOT EXISTS (SELECT 1 FROM sys_auto_code WHERE id = 'acode-1');
INSERT INTO sys_auto_code (id, code, bizObject, prefix, ruleExample, resetCycle, status, remark)
SELECT 'acode-2', 'CODE-002', 'Repair Ticket', 'RA', 'RA-202603-001', 'Monthly', 'Enabled', 'Repair order number rule'
WHERE NOT EXISTS (SELECT 1 FROM sys_auto_code WHERE id = 'acode-2');
INSERT INTO sys_auto_code (id, code, bizObject, prefix, ruleExample, resetCycle, status, remark)
SELECT 'acode-3', 'CODE-003', 'Accessory Apply', 'AA', 'AA-202603-001', 'Monthly', 'Enabled', 'Accessory application code rule'
WHERE NOT EXISTS (SELECT 1 FROM sys_auto_code WHERE id = 'acode-3');

INSERT INTO sys_common_dict (id, code, name, valueType, itemCount, modifyCount, status, remark)
SELECT 'dict-1', 'D-001', 'Shift', 'String', 3, 1, 'Enabled', 'Shift dictionary'
WHERE NOT EXISTS (SELECT 1 FROM sys_common_dict WHERE id = 'dict-1');
INSERT INTO sys_common_dict (id, code, name, valueType, itemCount, modifyCount, status, remark)
SELECT 'dict-2', 'D-002', 'Fault Level', 'String', 4, 2, 'Enabled', 'Fault level dictionary'
WHERE NOT EXISTS (SELECT 1 FROM sys_common_dict WHERE id = 'dict-2');
INSERT INTO sys_common_dict (id, code, name, valueType, itemCount, modifyCount, status, remark)
SELECT 'dict-3', 'D-003', 'Notification Mode', 'String', 5, 1, 'Enabled', 'Notification method dictionary'
WHERE NOT EXISTS (SELECT 1 FROM sys_common_dict WHERE id = 'dict-3');

INSERT INTO sys_role (id, code, name, userCount, menuCount, permissionCount, status, remark)
SELECT 'role-1', 'admin', 'Super Admin', 1, 38, 39, 'Enabled', 'Full system permission role'
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE id = 'role-1');
INSERT INTO sys_role (id, code, name, userCount, menuCount, permissionCount, status, remark)
SELECT 'role-2', 'maintainer', 'Equipment Admin', 12, 18, 26, 'Enabled', 'Equipment maintenance operations role'
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE id = 'role-2');
INSERT INTO sys_role (id, code, name, userCount, menuCount, permissionCount, status, remark)
SELECT 'role-3', 'storekeeper', 'Warehouse Admin', 4, 7, 12, 'Enabled', 'Accessory warehouse operations role'
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE id = 'role-3');

INSERT INTO sys_dict_item (id, dictName, itemCode, itemName, sortOrder, defaultValue, status, remark)
SELECT 'di-1', 'Shift', 'SHIFT-A', 'Morning Shift', 1, 'No', 'Enabled', 'Shift item configuration'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE id = 'di-1');
INSERT INTO sys_dict_item (id, dictName, itemCode, itemName, sortOrder, defaultValue, status, remark)
SELECT 'di-2', 'Shift', 'SHIFT-B', 'Middle Shift', 2, 'No', 'Enabled', 'Shift item configuration'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE id = 'di-2');
INSERT INTO sys_dict_item (id, dictName, itemCode, itemName, sortOrder, defaultValue, status, remark)
SELECT 'di-3', 'Shift', 'SHIFT-C', 'Night Shift', 3, 'Yes', 'Enabled', 'Default shift item'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE id = 'di-3');

INSERT INTO sys_dept (id, code, name, parentDept, owner, syncStatus, status, remark)
SELECT 'dep-1', 'DEP-001', 'Maintenance Dept', 'Factory 1', 'Wang Kai', 'Synced', 'Enabled', 'Equipment maintenance department'
WHERE NOT EXISTS (SELECT 1 FROM sys_dept WHERE id = 'dep-1');
INSERT INTO sys_dept (id, code, name, parentDept, owner, syncStatus, status, remark)
SELECT 'dep-2', 'DEP-002', 'Welding Dept', 'Factory 1', 'Li Shan', 'Synced', 'Enabled', 'Welding manufacturing department'
WHERE NOT EXISTS (SELECT 1 FROM sys_dept WHERE id = 'dep-2');
INSERT INTO sys_dept (id, code, name, parentDept, owner, syncStatus, status, remark)
SELECT 'dep-3', 'DEP-003', 'Logistics Dept', 'Factory 1', 'Liu Haiyang', 'Pending Sync', 'Enabled', 'Warehouse and logistics department'
WHERE NOT EXISTS (SELECT 1 FROM sys_dept WHERE id = 'dep-3');

INSERT INTO sys_permissions (id, targetName, targetType, menuCount, buttonCount, lastAdjust, status, remark)
SELECT 'perm-1', 'Super Admin', 'Role', 38, 39, '2026-03-15', 'Effective', 'Highest permission set'
WHERE NOT EXISTS (SELECT 1 FROM sys_permissions WHERE id = 'perm-1');
INSERT INTO sys_permissions (id, targetName, targetType, menuCount, buttonCount, lastAdjust, status, remark)
SELECT 'perm-2', 'Equipment Admin', 'Role', 18, 26, '2026-03-14', 'Effective', 'Equipment management permission set'
WHERE NOT EXISTS (SELECT 1 FROM sys_permissions WHERE id = 'perm-2');
INSERT INTO sys_permissions (id, targetName, targetType, menuCount, buttonCount, lastAdjust, status, remark)
SELECT 'perm-3', 'Warehouse Admin', 'Role', 7, 12, '2026-03-13', 'Effective', 'Warehouse permission set'
WHERE NOT EXISTS (SELECT 1 FROM sys_permissions WHERE id = 'perm-3');

INSERT INTO sys_user (id, account, name, dept, role, mobile, status, email, syncStatus, remark)
SELECT 'user-1', 'admin', 'Admin', 'Maintenance Dept', 'Super Admin', '00000000000', 'Enabled', 'demo@example.invalid', 'Synced', 'Built-in system account'
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE id = 'user-1');

INSERT INTO sys_user (id, account, name, dept, role, mobile, status, email, syncStatus, remark)
SELECT 'user-2', 'zhanglei', 'Zhang Lei', 'Press Workshop', 'Equipment Admin', '00000000000', 'Enabled', 'demo@example.invalid', 'Synced', 'Responsible for press equipment'
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE id = 'user-2');

INSERT INTO sys_user (id, account, name, dept, role, mobile, status, email, syncStatus, remark)
SELECT 'user-3', 'lishan', 'Li Shan', 'Welding Workshop', 'Equipment Admin', '00000000000', 'Enabled', 'demo@example.invalid', 'Pending Sync', 'Responsible for welding robot'
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE id = 'user-3');
