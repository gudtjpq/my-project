import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from './router'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'bootstrap-icons/font/bootstrap-icons.css'

import AOS from 'aos'
import 'aos/dist/aos.css'

import './assets/css/core.css'
import './assets/css/main.css'
import './assets/css/forms.css'

const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

app.use(pinia)
app.use(router)
app.mount('#app')

// AOS 초기화
AOS.init({
  duration: 1000,   // 애니메이션 지속시간
  once: true        // 스크롤 시 한 번만 실행
})