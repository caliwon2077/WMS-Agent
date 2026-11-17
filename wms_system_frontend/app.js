const STORAGE_KEY = "wms-system-prototype-auth";

const moduleMeta = {
  home: { title: "运营总览", description: "聚合设备、点检、保养、报修、配件与审批数据，形成管理驾驶舱。", permissions: [], columns: [], rows: [] },
  manufacturer: { title: "设备厂商", description: "维护设备厂商档案，支撑设备台账、采购与售后追踪。", permissions: ["add", "edit", "delete", "import"], columns: ["厂商编码", "厂商名称", "联系人", "联系电话", "状态"], rows: [["M001", "华创设备", "刘海涛", "00000000000", "启用"], ["M002", "启远自动化", "周洁", "00000000000", "启用"], ["M003", "博联机械", "冯楠", "00000000000", "停用"]] },
  equipmentCategory: { title: "设备分类", description: "建立设备分类树，用于设备归档、统计和计划配置。", permissions: ["add", "edit", "delete"], columns: ["分类编码", "分类名称", "上级分类", "点检策略", "状态"], rows: [["EC001", "冲压设备", "-", "标准周检", "启用"], ["EC002", "焊接设备", "-", "标准日检", "启用"], ["EC003", "空压系统", "动力设备", "月度保养", "启用"]] },
  equipmentAssets: { title: "设备列表", description: "维护设备全生命周期台账，关联点检、保养、报修、知识库和 IoT 实例。", permissions: ["add", "edit", "delete", "import", "addKnowledge", "editKnowledge"], columns: ["设备编码", "设备名称", "分类", "厂商", "责任人", "运行状态"], rows: [["EQ-1001", "数控冲床 A01", "冲压设备", "华创设备", "张磊", "运行中"], ["EQ-1002", "焊接机器人 B07", "焊接设备", "启远自动化", "李珊", "待保养"], ["EQ-1003", "空压机 C02", "空压系统", "博联机械", "王凯", "报修中"]] },
  checkPlan: { title: "点检计划", description: "按设备、区域、周期定义点检任务和责任人。", permissions: ["add", "edit", "delete"], columns: ["计划编码", "计划名称", "点检对象", "频率", "执行人", "状态"], rows: [["CP-001", "冲压设备日检", "冲压设备", "每日", "张磊", "执行中"], ["CP-002", "焊接设备周检", "焊接设备", "每周", "李珊", "执行中"], ["CP-003", "动力设备月检", "动力设备", "每月", "王凯", "草稿"]] },
  checkItem: { title: "点检项目", description: "维护点检项目、标准值和判定规则。", permissions: ["add", "edit", "delete"], columns: ["项目编码", "项目名称", "判定方式", "标准值", "适用设备", "状态"], rows: [["CI-001", "油压检查", "范围判定", "4.5-5.5MPa", "冲压设备", "启用"], ["CI-002", "焊枪温度", "阈值判定", "≤ 75℃", "焊接设备", "启用"], ["CI-003", "噪音检测", "人工确认", "无异常噪声", "动力设备", "启用"]] },
  checkRecord: { title: "点检记录", description: "记录点检执行结果，并支持批量完成和异常转报修。", permissions: ["add", "edit", "batchFinish", "batchCancel"], columns: ["记录编号", "设备名称", "执行人", "执行时间", "结果", "处理状态"], rows: [["CR-202603-001", "数控冲床 A01", "张磊", "2026-03-19 08:20", "正常", "已完成"], ["CR-202603-002", "焊接机器人 B07", "李珊", "2026-03-19 09:00", "异常", "待转报修"], ["CR-202603-003", "空压机 C02", "王凯", "2026-03-18 15:30", "正常", "已完成"]] },
  maintenancePlan: { title: "保养计划", description: "定义保养周期、执行策略和责任人。", permissions: ["add", "edit", "delete"], columns: ["计划编号", "保养计划", "对象", "周期", "责任人", "状态"], rows: [["MP-001", "空压系统月度保养", "空压机 C02", "30 天", "王凯", "执行中"], ["MP-002", "焊接臂季度保养", "焊接机器人 B07", "90 天", "李珊", "执行中"], ["MP-003", "冲床半年保养", "数控冲床 A01", "180 天", "张磊", "草稿"]] },
  maintenanceItem: { title: "保养项目", description: "维护保养标准项、工时和注意事项。", permissions: ["add", "edit", "delete"], columns: ["项目编号", "项目名称", "标准工时", "适用对象", "风险级别", "状态"], rows: [["MI-001", "滤芯更换", "2 小时", "空压系统", "中", "启用"], ["MI-002", "润滑补充", "1 小时", "冲压设备", "低", "启用"], ["MI-003", "伺服校准", "4 小时", "焊接设备", "高", "启用"]] },
  maintenanceRecord: { title: "保养记录", description: "沉淀保养执行过程与结果，支持批量完成、取消。", permissions: ["add", "edit", "batchFinish", "batchCancel"], columns: ["记录编号", "保养对象", "执行人", "保养日期", "结果", "状态"], rows: [["MR-001", "空压机 C02", "王凯", "2026-03-18", "完成", "已归档"], ["MR-002", "焊接机器人 B07", "李珊", "2026-03-17", "延期", "待执行"], ["MR-003", "数控冲床 A01", "张磊", "2026-03-15", "完成", "已归档"]] },
  deviceModel: { title: "设备模型", description: "定义设备采集点、属性、告警和控制命令。", permissions: ["add", "edit", "delete"], columns: ["模型编码", "模型名称", "采集点数", "告警规则", "接入协议", "状态"], rows: [["DM-001", "冲床基础模型", "18", "5 条", "Modbus TCP", "启用"], ["DM-002", "焊接机器人模型", "26", "8 条", "OPC UA", "启用"], ["DM-003", "空压机模型", "14", "4 条", "MQTT", "启用"]] },
  iotDevice: { title: "设备实例", description: "绑定设备台账与接入模型，管理实例状态和连接参数。", permissions: ["add", "edit", "delete"], columns: ["实例编码", "设备名称", "模型", "连接状态", "最后上报", "责任人"], rows: [["ID-001", "数控冲床 A01", "冲床基础模型", "在线", "2026-03-19 09:41", "张磊"], ["ID-002", "焊接机器人 B07", "焊接机器人模型", "在线", "2026-03-19 09:39", "李珊"], ["ID-003", "空压机 C02", "空压机模型", "离线", "2026-03-18 20:13", "王凯"]] },
  networkDriver: { title: "网络驱动", description: "统一维护设备接入驱动、协议和连接模板。", permissions: ["add", "edit"], columns: ["驱动编码", "驱动名称", "协议", "版本", "供应商", "状态"], rows: [["ND-001", "Modbus TCP 驱动", "Modbus TCP", "1.2.0", "简信工业", "启用"], ["ND-002", "OPC UA 驱动", "OPC UA", "2.0.1", "简信工业", "启用"], ["ND-003", "MQTT 边缘网关", "MQTT", "1.9.8", "简信工业", "测试中"]] },
  repairStage: { title: "报修阶段", description: "配置报修工单从申请到关闭的业务阶段。", permissions: ["add", "edit", "delete"], columns: ["阶段编码", "阶段名称", "顺序", "是否可回退", "SLA", "状态"], rows: [["RS-01", "待受理", "1", "否", "2 小时", "启用"], ["RS-02", "处理中", "2", "是", "8 小时", "启用"], ["RS-03", "待验收", "3", "是", "4 小时", "启用"]] },
  repairProcess: { title: "报修流程", description: "维护报修流转模板、节点规则和责任角色。", permissions: ["add", "edit", "delete"], columns: ["流程编码", "流程名称", "适用范围", "节点数", "审批方式", "状态"], rows: [["RP-001", "设备紧急报修流程", "关键设备", "6", "串行", "启用"], ["RP-002", "常规报修流程", "普通设备", "4", "串行", "启用"], ["RP-003", "外协维修流程", "外部服务", "5", "混合", "草稿"]] },
  repairApply: { title: "报修申请", description: "发起故障报修、工单分派、维修处理和验收闭环。", permissions: ["add", "edit", "delete", "allot"], columns: ["工单编号", "设备名称", "故障等级", "当前阶段", "责任班组", "状态"], rows: [["RA-001", "空压机 C02", "高", "处理中", "动力班组", "报修中"], ["RA-002", "焊接机器人 B07", "中", "待验收", "机器人班组", "待关闭"], ["RA-003", "数控冲床 A01", "低", "待受理", "冲压班组", "新建"]] },
  material: { title: "产品管理", description: "维护产品主数据，服务于设备、工艺和产线联动场景。", permissions: ["add"], columns: ["产品编码", "产品名称", "规格", "单位", "产品分类", "状态"], rows: [["MAT-001", "箱体总成", "A 型", "件", "总成类", "启用"], ["MAT-002", "支架组件", "B 型", "件", "结构件", "启用"], ["MAT-003", "导轨座", "C 型", "件", "机加件", "启用"]] },
  workArea: { title: "工作区域", description: "管理厂区、楼层、产线、工位等区域结构。", permissions: ["add", "edit", "delete"], columns: ["区域编码", "区域名称", "上级区域", "区域负责人", "设备数", "状态"], rows: [["WA-001", "一号厂房", "-", "刘海涛", "28", "启用"], ["WA-002", "焊接产线", "一号厂房", "李珊", "12", "启用"], ["WA-003", "动力机房", "一号厂房", "王凯", "6", "启用"]] },
  workshop: { title: "车间配置", description: "维护车间主数据与组织归属。", permissions: ["add", "edit", "delete"], columns: ["车间编码", "车间名称", "所属区域", "负责人", "班组数", "状态"], rows: [["WS-001", "冲压车间", "一号厂房", "张磊", "3", "启用"], ["WS-002", "焊接车间", "焊接产线", "李珊", "2", "启用"], ["WS-003", "动力车间", "动力机房", "王凯", "1", "启用"]] },
  process: { title: "工序配置", description: "配置标准工序，为生产与设备管理打通上下游关联。", permissions: ["add", "edit", "delete"], columns: ["工序编码", "工序名称", "标准工时", "责任工种", "关联设备", "状态"], rows: [["PR-001", "下料", "18 分钟", "操作工", "数控冲床 A01", "启用"], ["PR-002", "焊接", "24 分钟", "焊工", "焊接机器人 B07", "启用"], ["PR-003", "气密检测", "10 分钟", "检验员", "空压系统", "启用"]] },
  oaSetting: { title: "流程管理", description: "维护 OA 流程配置、开关和适用范围。", permissions: [], columns: ["配置项", "说明", "当前值", "更新时间", "维护人", "状态"], rows: [["审批超时提醒", "超过 SLA 自动提醒", "开启", "2026-03-16", "管理员", "生效"], ["会签模式", "多人节点处理方式", "全部同意", "2026-03-14", "管理员", "生效"], ["抄送通知", "审批完成自动通知", "开启", "2026-03-12", "管理员", "生效"]] },
  flow: { title: "流程设计", description: "可视化设计审批流程、分支规则和处理节点。", permissions: ["add"], columns: ["流程编码", "流程名称", "业务类型", "节点数", "版本", "发布状态"], rows: [["FL-001", "设备报修审批流", "设备报修", "5", "v1.4", "已发布"], ["FL-002", "配件申请审批流", "配件申请", "4", "v1.1", "已发布"], ["FL-003", "保养延期审批流", "保养管理", "3", "v0.9", "草稿"]] },
  approvalType: { title: "审批类型", description: "维护审批分类并绑定流程模板。", permissions: ["add", "edit", "delete", "modifyLog"], columns: ["类型编码", "类型名称", "绑定流程", "审批人策略", "修改日志", "状态"], rows: [["AT-001", "设备报修审批", "设备报修审批流", "按角色", "3 次", "启用"], ["AT-002", "配件领用审批", "配件申请审批流", "按部门", "2 次", "启用"], ["AT-003", "保养延期审批", "保养延期审批流", "指定人", "1 次", "启用"]] },
  accessory: { title: "配件列表", description: "维护配件主档、库存单位和安全库存线。", permissions: ["add", "edit", "delete", "import"], columns: ["配件编码", "配件名称", "规格", "仓库", "当前库存", "状态"], rows: [["AC-001", "轴承 6208", "标准件", "一号配件仓", "126", "充足"], ["AC-002", "伺服皮带", "B-300", "一号配件仓", "16", "预警"], ["AC-003", "过滤芯", "F-20", "动力配件仓", "52", "充足"]] },
  accessoryCategory: { title: "配件分类", description: "管理配件分类和层级结构。", permissions: ["add", "edit", "delete", "import"], columns: ["分类编码", "分类名称", "上级分类", "配件数", "状态", "备注"], rows: [["ACC-001", "标准件", "-", "168", "启用", "通用配件"], ["ACC-002", "电气件", "-", "94", "启用", "电控模块"], ["ACC-003", "耗材", "-", "73", "启用", "周期更换"]] },
  accessorySupplier: { title: "供应商", description: "维护配件供应商、交付周期和评级信息。", permissions: ["add", "edit", "delete", "import"], columns: ["供应商编码", "供应商名称", "联系人", "供货周期", "评级", "状态"], rows: [["SUP-001", "鑫达工业件", "赵新", "3 天", "A", "合作中"], ["SUP-002", "北辰电气", "韩梅", "5 天", "A", "合作中"], ["SUP-003", "安润耗材", "孙亮", "2 天", "B", "合作中"]] },
  accessoryWarehouse: { title: "配件仓库", description: "维护仓库、库区、容量和库存控制参数。", permissions: ["add", "edit", "delete", "import"], columns: ["仓库编码", "仓库名称", "库区数", "管理员", "库存总量", "状态"], rows: [["WH-001", "一号配件仓", "6", "刘海涛", "3840", "启用"], ["WH-002", "动力配件仓", "3", "王凯", "1240", "启用"], ["WH-003", "应急备件仓", "2", "李珊", "620", "启用"]] },
  accessoryApply: { title: "配件申请", description: "发起配件申请、审批、出库和领用闭环。", permissions: ["add", "edit", "delete", "delivery"], columns: ["申请单号", "申请人", "配件名称", "申请数量", "当前状态", "出库状态"], rows: [["AA-001", "张磊", "轴承 6208", "12", "审批通过", "待发料"], ["AA-002", "李珊", "伺服皮带", "4", "审批中", "未出库"], ["AA-003", "王凯", "过滤芯", "10", "已完成", "已发料"]] },
  menu: { title: "菜单管理", description: "维护系统菜单、页面路由、按钮和图标。", permissions: ["add", "edit", "delete"], columns: ["菜单名称", "类型", "路由", "组件", "排序", "状态"], rows: [["设备资产", "目录", "/ems", "-", "2", "显示"], ["设备列表", "菜单", "/ems/equipmentAssets", "/ems/equipmentAssets/index", "3", "显示"], ["新增按钮", "按钮", "equipmentAssets:add", "-", "1", "显示"]] },
  dictTree: { title: "树形字典", description: "适配层级型业务字典，并支持修改日志。", permissions: ["add", "edit", "delete", "modifyLog"], columns: ["字典编码", "字典名称", "层级", "明细数", "修改记录", "状态"], rows: [["DT-001", "设备故障树", "3", "42", "5 条", "启用"], ["DT-002", "区域层级", "4", "31", "2 条", "启用"], ["DT-003", "安全等级", "2", "8", "1 条", "启用"]] },
  setting: { title: "系统配置", description: "配置系统参数、开关项和业务默认值。", permissions: ["add", "edit", "modifyLog"], columns: ["配置键", "配置名称", "当前值", "分类", "修改记录", "状态"], rows: [["repair.timeout", "报修超时小时", "8", "报修管理", "4 条", "启用"], ["check.autoCreate", "点检自动生成", "true", "点检管理", "2 条", "启用"], ["inventory.warnDays", "库存预警天数", "15", "配件管理", "3 条", "启用"]] },
  apiLog: { title: "接口日志", description: "跟踪系统接口调用情况与异常响应。", permissions: ["delete"], columns: ["日志编号", "接口地址", "调用人", "耗时", "结果", "时间"], rows: [["LOG-001", "/api/auth/menus", "admin", "86ms", "成功", "2026-03-19 09:42"], ["LOG-002", "/api/repair/apply/list", "admin", "102ms", "成功", "2026-03-19 09:40"], ["LOG-003", "/api/iot/device/report", "gateway-01", "188ms", "失败", "2026-03-19 08:58"]] },
  quartz: { title: "定时任务", description: "维护任务调度表达式和执行策略。", permissions: ["add", "edit", "delete", "modifyLog"], columns: ["任务编码", "任务名称", "Cron", "最近执行", "结果", "状态"], rows: [["JOB-001", "点检任务生成", "0 0 6 * * ?", "2026-03-19 06:00", "成功", "运行中"], ["JOB-002", "库存预警扫描", "0 0/30 * * * ?", "2026-03-19 09:30", "成功", "运行中"], ["JOB-003", "IoT 离线检测", "0 */10 * * * ?", "2026-03-19 09:40", "成功", "运行中"]] },
  org: { title: "单位区域", description: "维护公司、工厂、事业部等组织层级。", permissions: ["add", "edit", "delete"], columns: ["组织编码", "组织名称", "上级组织", "负责人", "人数", "状态"], rows: [["ORG-001", "集团总部", "-", "陈总", "24", "启用"], ["ORG-002", "一号工厂", "集团总部", "刘海涛", "168", "启用"], ["ORG-003", "设备运维部", "一号工厂", "王凯", "24", "启用"]] },
  city: { title: "城市管理", description: "维护区域化数据，为组织、仓库、地址提供基础数据。", permissions: ["add", "edit", "delete"], columns: ["城市编码", "城市名称", "省份", "区域", "仓库数", "状态"], rows: [["CITY-001", "郑州", "河南", "华中", "2", "启用"], ["CITY-002", "洛阳", "河南", "华中", "1", "启用"], ["CITY-003", "新乡", "河南", "华中", "1", "启用"]] },
  dictSys: { title: "系统字典", description: "维护全局基础字典，支持修改日志。", permissions: ["add", "edit", "delete", "modifyLog"], columns: ["字典编码", "字典名称", "分类", "项数", "修改记录", "状态"], rows: [["DS-001", "设备状态", "系统", "6", "3 条", "启用"], ["DS-002", "报修等级", "系统", "4", "2 条", "启用"], ["DS-003", "审批结果", "系统", "5", "2 条", "启用"]] },
  autoCode: { title: "编码规则", description: "配置设备、工单、申请单等业务编码生成规则。", permissions: ["add", "edit", "delete"], columns: ["规则编码", "业务对象", "前缀", "规则示例", "重置周期", "状态"], rows: [["CODE-001", "设备编号", "EQ", "EQ-202603-001", "月", "启用"], ["CODE-002", "报修工单", "RA", "RA-202603-001", "月", "启用"], ["CODE-003", "配件申请", "AA", "AA-202603-001", "月", "启用"]] },
  dict: { title: "字典管理", description: "维护普通字典及其配置元数据。", permissions: ["add", "edit", "delete", "modifyLog"], columns: ["字典编码", "字典名称", "值类型", "条目数", "修改记录", "状态"], rows: [["D-001", "班次", "字符串", "3", "1 条", "启用"], ["D-002", "故障等级", "字符串", "4", "2 条", "启用"], ["D-003", "通知方式", "字符串", "5", "1 条", "启用"]] },
  role: { title: "角色管理", description: "配置角色、菜单权限和按钮权限。", permissions: ["add", "edit", "delete"], columns: ["角色编码", "角色名称", "用户数", "菜单数", "按钮权限数", "状态"], rows: [["admin", "超级管理员", "1", "38", "39", "启用"], ["maintainer", "设备管理员", "12", "18", "26", "启用"], ["storekeeper", "仓库管理员", "4", "7", "12", "启用"]] },
  dictItem: { title: "字典明细", description: "维护字典项值、排序和展示策略。", permissions: ["add", "edit", "delete"], columns: ["所属字典", "项编码", "项名称", "排序", "默认值", "状态"], rows: [["班次", "SHIFT-A", "早班", "1", "否", "启用"], ["班次", "SHIFT-B", "中班", "2", "否", "启用"], ["班次", "SHIFT-C", "晚班", "3", "是", "启用"]] },
  dept: { title: "部门管理", description: "维护部门树，并支持同步与全量同步。", permissions: ["sync", "edit", "delete", "syncAll", "add"], columns: ["部门编码", "部门名称", "上级部门", "负责人", "同步状态", "状态"], rows: [["DEP-001", "设备运维部", "一号工厂", "王凯", "已同步", "启用"], ["DEP-002", "焊接制造部", "一号工厂", "李珊", "已同步", "启用"], ["DEP-003", "仓储物流部", "一号工厂", "刘海涛", "待同步", "启用"]] },
  user: { title: "用户管理", description: "维护用户档案、角色、部门和同步状态。", permissions: ["sync", "edit", "delete", "modifyLog", "export", "syncAll", "add", "import"], columns: ["账号", "姓名", "部门", "角色", "手机号", "状态"], rows: [["admin", "管理员", "设备运维部", "超级管理员", "00000000000", "启用"], ["zhanglei", "张磊", "冲压车间", "设备管理员", "00000000000", "启用"], ["lishan", "李珊", "焊接车间", "设备管理员", "00000000000", "启用"]] },
  permissions: { title: "权限设置", description: "面向角色、用户和组织进行授权、撤权和权限配置。", permissions: ["setting", "grant", "revoke"], columns: ["授权对象", "对象类型", "当前菜单数", "按钮权限数", "最后调整", "状态"], rows: [["超级管理员", "角色", "38", "39", "2026-03-15", "生效"], ["设备管理员", "角色", "18", "26", "2026-03-14", "生效"], ["仓库管理员", "角色", "7", "12", "2026-03-13", "生效"]] }
};

