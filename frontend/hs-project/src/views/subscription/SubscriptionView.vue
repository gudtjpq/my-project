<template>
  <div class="layout-wrapper">
    <BackgroundScene />
    <Header />
    <main class="main-content">
      <div class="dashboard-container">
        <div class="dashboard-header glass-element-pro">
          <div class="title-group">
            <h2 class="page-title">나의 구독 관리</h2>
            <p class="page-subtitle">현재 이용 중인 서비스를 한눈에 파악하세요.</p>
          </div>
          <button class="btn-primary" @click="openModal">+ 새 구독 추가</button>
        </div>
        <div class="stats-grid">
          <div class="stat-card glass-card-pro">
            <div class="stat-icon-wrap bg-purple glass-element-pro"><span class="stat-icon">💰</span></div>
            <div class="stat-info glass-element-pro">
              <span class="stat-label">이번 달 총 지출</span>
              <strong class="stat-value">₩ {{ totalCost.toLocaleString() }}</strong>
            </div>
          </div>
          <div class="stat-card glass-card-pro">
            <div class="stat-icon-wrap bg-blue glass-element-pro"><span class="stat-icon">📦</span></div>
            <div class="stat-info glass-element-pro">
              <span class="stat-label">구독 중인 서비스</span>
              <strong class="stat-value">{{ subscriptions.length }} 개</strong>
            </div>
          </div>
          <div class="stat-card glass-card-pro highlight-card">
            <div class="stat-icon-wrap bg-white glass-element-pro"><span class="stat-icon">⏰</span></div>
            <div class="stat-info glass-element-pro">
              <span class="stat-label text-light">결제 임박</span>
              <strong class="stat-value text-white">
                {{ upcomingSubscription.dDayText }} 
                <span class="upcoming-names" v-if="upcomingSubscription.name">({{ upcomingSubscription.name }})</span>
              </strong>
            </div>
          </div>
        </div>

        <div class="middle-layout-grid">
          <div class="chart-section">
            <h3 class="section-title glass-element-pro">카테고리별 지출</h3>
            <div class="chart-card glass-card-pro">
              
              <template v-if="subscriptions.length > 0">
                <div class="chart-wrapper glass-element-pro">
                  <svg viewBox="0 0 42 42" class="donut">
                    <circle class="donut-ring" cx="21" cy="21" r="15.91549430918954" fill="transparent" stroke="rgba(255,255,255,0.05)" stroke-width="4"></circle>
                    <circle v-for="seg in categoryStats" :key="seg.category" class="donut-segment" cx="21" cy="21" r="15.91549430918954" fill="transparent" :stroke="seg.color" stroke-width="4" :stroke-dasharray="`${seg.percent} ${100 - seg.percent}`" :stroke-dashoffset="seg.offset"></circle>
                  </svg>
                  <div class="chart-inner-text">
                    <span class="total-label">총 지출</span>
                    <strong class="total-amount">₩ {{ totalCost.toLocaleString() }}</strong>
                  </div>
                </div>
                <div class="chart-legend glass-element-pro">
                  <div class="legend-item" v-for="seg in categoryStats" :key="seg.category">
                    <div class="legend-label">
                      <span class="color-dot" :style="{ backgroundColor: seg.color, boxShadow: `0 0 10px ${seg.color}` }"></span>
                      <span class="cat-name">{{ seg.category }}</span>
                      <span class="cat-percent">{{ seg.percent.toFixed(0) }}%</span>
                    </div>
                    <strong class="cat-amount">₩ {{ seg.amount.toLocaleString() }}</strong>
                  </div>
                </div>
              </template>

              <template v-else>
                <div class="chart-empty-state glass-element-pro">
                  <div class="chart-wrapper">
                    <svg viewBox="0 0 42 42" class="donut">
                      <circle class="donut-ring" cx="21" cy="21" r="15.91549430918954" fill="transparent" stroke="rgba(255,255,255,0.05)" stroke-width="4"></circle>
                    </svg>
                    <div class="chart-inner-text">
                      <span class="total-label">총 지출</span>
                      <strong class="total-amount">₩ 0</strong>
                    </div>
                  </div>
                  <p class="chart-empty-text">아직 등록된 구독 서비스가 없습니다.<br/>새 구독을 추가해 보세요!</p>
                </div>
              </template>

            </div>
          </div>

          <div class="calendar-section">
            <h3 class="section-title glass-element-pro">구독 캘린더</h3>
            <div class="calendar-card glass-card-pro">
              <div class="calendar-header glass-element-pro">
                <button @click="prevMonth" class="btn-cal-nav">◀</button>
                <h4 class="current-month">{{ currentYear }}년 {{ currentMonth + 1 }}월</h4>
                <button @click="nextMonth" class="btn-cal-nav">▶</button>
              </div>
              <div class="calendar-body glass-element-pro">
                <div class="calendar-weekdays">
                  <div class="weekday text-danger">일</div><div>월</div><div>화</div><div>수</div><div>목</div><div>금</div><div class="text-blue">토</div>
                </div>
                <div class="calendar-days">
                  <div v-for="(day, idx) in calendarDays" :key="idx" 
                       class="calendar-day" 
                       :class="{ 'empty': !day, 'today': isToday(day) }"
                       @click="openMemo(day)">
                    <span v-if="day">{{ day.getDate() }}</span>
                    <div class="indicators" v-if="day">
                      <span class="dot payment-dot" 
                            v-for="(sub, i) in getPaymentsOnDay(day)" 
                            :key="'pay-'+i"
                            :style="{ backgroundColor: getChartColor(sub.category), boxShadow: `0 0 6px ${getChartColor(sub.category)}` }">
                      </span>
                      <span class="dot memo-dot" v-if="hasMemo(day)"></span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="list-section">
          <h3 class="section-title glass-element-pro">구독 목록</h3>
          <div v-if="subscriptions.length === 0" class="empty-state glass-card-pro">아직 등록된 구독 서비스가 없습니다.<br/>새 구독을 추가해 보세요!</div>
          <div v-else class="subscription-grid">
            <div class="sub-card glass-card-pro" v-for="(sub, index) in subscriptions" :key="index" :style="{ animationDelay: `${index * 0.1}s` }">
              <div class="sub-card-header glass-element-pro">
                <div class="sub-name-group">
                  <div class="sub-logo" :style="{ background: getCategoryGradient(sub.category) }"></div>
                  <div>
                    <h4 class="sub-name">{{ sub.serviceName }}</h4>
                    <span class="sub-category">{{ sub.category }}</span>
                  </div>
                </div>
                <div class="card-menu-wrap">
                  <button class="btn-icon" @click.stop="toggleDropdown(index)">⋮</button>
                  <div v-if="activeDropdown === index" class="dropdown-overlay" @click.stop="activeDropdown = null"></div>
                  <div v-if="activeDropdown === index" class="custom-action-menu glass-card-pro">
                    <button @click.stop="openEditModal(sub)" class="action-btn glass-element-pro">수정</button>
                    <button @click.stop="deleteSubscription(sub.subId)" class="action-btn text-danger glass-element-pro">삭제</button>
                  </div>
                </div>
              </div>
              <div class="sub-card-body glass-element-pro">
                <div class="price-info">
                  <strong class="price">₩ {{ sub.price.toLocaleString() }}</strong>
                  <span class="cycle">/ 월</span>
                </div>
                <div class="date-info"><span>매월 {{ sub.paymentDay }}일 결제</span></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
    <Footer />

    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-card glass-card-pro" @click="isCategoryOpen = false">
        <div class="modal-header glass-element-pro">
          <h3>{{ isEditMode ? '구독 정보 수정' : '새 구독 추가' }}</h3>
          <button class="btn-close" @click="closeModal">✕</button>
        </div>
        <form @submit.prevent="saveSubscription" class="modal-form glass-element-pro">
          <div class="input-group">
            <label>서비스 이름</label>
            <input type="text" v-model="newSub.serviceName" placeholder="예: 넷플릭스, 스포티파이" required />
          </div>
          
          <div class="input-group">
            <label>카테고리</label>
            <div class="custom-select-wrapper" @click.stop="isCategoryOpen = !isCategoryOpen">
              <div class="custom-select-trigger" :class="{ 'selected': newSub.category }">
                {{ newSub.category || '카테고리를 선택하세요' }}
                <span class="arrow" :class="{ 'open': isCategoryOpen }">▼</span>
              </div>
              <div class="custom-select-options glass-card-pro" v-if="isCategoryOpen">
                <div class="custom-option" v-for="cat in categoryList" :key="cat" @click.stop="selectCategory(cat)">{{ cat }}</div>
              </div>
            </div>
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

    <div v-if="isMemoModalOpen" class="modal-overlay" @click.self="closeMemoModal">
      <div class="modal-card glass-card-pro">
        <div class="modal-header glass-element-pro">
          <h3>{{ formatDateObj(selectedDate) }} 메모</h3>
          <button class="btn-close" @click="closeMemoModal">✕</button>
        </div>
        <div class="modal-form glass-element-pro">
          <div class="input-group" v-if="getPaymentsOnDay(selectedDate).length > 0">
            <label class="text-blue">💡 오늘 결제 예정 서비스</label>
            <ul class="payment-list-in-memo">
              <li v-for="sub in getPaymentsOnDay(selectedDate)" :key="sub.subId">
                {{ sub.serviceName }} (₩{{ sub.price.toLocaleString() }})
              </li>
            </ul>
          </div>
          <div class="input-group">
            <label>메모 작성</label>
            <textarea v-model="memoText" rows="4" placeholder="이 날짜에 기억할 내용을 적어주세요."></textarea>
          </div>
          <div class="modal-actions">
            <button type="button" class="btn-secondary" @click="closeMemoModal">취소</button>
            <button type="button" class="btn-primary-full" @click="saveMemo">저장하기</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import Header from '@/components/layout/Header.vue'
