<script setup>
import { computed, onBeforeUnmount, onMounted, ref, watch } from "vue";
import { createEmptyRecord, crudConfigs } from "../data/crudStore";
import { formatStatus } from "../data/systemData";
import { createCrudRow, deleteCrudRow, fetchCrudRows, updateCrudRow } from "../api/crud";

const props = defineProps({
  pageKey: { type: String, required: true },
  page: { type: Object, required: true }
});

const config = computed(() => crudConfigs[props.pageKey]);
const rows = ref([]);
const keyword = ref("");
const statusFilter = ref("全部");
const panelOpen = ref(false);
const editingId = ref("");
const panelMode = ref("edit");
const formState = ref(createEmptyRecord(props.pageKey));
const initialFormSnapshot = ref("");
const collapsedSections = ref({});
const leaveConfirmOpen = ref(false);
const loading = ref(false);
const submitting = ref(false);
const errorMessage = ref("");

watch(
  () => props.pageKey,
  async (nextKey) => {
    keyword.value = "";
    statusFilter.value = "全部";
    panelOpen.value = false;
    editingId.value = "";
    panelMode.value = "edit";
    formState.value = createEmptyRecord(nextKey);
    initialFormSnapshot.value = "";
    collapsedSections.value = {};
    leaveConfirmOpen.value = false;
    await loadRows();
  }
);

const isPreviewMode = computed(() => panelMode.value === "preview");
const isDirty = computed(() => panelOpen.value && initialFormSnapshot.value !== JSON.stringify(formState.value));

const currentPanelTitle = computed(() => {
  if (!editingId.value) return `新增${config.value.entityName}`;
  return isPreviewMode.value ? `预览${config.value.entityName}` : `编辑${config.value.entityName}`;
});

const currentModeLabel = computed(() => {
  if (!editingId.value) return "新增";
  return isPreviewMode.value ? "预览" : "编辑";
});

const statusOptions = computed(() => {
  const values = new Set();
  const statusField = config.value.statusField;
  rows.value.forEach((row) => {
    if (row[statusField]) values.add(row[statusField]);
  });
  return ["全部", ...values];
});

const filteredRows = computed(() => {
  const lowerKeyword = keyword.value.trim().toLowerCase();
  const statusField = config.value.statusField;

  return rows.value.filter((row) => {
    const matchesKeyword =
      !lowerKeyword || Object.values(row).some((value) => String(value ?? "").toLowerCase().includes(lowerKeyword));
    const matchesStatus = statusFilter.value === "全部" || row[statusField] === statusFilter.value;
    return matchesKeyword && matchesStatus;
  });
});

const summary = computed(() => {
  const statusField = config.value.statusField;
  const activeCount = rows.value.filter((row) => {
    const value = row[statusField];
    return ["启用", "运行中", "审批通过", "已完成", "处理中", "已同步", "执行中", "Enabled", "Running", "Approved", "Completed", "Processing", "Synced"].includes(value);
  }).length;

  return [
    ["记录总数", String(rows.value.length)],
    ["当前筛选", String(filteredRows.value.length)],
    ["正常/有效", String(activeCount)],
    ["待处理", String(rows.value.length - activeCount)]
  ];
});

