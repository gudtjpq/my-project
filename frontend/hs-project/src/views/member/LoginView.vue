<template>
  <div class="auth-wrapper">
    <Header />
    <main class="auth-main">
      <div class="auth-card">
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
.auth-wrapper { display: flex; flex-direction: column; min-height: 100vh; }
.auth-main { flex: 1; display: flex; align-items: center; justify-content: center; padding: 100px 20px; }
.auth-card { width: 100%; max-width: 420px; background: #ffffff; border-radius: 24px; padding: 50px 40px; border: 1px solid rgba(0, 0, 0, 0.05); box-shadow: 0 10px 40px rgba(0, 0, 0, 0.03); }
.auth-title { font-size: 28px; font-weight: 700; color: #1d1d1f; text-align: center; margin: 0 0 10px 0; letter-spacing: -0.5px; }
.auth-subtitle { font-size: 15px; color: #86868b; text-align: center; margin: 0 0 40px 0; }
.auth-form { display: flex; flex-direction: column; gap: 16px; }
.input-group input { width: 100%; padding: 16px; border: 1px solid #d2d2d7; border-radius: 12px; font-size: 15px; color: #1d1d1f; background-color: #fbfbfd; transition: border-color 0.2s ease, background-color 0.2s ease; box-sizing: border-box; }
.input-group input:focus { outline: none; border-color: #0071e3; background-color: #ffffff; }
.btn-primary-full { width: 100%; padding: 16px; margin-top: 8px; background-color: #0071e3; color: #ffffff; border: none; border-radius: 12px; font-size: 16px; font-weight: 600; cursor: pointer; transition: background-color 0.2s ease; }
.btn-primary-full:hover { background-color: #0077ed; }
.auth-links { margin-top: 30px; text-align: center; }
.link-text { font-size: 14px; color: #0071e3; font-weight: 500; }
.link-text:hover { text-decoration: underline; }
</style>