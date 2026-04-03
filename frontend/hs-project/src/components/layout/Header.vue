<template>
  <header class="header-container">
    <div class="header-inner">
      <router-link to="/" class="logo">
        <span class="logo-dot"></span>SubManager.
      </router-link>
      <nav class="nav-menu">
        <router-link to="/" class="nav-item">서비스 소개</router-link>
        <router-link to="/subscription" class="nav-item">구독 관리</router-link>
      </nav>
      <div class="auth-menu">
        <template v-if="isAuthenticated">
          <span class="user-greeting">{{ auth.username }}님</span>
          <button @click="handleLogout" class="btn-logout">로그아웃</button>
        </template>
        <template v-else>
          <router-link to="/login" class="login-text">로그인</router-link>
          <router-link to="/signup" class="btn-start">시작하기</router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { storeToRefs } from 'pinia'

const router = useRouter()
const authStore = useAuthStore()
const { isAuthenticated, auth } = storeToRefs(authStore)

const handleLogout = () => {
  authStore.logout()
  router.push('/')
}
</script>

<style scoped>
@import '@/assets/css/header.css';
</style>