<template>
  <div class="layout-wrapper">
    <BackgroundScene />
    <Header />
    <main class="main-content">
      <div class="dashboard-container">
        <div class="dashboard-header glass-element-pro">
          <div class="title-group">
            <h2 class="page-title">마이페이지</h2>
            <p class="page-subtitle">내 정보를 안전하게 관리하세요.</p>
          </div>
        </div>
        <div class="mypage-grid">
          <div class="profile-card glass-card-pro">
            <div class="profile-info glass-element-pro">
              <div class="profile-avatar">
                <span class="avatar-text">{{ username?.charAt(0) || 'U' }}</span>
              </div>
              <div class="profile-details">
                <h3 class="profile-name">{{ username }}</h3>
                <p class="profile-role">{{ role === 'ADMIN' ? '관리자' : '일반 회원' }}</p>
              </div>
            </div>
          </div>
          <div class="edit-card glass-card-pro">
            <form @submit.prevent="updateProfile" class="glass-element-pro edit-form">
              <div class="input-group-pro">
                <label>비밀번호 변경</label>
                <input type="password" v-model="form.password" placeholder="새 비밀번호 입력" />
              </div>
              <div class="input-group-pro">
                <label>이메일</label>
                <input type="email" v-model="form.email" placeholder="이메일 입력" required />
              </div>
              <div class="input-group-pro">
                <label>연락처</label>
                <input type="text" v-model="form.tel" placeholder="연락처 입력 (예: 010-1234-5678)" />
              </div>
              <button type="submit" class="btn-primary-full">정보 수정하기</button>
            </form>
          </div>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { storeToRefs } from 'pinia'
import http from '@/util/http'
import Header from '@/components/layout/Header.vue'
import Footer from '@/components/layout/Footer.vue'
import BackgroundScene from '@/components/layout/BackgroundScene.vue'

const authStore = useAuthStore()

const { username, role, member_id } = storeToRefs(authStore)

const form = ref({
  password: '',
  email: '',
  tel: ''
})

onMounted(async () => {
  try {
    const res = await http.get(`/member/info/${member_id.value}`)
    if (res.data) {
      form.value.email = res.data.email || ''
      form.value.tel = res.data.tel || ''
    }
  } catch (error) {
    console.error('회원 정보 로드 실패:', error)
  }
})

const updateProfile = async () => {
  try {
    await http.put('/member/update', {
      member_id: member_id.value,
      ...form.value
    })
    alert('정보가 성공적으로 수정되었습니다.')
    form.value.password = ''
  } catch (error) {
    alert('정보 수정에 실패했습니다.')
  }
}
</script>

<style scoped>
@import '@/assets/css/glass.css';

.layout-wrapper { display: flex; flex-direction: column; min-height: 100vh; color: #ffffff !important; overflow-x: hidden; position: relative; z-index: 1; background-color: transparent !important; }
.main-content { flex: 1; padding: 120px 24px 80px; position: relative; z-index: 1; background-color: transparent !important; }
.dashboard-container { max-width: 1200px; margin: 0 auto; animation: fadeUp 0.8s cubic-bezier(0.16, 1, 0.3, 1) forwards; opacity: 0; }
.dashboard-header { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 48px; background-color: transparent !important; }
.title-group { background-color: transparent !important; display: flex; flex-direction: column; gap: 8px; }
.page-title { font-size: 36px !important; font-weight: 800 !important; color: #ffffff !important; margin: 0 !important; letter-spacing: -1px !important; background-color: transparent !important; text-shadow: 0 4px 20px rgba(0,0,0,0.3) !important; line-height: 1.2 !important; }
.page-subtitle { font-size: 16px !important; color: rgba(255,255,255,0.6) !important; margin: 0 !important; background-color: transparent !important; }

.mypage-grid { display: grid; grid-template-columns: 1fr 2fr; gap: 24px; }
.profile-card, .edit-card { padding: 40px; border-radius: 24px !important; }
.profile-card::before, .edit-card::before { border-radius: 24px !important; }

.profile-info { display: flex; flex-direction: column; align-items: center; gap: 20px; text-align: center; background: transparent !important; padding: 20px 0; }
.profile-avatar { width: 100px; height: 100px; border-radius: 50%; background: linear-gradient(135deg, #ff9800 0%, #ff5252 100%); display: flex; align-items: center; justify-content: center; box-shadow: 0 10px 20px rgba(255, 152, 0, 0.3); }
.avatar-text { font-size: 36px; font-weight: 800; color: #ffffff; }
.profile-name { font-size: 24px; font-weight: 700; color: #ffffff !important; margin: 0; }
.profile-role { font-size: 14px; color: rgba(255,255,255,0.6) !important; margin: 0; }

.edit-form { display: flex; flex-direction: column; gap: 24px; background: transparent !important; }
.input-group-pro { display: flex; flex-direction: column; gap: 10px; background: transparent !important; }
.input-group-pro label { font-size: 13px; font-weight: 700; color: rgba(255, 255, 255, 0.8) !important; letter-spacing: 0.5px; background: transparent !important; }
.input-group-pro input { width: 100%; padding: 16px 20px; border: 1.5px solid rgba(255, 255, 255, 0.12) !important; border-radius: 14px; font-size: 15px; font-weight: 500; color: #ffffff !important; background-color: rgba(0, 0, 0, 0.3) !important; box-sizing: border-box; transition: all 0.3s ease; font-family: inherit; }
.input-group-pro input:focus { outline: none; border-color: #ff9800 !important; background-color: rgba(0, 0, 0, 0.5) !important; box-shadow: 0 0 0 3px rgba(255, 152, 0, 0.2); }
.input-group-pro input::placeholder { color: rgba(255, 255, 255, 0.3) !important; }
.btn-primary-full { width: 100%; padding: 18px; margin-top: 16px; background: #ff9800 !important; color: #ffffff !important; border: none; border-radius: 14px; font-size: 16px; font-weight: 700; cursor: pointer; transition: all 0.3s ease; box-shadow: 0 6px 15px rgba(255, 152, 0, 0.3); }
.btn-primary-full:hover { transform: translateY(-3px); box-shadow: 0 10px 25px rgba(255, 152, 0, 0.5); background: #ffb74d !important; }

@keyframes fadeUp { 0% { opacity: 0; transform: translateY(40px); } 100% { opacity: 1; transform: translateY(0); } }
@media (max-width: 768px) { .mypage-grid { grid-template-columns: 1fr; } }
</style>