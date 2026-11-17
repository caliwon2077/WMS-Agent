<script setup>
import { ref, watch, onBeforeUnmount, nextTick } from 'vue';
import { api, post, stateName, factoryName } from './api';
import ResultCard from './ResultCard.vue';
const props = defineProps({ factory: String, model: Object, full: Boolean });
const emit = defineEmits(['source', 'report', 'changed', 'error']);
const text = ref(''), conversation = ref(''), turns = ref([]), pending = ref(false), activeRun = ref(null), feed = ref(null);
let generation = 0, timer;
watch(() => props.factory, () => { generation++; clearTimeout(timer); conversation.value = ''; turns.value = []; pending.value = false; activeRun.value = null; });
onBeforeUnmount(() => { generation++; clearTimeout(timer); });
async function scroll() { await nextTick(); if (feed.value) feed.value.scrollTop = feed.value.scrollHeight; }
async function send(message = text.value) {
  if (!message.trim() || pending.value) return;
  const current = generation; text.value = ''; turns.value.push({ role: 'user', text: message }); pending.value = true; await scroll();
  try {
    const run = await post('/chat', { message, factory_id: props.factory, conversation_id: conversation.value });
    if (generation !== current) return; conversation.value = run.conversation_id; activeRun.value = run; poll(run.id, current);
  } catch (e) { if (generation === current) { pending.value = false; turns.value.push({ role: 'assistant', error: e.message }); scroll(); } }
}
async function poll(id, current) {
  try {
    const run = await api(`/runs/${id}`); if (current !== generation) return; activeRun.value = run;
    if (['running', 'queued'].includes(run.state)) { timer = setTimeout(() => poll(id, current), 900); return; }
    pending.value = false;
    turns.value.push({ role: 'assistant', run, text: run.question, error: run.error?.message || (['cancelled', 'timeout', 'interrupted'].includes(run.state) ? stateName(run.state) : ''), cards: run.cards || [] });
    await scroll(); emit('changed');
  } catch (e) { if (current === generation) { pending.value = false; turns.value.push({ role: 'assistant', error: e.message }); } }
}
async function cancel() { if (activeRun.value) { try { await post(`/runs/${activeRun.value.id}/cancel`); } catch (e) { emit('error', e.message); } } }
defineExpose({ send });
</script>
<template>
  <section class="agent-panel" :class="{ 'full-agent': full }">
    <header class="agent-header"><div class="agent-avatar">✦</div><div><strong>机动能源业务助手</strong><small>DeepSeek · {{ factoryName(factory) }}</small></div><span class="online-dot" :class="{ offline: !model?.configured }" /></header>
    <div class="chat-feed" ref="feed" aria-live="polite">
      <div v-if="!turns.length" class="chat-welcome"><span class="eyebrow">把工作交给助手</span><h3>从一句话开始，<br>一起把事情办完。</h3><p>查询设备、跟进计划，或协调工厂生成一份月报。我会先核对范围，涉及修改时请你确认。</p><div class="suggestions"><button @click="send(`查看${factory === 'ALL' ? '二厂' : factoryName(factory)}未完成的维修计划`)">查一下未完成计划 ↗</button><button @click="send('汇总一、二、三厂2026年10月资产月报，使用V2模板')">让三个工厂提交月报 ↗</button><button @click="send('分析二厂空调2026年11月1日至16日的模拟用电，与2026年10月1日至16日对比')">看看空调模拟用电 ↗</button></div></div>
      <div v-for="(turn, i) in turns" :key="i" class="chat-turn" :class="turn.role"><div v-if="turn.role === 'assistant'" class="assistant-label">✦ 业务助手 <span v-if="turn.run">{{ stateName(turn.run.state) }}</span></div><p v-if="turn.text" class="message-text">{{ turn.text }}</p><div v-if="turn.error" class="inline-error">{{ turn.error }}<small>本次未计为 AI 办理成功。可重试或使用业务页面。</small></div><ResultCard v-for="(card,j) in turn.cards" :key="j" :card="card" @source="emit('source', $event)" @report="emit('report', $event)" @changed="turn.run.state = $event?.cancelled ? 'cancelled' : 'success'; emit('changed')" @error="emit('error', $event)" /><details v-if="turn.run?.trace?.length" class="trace-details"><summary>查看本次工具执行记录</summary><div v-for="(t,j) in turn.run.trace" :key="j" class="trace-item"><span>{{ t.action }}</span><span>{{ stateName(t.state) }}</span></div><small>{{ turn.run.id }}</small></details></div>
      <div v-if="pending" class="working-state"><span class="spinner" /><div><strong>助手正在办理</strong><small>{{ activeRun?.trace?.at(-1)?.action || '正在提交任务' }} · {{ stateName(activeRun?.trace?.at(-1)?.state || 'queued') }}</small></div><button class="text-button" @click="cancel">取消</button></div>
    </div>
    <form class="chat-composer" @submit.prevent="send()"><textarea v-model="text" :disabled="pending" rows="2" maxlength="4000" placeholder="例如：二厂哪些维修计划已经逾期？" aria-label="给业务助手的任务" @keydown.enter.exact.prevent="send()" /><div><small>合成演示数据 · 修改前确认</small><button class="send-button" :disabled="pending || !text.trim()" aria-label="发送任务">↑</button></div></form>
  </section>
</template>
