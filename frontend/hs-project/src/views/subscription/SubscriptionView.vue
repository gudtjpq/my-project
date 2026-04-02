<template>
  <div class="layout-wrapper">
    <Header />
    
    <main class="main-content">
      <div class="dashboard-container">
        <div class="dashboard-header">
          <div>
            <h2 class="page-title">나의 구독 관리</h2>
            <p class="page-subtitle">현재 이용 중인 서비스를 한눈에 파악하세요.</p>
          </div>
          <button class="btn-primary" @click="openModal">+ 새 구독 추가</button>
        </div>

        <div class="stats-grid">
          <div class="stat-card">
            <span class="stat-icon">💰</span>
            <div class="stat-info">
              <span class="stat-label">이번 달 총 지출</span>
              <strong class="stat-value">₩ {{ totalCost.toLocaleString() }}</strong>
            </div>
          </div>
          <div class="stat-card">
            <span class="stat-icon">📦</span>
            <div class="stat-info">
              <span class="stat-label">구독 중인 서비스</span>
              <strong class="stat-value">{{ subscriptions.length }} 개</strong>
            </div>
          </div>
          <div class="stat-card highlight">
            <span class="stat-icon">⏰</span>
            <div class="stat-info">
              <span class="stat-label">결제 임박</span>
              <strong class="stat-value">확인 중...</strong>
            </div>
          </div>
        </div>

        <div class="list-section">
          <h3 class="section-title">구독 목록</h3>
          
          <div v-if="subscriptions.length === 0" class="empty-state">
            아직 등록된 구독 서비스가 없습니다.<br/>새 구독을 추가해 보세요!
          </div>

          <div v-else class="subscription-grid">
            <div class="sub-card" v-for="(sub, index) in subscriptions" :key="index">
              <div class="sub-card-header">
                <div class="sub-name-group">
                  <div class="sub-logo" :class="getCategoryColor(sub.category)"></div>
                  <div>
                    <h4 class="sub-name">{{ sub.serviceName }}</h4>
                    <span class="sub-category">{{ sub.category }}</span>
                  </div>
                </div>
                
                <div class="card-menu-wrap">
                  <button class="btn-icon" @click.stop="toggleDropdown(index)">⋮</button>
                  
                  <div v-if="activeDropdown === index" class="dropdown-overlay" @click.stop="activeDropdown = null"></div>

                  <div v-if="activeDropdown === index" class="custom-action-menu">
                    <button @click.stop="openEditModal(sub)" class="action-btn">수정</button>
                    <button @click.stop="deleteSubscription(sub.subId)" class="action-btn text-danger">삭제</button>
                  </div>
                </div>

              </div>
              <div class="sub-card-body">
                <div class="price-info">
                  <strong class="price">₩ {{ sub.price.toLocaleString() }}</strong>
                  <span class="cycle">/ 월</span>
                </div>
                <div class="date-info">
                  <span>매월 {{ sub.paymentDay }}일 결제</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <Footer />

    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-card">
        <div class="modal-header">
          <h3>{{ isEditMode ? '구독 정보 수정' : '새 구독 추가' }}</h3>
          <button class="btn-close" @click="closeModal">✕</button>
        </div>
        
        <form @submit.prevent="saveSubscription" class="modal-form">
          <div class="input-group">
            <label>서비스 이름</label>
            <input type="text" v-model="newSub.serviceName" placeholder="예: 넷플릭스, 스포티파이" required />
          </div>
          
          <div class="input-group">
            <label>카테고리</label>
            <select v-model="newSub.category" required>
              <option value="" disabled>카테고리를 선택하세요</option>
              <option value="OTT/영상">OTT/영상</option>
              <option value="음악">음악</option>
              <option value="도구/소프트웨어">도구/소프트웨어</option>
              <option value="멤버십">멤버십</option>
              <option value="기타">기타</option>
            </select>
          </div>
          
          <div class="form-row">
            <div class="input-group">
              <label>결제 금액 (원)</label>
              <input type="number" v-model="newSub.price" placeholder="0" required />
            </div>
            
            <div class="input-group">
              <label>매월 결제일</label>
              <input type="number" v-model="newSub.paymentDay" min="1" max="31" placeholder="1~31" required />
            </div>
          </div>
          
          <div class="modal-actions">
            <button type="button" class="btn-secondary" @click="closeModal">취소</button>
            <button type="submit" class="btn-primary-full">{{ isEditMode ? '수정 완료' : '추가하기' }}</button>
          </div>
        </form>
      </div>
    </div>
    
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import Header from '@/components/layout/Header.vue'
import Footer from '@/components/layout/Footer.vue'
import http from '@/util/http'
import { useAuthStore } from '@/stores/authStore'

const authStore = useAuthStore()

const isModalOpen = ref(false)
const isEditMode = ref(false) 
const activeDropdown = ref(null)
const subscriptions = ref([])
const backendStats = ref({})

const initialForm = { serviceName: '', category: '', price: '', paymentDay: '' }
const newSub = ref({ ...initialForm })

const totalCost = computed(() => {
  return subscriptions.value.reduce((acc, sub) => acc + sub.price, 0)
})

onMounted(() => {
  fetchSubscriptions()
})

const fetchSubscriptions = async () => {
  try {
    const memberId = authStore.auth.member_id
    if(!memberId) return
    const res = await http.get(`/subscription/list/${memberId}`)
    subscriptions.value = res.list || []
    backendStats.value = res.stats || {}
  } catch (error) {
    console.error("목록 호출 오류:", error)
  }
}

const saveSubscription = async () => {
  try {
    const payload = {
      ...newSub.value,
      memberId: authStore.auth.member_id
    }

    const url = isEditMode.value ? '/subscription/update' : '/subscription/register'
    const res = await http.post(url, payload)

    if (res.state === 'true') {
      closeModal()
      fetchSubscriptions()
    } else {
      alert("처리에 실패했습니다.")
    }
  } catch (error) {
    console.error("저장 중 오류:", error)
    alert("오류가 발생했습니다.")
  }
}

const deleteSubscription = async (subId) => {
  if (!subId) {
    alert("삭제할 수 없는 항목입니다. (ID 없음)")
    return
  }

  if(!confirm("정말 이 구독 정보를 삭제하시겠습니까?")) return;
  
  try {
    const res = await http.delete(`/subscription/delete/${subId}`)
    if (res.state === 'true') {
      activeDropdown.value = null
      fetchSubscriptions()
    } else {
      alert("삭제에 실패했습니다.")
    }
  } catch (error) {
    console.error("삭제 중 오류:", error)
    alert("오류가 발생했습니다.")
  }
}

const toggleDropdown = (index) => {
  activeDropdown.value = activeDropdown.value === index ? null : index
}

const openModal = () => {
  isEditMode.value = false
  newSub.value = { ...initialForm }
  isModalOpen.value = true
}

const openEditModal = (sub) => {
  isEditMode.value = true
  newSub.value = { ...sub } 
  activeDropdown.value = null 
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
  newSub.value = { ...initialForm }
}

const getCategoryColor = (category) => {
  if (category === 'OTT/영상') return 'color-red'
  if (category === '음악') return 'color-green'
  if (category === '도구/소프트웨어') return 'color-blue'
  return 'color-default'
}
</script>

<style scoped>
@import '@/assets/css/subscription.css';
</style>