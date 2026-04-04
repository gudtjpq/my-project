<template>
  <div class="callback-wrapper">
    <div class="loader"></div>
    <p>카카오 로그인 처리 중입니다...</p>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import http from '@/util/http'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

onMounted(async () => {
  const code = route.query.code

  if (code) {
    try {
      const res = await http.post('/auth/kakao', { code })
      authStore.login(res)
      router.push('/')
    } catch (error) {
      console.error('카카오 로그인 에러 상세:', error)
      alert('카카오 로그인 중 오류가 발생했습니다.')
      router.push('/login')
    }
  } else {
    alert('잘못된 접근입니다.')
    router.push('/login')
  }
})
</script>

<style scoped>
.callback-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #121214;
  color: #ffffff;
}

.loader {
  border: 4px solid rgba(255, 255, 255, 0.1);
  border-top: 4px solid #FEE500;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>