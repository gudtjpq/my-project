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

          <div class="divider-container">
            <span class="divider-line"></span>
            <span class="divider-text">또는</span>
            <span class="divider-line"></span>
          </div>

          <button @click="loginWithKakao" class="btn-kakao">
            <svg viewBox="0 0 24 24" class="kakao-icon" fill="currentColor">
              <path d="M12 3c-5.523 0-10 3.535-10 7.896 0 2.82 1.83 5.297 4.664 6.643-.16 1.082-.604 3.73-.687 4.29-.1.677.34.654.717.406.3-.2 4.095-2.775 4.805-3.26.166.014.336.023.506.023 5.523 0 10-3.536 10-7.897C22 6.535 17.523 3 12 3z"/>
            </svg>
            <span>카카오로 1초 만에 시작하기</span>
          </button>
          
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

const REST_API_KEY = import.meta.env.VITE_KAKAO_REST_API_KEY
const REDIRECT_URI = 'http://localhost:5173/oauth/kakao/callback'

const loginWithKakao = () => {
  const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`
  window.location.href = kakaoAuthUrl
}

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

.divider-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 24px 0;
  width: 100%;
}

.divider-line {
  flex: 1;
  height: 1px;
  background-color: rgba(255, 255, 255, 0.15);
}

.divider-text {
  margin: 0 16px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.5);
  font-weight: 500;
}

.btn-kakao {
  width: 100%;
  padding: 16px;
  background-color: #FEE500;
  color: #000000;
  border: none;
  border-radius: 14px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  transition: all 0.3s ease;
  box-shadow: 0 6px 15px rgba(254, 229, 0, 0.15);
}

.btn-kakao:hover {
  transform: translateY(-2px);
  background-color: #FFDE00;
  box-shadow: 0 10px 25px rgba(254, 229, 0, 0.3);
}

.btn-kakao > span {
  width: 100%;
  text-align: center;
  display: block;
}

.kakao-icon {
  position: absolute;
  left: 20px;
  width: 22px;
  height: 22px;
}
</style>