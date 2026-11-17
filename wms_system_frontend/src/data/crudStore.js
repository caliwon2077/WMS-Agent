const STORAGE_PREFIX = "wms-system-crud";

function createManufacturerRows() {
  return [
    { id: "mf-1", code: "M001", name: "华创设备", contact: "刘海洋", mobile: "00000000000", email: "demo@example.invalid", address: "郑州市高新区工业路 88 号", status: "启用", remark: "冲压设备长期合作厂商" },
    { id: "mf-2", code: "M002", name: "启远自动化", contact: "周洁", mobile: "00000000000", email: "demo@example.invalid", address: "苏州市工业园区胜利大道 16 号", status: "启用", remark: "机器人与自动化改造供应商" },
    { id: "mf-3", code: "M003", name: "博联机械", contact: "冯蕾", mobile: "00000000000", email: "demo@example.invalid", address: "青岛市西海岸新区装备路 6 号", status: "停用", remark: "空压设备原始供应商" }
  ];
}

function createEquipmentCategoryRows() {
  return [
    { id: "ec-1", code: "EC001", name: "冲压设备", parentCategory: "-", checkStrategy: "标准周检", maintenanceStrategy: "半年度保养", status: "启用", remark: "覆盖冲床、折弯和周边辅机" },
    { id: "ec-2", code: "EC002", name: "焊接设备", parentCategory: "-", checkStrategy: "标准日检", maintenanceStrategy: "季度保养", status: "启用", remark: "覆盖机器人焊接与人工焊接设备" },
    { id: "ec-3", code: "EC003", name: "动力设备", parentCategory: "-", checkStrategy: "月度巡检", maintenanceStrategy: "月度保养", status: "启用", remark: "覆盖空压、供电和动能系统" }
  ];
}

function createEquipmentRows() {
  return [
    { id: "eq-1", code: "EQ-1001", name: "数控冲床 A01", category: "冲压设备", workshop: "冲压车间", owner: "张磊", status: "运行中", purchaseDate: "2024-05-12", model: "JH21-125", supplier: "华创设备", remark: "主力产线设备" },
    { id: "eq-2", code: "EQ-1002", name: "焊接机器人 B07", category: "焊接设备", workshop: "焊接车间", owner: "李珊", status: "待保养", purchaseDate: "2023-11-08", model: "RX160", supplier: "启远自动化", remark: "三班制使用" },
    { id: "eq-3", code: "EQ-1003", name: "空压机 C02", category: "动力设备", workshop: "动力车间", owner: "王凯", status: "报修中", purchaseDate: "2022-08-18", model: "GA37", supplier: "博联机械", remark: "高温告警待处理" }
  ];
}

function createCheckPlanRows() {
  return [
    { id: "cp-1", code: "CP-001", name: "冲压设备日检", target: "冲压设备", frequency: "每日", executor: "张磊", status: "执行中", startDate: "2026-03-01", cycleRule: "每日 08:00", remark: "覆盖冲床主线设备" },
    { id: "cp-2", code: "CP-002", name: "焊接设备周检", target: "焊接设备", frequency: "每周", executor: "李珊", status: "执行中", startDate: "2026-03-03", cycleRule: "每周二 09:00", remark: "机器人焊接工位重点巡检" },
    { id: "cp-3", code: "CP-003", name: "动力设备月检", target: "动力设备", frequency: "每月", executor: "王凯", status: "草稿", startDate: "2026-03-10", cycleRule: "每月 10 日 10:00", remark: "空压与供电系统例行检查" }
  ];
}

function createCheckItemRows() {
  return [
    { id: "ci-1", code: "CI-001", name: "油压检查", judgeType: "范围判定", standardValue: "4.5-5.5MPa", equipmentType: "冲压设备", riskLevel: "中", status: "启用", remark: "重点确认油压波动" },
    { id: "ci-2", code: "CI-002", name: "焊枪温度", judgeType: "阈值判定", standardValue: "<= 75℃", equipmentType: "焊接设备", riskLevel: "高", status: "启用", remark: "超温需立即上报" },
    { id: "ci-3", code: "CI-003", name: "噪音检测", judgeType: "人工确认", standardValue: "无异常噪音", equipmentType: "动力设备", riskLevel: "低", status: "启用", remark: "结合现场听诊执行" }
  ];
}

