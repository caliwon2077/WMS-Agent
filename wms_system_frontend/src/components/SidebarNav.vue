<script setup>
import { ref, watch } from "vue";

const props = defineProps({
  menus: { type: Array, required: true },
  currentRoute: { type: String, required: true }
});

const emit = defineEmits(["navigate"]);

const expandedGroupKey = ref("");

function hasActiveChild(group) {
  return group.children.some((item) => item.key === props.currentRoute);
}

function isExpanded(group) {
  return expandedGroupKey.value === group.key;
}

function toggleGroup(group) {
  if (isExpanded(group)) {
    expandedGroupKey.value = "";
    return;
  }

  expandedGroupKey.value = group.key;

  if (!hasActiveChild(group)) {
    emit("navigate", group.children[0].key);
  }
}

watch(
  () => props.currentRoute,
  () => {
    const activeGroup = props.menus.find((group) => hasActiveChild(group));
    if (activeGroup) expandedGroupKey.value = activeGroup.key;
  },
  { immediate: true }
);
</script>

<template>
  <aside class="sidebar">
    <div class="brand-box">
      <p class="brand-title">WMS System</p>
      <p class="brand-subtitle">企业设备与配件协同管理平台原型</p>
    </div>

    <div class="menu-heading">功能导航</div>

    <div v-for="group in menus" :key="group.key" class="menu-group">
      <section class="menu-section" :class="{ active: hasActiveChild(group), expanded: isExpanded(group) }">
        <button
          class="menu-item"
          :class="{ active: hasActiveChild(group) }"
          @click="toggleGroup(group)"
        >
          <span class="menu-item-badge">{{ group.title.slice(0, 2) }}</span>
          <span class="menu-item-main">
            <span class="menu-item-title">{{ group.title }}</span>
            <span class="menu-item-meta">{{ group.children.length }} 项子功能</span>
          </span>
          <span class="menu-item-arrow" :class="{ expanded: isExpanded(group) }">⌄</span>
        </button>

        <div
          class="submenu-wrap"
          :class="{ active: hasActiveChild(group), collapsed: !isExpanded(group) }"
        >
          <div class="submenu-label">子功能</div>
          <div class="submenu-list">
            <button
              v-for="item in group.children"
              :key="item.key"
              class="submenu-item"
              :class="{ active: props.currentRoute === item.key }"
              @click="emit('navigate', item.key)"
            >
              <span class="submenu-dot" />
              <span>{{ item.title }}</span>
            </button>
          </div>
        </div>
      </section>
    </div>
  </aside>
</template>
