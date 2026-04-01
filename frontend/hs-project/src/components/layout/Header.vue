<script setup>
import { useAuthStore } from '@/stores/authStore';
import { useRouter, useRoute } from 'vue-router';
import { ref, watch, onMounted, onUnmounted, computed } from 'vue';
import avatar from '@/assets/img/avatar.png';

const authStore = useAuthStore();
const router = useRouter();
const route = useRoute();

// 메뉴 데이터 정의
const menuItems = [
  { name: '홈', routeName: 'Home' },
  { name: '소개', routeName: 'About' },
  {
    key: 'lecture',
    name: '강좌',
    children: [
      { name: '프로그래밍', routeName: 'Lecture', params: { category: 'L100001' } },
      { name: '데이터베이스', routeName: 'Lecture', params: { category: 'L100002' } },
      { name: '웹 프로그래밍', routeName: 'Lecture', params: { category: 'L100003' } },
      { name: '데이터분석 및 AI', routeName: 'Lecture', params: { category: 'L100004' } },
      { name: '클라우드 및 기타', routeName: 'Lecture', params: { category: 'L100005' } },
      { type: 'divider' },
      { name: '질문과 답변', path: '/lecture/qna' },
    ]
  },
  { name: '블로그', path: '/' },
  {
    key: 'service',
    name: '서비스',
    children: [
      { name: '맛집정보', path: '/' },
      { name: '레시피', path: '/recipe' },
      { name: '관광정보', path: '/tour' },
      { name: '날씨', path: '/weather' },
      { name: '지역별날씨', path: '/weatherCity' },
    ]
  },
  {
    key: 'community',
    name: '커뮤니티',
    children: [
      { name: '방명록', path: '/guest' },
      { name: '자유 게시판', path: '/board' },
      { name: '답변형 게시판', path: '/' },
      { name: '포토 갤러리', path: '/photo' },
      { type: 'divider' },
      { name: '채팅', path: '/' },
    ]
  },
  {
    key: 'customer',
    name: '고객센터',
    children: [
      { name: '자주하는질문', path: '/faq' },
      { name: '공지사항', path: '/notice' },
      { name: '1:1문의', path: '/inquiry' },
      { type: 'divider' },
      { name: '이벤트', path: '/' },
    ]
  }
];

// 상태 관리
const isMobileNavActive = ref(false);
const mobileOpenKey = ref(null);
const pcOpenKey = ref(null);
const windowWidth = ref(typeof window !== 'undefined' ? window.innerWidth : 1200);

const isMobile = computed(() => windowWidth.value < 1200);

// 핸들러 함수
const handleResize = () => {
  windowWidth.value = window.innerWidth;
  if (!isMobile.value) {
    isMobileNavActive.value = false;
    mobileOpenKey.value = null;
    document.body.classList.remove('mobile-nav-active');
  }
};

const toggleMobileNav = () => {
  isMobileNavActive.value = !isMobileNavActive.value;
  if (isMobileNavActive.value) {
    document.body.classList.add('mobile-nav-active');
  } else {
    document.body.classList.remove('mobile-nav-active');
    mobileOpenKey.value = null;
  }
};

const handleMouseEnter = (key) => { if (!isMobile.value) pcOpenKey.value = key; };
const handleMouseLeave = () => { if (!isMobile.value) pcOpenKey.value = null; };

const closeAll = () => {
  mobileOpenKey.value = null;
  pcOpenKey.value = null;
  isMobileNavActive.value = false;
  document.body.classList.remove('mobile-nav-active');
};

const toggleDropdown = (key) => {
  if (isMobile.value) {
    mobileOpenKey.value = mobileOpenKey.value === key ? null : key;
  }
};

const handleLogout = () => {
  authStore.logout();
  router.push('/');
  closeAll();
};

// 라이프사이클 및 감시
watch(() => route.path, closeAll);

onMounted(() => { window.addEventListener('resize', handleResize); });
onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  document.body.classList.remove('mobile-nav-active');
});
</script>