function createCheckRecordRows() {
  return [
    { id: "cr-1", code: "CR-202603-001", equipmentName: "数控冲床 A01", executor: "张磊", executeTime: "2026-03-19T08:20", result: "正常", status: "已完成", planName: "冲压设备日检", abnormalDesc: "", remark: "设备状态稳定" },
    { id: "cr-2", code: "CR-202603-002", equipmentName: "焊接机器人 B07", executor: "李珊", executeTime: "2026-03-19T09:00", result: "异常", status: "待转报修", planName: "焊接设备周检", abnormalDesc: "焊点偏移，轨迹重复误差超标", remark: "已通知运维确认" },
    { id: "cr-3", code: "CR-202603-003", equipmentName: "空压机 C02", executor: "王凯", executeTime: "2026-03-18T15:30", result: "正常", status: "已完成", planName: "动力设备月检", abnormalDesc: "", remark: "滤芯压差正常" }
  ];
}

function createMaintenancePlanRows() {
  return [
    { id: "mp-1", code: "MP-001", name: "空压系统月保养", target: "空压机 C02", cycle: "30 天", owner: "王凯", status: "执行中", nextDate: "2026-04-18", strategy: "月度保养", remark: "重点检查滤芯与排水阀" },
    { id: "mp-2", code: "MP-002", name: "焊接机器人季度保养", target: "焊接机器人 B07", cycle: "90 天", owner: "李珊", status: "执行中", nextDate: "2026-06-16", strategy: "季度保养", remark: "重点检查伺服与焊枪总成" },
    { id: "mp-3", code: "MP-003", name: "冲床半年度保养", target: "数控冲床 A01", cycle: "180 天", owner: "张磊", status: "草稿", nextDate: "2026-09-15", strategy: "半年度保养", remark: "准备安排停机窗口" }
  ];
}

function createMaintenanceItemRows() {
  return [
    { id: "mi-1", code: "MI-001", name: "过滤芯更换", standardHours: "2 小时", targetType: "动力设备", riskLevel: "中", status: "启用", executeStandard: "按月更换并记录压差", remark: "空压系统专用" },
    { id: "mi-2", code: "MI-002", name: "润滑补充", standardHours: "1 小时", targetType: "冲压设备", riskLevel: "低", status: "启用", executeStandard: "按点位补充润滑脂", remark: "需拍照留档" },
    { id: "mi-3", code: "MI-003", name: "伺服校准", standardHours: "4 小时", targetType: "焊接设备", riskLevel: "高", status: "启用", executeStandard: "校准后进行空载联调", remark: "需工程师复核" }
  ];
}

function createMaintenanceRecordRows() {
  return [
    { id: "mr-1", code: "MR-001", target: "空压机 C02", executor: "王凯", maintainDate: "2026-03-18", result: "完成", status: "已归档", planName: "空压系统月保养", remark: "压差恢复正常" },
    { id: "mr-2", code: "MR-002", target: "焊接机器人 B07", executor: "李珊", maintainDate: "2026-03-17", result: "延期", status: "待执行", planName: "焊接机器人季度保养", remark: "等待停机窗口" },
    { id: "mr-3", code: "MR-003", target: "数控冲床 A01", executor: "张磊", maintainDate: "2026-03-15", result: "完成", status: "已归档", planName: "冲床半年度保养", remark: "润滑与紧固项已完成" }
  ];
}

function createRepairRows() {
  return [
    { id: "ra-1", ticketNo: "RA-001", equipmentName: "空压机 C02", level: "高", stage: "处理中", team: "动力班组", status: "报修中", applicant: "王凯", applyDate: "2026-03-19", faultType: "高温报警", contact: "00000000000", description: "运行 30 分钟后持续高温并伴随噪音" },
    { id: "ra-2", ticketNo: "RA-002", equipmentName: "焊接机器人 B07", level: "中", stage: "待验收", team: "机器人班组", status: "待关闭", applicant: "李珊", applyDate: "2026-03-18", faultType: "轨迹偏差", contact: "00000000000", description: "焊点偏移，已完成伺服校准待现场验收" },
    { id: "ra-3", ticketNo: "RA-003", equipmentName: "数控冲床 A01", level: "低", stage: "待受理", team: "冲压班组", status: "新建", applicant: "张磊", applyDate: "2026-03-19", faultType: "光栅报警", contact: "00000000000", description: "偶发停机，待运维接单排查" }
  ];
}