import Footer from '@/components/layout/Footer.vue'
import BackgroundScene from '@/components/layout/BackgroundScene.vue'
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

const isCategoryOpen = ref(false)
const categoryList = ['OTT/영상', '음악', '도구/소프트웨어', '멤버십', '기타']

const today = new Date()
const currentDate = ref(new Date(today.getFullYear(), today.getMonth(), 1))
const currentYear = computed(() => currentDate.value.getFullYear())
const currentMonth = computed(() => currentDate.value.getMonth())

const calendarDays = computed(() => {
  const year = currentYear.value
  const month = currentMonth.value
  const firstDay = new Date(year, month, 1).getDay()
  const daysInMonth = new Date(year, month + 1, 0).getDate()

  const days = []
  for (let i = 0; i < firstDay; i++) days.push(null)
  for (let i = 1; i <= daysInMonth; i++) {
    days.push(new Date(year, month, i))
  }
  return days
})

const prevMonth = () => { currentDate.value = new Date(currentYear.value, currentMonth.value - 1, 1) }
const nextMonth = () => { currentDate.value = new Date(currentYear.value, currentMonth.value + 1, 1) }

const isToday = (day) => {
  if (!day) return false
  return day.getDate() === today.getDate() && day.getMonth() === today.getMonth() && day.getFullYear() === today.getFullYear()
}

