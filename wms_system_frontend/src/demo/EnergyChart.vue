<script setup>
import { computed } from 'vue';
import { factoryName } from './api';
const props = defineProps({ data: Object });
const max = computed(() => Math.max(props.data.current || 0, props.data.previous || 0, 1) * 1.2);
const height = v => v == null ? 0 : v / max.value * 135;
</script>
<template>
  <div class="energy-result">
    <div class="section-heading"><div><span class="eyebrow">ENERGY INSIGHT</span><h3>{{ factoryName(data.factory_id) }} · 空调能耗</h3></div><span class="tag amber">模拟能耗数据</span></div>
    <div class="energy-numbers"><div><span>本期用电量</span><strong>{{ data.current ?? '未采集' }} <small v-if="data.current != null">kWh</small></strong></div><div><span>相较同口径期间</span><strong :class="{ 'text-amber': data.change_percent > 0 }">{{ data.change_percent == null ? '不适用' : `${data.change_percent > 0 ? '+' : ''}${data.change_percent}%` }}</strong></div></div>
    <svg viewBox="0 0 500 215" role="img" aria-label="模拟能耗对比柱状图" class="energy-svg">
      <g v-for="i in 4" :key="i"><line x1="35" :y1="25 + (i - 1) * 45" x2="470" :y2="25 + (i - 1) * 45" stroke="#e4e9f1" stroke-dasharray="4 5" /></g>
      <rect x="117" :y="170 - height(data.previous)" width="78" :height="height(data.previous)" rx="7" fill="#b7c7db" />
      <rect x="305" :y="170 - height(data.current)" width="78" :height="height(data.current)" rx="7" fill="#17897b" />
      <text x="156" :y="160 - height(data.previous)" text-anchor="middle" fill="#4b5a70" font-size="15">{{ data.previous ?? '无数据' }}</text>
      <text x="344" :y="160 - height(data.current)" text-anchor="middle" fill="#087b6d" font-size="15">{{ data.current ?? '无数据' }}</text>
      <text x="156" y="197" text-anchor="middle" fill="#6d7c91" font-size="12">{{ data.compare_start?.slice(5) || '未选择' }} — {{ data.compare_end?.slice(5) }}</text>
      <text x="344" y="197" text-anchor="middle" fill="#6d7c91" font-size="12">{{ data.start?.slice(5) }} — {{ data.end?.slice(5) }}</text>
    </svg>
    <p class="muted small">{{ data.notice }}。统计期间 {{ data.start }} 至 {{ data.end }}。</p>
    <button v-if="data.source_ref" class="text-button" @click="$emit('source', data.source_ref)">查看计量来源 ↗</button>
  </div>
</template>
