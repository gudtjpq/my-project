import { createRouter, createWebHistory } from 'vue-router'
  import { useAuthStore } from '@/stores/authStore';

import HomeView from '@/views/HomeView.vue'

const routes = [
  { path: '/', name: 'Home', component: HomeView },
  { path: '/about', name: 'About', component: () => import('@/views/AboutView.vue') },
  { path: '/login', name: 'Login', component: () => import('@/views/member/LoginView.vue') },
  { path: '/signup', name: 'Signup', component: () => import('@/views/member/SignupView.vue') },  
  { path: '/:catchAll(.*)', name: 'NotFound', component: () => import('@/views/error/NotFound.vue') },

  // 로그인이 필요한 메뉴
  { path: '/lecture/:category', name: 'Lecture', component: () => import('@/views/lecture/LectureView.vue'), meta: { requiresAuth: true } },
  { path: '/lecture/qna', name: 'Question', component: () => import('@/views/lecture/QuestionView.vue'), meta: { requiresAuth: true } },

  { path: '/recipe', name: 'Recipe', component: () => import('@/views/service/RecipeView.vue'), meta: { requiresAuth: true } },
  { path: '/tour', name: 'Tour', component: () => import('@/views/service/TourView.vue'), meta: { requiresAuth: true } },
  { path: '/weather', name: 'Weather', component: () => import('@/views/service/WeatherView.vue'), meta: { requiresAuth: true } },
  { path: '/weatherCity', name: 'WeatherCity', component: () => import('@/views/service/WeatherCityView.vue'), meta: { requiresAuth: true } },

  { path: '/guest', name: 'Guest', component: () => import('@/views/community/GuestView.vue'), meta: { requiresAuth: true } },
  { path: '/board', name: 'Board', component: () => import('@/views/community/BoardView.vue'), meta: { requiresAuth: true } },
  { path: '/photo', name: 'Photo', component: () => import('@/views/community/PhotoView.vue'), meta: { requiresAuth: true } },

  { path: '/faq', name: 'Faq', component: () => import('@/views/customer/FaqView.vue'), meta: { requiresAuth: true } },
  { path: '/notice', name: 'Notice', component: () => import('@/views/customer/NoticeView.vue'), meta: { requiresAuth: true } },
  { path: '/inquiry', name: 'Inquiry', component: () => import('@/views/customer/InquiryView.vue'), meta: { requiresAuth: true } },

  { path: '/album', name: 'Album', component: () => import('@/views/mypage/AlbumView.vue'), meta: { requiresAuth: true } },
  { path: '/schedule', name: 'Schedule', component: () => import('@/views/mypage/ScheduleView.vue'), meta: { requiresAuth: true } },
  { path: '/note', name: 'Note', component: () => import('@/views/mypage/NoteView.vue'), meta: { requiresAuth: true } },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 네비게이션 가드 설정
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();

    // 이동할 페이지가 인증을 필요하는 경우
    if (to.meta.requiresAuth) {
        // 로그인 상태(토큰 존재 여부) 확인
        if (!authStore.auth.token) {
            // 로그인 페이지로 이동 (가려던 주소를 query로 넘기면 로그인 후 복귀 가능)
            next({ name: 'Login', query: { redirect: to.fullPath } });
        } else {
            next(); // 로그인 되어 있으면 통과
        }
    } else {
        next(); // 인증이 필요 없는 페이지는 무조건 통과
    }
});

export default router