const getPaymentsOnDay = (day) => {
  if (!day) return []
  return subscriptions.value.filter(sub => sub.paymentDay === day.getDate())
}

const memos = ref({})
const isMemoModalOpen = ref(false)
const selectedDate = ref(null)
const memoText = ref('')

const formatDateStr = (date) => {
  if(!date) return ''
  return `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`
}

const formatDateObj = (date) => {
  if(!date) return ''
  return `${date.getMonth()+1}월 ${date.getDate()}일`
}

const hasMemo = (day) => {
  if (!day) return false
  return !!memos.value[formatDateStr(day)]
}

const openMemo = (day) => {
  if(!day) return
  selectedDate.value = day
  const dateStr = formatDateStr(day)
  memoText.value = memos.value[dateStr] || ''
  isMemoModalOpen.value = true
}

const saveMemo = () => {
  const dateStr = formatDateStr(selectedDate.value)
  if (memoText.value.trim()) {
    memos.value[dateStr] = memoText.value
  } else {
    delete memos.value[dateStr]
  }
  isMemoModalOpen.value = false
}

const closeMemoModal = () => { isMemoModalOpen.value = false }

const selectCategory = (cat) => {
  newSub.value.category = cat
  isCategoryOpen.value = false
}

const totalCost = computed(() => {
  return subscriptions.value.reduce((acc, sub) => acc + sub.price, 0)
})

