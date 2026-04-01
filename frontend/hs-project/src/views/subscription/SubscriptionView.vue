<template>
  <div class="sub-wrapper">
    <div class="header-section">
      <h1>구독 관리</h1>
    </div>

    <div class="grid-layout">
      <div class="side-panel">
        <div class="glass-card">
          <form @submit.prevent="saveSub">
            <div class="input-group">
              <input type="text" v-model="form.serviceName" placeholder="서비스 이름" required />
            </div>
            <div class="input-group">
              <input type="number" v-model="form.price" placeholder="결제 금액" required />
            </div>
            <div class="input-group">
              <input type="number" v-model="form.paymentDay" placeholder="결제일 (1-31)" min="1" max="31" required />
            </div>
            <div class="input-group">
              <select v-model="form.category" required>
                <option value="">카테고리 선택</option>
                <option value="영상">영상(OTT)</option>
                <option value="음악">음악</option>
                <option value="도구">작업 도구</option>
                <option value="기타">기타</option>
              </select>
            </div>
            <button class="btn-black">추가하기</button>
          </form>

          <div class="stats-list">
            <div class="stat-row" v-for="s in stats" :key="s.CATEGORY">
              <span>{{ s.CATEGORY }}</span>
              <span>{{ Number(s.TOTALAMOUNT).toLocaleString() }}원</span>
            </div>
          </div>
        </div>
      </div>

      <div class="main-panel">
        <div class="glass-card">
          <table class="sub-table">
            <thead>
              <tr>
                <th>서비스</th>
                <th>카테고리</th>
                <th>금액</th>
                <th>결제일</th>
                <th>상태</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in subList" :key="item.subId">
                <td>{{ item.serviceName }}</td>
                <td>{{ item.category }}</td>
                <td>{{ Number(item.price).toLocaleString() }}원</td>
                <td>매달 {{ item.paymentDay }}일</td>
                <td><span class="badge-day">D-{{ calcDday(item.paymentDay) }}</span></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import http from '@/util/http'
import { useAuthStore } from '@/stores/authStore'

const authStore = useAuthStore()

const form = ref({
  serviceName: '',
  price: '',
  paymentDay: '',
  category: ''
})

const subList = ref([])
const stats = ref([])

const loadData = async () => {
  try {
    const res = await http.get(`/subscription/list/${authStore.auth.member_id}`)
    subList.value = res.list || []
    stats.value = res.stats || []
  } catch (error) {
    console.error(error)
  }
}

const saveSub = async () => {
  try {
    const payload = {
      serviceName: form.value.serviceName,
      price: parseInt(form.value.price),
      paymentDay: parseInt(form.value.paymentDay),
      category: form.value.category,
      memberId: authStore.auth.member_id
    }
    
    const res = await http.post('/subscription/register', payload)
    
    if(res.state === "true") {
      form.value = { serviceName: '', price: '', paymentDay: '', category: '' }
      await loadData()
    }
  } catch (error) {
    console.error(error)
  }
}

const calcDday = (day) => {
  const now = new Date()
  const today = now.getDate()
  if (day >= today) return day - today
  const lastDay = new Date(now.getFullYear(), now.getMonth() + 1, 0).getDate()
  return lastDay - today + day
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
@import '@/assets/css/subscription.css';
</style>