<template>
  <div class="container-fluid py-2 px-5 header-top border-bottom">
    <div class="container">
      <div class="row align-items-center">
        <div class="col d-none d-md-flex contact-info">
          <i class="bi bi-telephone-inbound-fill"></i>&nbsp;<span>+82-1234-5678</span>
        </div>
        <div class="col">
          <div class="d-flex justify-content-end header-top-links">
            <template v-if="!authStore.isAuthenticated">
              <router-link to="/login"><i class="bi bi-lock"></i></router-link>
              <router-link to="/signup"><i class="bi bi-person-plus"></i></router-link>
            </template>
            <template v-else>
              <router-link to="/"><i class="bi bi-bell"></i></router-link>
              <a href="javascript:void(0)" @click="handleLogout"><i class="bi bi-unlock"></i></a>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>

  <header id="header" class="header d-flex align-items-center sticky-top bg-white shadow-sm">
    <div class="container position-relative d-flex align-items-center justify-content-between">
      
      <router-link to="/" class="logo d-flex align-items-center me-auto me-xl-0" @click="closeAll">
        <span class="sitename">SPRING</span><span class="dot">.</span>
      </router-link>
      
      <nav id="navmenu" class="navmenu">
        <ul :class="{ 'mobile-nav-show': isMobileNavActive }">
          <template v-for="item in menuItems" :key="item.name">
            
            <li v-if="!item.children">
              <router-link 
                :to="item.routeName ? { name: item.routeName } : item.path" 
                active-class="active" 
                @click="closeAll">
                {{ item.name }}
              </router-link>
            </li>

            <li v-else class="dropdown" 
                @mouseenter="handleMouseEnter(item.key)" 
                @mouseleave="handleMouseLeave">
              <a href="#" @click.prevent="toggleDropdown(item.key)" 
                 :class="{ 'active': (isMobile ? mobileOpenKey === item.key : pcOpenKey === item.key) }">
                <span>{{ item.name }}</span> <i class="bi bi-chevron-down toggle-dropdown"></i>
              </a>
              
              <ul v-show="(isMobile && mobileOpenKey === item.key) || (!isMobile && pcOpenKey === item.key)" 
                  :style="isMobile && mobileOpenKey === item.key ? { display: 'block', position: 'static', opacity: '1', visibility: 'visible' } : {}"
                  @click="closeAll">
                <template v-for="(sub, index) in item.children" :key="index">
                  <li v-if="sub.type === 'divider'"><hr class="dropdown-divider"></li>
                  <li v-else>
                    <router-link :to="sub.routeName ? { name: sub.routeName, params: sub.params } : sub.path">
                      {{ sub.name }}
                    </router-link>
                  </li>
                </template>
              </ul>
            </li>

          </template>
        </ul>
        
        <i class="mobile-nav-toggle d-xl-none bi" 
           :class="isMobileNavActive ? 'bi-x' : 'bi-list'" 
           @click="toggleMobileNav"></i>
      </nav>
      
      <div class="d-flex align-items-center header-right">
        <template v-if="authStore.isAuthenticated">
          <div class="header-avatar dropdown">
            <img :src="avatar" class="avatar-sm dropdown-toggle shadow-sm" data-bs-toggle="dropdown" />
            <ul class="dropdown-menu dropdown-menu-end shadow border-0">
              <li><router-link to="/album" class="dropdown-item" @click="closeAll">사진첩</router-link></li>
              <li><router-link to="/schedule" class="dropdown-item" @click="closeAll">일정관리</router-link></li>
              <li><router-link to="/note" class="dropdown-item" @click="closeAll">쪽지함</router-link></li>
              <li><hr class="dropdown-divider"></li>
              <li><router-link to="/" class="dropdown-item" @click="closeAll">정보수정</router-link></li>
            </ul>
          </div>
        </template>
      </div>

    </div>
  </header>
  
  <div v-if="isMobileNavActive" class="mobile-nav-overlay" @click="closeAll"></div>
</template>

<style scoped>
  :global(body.mobile-nav-active) { overflow: hidden; }
</style>