const fieldSections = computed(() => {
  const sectionMap = {
    manufacturer: [
      { key: "basic", title: "基础信息", fields: ["code", "name", "contact", "mobile", "status"] },
      { key: "detail", title: "联系信息", fields: ["email", "address", "remark"] }
    ],
    equipmentCategory: [
      { key: "basic", title: "分类信息", fields: ["code", "name", "parentCategory", "status"] },
      { key: "strategy", title: "策略配置", fields: ["checkStrategy", "maintenanceStrategy", "remark"] }
    ],
    equipmentAssets: [
      { key: "basic", title: "基础信息", fields: ["code", "name", "category", "workshop", "owner", "status"] },
      { key: "extra", title: "补充信息", fields: ["purchaseDate", "model", "supplier", "remark"] }
    ],
    checkPlan: [
      { key: "basic", title: "计划信息", fields: ["code", "name", "target", "frequency", "executor", "status"] },
      { key: "schedule", title: "执行配置", fields: ["startDate", "cycleRule", "remark"] }
    ],
    checkItem: [
      { key: "basic", title: "项目信息", fields: ["code", "name", "judgeType", "standardValue", "equipmentType", "status"] },
      { key: "detail", title: "判定配置", fields: ["riskLevel", "remark"] }
    ],
    checkRecord: [
      { key: "basic", title: "记录信息", fields: ["code", "equipmentName", "executor", "executeTime", "result", "status"] },
      { key: "detail", title: "执行详情", fields: ["planName", "abnormalDesc", "remark"] }
    ],
    maintenancePlan: [
      { key: "basic", title: "计划信息", fields: ["code", "name", "target", "cycle", "owner", "status"] },
      { key: "schedule", title: "保养配置", fields: ["nextDate", "strategy", "remark"] }
    ],
    maintenanceItem: [
      { key: "basic", title: "项目信息", fields: ["code", "name", "standardHours", "targetType", "riskLevel", "status"] },
      { key: "detail", title: "执行说明", fields: ["executeStandard", "remark"] }
    ],
    maintenanceRecord: [
      { key: "basic", title: "记录信息", fields: ["code", "target", "executor", "maintainDate", "result", "status"] },
      { key: "detail", title: "执行详情", fields: ["planName", "remark"] }
    ],
    repairApply: [
      { key: "basic", title: "工单信息", fields: ["ticketNo", "equipmentName", "level", "stage", "team", "status"] },
      { key: "detail", title: "报修详情", fields: ["applicant", "applyDate", "faultType", "contact", "description"] }
    ],
    accessoryApply: [
      { key: "basic", title: "申请信息", fields: ["applyNo", "applicant", "accessoryName", "quantity", "approvalStatus", "deliveryStatus"] },
      { key: "detail", title: "领用详情", fields: ["warehouse", "applyDate", "purpose", "receiver", "remark"] }
    ],
    user: [
      { key: "basic", title: "账号信息", fields: ["account", "name", "dept", "role", "mobile", "status"] },
      { key: "detail", title: "扩展信息", fields: ["email", "syncStatus", "remark"] }
    ]
  };

  const preset = sectionMap[props.pageKey];
  if (!preset) return [{ key: "default", title: "表单信息", fieldItems: config.value.fields }];

  return preset.map((section) => ({
    key: section.key,
    title: section.title,
    fieldItems: section.fields.map((fieldKey) => config.value.fields.find((field) => field.key === fieldKey)).filter(Boolean)
  }));
});

async function loadRows() {
  loading.value = true;
  errorMessage.value = "";
  try {
    rows.value = await fetchCrudRows(props.pageKey);
  } catch (error) {
    rows.value = [];
    errorMessage.value = error.message || "数据加载失败";
  } finally {
    loading.value = false;
  }
}

function openCreate() {
  editingId.value = "";
  panelMode.value = "edit";
  formState.value = createEmptyRecord(props.pageKey);
  initialFormSnapshot.value = JSON.stringify(formState.value);
  collapsedSections.value = {};
  leaveConfirmOpen.value = false;
  panelOpen.value = true;
}

function openDetail(row, mode = "preview") {
  editingId.value = row.id;
  panelMode.value = mode;
  formState.value = { ...row };
  initialFormSnapshot.value = JSON.stringify(formState.value);
  collapsedSections.value = {};
  leaveConfirmOpen.value = false;
  panelOpen.value = true;
}

function reallyClosePanel() {
  panelOpen.value = false;
  editingId.value = "";
  panelMode.value = "edit";
  leaveConfirmOpen.value = false;
}

function closePanel() {
  if (isDirty.value) {
    leaveConfirmOpen.value = true;
    return;
  }
  reallyClosePanel();
}

function confirmLeavePanel() {
  reallyClosePanel();
}

