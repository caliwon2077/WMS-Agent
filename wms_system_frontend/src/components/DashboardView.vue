<script setup>
import { ref } from "vue";

const emit = defineEmits(["navigate"]);

const highlights = [
  {
    id: "highlight-1",
    route: "repairApply",
    title: "空压机 C02 高温告警仍未关闭",
    summary: "当前阶段：处理中，责任班组：动力班组，建议优先派单。",
    level: "高优先级",
    device: "空压机 C02",
    stage: "处理中",
    owner: "动力班组 / 王凯",
    happenedAt: "2026-03-19 09:12",
    impact: "如果持续高温运行，可能影响动力机房稳定性，并波及一号产线供气。",
    detail: "设备在早班启动后 30 分钟内连续触发 3 次高温报警，现场伴随异常噪音。当前已创建报修单 RA-001，维修人员已接单，但尚未完成停机检修。",
    suggestion: "建议立即切换备用空压机，安排动力班组现场复检油温、压力阀和风冷系统。"
  },
  {
    id: "highlight-2",
    route: "maintenancePlan",
    title: "焊接机器人 B07 保养延期待审批",
    summary: "关联流程：保养延期审批流，预计影响焊接产线 2 个班次。",
    level: "审批待办",
    device: "焊接机器人 B07",
    stage: "待审批",
    owner: "设备运维部 / 李珊",
    happenedAt: "2026-03-19 08:40",
    impact: "若延期未获批，保养计划将直接影响焊接工位排产，可能造成当日交付延迟。",
    detail: "该设备原定于今日进行季度保养，但因客户加急订单，产线提出延期申请。当前审批流已提交至设备运维部负责人，尚未形成最终结论。",
    suggestion: "建议同步评估订单紧急程度与设备风险等级，必要时安排夜班窗口执行保养。"
  },
  {
    id: "highlight-3",
    route: "accessoryApply",
    title: "伺服皮带库存进入预警阈值",
    summary: "当前库存 16，安全库存 20，建议触发采购或调拨。",
    level: "库存预警",
    device: "伺服皮带 B-300",
    stage: "待处理",
    owner: "配件仓 / 刘海洋",
    happenedAt: "2026-03-19 10:05",
    impact: "若后续发生机器人抢修，现有库存可能不足以支撑两条产线同时更换。",
    detail: "系统在 10:05 自动触发库存预警。近 7 天该配件已领用 9 条，当前库存周转速度明显高于月均水平，且在途采购单尚未到仓。",
    suggestion: "建议优先检查在途采购状态，同时从动力配件仓评估可用替代库存。"
  }
];

const activeHighlight = ref(null);

function openHighlight(item) {
  activeHighlight.value = item;
}

function closeHighlight() {
  activeHighlight.value = null;
}

function goHandle(item) {
  closeHighlight();
  emit("navigate", item.route);
}
</script>

<template>
  <div class="dashboard-grid">
    <section class="card metric-card">
      <span class="card-subtitle">设备总量</span>
      <strong>286</strong>
      <small>其中在线设备 241 台，关键设备 38 台</small>
    </section>

    <section class="card metric-card">
      <span class="card-subtitle">今日点检</span>
      <strong>93%</strong>
      <small>应检 128，已完成 119，异常 7</small>
    </section>

    <section class="card metric-card">
      <span class="card-subtitle">待处理报修</span>
      <strong>14</strong>
      <small>高优先级 3，超时 1</small>
    </section>

    <section class="card metric-card">
      <span class="card-subtitle">库存预警</span>
      <strong>21</strong>
      <small>低于安全库存的配件共 21 项</small>
    </section>

    <section class="card wide-card">
      <div class="card-title">
        <h2>今日重点事项</h2>
        <span class="muted">设备运维中心</span>
      </div>
      <div class="list-plain">
        <article
          v-for="item in highlights"
          :key="item.id"
          class="list-item list-item-panel"
        >
          <button class="list-item-button" @click="openHighlight(item)">
            <span class="highlight-tag">{{ item.level }}</span>
            <strong>{{ item.title }}</strong>
            <span class="muted">{{ item.summary }}</span>
            <span class="list-link-text">点击查看详情</span>
          </button>

          <div class="highlight-actions">
            <button class="btn-secondary" @click="openHighlight(item)">查看详情</button>
            <button class="btn-primary" @click="goHandle(item)">去处理</button>
          </div>
        </article>
      </div>
    </section>

    <section class="card side-card">
      <div class="card-title">
        <h2>待办审批</h2>
        <span class="muted">共 8 条</span>
      </div>
      <div class="list-plain">
        <div class="list-item">
          <strong>配件申请 AA-001</strong>
          <span class="muted">申请人：张磊，状态：审批通过待出库。</span>
        </div>
        <div class="list-item">
          <strong>报修工单 RA-001</strong>
          <span class="muted">设备：空压机 C02，故障等级：高。</span>
        </div>
        <div class="list-item">
          <strong>保养延期审批 AT-003</strong>
          <span class="muted">设备：焊接机器人 B07，发起人：李珊。</span>
        </div>
      </div>
    </section>

    <div v-if="activeHighlight" class="modal-mask" @click.self="closeHighlight">
      <section class="modal-card detail-modal">
        <div class="modal-header">
          <div>
            <h3>{{ activeHighlight.title }}</h3>
            <p class="muted">今日重点事项详情</p>
          </div>
          <button class="btn-ghost" @click="closeHighlight">关闭</button>
        </div>

        <div class="detail-grid">
          <div class="detail-block">
            <span class="detail-label">事项等级</span>
            <strong>{{ activeHighlight.level }}</strong>
          </div>
          <div class="detail-block">
            <span class="detail-label">关联对象</span>
            <strong>{{ activeHighlight.device }}</strong>
          </div>
          <div class="detail-block">
            <span class="detail-label">当前阶段</span>
            <strong>{{ activeHighlight.stage }}</strong>
          </div>
          <div class="detail-block">
            <span class="detail-label">责任人/班组</span>
            <strong>{{ activeHighlight.owner }}</strong>
          </div>
        </div>

        <div class="detail-section">
          <span class="detail-label">发生时间</span>
          <p>{{ activeHighlight.happenedAt }}</p>
        </div>

        <div class="detail-section">
          <span class="detail-label">详细描述</span>
          <p>{{ activeHighlight.detail }}</p>
        </div>

        <div class="detail-section">
          <span class="detail-label">影响范围</span>
          <p>{{ activeHighlight.impact }}</p>
        </div>

        <div class="detail-section">
          <span class="detail-label">处理建议</span>
          <p>{{ activeHighlight.suggestion }}</p>
        </div>

        <div class="modal-actions">
          <button class="btn-primary" @click="goHandle(activeHighlight)">去处理</button>
          <button class="btn-secondary" @click="closeHighlight">稍后查看</button>
        </div>
      </section>
    </div>
  </div>
</template>