const menus = [
  { key: "home", title: "首页", children: [{ key: "home", title: "运营总览" }] },
  { key: "ems", title: "设备资产", children: [{ key: "manufacturer", title: "设备厂商" }, { key: "equipmentCategory", title: "设备分类" }, { key: "equipmentAssets", title: "设备列表" }] },
  { key: "check", title: "设备点检", children: [{ key: "checkPlan", title: "点检计划" }, { key: "checkItem", title: "点检项目" }, { key: "checkRecord", title: "点检记录" }] },
  { key: "maintenance", title: "设备保养", children: [{ key: "maintenancePlan", title: "保养计划" }, { key: "maintenanceItem", title: "保养项目" }, { key: "maintenanceRecord", title: "保养记录" }] },
  { key: "iot", title: "设备接入", children: [{ key: "deviceModel", title: "设备模型" }, { key: "iotDevice", title: "设备实例" }, { key: "networkDriver", title: "网络驱动" }] },
  { key: "repair", title: "设备报修", children: [{ key: "repairStage", title: "报修阶段" }, { key: "repairProcess", title: "报修流程" }, { key: "repairApply", title: "报修申请" }] },
  { key: "mdm", title: "基础配置", children: [{ key: "material", title: "产品管理" }, { key: "workArea", title: "工作区域" }, { key: "workshop", title: "车间配置" }, { key: "process", title: "工序配置" }] },
  { key: "oa", title: "办公审批", children: [{ key: "oaSetting", title: "流程管理" }, { key: "flow", title: "流程设计" }, { key: "approvalType", title: "审批类型" }] },
  { key: "ams", title: "配件管理", children: [{ key: "accessory", title: "配件列表" }, { key: "accessoryCategory", title: "配件分类" }, { key: "accessorySupplier", title: "供应商" }, { key: "accessoryWarehouse", title: "配件仓库" }, { key: "accessoryApply", title: "配件申请" }] },
  { key: "system", title: "系统管理", children: [{ key: "menu", title: "菜单管理" }, { key: "dictTree", title: "树形字典" }, { key: "setting", title: "系统配置" }, { key: "apiLog", title: "接口日志" }, { key: "quartz", title: "定时任务" }, { key: "org", title: "单位区域" }, { key: "city", title: "城市管理" }, { key: "dictSys", title: "系统字典" }, { key: "autoCode", title: "编码规则" }, { key: "dict", title: "字典管理" }, { key: "role", title: "角色管理" }, { key: "dictItem", title: "字典明细" }, { key: "dept", title: "部门管理" }, { key: "user", title: "用户管理" }, { key: "permissions", title: "权限设置" }] }
];

