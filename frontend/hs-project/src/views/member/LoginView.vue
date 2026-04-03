<template>
  <div class="auth-wrapper">
    <BackgroundScene />
    <Header />
    <main class="auth-main">
      <div class="auth-card glass-card-pro">
        <div class="glass-element-pro">
          <h2 class="auth-title">로그인</h2>
          <p class="auth-subtitle">SubManager에 오신 것을 환영합니다.</p>
          
          <form @submit.prevent="onSubmit" class="auth-form">
            <div class="input-group">
              <input type="text" v-model="form.login_id" placeholder="아이디" required />
            </div>
            <div class="input-group">
              <input type="password" v-model="form.password" placeholder="비밀번호" required />
            </div>
            
            <button type="submit" class="btn-primary-full">로그인</button>
          </form>
          
          <div class="auth-links">
            <router-link to="/signup" class="link-text">계정이 없으신가요? 회원가입</router-link>
          </div>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import http from '@/util/http'
import Header from '@/components/layout/Header.vue'
import Footer from '@/components/layout/Footer.vue'
import BackgroundScene from '@/components/layout/BackgroundScene.vue'

const router = useRouter()
const authStore = useAuthStore()

const form = ref({
  login_id: '',
  password: ''
})

const onSubmit = async () => {
  try {
    const res = await http.post('/auth/login', form.value)
    const responseData = res.data ? res.data : res;
    authStore.login(responseData)
    router.push('/')
  } catch (error) {
    alert('아이디 또는 비밀번호를 확인해주세요.')
  }
}
</script>

<style scoped>
@import '@/assets/css/glass.css';
@import '@/assets/css/auth.css';
</style>a