function createAccessoryApplyRows() {
  return [
    { id: "aa-1", applyNo: "AA-001", applicant: "张磊", accessoryName: "轴承 6208", quantity: 12, approvalStatus: "审批通过", deliveryStatus: "待出库", warehouse: "一号配件仓", applyDate: "2026-03-19", purpose: "冲床 A01 定保备件", receiver: "张磊", remark: "周五前需要到位" },
    { id: "aa-2", applyNo: "AA-002", applicant: "李珊", accessoryName: "伺服皮带", quantity: 4, approvalStatus: "审批中", deliveryStatus: "未出库", warehouse: "一号配件仓", applyDate: "2026-03-18", purpose: "机器人 B07 报修", receiver: "李珊", remark: "需审批后立即出库" },
    { id: "aa-3", applyNo: "AA-003", applicant: "王凯", accessoryName: "过滤芯", quantity: 10, approvalStatus: "已完成", deliveryStatus: "已出库", warehouse: "动力配件仓", applyDate: "2026-03-17", purpose: "空压系统月保养", receiver: "王凯", remark: "已由仓库签收" }
  ];
}

function createUserRows() {
  return [
    { id: "user-1", account: "admin", name: "管理员", dept: "设备运维部", role: "超级管理员", mobile: "00000000000", status: "启用", email: "demo@example.invalid", syncStatus: "已同步", remark: "系统内置账号" },
    { id: "user-2", account: "zhanglei", name: "张磊", dept: "冲压车间", role: "设备管理员", mobile: "00000000000", status: "启用", email: "demo@example.invalid", syncStatus: "已同步", remark: "负责冲压设备" },
    { id: "user-3", account: "lishan", name: "李珊", dept: "焊接车间", role: "设备管理员", mobile: "00000000000", status: "启用", email: "demo@example.invalid", syncStatus: "待同步", remark: "负责焊接机器人" }
  ];
}