const state = {
  loggedIn: localStorage.getItem(STORAGE_KEY) === "1",
  route: "home",
  user: { name: "管理员", account: "admin", role: "超级管理员", mobile: "00000000000" }
};

const app = document.getElementById("app");

function formatStatus(value) {
  if (["启用", "运行中", "已完成", "成功", "生效", "已发布", "在线", "合作中", "显示", "已同步", "充足", "已归档"].includes(value)) return "success";
  if (["预警", "草稿", "待执行", "审批中", "待发料", "待受理", "待验收", "待保养", "测试中"].includes(value)) return "warning";
  if (["停用", "离线", "失败", "报修中", "新建"].includes(value)) return "danger";
  return "";
}

function actionButtons(permissions) {
  const buttons = [];
  if (permissions.includes("edit")) buttons.push('<button class="action-btn edit">编辑</button>');
  if (permissions.includes("delete")) buttons.push('<button class="action-btn delete">删除</button>');
  if (permissions.includes("setting") || permissions.includes("grant") || permissions.includes("revoke")) buttons.push('<button class="action-btn more">权限配置</button>');
  if (permissions.includes("delivery")) buttons.push('<button class="action-btn more">发料</button>');
  if (permissions.includes("allot")) buttons.push('<button class="action-btn more">分派</button>');
  if (permissions.includes("modifyLog")) buttons.push('<button class="action-btn more">修改日志</button>');
  if (buttons.length === 0) buttons.push('<button class="action-btn more">查看详情</button>');
  return buttons.join("");
}