const upcomingSubscription = computed(() => {
  if (subscriptions.value.length === 0) return { dDayText: '구독 없음', name: '' }
  const todayDate = new Date()
  const currentDay = todayDate.getDate()
  const daysInMonth = new Date(todayDate.getFullYear(), todayDate.getMonth() + 1, 0).getDate()
  let minDday = 999
  let closestSubs = []
  subscriptions.value.forEach(sub => {
    let dDay
    if (sub.paymentDay === currentDay) dDay = 0
    else if (sub.paymentDay > currentDay) dDay = sub.paymentDay - currentDay
    else dDay = (daysInMonth - currentDay) + sub.paymentDay
    if (dDay < minDday) {
      minDday = dDay
      closestSubs = [sub.serviceName]
    } else if (dDay === minDday) {
      closestSubs.push(sub.serviceName)
    }
  })
  if (closestSubs.length === 0) return { dDayText: '-', name: '' }
  const nameText = closestSubs.join(', ')
  if (minDday === 0) return { dDayText: 'D-Day', name: nameText }
  return { dDayText: `D-${minDday}`, name: nameText }
})

const getChartColor = (category) => {
  if (category === 'OTT/영상') return '#ff2a5f'
  if (category === '음악') return '#00e676'
  if (category === '도구/소프트웨어') return '#00d4ff'
  if (category === '멤버십') return '#ffab00'
  if (category === '기타') return '#b388ff'
  return '#b0bec5'
}

const getCategoryGradient = (category) => {
  if (category === 'OTT/영상') return 'linear-gradient(135deg, #ff2a5f 0%, #ff5252 100%)'
  if (category === '음악') return 'linear-gradient(135deg, #00e676 0%, #69f0ae 100%)'
  if (category === '도구/소프트웨어') return 'linear-gradient(135deg, #00d4ff 0%, #0071e3 100%)'
  if (category === '멤버십') return 'linear-gradient(135deg, #ffab00 0%, #ffd600 100%)'
  if (category === '기타') return 'linear-gradient(135deg, #b388ff 0%, #7c4dff 100%)'
  return 'linear-gradient(135deg, #45454a 0%, #2c2c2f 100%)'
}

const categoryStats = computed(() => {
  if (totalCost.value === 0) return []
  const stats = {}
  subscriptions.value.forEach(sub => {
    if (!stats[sub.category]) {
      stats[sub.category] = { amount: 0, color: getChartColor(sub.category) }
    }
    stats[sub.category].amount += sub.price
  })
  let cumulativePercent = 0
  const result = []
  const sortedKeys = Object.keys(stats).sort((a, b) => stats[b].amount - stats[a].amount)
  sortedKeys.forEach(cat => {
    const percent = (stats[cat].amount / totalCost.value) * 100
    let offset = 25 - cumulativePercent
    if (offset < 0) offset += 100
    result.push({ category: cat, amount: stats[cat].amount, percent: percent, color: stats[cat].color, offset: offset })
    cumulativePercent += percent
  })
  return result
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
    console.error("오류:", error)
  }
}

const saveSubscription = async () => {
  if (!newSub.value.category) {
    alert("카테고리를 선택해주세요.")
    return
  }
  try {
    const payload = { ...newSub.value, memberId: authStore.auth.member_id }
    const url = isEditMode.value ? '/subscription/update' : '/subscription/register'
    const res = await http.post(url, payload)
    if (res.state === 'true') {
      closeModal()
      fetchSubscriptions()
    } else {
      alert("실패했습니다.")
    }
  } catch (error) {
    console.error("오류:", error)
  }
}

const deleteSubscription = async (subId) => {
  if (!subId) return
  if(!confirm("정말 삭제하시겠습니까?")) return
  try {
    const res = await http.delete(`/subscription/delete/${subId}`)
    if (res.state === 'true') {
      activeDropdown.value = null
      fetchSubscriptions()
    } else {
      alert("실패했습니다.")
    }
  } catch (error) {
    console.error("오류:", error)
  }
}

const toggleDropdown = (index) => {
  activeDropdown.value = activeDropdown.value === index ? null : index
}

const openModal = () => {
  isEditMode.value = false
  newSub.value = { ...initialForm }
  isCategoryOpen.value = false
  isModalOpen.value = true
}

const openEditModal = (sub) => {
  isEditMode.value = true
  newSub.value = { ...sub } 
  activeDropdown.value = null 
  isCategoryOpen.value = false
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
  newSub.value = { ...initialForm }
  isCategoryOpen.value = false
}
</script>

<style scoped>
@import '@/assets/css/glass.css';
@import '@/assets/css/subscription.css';
</style>