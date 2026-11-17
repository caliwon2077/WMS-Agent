<script setup>
import { computed } from "vue";
import { formatStatus } from "../data/systemData";

const props = defineProps({
  page: { type: Object, required: true }
});

const summary = computed(() => [
  ["记录数", String(props.page.rows.length * 12 + 8)],
  ["启用项", String(props.page.rows.filter((row) => row.includes("启用") || row.includes("运行中")).length + 18)],
  ["今日变更", String((props.page.permissions.length + props.page.rows.length) % 7 + 2)],
  ["关注项", String(props.page.permissions.length + 3)]
]);

function buildActionButtons(permissions) {
  const buttons = [];
  if (permissions.includes("edit")) buttons.push("编辑");
  if (permissions.includes("delete")) buttons.push("删除");
  if (permissions.includes("setting") || permissions.includes("grant") || permissions.includes("revoke")) buttons.push("权限配置");
  if (permissions.includes("delivery")) buttons.push("发料");
  if (permissions.includes("allot")) buttons.push("分派");
  if (permissions.includes("modifyLog")) buttons.push("修改日志");
  if (buttons.length === 0) buttons.push("查看详情");
  return buttons;
}
</script>

<template>
  <div class="page-layout">
    <section class="page-summary">
      <div v-for="[label, value] in summary" :key="label" class="mini-kpi">
        <span class="muted">{{ label }}</span>
        <strong>{{ value }}</strong>
      </div>
    </section>

    <section class="card">
      <div class="card-title">
        <h2>{{ page.title }}</h2>
        <span class="muted">模块原型页</span>
      </div>
      <p class="module-note">{{ page.description }}</p>
    </section>

    <section class="card table-card">
      <div class="table-head">
        <div>
          <strong>{{ page.title }}数据列表</strong>
          <div class="permission-row">
            <template v-if="page.permissions.length">
              <span v-for="item in page.permissions" :key="item" class="perm-chip">{{ item }}</span>
            </template>
            <span v-else class="muted">当前页面以查看与配置展示为主。</span>
          </div>
        </div>

        <div class="page-toolbar">
          <div class="toolbar-left">
            <input class="search-input" :placeholder="`搜索${page.title}关键字`">
            <select class="toolbar-select">
              <option>全部状态</option>
              <option>启用</option>
              <option>停用</option>
              <option>处理中</option>
            </select>
          </div>

          <div class="toolbar-right">
            <button v-if="page.permissions.includes('add')" class="btn-primary">新增</button>
            <button v-if="page.permissions.includes('import')" class="btn-secondary">导入</button>
            <button v-if="page.permissions.includes('export')" class="btn-secondary">导出</button>
            <button v-if="page.permissions.includes('sync')" class="btn-secondary">同步</button>
            <button v-if="page.permissions.includes('syncAll')" class="btn-secondary">全量同步</button>
          </div>
        </div>
      </div>

      <table>
        <thead>
          <tr>
            <th v-for="column in page.columns" :key="column">{{ column }}</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, rowIndex) in page.rows" :key="`${page.title}-${rowIndex}`">
            <td v-for="(value, colIndex) in row" :key="`${rowIndex}-${colIndex}`">
              <span v-if="colIndex === row.length - 1 && formatStatus(value)" class="status-chip" :class="formatStatus(value)">
                {{ value }}
              </span>
              <template v-else>{{ value }}</template>
            </td>
            <td>
              <div class="table-actions">
                <button
                  v-for="action in buildActionButtons(page.permissions)"
                  :key="action"
                  class="action-btn"
                  :class="{ edit: action === '编辑', delete: action === '删除', more: action !== '编辑' && action !== '删除' }"
                >
                  {{ action }}
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>
