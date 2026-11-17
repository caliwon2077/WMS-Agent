<script setup>
import { ref } from "vue";

const emit = defineEmits(["login"]);

const account = ref("admin");
const password = ref("123456");

function fillDemo() {
  account.value = "admin";
  password.value = "123456";
}

function submit() {
  if (account.value.trim() !== "admin" || password.value.trim() !== "123456") {
    window.alert("演示环境仅开放 admin / 123456");
    return;
  }
  emit("login");
}
</script>

<template>
  <div class="login-shell">
    <section class="login-hero">
      <div>
        <div class="hero-badge">类似 EMS/WMS 的企业管理原型</div>
        <h1>设备、维修、配件与人员协同一体化管理平台</h1>
        <p>
          这是一套基于 Vue 3 搭建的正式前端工程，页面结构参考真实设备管理系统，
          当前已有十二个页面接入后端，支持设备、点检、保养、报修、配件申请和用户数据的增删改查。
        </p>
      </div>

      <div class="hero-metrics">
        <div class="hero-metric"><strong>10</strong><span>一级模块</span></div>
        <div class="hero-metric"><strong>12</strong><span>可用 CRUD 页面</span></div>
        <div class="hero-metric"><strong>39</strong><span>权限动作示意</span></div>
      </div>
    </section>

    <section class="login-panel">
      <div class="login-card">
        <h2>系统登录</h2>
        <p>演示账号已内置，可以直接进入系统体验页面与业务操作。</p>

        <div class="form-grid">
          <div class="form-field">
            <label for="account">账号</label>
            <input id="account" v-model="account" placeholder="请输入账号">
          </div>

          <div class="form-field">
            <label for="password">密码</label>
            <input id="password" v-model="password" type="password" placeholder="请输入密码">
          </div>

          <div class="form-tip">
            默认演示账号：<strong>admin / 123456</strong><br>
            数据通过后端保存；此登录仅用于演示，不提供真实身份认证。
          </div>

          <div class="btn-row">
            <button class="btn-primary" @click="submit">进入系统</button>
            <button class="btn-secondary" @click="fillDemo">填入演示账号</button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