function renderLogin() {
  app.innerHTML = `
    <div class="login-shell">
      <section class="login-hero">
        <div>
          <div class="hero-badge">类似 EMS/WMS 的企业管理原型</div>
          <h1>设备管理、配件管理与审批协同一体化平台</h1>
          <p>这是一版根据你刚才分析站点结构整理出来的“类似软件”原型。当前重点把系统骨架、模块导航、运营总览、列表页、权限按钮和典型业务页全部搭好，方便后续继续扩展为正式项目。</p>
        </div>
        <div class="hero-metrics">
          <div class="hero-metric"><strong>10</strong><span>一级模块</span></div>
          <div class="hero-metric"><strong>38+</strong><span>业务页面</span></div>
          <div class="hero-metric"><strong>39</strong><span>权限动作</span></div>
        </div>
      </section>
      <section class="login-panel">
        <div class="login-card">
          <h2>系统登录</h2>
          <p>演示账号已内置。你可以直接登录并浏览全部模块原型。</p>
          <div class="form-grid">
            <div class="form-field"><label for="account">账号</label><input id="account" value="admin" placeholder="请输入账号"></div>
            <div class="form-field"><label for="password">密码</label><input id="password" type="password" value="123456" placeholder="请输入密码"></div>
            <div class="form-tip">默认内置账号：<strong>admin / 123456</strong><br>当前原型为前端静态版，重点展示系统结构、页面风格和交互骨架。</div>
            <div class="btn-row">
              <button class="btn-primary" id="login-btn">进入系统</button>
              <button class="btn-secondary" id="fill-btn">填入演示账号</button>
            </div>
          </div>
        </div>
      </section>
    </div>`;

  document.getElementById("fill-btn").addEventListener("click", () => {
    document.getElementById("account").value = "admin";
    document.getElementById("password").value = "123456";
  });

  document.getElementById("login-btn").addEventListener("click", () => {
    const account = document.getElementById("account").value.trim();
    const password = document.getElementById("password").value.trim();
    if (account !== "admin" || password !== "123456") {
      alert("演示环境仅开放 admin / 123456");
      return;
    }
    localStorage.setItem(STORAGE_KEY, "1");
    state.loggedIn = true;
    renderApp();
  });
}