function cancelLeavePanel() {
  leaveConfirmOpen.value = false;
}

async function removeRow(row) {
  const confirmed = window.confirm(`确定删除${config.value.entityName}“${row[config.value.primaryField]}”吗？`);
  if (!confirmed) return;
  try {
    await deleteCrudRow(props.pageKey, row.id);
    rows.value = rows.value.filter((item) => item.id !== row.id);
  } catch (error) {
    window.alert(error.message || "删除失败");
  }
}

async function submitForm() {
  const missingField = config.value.fields.find((field) => field.required && String(formState.value[field.key] ?? "").trim() === "");
  if (missingField) {
    window.alert(`请填写${missingField.label}`);
    return;
  }

  const payload = { ...formState.value };
  submitting.value = true;

  try {
    let savedRow;
    if (editingId.value) {
      savedRow = await updateCrudRow(props.pageKey, editingId.value, payload);
      rows.value = rows.value.map((row) => (row.id === editingId.value ? savedRow : row));
    } else {
      savedRow = await createCrudRow(props.pageKey, payload);
      rows.value = [savedRow, ...rows.value];
    }
    initialFormSnapshot.value = JSON.stringify(savedRow);
    reallyClosePanel();
  } catch (error) {
    window.alert(error.message || "保存失败");
  } finally {
    submitting.value = false;
  }
}


function setPanelMode(nextMode) {
  panelMode.value = nextMode;
}

function toggleSection(sectionKey) {
  collapsedSections.value = { ...collapsedSections.value, [sectionKey]: !collapsedSections.value[sectionKey] };
}

function beforeUnloadHandler(event) {
  if (!isDirty.value) return;
  event.preventDefault();
  event.returnValue = "";
}

watch(isDirty, (dirty) => {
  if (dirty) window.addEventListener("beforeunload", beforeUnloadHandler);
  else window.removeEventListener("beforeunload", beforeUnloadHandler);
});

