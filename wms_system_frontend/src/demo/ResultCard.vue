<script setup>
import { ref } from 'vue';
import { post, factoryName, stateName } from './api';
import EnergyChart from './EnergyChart.vue';
const props = defineProps({ card: Object });
const emit = defineEmits(['source', 'report', 'changed', 'error']);
const busy = ref(false), saved = ref(null), cancelled = ref(false);
async function confirm(cancel = false) {
  busy.value = true;
  try { const result = await post(`/confirmations/${props.card.confirmation.id}/${cancel ? 'cancel' : 'confirm'}`); if (cancel) { cancelled.value = true; emit('changed', { cancelled: true }); } else { saved.value = result; emit('changed', { saved: true }); } }
  catch (e) { emit('error', e.message); } finally { busy.value = false; }
}
</script>
<template>
  <div class="result-card">
    <template v-if="card.type === 'energy'"><EnergyChart :data="card" @source="emit('source', $event)" /></template>
    <template v-else-if="card.type === 'confirmation'">
      <div class="section-heading"><h4>确认这次业务变更</h4><span class="tag" :class="saved ? 'green' : 'amber'">{{ saved ? '已保存并回读' : cancelled ? '已取消' : '确认前不修改' }}</span></div>
      <p class="muted small">{{ factoryName(card.confirmation.factory) }} · {{ card.confirmation.action === 'create' ? '新建作业计划' : card.confirmation.before?.id }}</p>
      <dl class="confirmation-fields"><dt>设备 / 计划</dt><dd>{{ card.confirmation.after.asset_name }} · {{ card.confirmation.after.title }}</dd><dt>状态</dt><dd>{{ card.confirmation.before?.state || '新建' }} → <strong>{{ card.confirmation.after.state }}</strong></dd><dt>责任人</dt><dd>{{ card.confirmation.after.assignee }}</dd><dt>计划截止</dt><dd>{{ card.confirmation.after.deadline }}</dd><dt>进度备注</dt><dd>{{ card.confirmation.after.note || '—' }}</dd><template v-if="card.confirmation.after.result"><dt>执行结果</dt><dd>{{ card.confirmation.after.result }}</dd></template></dl>
      <div v-if="!saved && !cancelled" class="button-row"><button class="primary" :disabled="busy" @click="confirm()">{{ busy ? '正在提交…' : '确认并保存' }}</button><button class="secondary" :disabled="busy" @click="confirm(true)">取消</button></div>
      <p v-if="saved" class="success-note">已回读 {{ saved.id }}：{{ saved.state }}。记录版本 {{ saved.version }}。</p><p v-else-if="!cancelled" class="muted small">确认卡 5 分钟内有效；记录变化后需重新确认。</p>
    </template>
    <template v-else-if="card.type === 'assets'">
      <h4>资产查询结果 <span class="tag">{{ factoryName(card.factory_id) }}</span></h4>
      <div class="result-stat-row"><b>{{ card.total }} <small>全部设备</small></b><b>{{ card.process_count }} <small>工艺设备</small></b><b>{{ card.unclassified_count }} <small>待分类</small></b></div>
      <div class="compact-record" v-for="r in card.records" :key="r.id"><div><strong>{{ r.name }}</strong><small>{{ r.code }} · {{ factoryName(r.factory) }} · {{ r.classification }}</small></div><button class="text-button" @click="emit('source', r.id)">{{ r.equipment_state }} ↗</button></div><p v-if="!card.records.length" class="muted">本次查询没有匹配设备。</p><p v-if="card.has_more" class="muted small">当前仅展示第 {{ card.page }} 页，尚有后续数据。</p>
    </template>
    <template v-else-if="card.type === 'plans'">
      <h4>找到 {{ card.total }} 条作业计划</h4><div class="compact-record" v-for="r in card.records" :key="r.id"><div><strong>{{ r.title }}</strong><small>{{ r.id }} · {{ r.type }} · 截止 {{ r.deadline }}</small></div><button class="text-button" @click="emit('source', r.id)">{{ r.state }} ↗</button></div><p v-if="!card.records.length" class="muted">本次查询没有匹配计划。</p><p v-if="card.has_more" class="small muted">第 {{ card.page }} 页，尚有后续数据。</p>
    </template>
    <template v-else-if="card.type === 'report_job'"><h4>月报任务已交办</h4><p class="muted">Main Agent 将协调各厂处理；可在任务面板查看进度与缺项。</p><button class="primary" @click="emit('report', card.run_id)">查看 Agent 协作进度 →</button></template>
    <template v-else-if="card.type === 'import_review'"><h4>文件映射与预检</h4><p>{{ card.row_count }} 行 · 格式 {{ card.format }} · {{ stateName(card.state) }}</p><ul class="issue-list"><li v-for="(e,i) in card.errors" :key="i">第 {{ e.row }} 行：{{ e.message }}</li></ul><p v-if="!card.errors?.length" class="success-note">预检通过，请在工厂上报页面确认提交。</p><button class="text-button" @click="emit('source', card.batch_id)">查看原批次 ↗</button></template>
    <template v-else-if="card.type === 'monthly_report'"><h4>{{ card.title }}</h4><p>已覆盖 {{ card.coverage }} 个工厂 · {{ card.metrics.total }} 台设备。</p><button class="text-button" @click="emit('report', card.source_ref)">打开报告 →</button></template>
    <p v-if="card.evidence_id" class="evidence-label">已核验工具结果 · {{ card.evidence_id }} · 合成演示数据</p>
  </div>
</template>