function renderDashboard() {
  return `
    <div class="dashboard-grid">
      <section class="card metric-card"><span class="card-subtitle">设备总量</span><strong>286</strong><small>其中在线设备 241 台，重点设备 38 台</small></section>
      <section class="card metric-card"><span class="card-subtitle">今日点检</span><strong>93%</strong><small>应检 128，已完成 119，异常 7</small></section>
      <section class="card metric-card"><span class="card-subtitle">待处理报修</span><strong>14</strong><small>高优先级 3，超时 1</small></section>
      <section class="card metric-card"><span class="card-subtitle">库存预警</span><strong>21</strong><small>安全库存以下配件 21 项</small></section>
      <section class="card wide-card">
        <div class="card-title"><h2>今日重点事项</h2><span class="muted">设备运维中心</span></div>
        <div class="list-plain">
          <div class="list-item"><strong>空压机 C02 高温告警仍未关闭</strong><span class="muted">当前阶段：处理中，责任班组：动力班组，建议优先派单。</span></div>
          <div class="list-item"><strong>焊接机器人 B07 保养延期待审批</strong><span class="muted">关联流程：保养延期审批流，预计影响焊接产线 2 班次。</span></div>
          <div class="list-item"><strong>伺服皮带库存进入预警阈值</strong><span class="muted">当前库存 16，安全库存 20，建议触发采购或调拨。</span></div>
        </div>
      </section>
      <section class="card side-card">
        <div class="card-title"><h2>待办审批</h2><span class="muted">共 8 条</span></div>
        <div class="list-plain">
          <div class="list-item"><strong>配件申请 AA-001</strong><span class="muted">申请人：张磊，状态：审批通过待发料</span></div>
          <div class="list-item"><strong>报修工单 RA-001</strong><span class="muted">设备：空压机 C02，故障等级：高</span></div>
          <div class="list-item"><strong>保养延期审批 AT-003</strong><span class="muted">焊接机器人 B07，发起人：李珊</span></div>
        </div>
      </section>
    </div>`;
}

