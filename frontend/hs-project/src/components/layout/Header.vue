<template>
  <header class="header-container">
    <div class="header-inner">
      <router-link to="/" class="logo">SubManager.</router-link>
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
.header-container {
  width: 100%; height: 60px; background-color: rgba(251, 251, 253, 0.8);
  backdrop-filter: blur(12px); position: fixed; top: 0; left: 0; z-index: 100;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}
.header-inner {
  max-width: 1080px; height: 100%; margin: 0 auto; display: flex;
  align-items: center; justify-content: space-between; padding: 0 20px;
}
.logo { font-size: 20px; font-weight: 700; letter-spacing: -0.5px; }
.nav-menu { display: flex; gap: 32px; }
.nav-item { font-size: 15px; color: #86868b; font-weight: 500; transition: color 0.2s ease; }
.nav-item:hover { color: #1d1d1f; }
.auth-menu { display: flex; align-items: center; gap: 20px; }
.login-text, .user-greeting { font-size: 14px; color: #1d1d1f; font-weight: 500; }
.btn-start {
  font-size: 14px; font-weight: 500; background-color: #1d1d1f; color: #ffffff;
  padding: 8px 16px; border-radius: 20px; transition: background-color 0.2s ease;
}
.btn-start:hover { background-color: #424245; }
.btn-logout {
  font-size: 14px; font-weight: 500; background-color: transparent; color: #86868b;
  border: 1px solid #d2d2d7; padding: 6px 14px; border-radius: 20px;
  cursor: pointer; transition: all 0.2s ease;
}
.btn-logout:hover { color: #1d1d1f; border-color: #1d1d1f; }
</style>