export const crudConfigs = {
  manufacturer: {
    storageKey: `${STORAGE_PREFIX}-manufacturer`,
    entityName: "厂商",
    primaryField: "name",
    statusField: "status",
    defaultRows: createManufacturerRows(),
    columns: [
      { key: "code", label: "厂商编码" },
      { key: "name", label: "厂商名称" },
      { key: "contact", label: "联系人" },
      { key: "mobile", label: "联系电话" },
      { key: "status", label: "状态", isStatus: true }
    ],
    fields: [
      { key: "code", label: "厂商编码", type: "text", required: true, placeholder: "如 M004" },
      { key: "name", label: "厂商名称", type: "text", required: true, placeholder: "请输入厂商名称" },
      { key: "contact", label: "联系人", type: "text", required: true, placeholder: "请输入联系人" },
      { key: "mobile", label: "联系电话", type: "text", required: true, placeholder: "请输入联系电话" },
      { key: "email", label: "邮箱", type: "text", placeholder: "请输入邮箱" },
      { key: "address", label: "联系地址", type: "text", placeholder: "请输入联系地址" },
      { key: "status", label: "状态", type: "select", required: true, options: ["启用", "停用"] },
      { key: "remark", label: "备注", type: "textarea", placeholder: "补充厂商说明" }
    ]
  },
  equipmentCategory: {
    storageKey: `${STORAGE_PREFIX}-equipmentCategory`,
    entityName: "设备分类",
    primaryField: "name",
    statusField: "status",
    defaultRows: createEquipmentCategoryRows(),
    columns: [
      { key: "code", label: "分类编码" },
      { key: "name", label: "分类名称" },
      { key: "parentCategory", label: "上级分类" },
      { key: "checkStrategy", label: "点检策略" },
      { key: "status", label: "状态", isStatus: true }
    ],
    fields: [
      { key: "code", label: "分类编码", type: "text", required: true, placeholder: "如 EC004" },
      { key: "name", label: "分类名称", type: "text", required: true, placeholder: "请输入分类名称" },
      { key: "parentCategory", label: "上级分类", type: "select", required: true, options: ["-", "冲压设备", "焊接设备", "动力设备", "检测设备"] },
      { key: "checkStrategy", label: "点检策略", type: "select", required: true, options: ["标准日检", "标准周检", "月度巡检", "专项点检"] },
      { key: "maintenanceStrategy", label: "保养策略", type: "select", required: true, options: ["月度保养", "季度保养", "半年度保养", "年度保养"] },
      { key: "status", label: "状态", type: "select", required: true, options: ["启用", "停用"] },
      { key: "remark", label: "备注", type: "textarea", placeholder: "补充分类说明" }
    ]
  },
  equipmentAssets: {
    storageKey: `${STORAGE_PREFIX}-equipmentAssets`,
    entityName: "设备",
    primaryField: "name",
    statusField: "status",
    defaultRows: createEquipmentRows(),
    columns: [
      { key: "code", label: "设备编码" },
      { key: "name", label: "设备名称" },
      { key: "category", label: "分类" },
      { key: "workshop", label: "所属车间" },
      { key: "owner", label: "负责人" },
      { key: "status", label: "运行状态", isStatus: true }
    ],
    fields: [
      { key: "code", label: "设备编码", type: "text", required: true, placeholder: "如 EQ-1004" },
      { key: "name", label: "设备名称", type: "text", required: true, placeholder: "请输入设备名称" },
      { key: "category", label: "分类", type: "select", required: true, options: ["冲压设备", "焊接设备", "动力设备", "检测设备"] },
      { key: "workshop", label: "所属车间", type: "select", required: true, options: ["冲压车间", "焊接车间", "动力车间", "总装车间"] },
      { key: "owner", label: "负责人", type: "text", required: true, placeholder: "请输入负责人" },
      { key: "status", label: "运行状态", type: "select", required: true, options: ["运行中", "待保养", "报修中", "停机"] },
      { key: "purchaseDate", label: "购置日期", type: "date", required: true },
      { key: "model", label: "设备型号", type: "text", placeholder: "请输入型号" },
      { key: "supplier", label: "供应商", type: "text", placeholder: "请输入供应商" },
      { key: "remark", label: "备注", type: "textarea", placeholder: "补充设备说明" }
    ]
  },
  checkPlan: {
    storageKey: `${STORAGE_PREFIX}-checkPlan`,
    entityName: "点检计划",
    primaryField: "name",
    statusField: "status",
    defaultRows: createCheckPlanRows(),
    columns: [
      { key: "code", label: "计划编号" },
      { key: "name", label: "计划名称" },
      { key: "target", label: "点检对象" },
      { key: "frequency", label: "频率" },
      { key: "executor", label: "执行人" },
      { key: "status", label: "状态", isStatus: true }
    ],
    fields: [
      { key: "code", label: "计划编号", type: "text", required: true, placeholder: "如 CP-004" },
      { key: "name", label: "计划名称", type: "text", required: true, placeholder: "请输入计划名称" },
      { key: "target", label: "点检对象", type: "select", required: true, options: ["冲压设备", "焊接设备", "动力设备", "检测设备"] },
      { key: "frequency", label: "频率", type: "select", required: true, options: ["每日", "每周", "每月", "每季度"] },
      { key: "executor", label: "执行人", type: "text", required: true, placeholder: "请输入执行人" },
      { key: "status", label: "状态", type: "select", required: true, options: ["执行中", "草稿", "停用"] },
      { key: "startDate", label: "开始日期", type: "date", required: true },
      { key: "cycleRule", label: "周期规则", type: "text", placeholder: "请输入周期规则" },
      { key: "remark", label: "备注", type: "textarea", placeholder: "补充点检计划说明" }
    ]
  },
  checkItem: {
    storageKey: `${STORAGE_PREFIX}-checkItem`,
    entityName: "点检项目",
    primaryField: "name",
    statusField: "status",
    defaultRows: createCheckItemRows(),
    columns: [
      { key: "code", label: "项目编码" },
      { key: "name", label: "项目名称" },
      { key: "judgeType", label: "判定方式" },
      { key: "standardValue", label: "标准值" },
      { key: "equipmentType", label: "适用设备" },
      { key: "status", label: "状态", isStatus: true }
    ],
    fields: [
      { key: "code", label: "项目编码", type: "text", required: true, placeholder: "如 CI-004" },
      { key: "name", label: "项目名称", type: "text", required: true, placeholder: "请输入项目名称" },
      { key: "judgeType", label: "判定方式", type: "select", required: true, options: ["范围判定", "阈值判定", "人工确认"] },
      { key: "standardValue", label: "标准值", type: "text", required: true, placeholder: "请输入标准值" },
      { key: "equipmentType", label: "适用设备", type: "select", required: true, options: ["冲压设备", "焊接设备", "动力设备", "检测设备"] },
      { key: "riskLevel", label: "风险等级", type: "select", required: true, options: ["低", "中", "高"] },
      { key: "status", label: "状态", type: "select", required: true, options: ["启用", "停用"] },
      { key: "remark", label: "备注", type: "textarea", placeholder: "补充项目说明" }
    ]
  },
  checkRecord: {
    storageKey: `${STORAGE_PREFIX}-checkRecord`,
    entityName: "点检记录",
    primaryField: "code",
    statusField: "status",
    defaultRows: createCheckRecordRows(),
    columns: [
      { key: "code", label: "记录编号" },
      { key: "equipmentName", label: "设备名称" },
      { key: "executor", label: "执行人" },
      { key: "executeTime", label: "执行时间" },
      { key: "result", label: "结果" },
      { key: "status", label: "状态", isStatus: true }
    ],
    fields: [
      { key: "code", label: "记录编号", type: "text", required: true, placeholder: "如 CR-202603-004" },
      { key: "equipmentName", label: "设备名称", type: "text", required: true, placeholder: "请输入设备名称" },
      { key: "executor", label: "执行人", type: "text", required: true, placeholder: "请输入执行人" },
      { key: "executeTime", label: "执行时间", type: "datetime-local", required: true },
      { key: "result", label: "点检结果", type: "select", required: true, options: ["正常", "异常"] },
      { key: "status", label: "状态", type: "select", required: true, options: ["已完成", "待转报修", "已取消"] },
      { key: "planName", label: "关联计划", type: "text", placeholder: "请输入关联计划" },
      { key: "abnormalDesc", label: "异常描述", type: "textarea", placeholder: "请输入异常描述" },
      { key: "remark", label: "备注", type: "textarea", placeholder: "补充点检记录说明" }
    ]
  },
  maintenancePlan: {
    storageKey: `${STORAGE_PREFIX}-maintenancePlan`,
    entityName: "保养计划",
    primaryField: "name",
    statusField: "status",
    defaultRows: createMaintenancePlanRows(),
    columns: [
      { key: "code", label: "计划编号" },
      { key: "name", label: "计划名称" },
      { key: "target", label: "保养对象" },
      { key: "cycle", label: "周期" },
      { key: "owner", label: "负责人" },
      { key: "status", label: "状态", isStatus: true }
    ],
    fields: [
      { key: "code", label: "计划编号", type: "text", required: true, placeholder: "如 MP-004" },
      { key: "name", label: "计划名称", type: "text", required: true, placeholder: "请输入计划名称" },
      { key: "target", label: "保养对象", type: "text", required: true, placeholder: "请输入保养对象" },
      { key: "cycle", label: "周期", type: "text", required: true, placeholder: "如 30 天" },
      { key: "owner", label: "负责人", type: "text", required: true, placeholder: "请输入负责人" },
      { key: "status", label: "状态", type: "select", required: true, options: ["执行中", "草稿", "停用"] },
      { key: "nextDate", label: "下次保养日期", type: "date", required: true },
      { key: "strategy", label: "保养策略", type: "select", required: true, options: ["月度保养", "季度保养", "半年度保养", "年度保养"] },
      { key: "remark", label: "备注", type: "textarea", placeholder: "补充保养计划说明" }
    ]
  },
  maintenanceItem: {
    storageKey: `${STORAGE_PREFIX}-maintenanceItem`,
    entityName: "保养项目",
    primaryField: "name",
    statusField: "status",
    defaultRows: createMaintenanceItemRows(),
    columns: [
      { key: "code", label: "项目编号" },
      { key: "name", label: "项目名称" },
      { key: "standardHours", label: "标准工时" },
      { key: "targetType", label: "适用对象" },
      { key: "riskLevel", label: "风险等级" },
      { key: "status", label: "状态", isStatus: true }
    ],
    fields: [
      { key: "code", label: "项目编号", type: "text", required: true, placeholder: "如 MI-004" },
      { key: "name", label: "项目名称", type: "text", required: true, placeholder: "请输入项目名称" },
      { key: "standardHours", label: "标准工时", type: "text", required: true, placeholder: "如 2 小时" },
      { key: "targetType", label: "适用对象", type: "select", required: true, options: ["冲压设备", "焊接设备", "动力设备", "检测设备"] },
      { key: "riskLevel", label: "风险等级", type: "select", required: true, options: ["低", "中", "高"] },
      { key: "status", label: "状态", type: "select", required: true, options: ["启用", "停用"] },
      { key: "executeStandard", label: "执行标准", type: "textarea", placeholder: "请输入执行标准" },
      { key: "remark", label: "备注", type: "textarea", placeholder: "补充保养项目说明" }
    ]
  },
  maintenanceRecord: {
    storageKey: `${STORAGE_PREFIX}-maintenanceRecord`,
    entityName: "保养记录",
    primaryField: "code",
    statusField: "status",
    defaultRows: createMaintenanceRecordRows(),
    columns: [
      { key: "code", label: "记录编号" },
      { key: "target", label: "保养对象" },
      { key: "executor", label: "执行人" },
      { key: "maintainDate", label: "保养日期" },
      { key: "result", label: "结果" },
      { key: "status", label: "状态", isStatus: true }
    ],
    fields: [
      { key: "code", label: "记录编号", type: "text", required: true, placeholder: "如 MR-004" },
      { key: "target", label: "保养对象", type: "text", required: true, placeholder: "请输入保养对象" },
      { key: "executor", label: "执行人", type: "text", required: true, placeholder: "请输入执行人" },
      { key: "maintainDate", label: "保养日期", type: "date", required: true },
      { key: "result", label: "保养结果", type: "select", required: true, options: ["完成", "延期", "取消"] },
      { key: "status", label: "状态", type: "select", required: true, options: ["已归档", "待执行", "已取消"] },
      { key: "planName", label: "关联计划", type: "text", placeholder: "请输入关联计划" },
      { key: "remark", label: "备注", type: "textarea", placeholder: "补充保养记录说明" }
    ]
  },
  repairApply: {
    storageKey: `${STORAGE_PREFIX}-repairApply`,
    entityName: "报修单",
    primaryField: "ticketNo",
    statusField: "status",
    defaultRows: createRepairRows(),
    columns: [
      { key: "ticketNo", label: "工单编号" },
      { key: "equipmentName", label: "设备名称" },
      { key: "level", label: "故障等级" },
      { key: "stage", label: "当前阶段" },
      { key: "team", label: "责任班组" },
      { key: "status", label: "状态", isStatus: true }
    ],
    fields: [
      { key: "ticketNo", label: "工单编号", type: "text", required: true, placeholder: "如 RA-004" },
      { key: "equipmentName", label: "设备名称", type: "text", required: true, placeholder: "请输入设备名称" },
      { key: "level", label: "故障等级", type: "select", required: true, options: ["高", "中", "低"] },
      { key: "stage", label: "当前阶段", type: "select", required: true, options: ["待受理", "处理中", "待验收", "已关闭"] },
      { key: "team", label: "责任班组", type: "select", required: true, options: ["动力班组", "机器人班组", "冲压班组", "外协班组"] },
      { key: "status", label: "状态", type: "select", required: true, options: ["新建", "报修中", "待关闭", "已完成"] },
      { key: "applicant", label: "报修人", type: "text", required: true, placeholder: "请输入报修人" },
      { key: "applyDate", label: "报修日期", type: "date", required: true },
      { key: "faultType", label: "故障类型", type: "text", placeholder: "如 高温报警" },
      { key: "contact", label: "联系电话", type: "text", placeholder: "请输入联系电话" },
      { key: "description", label: "故障描述", type: "textarea", required: true, placeholder: "请描述故障现象" }
    ]
  },
  accessoryApply: {
    storageKey: `${STORAGE_PREFIX}-accessoryApply`,
    entityName: "配件申请",
    primaryField: "applyNo",
    statusField: "approvalStatus",
    defaultRows: createAccessoryApplyRows(),
    columns: [
      { key: "applyNo", label: "申请单号" },
      { key: "applicant", label: "申请人" },
      { key: "accessoryName", label: "配件名称" },
      { key: "quantity", label: "申请数量" },
      { key: "approvalStatus", label: "审批状态", isStatus: true },
      { key: "deliveryStatus", label: "出库状态", isStatus: true }
    ],
    fields: [
      { key: "applyNo", label: "申请单号", type: "text", required: true, placeholder: "如 AA-004" },
      { key: "applicant", label: "申请人", type: "text", required: true, placeholder: "请输入申请人" },
      { key: "accessoryName", label: "配件名称", type: "text", required: true, placeholder: "请输入配件名称" },
      { key: "quantity", label: "申请数量", type: "number", required: true, min: 1 },
      { key: "approvalStatus", label: "审批状态", type: "select", required: true, options: ["审批中", "审批通过", "已完成", "已驳回"] },
      { key: "deliveryStatus", label: "出库状态", type: "select", required: true, options: ["未出库", "待出库", "已出库"] },
      { key: "warehouse", label: "仓库", type: "select", required: true, options: ["一号配件仓", "动力配件仓", "应急备件仓"] },
      { key: "applyDate", label: "申请日期", type: "date", required: true },
      { key: "purpose", label: "用途说明", type: "text", placeholder: "请输入用途" },
      { key: "receiver", label: "领用人", type: "text", placeholder: "请输入领用人" },
      { key: "remark", label: "备注", type: "textarea", placeholder: "补充申请说明" }
    ]
  },
  user: {
    storageKey: `${STORAGE_PREFIX}-user`,
    entityName: "用户",
    primaryField: "account",
    statusField: "status",
    defaultRows: createUserRows(),
    columns: [
      { key: "account", label: "账号" },
      { key: "name", label: "姓名" },
      { key: "dept", label: "部门" },
      { key: "role", label: "角色" },
      { key: "mobile", label: "手机号" },
      { key: "status", label: "状态", isStatus: true }
    ],
    fields: [
      { key: "account", label: "账号", type: "text", required: true, placeholder: "请输入账号" },
      { key: "name", label: "姓名", type: "text", required: true, placeholder: "请输入姓名" },
      { key: "dept", label: "部门", type: "select", required: true, options: ["设备运维部", "冲压车间", "焊接车间", "动力车间", "仓储物流部"] },
      { key: "role", label: "角色", type: "select", required: true, options: ["超级管理员", "设备管理员", "仓库管理员", "维修工程师"] },
      { key: "mobile", label: "手机号", type: "text", required: true, placeholder: "请输入手机号" },
      { key: "status", label: "状态", type: "select", required: true, options: ["启用", "停用"] },
      { key: "email", label: "邮箱", type: "text", placeholder: "请输入邮箱" },
      { key: "syncStatus", label: "同步状态", type: "select", options: ["已同步", "待同步"] },
      { key: "remark", label: "备注", type: "textarea", placeholder: "补充账号说明" }
    ]
  }
};

export function createEmptyRecord(pageKey) {
  const config = crudConfigs[pageKey];
  return config.fields.reduce((record, field) => {
    if (field.type === "select") record[field.key] = field.options?.[0] ?? "";
    else if (field.type === "number") record[field.key] = field.min ?? 0;
    else record[field.key] = "";
    return record;
  }, { id: "" });
}

export function loadCrudRows(pageKey) {
  const config = crudConfigs[pageKey];
  const raw = localStorage.getItem(config.storageKey);
  if (!raw) {
    const seeded = config.defaultRows.map((row) => ({ ...row }));
    saveCrudRows(pageKey, seeded);
    return seeded;
  }

  try {
    const parsed = JSON.parse(raw);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    const seeded = config.defaultRows.map((row) => ({ ...row }));
    saveCrudRows(pageKey, seeded);
    return seeded;
  }
}

export function saveCrudRows(pageKey, rows) {
  const config = crudConfigs[pageKey];
  localStorage.setItem(config.storageKey, JSON.stringify(rows));
}

export function createRecordId(pageKey) {
  return `${pageKey}-${Date.now()}-${Math.random().toString(16).slice(2, 8)}`;
}