function renderModulePage(routeKey) {
  const page = moduleMeta[routeKey];
  const summary = [["记录数", String(page.rows.length * 12 + 8)], ["启用项", String(page.rows.filter(row => row.includes("启用") || row.includes("运行中")).length + 18)], ["今日变更", String((page.permissions.length + page.rows.length) % 7 + 2)], ["关注项", String(page.permissions.length + 3)]];

  const rowsHtml = page.rows.map((row) => {
    const lastValue = row[row.length - 1];
    const statusClass = formatStatus(lastValue);
    const cells = row.map((value, index) => {
      if (index === row.length - 1 && statusClass) return `<td><span class="status-chip ${statusClass}">${value}</span></td>`;
      return `<td>${value}</td>`;
    }).join("");
    return `<tr>${cells}<td><div class="table-actions">${actionButtons(page.permissions)}</div></td></tr>`;
  }).join("");

  const permissionHtml = page.permissions.length ? page.permissions.map(item => `<span class="perm-chip">${item}</span>`).join("") : `<span class="muted">当前页面以查看与配置展示为主。</span>`;

  return `
    <div class="page-layout">
      <section class="page-summary">
        ${summary.map(([label, value]) => `<div class="mini-kpi"><span class="muted">${label}</span><strong>${value}</strong></div>`).join("")}
      </section>
      <section class="card">
        <div class="card-title"><h2>${page.title}</h2><span class="muted">模块原型页</span></div>
        <p class="module-note">${page.description}</p>
      </section>
      <section class="card table-card">
        <div class="table-head">
          <div><strong>${page.title}数据列表</strong><div class="permission-row">${permissionHtml}</div></div>
          <div class="page-toolbar">
            <div class="toolbar-left">
              <input class="search-input" placeholder="搜索 ${page.title} 关键字">
              <select class="toolbar-select"><option>全部状态</option><option>启用</option><option>停用</option><option>处理中</option></select>
            </div>
            <div class="toolbar-right">
              ${page.permissions.includes("add") ? '<button class="btn-primary">新增</button>' : ""}
              ${page.permissions.includes("import") ? '<button class="btn-secondary">导入</button>' : ""}
              ${page.permissions.includes("export") ? '<button class="btn-secondary">导出</button>' : ""}
              ${page.permissions.includes("sync") ? '<button class="btn-secondary">同步</button>' : ""}
              ${page.permissions.includes("syncAll") ? '<button class="btn-secondary">全量同步</button>' : ""}
            </div>
          </div>
        </div>
        <table>
          <thead><tr>${page.columns.map(col => `<th>${col}</th>`).join("")}<th>操作</th></tr></thead>
          <tbody>${rowsHtml || `<tr><td colspan="${page.columns.length + 1}" class="empty-state">暂无数据</td></tr>`}</tbody>
        </table>
      </section>
    </div>`;
}

