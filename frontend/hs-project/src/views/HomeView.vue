<template>
  <div class="layout-wrapper">
    <BackgroundScene />
    
    <Header />
    <main class="main-content">
      <div class="glass-container-wrapper">
        <div class="glass-main-card glass-card-pro">
          <div class="hero-content glass-element-pro">
            <h1 class="hero-title">
              흩어진 정기결제,<br/>
              이제 한곳에서<br/>
              <span class="highlight-yellow">완벽하게</span>
            </h1>
            <p class="hero-subtitle">매달 빠져나가는 구독료를 스마트하게 관리하고 불필요한 지출을 줄이세요.</p>
            <div class="hero-actions">
              <router-link to="/signup" class="btn-hero-primary">무료 시작</router-link>
              <router-link to="/" class="btn-hero-secondary">더 알아보기</router-link>
            </div>
          </div>
          
          <div class="feature-grid glass-element-pro">
            <div class="feature-card">
              <div class="feature-icon-wrapper"><span class="feature-icon">📊</span></div>
              <h3 class="feature-title">한눈에 보는 지출</h3>
              <p class="feature-desc">카테고리별 통계로 이번 달 총 고정 지출액을 파악하세요.</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon-wrapper"><span class="feature-icon">⏰</span></div>
              <h3 class="feature-title">스마트 결제 알림</h3>
              <p class="feature-desc">결제되기 전, 잊지 않도록 미리 알림을 보내드립니다.</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon-wrapper"><span class="feature-icon">🔒</span></div>
              <h3 class="feature-title">안전한 데이터 관리</h3>
              <p class="feature-desc">모든 개인정보는 최고 수준의 보안으로 보호됩니다.</p>
            </div>
          </div>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import Header from '@/components/layout/Header.vue'
import Footer from '@/components/layout/Footer.vue'
import BackgroundScene from '@/components/layout/BackgroundScene.vue'
</script>

<style scoped>
@import '@/assets/css/glass.css';
@import '@/assets/css/home.css';
</style>