# WMS Agent · V1.0.0

面向机动能源部门的工业资产与检维修协作 Demo。用 Main Agent 协调工厂 Agent 归集月报，用对话助手查询资产、跟进维修计划，在人员确认后完成业务变更。

技术栈：Vue 3 + Vite / Spring Boot 2.7.6 / SQLite / DeepSeek 官方 API。全部内置业务数据为合成演示数据。

## 1.0 可以演示什么

| 场景 | 体验 |
| --- | --- |
| 多厂资产月报 | Main Agent 分派三厂任务，各厂独立上下文处理，展示工具轨迹、来源和汇总 |
| 缺项补报 | 初次只有两厂72台；三厂补报后只重试未完成工厂，最终108台、工艺72台、停机18台 |
| 不同格式上报 | Excel / CSV，14个标准字段，A/B/C三种定义好的布局；预检、映射确认、快照与原文件保留 |
| 对话跟计划 | 零星维修、大修、巡检查询；自然语言提出变更，确认后保存并回读 |
| 模拟能耗分析 | 100 → 120 kWh，变化+20%；明确标注模拟来源和对比期间 |
| 资产与权限 | 12厂432台设备，全部资产/工艺设备并排查看；总部、工厂资产员、维修员角色 |

## 快速启动

准备 JDK 17、Maven 3.9+、Node.js 18+ 和 npm。macOS/Linux 启动器还需要 `bash`、`curl`、`lsof`。

```bash
git clone https://github.com/caliwon2077/WMS-Agent.git
cd WMS-Agent
mkdir -p project/local
cp project/deepseek.env.example project/local/deepseek.env
```

在本机 `project/local/deepseek.env` 填写 `DEEPSEEK_API_KEY`，然后执行：

```bash
./start-demo.command
```

打开 <http://127.0.0.1:5173>。演示账号 `admin`，密码 `Demo@2026`。其他角色和逐步演示路径见 [启动与演示手册](QUICKSTART.md)。未配置密钥时，人工业务查询仍可使用，AI任务会明确提示未配置。

## 文档与版本

- [1.0版本说明](project/files/releases/V1.0.0/RELEASE_NOTES.md)
- [启动与演示手册](QUICKSTART.md)
- [验证说明](project/files/releases/V1.0.0/VALIDATION.md)
- [AI Agent 体验清单](project/files/releases/V1.0.0/AI_AGENT_EXPERIENCE.md)
- [更新记录](CHANGELOG.md)

软件版本为 `1.0.0`，Git标签为 `v1.0.0`。PRD需求基线仍为V4.1，两种编号分别管理软件发布和需求文档。

## 代码与数据

```text
wms_system_frontend/src/demo/                 AI协作界面
wms_system_javabackend/.../modules/demo/       业务、权限和Agent执行器
project/files/releases/V1.0.0/                 版本说明、样例与验证记录
project/scripts/                              真实模型预检与联调脚本
project/local/                                本地密钥，不提交
db/                                           运行数据库，不提交
```

启动器创建 `db/wms_demo.sqlite`，重启保留数据。模型只能调用白名单业务工具；模型调用和数据库写入分离，计划变更必须由人员确认。内置测试使用隔离临时数据库。

前端构建：在 `wms_system_frontend/` 执行 `npm ci` 和 `npm run build`。后端验证：在 `wms_system_javabackend/` 执行 `mvn package`。

## 本版边界

本版为可运行Demo：保留12厂组织，Agent月报先覆盖一、二、三厂；审批为示意，能耗为模拟。自动定时汇报、12厂完整归集、任务自动恢复、完整双表同步/解锁、真实能源接入及生产部署后置。默认仅监听本机，旧业务API在Demo配置下关闭。

公开版本包含源码、合成样例和交付说明，不包含API密钥、运行数据库、客户原始附件和本地审查日志。