function renderSidebar() {
  return menus.map((group) => {
    const isActiveGroup = group.children.some(item => item.key === state.route);
    return `
      <div class="menu-group">
        <div class="menu-section">
          <button class="menu-item ${isActiveGroup ? "active" : ""}" data-route="${group.children[0].key}">${group.title}</button>
          <div class="submenu-list">
            ${group.children.map(item => `<button class="submenu-item ${state.route === item.key ? "active" : ""}" data-route="${item.key}">${item.title}</button>`).join("")}
          </div>
        </div>
      </div>`;
  }).join("");
}

function renderContent() {
  if (state.route === "home") return renderDashboard();
  return renderModulePage(state.route);
}

function bindAppEvents() {
  app.querySelectorAll("[data-route]").forEach((node) => {
    node.addEventListener("click", () => {
      state.route = node.dataset.route;
      renderApp();
    });
  });

  document.getElementById("go-home").addEventListener("click", () => {
    state.route = "home";
    renderApp();
  });

  document.getElementById("logout-btn").addEventListener("click", () => {
    localStorage.removeItem(STORAGE_KEY);
    state.loggedIn = false;
    state.route = "home";
    renderLogin();
  });
}

function renderApp() {
  const currentPage = moduleMeta[state.route];
  app.innerHTML = `
    <div class="app-shell">
      <aside class="sidebar">
        <div class="brand-box">
          <p class="brand-title">WMS System</p>
          <p class="brand-subtitle">企业设备与配件协同管理平台原型</p>
        </div>
        <div class="menu-heading">功能导航</div>
        ${renderSidebar()}
      </aside>
      <main class="main-shell">
        <header class="topbar">
          <div class="topbar-left">
            <h1>${currentPage.title}</h1>
            <p>${currentPage.description}</p>
          </div>
          <div class="topbar-right">
            <div class="user-pill">
              <div class="avatar-dot">${state.user.name.slice(0, 1)}</div>
              <div><strong>${state.user.name}</strong><br><span class="muted">${state.user.role}</span></div>
            </div>
            <button class="btn-ghost" id="go-home">回到首页</button>
            <button class="btn-danger" id="logout-btn">退出系统</button>
          </div>
        </header>
        ${renderContent()}
      </main>
    </div>`;
  bindAppEvents();
}

if (state.loggedIn) renderApp();
else renderLogin();
