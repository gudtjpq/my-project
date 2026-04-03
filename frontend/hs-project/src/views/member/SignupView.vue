<template>
  <div class="auth-wrapper">
    <BackgroundScene />
    <Header />
    <main class="auth-main">
      <div class="auth-card glass-card-pro">
        <div class="glass-element-pro">
          <h2 class="auth-title">회원가입</h2>
          <p class="auth-subtitle">SubManager와 함께 스마트한 관리를 시작하세요.</p>
          
          <form @submit.prevent="onSubmit" class="auth-form">
            <div class="input-group">
              <input type="text" v-model="form.userId" placeholder="아이디" required />
            </div>
            <div class="input-group">
              <input type="password" v-model="form.userPwd" placeholder="비밀번호" required />
            </div>
            <div class="input-group">
              <input type="text" v-model="form.userName" placeholder="이름" required />
            </div>
            <div class="input-group">
              <input type="email" v-model="form.email" placeholder="이메일" required />
            </div>
            
            <button type="submit" class="btn-primary-full">가입하기</button>
          </form>
          
          <div class="auth-links">
            <router-link to="/login" class="link-text">이미 계정이 있으신가요? 로그인</router-link>
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
import http from '@/util/http'
import Header from '@/components/layout/Header.vue'
import Footer from '@/components/layout/Footer.vue'
import BackgroundScene from '@/components/layout/BackgroundScene.vue'

const router = useRouter()

const form = ref({
  userId: '',
  userPwd: '',
  userName: '',
  email: ''
})

const onSubmit = async () => {
  try {
    await http.post('/member/signup', form.value)
    alert('회원가입이 완료되었습니다.')
    router.push('/login')
  } catch (error) {
    alert('회원가입 처리 중 오류가 발생했습니다.')
  }
}
</script>

<style scoped>
@import '@/assets/css/glass.css';
@import '@/assets/css/auth.css';
</style>