onMounted(loadRows);
onBeforeUnmount(() => {
  window.removeEventListener("beforeunload", beforeUnloadHandler);
});
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
        <span class="muted">数据管理</span>
      </div>
      <p class="module-note">{{ page.description }}</p>
      <p v-if="errorMessage" class="form-tip">{{ errorMessage }}</p>
    </section>

    <section class="card table-card">
      <div class="table-head">
        <div>
          <strong>{{ page.title }}数据列表</strong>
          <div class="permission-row">
            <span v-for="item in page.permissions" :key="item" class="perm-chip">{{ item }}</span>
          </div>
        </div>

        <div class="page-toolbar">
          <div class="toolbar-left">
            <input v-model="keyword" class="search-input" :placeholder="`搜索${page.title}关键字`">
            <select v-model="statusFilter" class="toolbar-select">
              <option v-for="item in statusOptions" :key="item" :value="item">{{ item }}</option>
            </select>
          </div>

          <div class="toolbar-right">
            <button class="btn-primary" :disabled="loading" @click="openCreate">新增</button>
          </div>
        </div>
      </div>

      <table>
        <thead>
          <tr>
            <th v-for="column in config.columns" :key="column.key">{{ column.label }}</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="loading">
            <td :colspan="config.columns.length + 1" class="empty-state">正在加载数据库数据...</td>
          </tr>
          <tr v-for="row in filteredRows" v-else :key="row.id">
            <td v-for="column in config.columns" :key="`${row.id}-${column.key}`">
              <span v-if="column.isStatus && formatStatus(row[column.key])" class="status-chip" :class="formatStatus(row[column.key])">
                {{ row[column.key] }}
              </span>
              <template v-else>{{ row[column.key] }}</template>
            </td>
            <td>
              <div class="table-actions">
                <button class="action-btn more" @click="openDetail(row, 'preview')">预览</button>
                <button class="action-btn edit" @click="openDetail(row, 'edit')">编辑</button>
                <button class="action-btn delete" @click="removeRow(row)">删除</button>
              </div>
            </td>
          </tr>
          <tr v-if="!loading && !filteredRows.length">
            <td :colspan="config.columns.length + 1" class="empty-state">当前没有符合条件的数据。</td>
          </tr>
        </tbody>
      </table>
    </section>

    <div v-if="panelOpen" class="side-panel-mask" @click.self="closePanel">
      <section class="detail-drawer">
        <button class="drawer-back-handle" @click="closePanel" aria-label="返回列表">
          <span>‹</span>
        </button>

        <div class="drawer-header">
          <div class="drawer-header-main">
            <div class="drawer-breadcrumbs">
              <span>{{ page.title }}</span>
              <span>/</span>
              <span>{{ config.entityName }}</span>
              <span>/</span>
              <strong>{{ currentModeLabel }}</strong>
            </div>
            <h3>{{ currentPanelTitle }}</h3>
            <p class="muted">当前面板作为列表页的详情子页展开，保存后会直接写入数据库。</p>
          </div>

          <div class="drawer-header-actions">
            <div v-if="editingId" class="mode-switch">
              <button class="mode-switch-btn" :class="{ active: isPreviewMode }" @click="setPanelMode('preview')">预览</button>
              <button class="mode-switch-btn" :class="{ active: !isPreviewMode }" @click="setPanelMode('edit')">编辑</button>
            </div>
            <button class="btn-ghost" @click="closePanel">返回列表</button>
          </div>
        </div>

        <div class="drawer-body">
          <div v-for="section in fieldSections" :key="section.key" class="drawer-section">
            <button class="drawer-section-header" @click="toggleSection(section.key)">
              <span>{{ section.title }}</span>
              <span class="drawer-section-arrow" :class="{ collapsed: collapsedSections[section.key] }">⌄</span>
            </button>

            <div v-if="!collapsedSections[section.key]" class="modal-form-grid">
              <div v-for="field in section.fieldItems" :key="field.key" class="form-field">
                <label :for="field.key">{{ field.label }}</label>

                <select v-if="field.type === 'select'" :id="field.key" v-model="formState[field.key]" class="toolbar-select" :disabled="isPreviewMode">
                  <option v-for="option in field.options" :key="option" :value="option">{{ option }}</option>
                </select>

                <textarea
                  v-else-if="field.type === 'textarea'"
                  :id="field.key"
                  v-model="formState[field.key]"
                  class="form-textarea"
                  :placeholder="field.placeholder || ''"
                  :readonly="isPreviewMode"
                  rows="4"
                />

                <input
                  v-else
                  :id="field.key"
                  v-model="formState[field.key]"
                  class="search-input"
                  :type="field.type === 'number' ? 'number' : field.type"
                  :min="field.min"
                  :placeholder="field.placeholder || ''"
                  :readonly="isPreviewMode"
                >
              </div>
            </div>
          </div>

          <div class="drawer-actions">
            <button v-if="isPreviewMode && editingId" class="btn-secondary" @click="setPanelMode('edit')">进入编辑</button>
            <button v-if="!isPreviewMode" class="btn-primary" :disabled="submitting" @click="submitForm">{{ submitting ? "保存中..." : "保存" }}</button>
            <button class="btn-secondary" @click="closePanel">{{ isPreviewMode ? "返回列表" : "取消" }}</button>
          </div>
        </div>
      </section>
    </div>

    <div v-if="leaveConfirmOpen" class="modal-mask">
      <section class="modal-card confirm-modal">
        <div class="modal-header">
          <div>
            <h3>确认离开</h3>
            <p class="muted">当前内容尚未保存，离开后本次编辑内容将丢失。</p>
          </div>
        </div>

        <div class="confirm-copy">
          <p>是否确认关闭当前详情面板并返回列表？</p>
        </div>

        <div class="modal-actions">
          <button class="btn-danger" @click="confirmLeavePanel">确认离开</button>
          <button class="btn-secondary" @click="cancelLeavePanel">继续编辑</button>
        </div>
      </section>
    </div>
  </div>
</template>
