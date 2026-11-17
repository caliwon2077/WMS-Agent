export const moduleDefinitions = {
  manufacturer: {
    table: "manufacturer",
    fields: ["code", "name", "contact", "mobile", "email", "address", "status", "remark"],
    seedRows: [
      { id: "mf-1", code: "M001", name: "华创设备", contact: "刘海洋", mobile: "00000000000", email: "demo@example.invalid", address: "郑州市高新区工业路 88 号", status: "启用", remark: "冲压设备长期合作厂商" },
      { id: "mf-2", code: "M002", name: "启远自动化", contact: "周洁", mobile: "00000000000", email: "demo@example.invalid", address: "苏州市工业园区胜利大道 16 号", status: "启用", remark: "机器人与自动化改造供应商" },
      { id: "mf-3", code: "M003", name: "博联机械", contact: "冯蕾", mobile: "00000000000", email: "demo@example.invalid", address: "青岛市西海岸新区装备路 6 号", status: "停用", remark: "空压设备原始供应商" }
    ]
  },
  equipmentCategory: {
    table: "equipment_category",
    fields: ["code", "name", "parentCategory", "checkStrategy", "maintenanceStrategy", "status", "remark"],
    seedRows: [
      { id: "ec-1", code: "EC001", name: "冲压设备", parentCategory: "-", checkStrategy: "标准周检", maintenanceStrategy: "半年度保养", status: "启用", remark: "覆盖冲床、折弯和周边辅机" },
      { id: "ec-2", code: "EC002", name: "焊接设备", parentCategory: "-", checkStrategy: "标准日检", maintenanceStrategy: "季度保养", status: "启用", remark: "覆盖机器人焊接与人工焊接设备" },
      { id: "ec-3", code: "EC003", name: "动力设备", parentCategory: "-", checkStrategy: "月度巡检", maintenanceStrategy: "月度保养", status: "启用", remark: "覆盖空压、供电和动能系统" }
    ]
  },
  equipmentAssets: {
    table: "equipment_assets",
    fields: ["code", "name", "category", "workshop", "owner", "status", "purchaseDate", "model", "supplier", "remark"],
    seedRows: [
      { id: "eq-1", code: "EQ-1001", name: "数控冲床 A01", category: "冲压设备", workshop: "冲压车间", owner: "张磊", status: "运行中", purchaseDate: "2024-05-12", model: "JH21-125", supplier: "华创设备", remark: "主力产线设备" },
      { id: "eq-2", code: "EQ-1002", name: "焊接机器人 B07", category: "焊接设备", workshop: "焊接车间", owner: "李珊", status: "待保养", purchaseDate: "2023-11-08", model: "RX160", supplier: "启远自动化", remark: "三班制使用" },
      { id: "eq-3", code: "EQ-1003", name: "空压机 C02", category: "动力设备", workshop: "动力车间", owner: "王凯", status: "报修中", purchaseDate: "2022-08-18", model: "GA37", supplier: "博联机械", remark: "高温告警待处理" }
    ]
  },
  checkPlan: {
    table: "check_plan",
    fields: ["code", "name", "target", "frequency", "executor", "status", "startDate", "cycleRule", "remark"],
    seedRows: [
      { id: "cp-1", code: "CP-001", name: "冲压设备日检", target: "冲压设备", frequency: "每日", executor: "张磊", status: "执行中", startDate: "2026-03-01", cycleRule: "每日 08:00", remark: "覆盖冲床主线设备" },
      { id: "cp-2", code: "CP-002", name: "焊接设备周检", target: "焊接设备", frequency: "每周", executor: "李珊", status: "执行中", startDate: "2026-03-03", cycleRule: "每周二 09:00", remark: "机器人焊接工位重点巡检" },
      { id: "cp-3", code: "CP-003", name: "动力设备月检", target: "动力设备", frequency: "每月", executor: "王凯", status: "草稿", startDate: "2026-03-10", cycleRule: "每月 10 日 10:00", remark: "空压与供电系统例行检查" }
    ]
  },
  checkItem: {
    table: "check_item",
    fields: ["code", "name", "judgeType", "standardValue", "equipmentType", "riskLevel", "status", "remark"],
    seedRows: [
      { id: "ci-1", code: "CI-001", name: "油压检查", judgeType: "范围判定", standardValue: "4.5-5.5MPa", equipmentType: "冲压设备", riskLevel: "中", status: "启用", remark: "重点确认油压波动" },
      { id: "ci-2", code: "CI-002", name: "焊枪温度", judgeType: "阈值判定", standardValue: "<= 75℃", equipmentType: "焊接设备", riskLevel: "高", status: "启用", remark: "超温需立即上报" },
      { id: "ci-3", code: "CI-003", name: "噪音检测", judgeType: "人工确认", standardValue: "无异常噪音", equipmentType: "动力设备", riskLevel: "低", status: "启用", remark: "结合现场听诊执行" }
    ]
  },
  checkRecord: {
    table: "check_record",
    fields: ["code", "equipmentName", "executor", "executeTime", "result", "status", "planName", "abnormalDesc", "remark"],
    seedRows: [
      { id: "cr-1", code: "CR-202603-001", equipmentName: "数控冲床 A01", executor: "张磊", executeTime: "2026-03-19T08:20", result: "正常", status: "已完成", planName: "冲压设备日检", abnormalDesc: "", remark: "设备状态稳定" },
      { id: "cr-2", code: "CR-202603-002", equipmentName: "焊接机器人 B07", executor: "李珊", executeTime: "2026-03-19T09:00", result: "异常", status: "待转报修", planName: "焊接设备周检", abnormalDesc: "焊点偏移，轨迹重复误差超标", remark: "已通知运维确认" },
      { id: "cr-3", code: "CR-202603-003", equipmentName: "空压机 C02", executor: "王凯", executeTime: "2026-03-18T15:30", result: "正常", status: "已完成", planName: "动力设备月检", abnormalDesc: "", remark: "滤芯压差正常" }
    ]
  },
  maintenancePlan: {
    table: "maintenance_plan",
    fields: ["code", "name", "target", "cycle", "owner", "status", "nextDate", "strategy", "remark"],
    seedRows: [
      { id: "mp-1", code: "MP-001", name: "空压系统月保养", target: "空压机 C02", cycle: "30 天", owner: "王凯", status: "执行中", nextDate: "2026-04-18", strategy: "月度保养", remark: "重点检查滤芯与排水阀" },
      { id: "mp-2", code: "MP-002", name: "焊接机器人季度保养", target: "焊接机器人 B07", cycle: "90 天", owner: "李珊", status: "执行中", nextDate: "2026-06-16", strategy: "季度保养", remark: "重点检查伺服与焊枪总成" },
      { id: "mp-3", code: "MP-003", name: "冲床半年度保养", target: "数控冲床 A01", cycle: "180 天", owner: "张磊", status: "草稿", nextDate: "2026-09-15", strategy: "半年度保养", remark: "准备安排停机窗口" }
    ]
  },
  maintenanceItem: {
    table: "maintenance_item",
    fields: ["code", "name", "standardHours", "targetType", "riskLevel", "status", "executeStandard", "remark"],
    seedRows: [
      { id: "mi-1", code: "MI-001", name: "过滤芯更换", standardHours: "2 小时", targetType: "动力设备", riskLevel: "中", status: "启用", executeStandard: "按月更换并记录压差", remark: "空压系统专用" },
      { id: "mi-2", code: "MI-002", name: "润滑补充", standardHours: "1 小时", targetType: "冲压设备", riskLevel: "低", status: "启用", executeStandard: "按点位补充润滑脂", remark: "需拍照留档" },
      { id: "mi-3", code: "MI-003", name: "伺服校准", standardHours: "4 小时", targetType: "焊接设备", riskLevel: "高", status: "启用", executeStandard: "校准后进行空载联调", remark: "需工程师复核" }
    ]
  },
  maintenanceRecord: {
    table: "maintenance_record",
    fields: ["code", "target", "executor", "maintainDate", "result", "status", "planName", "remark"],
    seedRows: [
      { id: "mr-1", code: "MR-001", target: "空压机 C02", executor: "王凯", maintainDate: "2026-03-18", result: "完成", status: "已归档", planName: "空压系统月保养", remark: "压差恢复正常" },
      { id: "mr-2", code: "MR-002", target: "焊接机器人 B07", executor: "李珊", maintainDate: "2026-03-17", result: "延期", status: "待执行", planName: "焊接机器人季度保养", remark: "等待停机窗口" },
      { id: "mr-3", code: "MR-003", target: "数控冲床 A01", executor: "张磊", maintainDate: "2026-03-15", result: "完成", status: "已归档", planName: "冲床半年度保养", remark: "润滑与紧固项已完成" }
    ]
  },
  repairApply: {
    table: "repair_apply",
    fields: ["ticketNo", "equipmentName", "level", "stage", "team", "status", "applicant", "applyDate", "faultType", "contact", "description"],
    seedRows: [
      { id: "ra-1", ticketNo: "RA-001", equipmentName: "空压机 C02", level: "高", stage: "处理中", team: "动力班组", status: "报修中", applicant: "王凯", applyDate: "2026-03-19", faultType: "高温报警", contact: "00000000000", description: "运行 30 分钟后持续高温并伴随噪音" },
      { id: "ra-2", ticketNo: "RA-002", equipmentName: "焊接机器人 B07", level: "中", stage: "待验收", team: "机器人班组", status: "待关闭", applicant: "李珊", applyDate: "2026-03-18", faultType: "轨迹偏差", contact: "00000000000", description: "焊点偏移，已完成伺服校准待现场验收" },
      { id: "ra-3", ticketNo: "RA-003", equipmentName: "数控冲床 A01", level: "低", stage: "待受理", team: "冲压班组", status: "新建", applicant: "张磊", applyDate: "2026-03-19", faultType: "光栅报警", contact: "00000000000", description: "偶发停机，待运维接单排查" }
    ]
  },
  accessoryApply: {
    table: "accessory_apply",
    fields: ["applyNo", "applicant", "accessoryName", "quantity", "approvalStatus", "deliveryStatus", "warehouse", "applyDate", "purpose", "receiver", "remark"],
    seedRows: [
      { id: "aa-1", applyNo: "AA-001", applicant: "张磊", accessoryName: "轴承 6208", quantity: 12, approvalStatus: "审批通过", deliveryStatus: "待出库", warehouse: "一号配件仓", applyDate: "2026-03-19", purpose: "冲床 A01 定保备件", receiver: "张磊", remark: "周五前需要到位" },
      { id: "aa-2", applyNo: "AA-002", applicant: "李珊", accessoryName: "伺服皮带", quantity: 4, approvalStatus: "审批中", deliveryStatus: "未出库", warehouse: "一号配件仓", applyDate: "2026-03-18", purpose: "机器人 B07 报修", receiver: "李珊", remark: "需审批后立即出库" },
      { id: "aa-3", applyNo: "AA-003", applicant: "王凯", accessoryName: "过滤芯", quantity: 10, approvalStatus: "已完成", deliveryStatus: "已出库", warehouse: "动力配件仓", applyDate: "2026-03-17", purpose: "空压系统月保养", receiver: "王凯", remark: "已由仓库签收" }
    ]
  },
  user: {
    table: "sys_user",
    fields: ["account", "name", "dept", "role", "mobile", "status", "email", "syncStatus", "remark"],
    seedRows: [
      { id: "user-1", account: "admin", name: "管理员", dept: "设备运维部", role: "超级管理员", mobile: "00000000000", status: "启用", email: "demo@example.invalid", syncStatus: "已同步", remark: "系统内置账号" },
      { id: "user-2", account: "zhanglei", name: "张磊", dept: "冲压车间", role: "设备管理员", mobile: "00000000000", status: "启用", email: "demo@example.invalid", syncStatus: "已同步", remark: "负责冲压设备" },
      { id: "user-3", account: "lishan", name: "李珊", dept: "焊接车间", role: "设备管理员", mobile: "00000000000", status: "启用", email: "demo@example.invalid", syncStatus: "待同步", remark: "负责焊接机器人" }
    ]
  }
};

export function getModuleDefinition(moduleKey) {
  return moduleDefinitions[moduleKey] ?? null;
}
