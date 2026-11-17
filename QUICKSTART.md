# WMS Agent 1.0 启动与演示

软件版本：V1.0.0。文档版本：V1.0。日期：2026-11-17。

## 环境与启动

需要JDK 17、Maven 3.9+、Node.js 18+、npm。首次构建需要联网下载依赖；AI功能需要有效的DeepSeek官方API密钥。macOS/Linux一键脚本需要bash、curl、lsof；本次实测环境为macOS和JDK 17。

从仓库根目录执行：

```bash
mkdir -p project/local
cp project/deepseek.env.example project/local/deepseek.env
chmod 600 project/local/deepseek.env
```

编辑该文件，在 `DEEPSEEK_API_KEY=` 后填写自己的密钥。不要放入前端或提交到Git。模型默认 `deepseek-v4-pro`。然后运行：

```bash
./start-demo.command
```

打开 <http://127.0.0.1:5173>；后端健康检查 <http://127.0.0.1:8080/api/v1/system/health>。Ctrl+C停止本次启动的服务。端口占用会明确提示，不会自动结束其他程序。

Windows或需要分开启动时，在一个终端进入 `wms_system_javabackend`：

```bash
mvn package
java -jar target/wms-system-backend-1.0.0.jar --server.address=127.0.0.1
```

另一个终端进入 `wms_system_frontend`：

```bash
npm ci
npm run dev
```

相对配置路径以该后端工作目录为准。独立启动方式遵循同一端口与本地密钥文件约定；Windows未在本次发布中实机验证。

## 账号

| 账号 | 密码 | 范围 |
| --- | --- | --- |
| admin | Demo@2026 | 总部、全部12厂 |
| factory01 ～ factory12 | Demo@2026 | 对应单厂资产员 |
| tech02 | Demo@2026 | 二厂维修员，仅可修改分配给自己的任务 |

这些是公开合成环境的测试账号。

## 十分钟演示路径

1. 用admin登录，协作总览选择2026-10、三个工厂、V2，点击“让Agent归集月报”。初始结果为两厂72台、工艺48台、停机12台；三厂待补资料。
2. 到工厂上报，选择三厂、月度盘点、2026-10，先上传缺字段样例，观察预检失败；再上传修正文件，确认36行映射并提交。页面可下载样例，仓库也保存在本版 `fixtures/`。
3. 回原月报，只重试未完成工厂。最终108台、工艺72台、停机18台。查看来源、资产动向、检维修明细；确认报告版本并下载CSV。
4. 用V1发起另一份报告，比较V2新增的停机指标。旧报告的模板与确认版本保持不变。
5. 在业务助手输入“查看二厂所有类型未完成的作业计划”，继续说“把计划WX-02-001改为进行中，备注维修人员已到场”。核对确认卡，再点击确认，查看保存与回读。
6. 输入“分析二厂空调2026年11月1日至16日模拟用电，与2026年10月1日至16日对比”。观察100 / 120 kWh和+20%，以及期间和模拟来源。
7. 退出，用factory01登录，检查只显示一厂；请求二厂数据应被拒绝。

## 重复演示

数据库默认 `db/wms_demo.sqlite`，重启不会重置。已有三厂快照时，新的月报会直接覆盖三厂；已变更的计划应选择新的合法状态或更新备注。

需要从初始缺厂状态重新演示时，先停止旧服务，再从项目根目录指定一个尚不存在的数据库文件：

```bash
WMS_DEMO_DB="$PWD/db/wms_demo_fresh_01.sqlite" ./start-demo.command
```

也可指定空闲端口：

```bash
WMS_BACKEND_PORT=18080 WMS_FRONTEND_PORT=15173 ./start-demo.command
```

运行日志在 `project/runtime/`。无密钥、外网异常或模型账户异常会显示失败；人工查询无需模型。不要把AI失败解释为业务数据为零。

## 验证脚本

`mvn package`运行自动测试且不调用真实模型；前端使用 `npm run build`。`python3 project/scripts/deepseek_preflight.py` 和 `python3 project/scripts/demo_acceptance.py` 会使用本机配置请求官方API并产生相应调用费用，后者使用独立临时数据库并覆盖当前版本的联调输出。仅在需要重新验收时运行。
