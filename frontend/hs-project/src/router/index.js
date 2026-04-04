import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

import HomeView from '@/views/HomeView.vue'

const routes = [
  { path: '/', name: 'Home', component: HomeView },
  { path: '/login', name: 'Login', component: () => import('@/views/member/LoginView.vue') },
  { path: '/signup', name: 'Signup', component: () => import('@/views/member/SignupView.vue') },
  { path: '/mypage', name: 'MyPage', component: () => import('@/views/member/MyPageView.vue'), meta: { requiresAuth: true } },
  { path: '/oauth/kakao/callback', name: 'KakaoCallback', component: () => import('@/views/member/KakaoCallbackView.vue') },
  { path: '/subscription', name: 'subscription', component: () => import('@/views/subscription/SubscriptionView.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()

    if (to.meta.requiresAuth) {
        if (!authStore.auth.token) {
            next({ name: 'Login', query: { redirect: to.fullPath } })
        } else {
            next()
        }
    } else {
        next()
    }
})

export default router