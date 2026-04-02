<template>
  <div class="layout-wrapper">
    <Header />
    
    <main class="main-content">
      <section class="hero-section">
        <h1 class="hero-title">
          흩어진 정기결제,<br/>이제 한곳에서 완벽하게.
        </h1>
        <p class="hero-subtitle">
          넷플릭스부터 노션까지. 매달 빠져나가는 구독료를 스마트하게 관리하고 불필요한 지출을 줄이세요.
        </p>
        <div class="hero-actions">
          <router-link to="/signup" class="btn-primary">무료로 시작하기</router-link>
        </div>
      </section>

      <section class="features-section">
        <div class="feature-grid">
          <div class="feature-card">
            <div class="feature-icon">📊</div>
            <h3 class="feature-title">한눈에 보는 지출</h3>
            <p class="feature-desc">카테고리별 통계로 이번 달 총 고정 지출액을 정확히 파악할 수 있습니다.</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">⏰</div>
            <h3 class="feature-title">스마트 결제 알림</h3>
            <p class="feature-desc">결제가 되기 전, 잊지 않도록 설정된 날짜에 미리 알려드립니다.</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🔒</div>
            <h3 class="feature-title">안전한 데이터 관리</h3>
            <p class="feature-desc">개인정보와 구독 내역은 최고 수준의 보안으로 안전하게 보호됩니다.</p>
          </div>
        </div>
      </section>
    </main>

    <Footer />
  </div>
</template>

<script setup>
import Header from '@/components/layout/Header.vue'
import Footer from '@/components/layout/Footer.vue'
</script>

<style scoped>
.layout-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  padding-top: 60px;
}

.hero-section {
  max-width: 1080px;
  margin: 0 auto;
  padding: 120px 20px;
  text-align: center;
}

.hero-title {
  font-size: 56px;
  font-weight: 700;
  line-height: 1.2;
  letter-spacing: -1.5px;
  margin: 0 0 24px 0;
  color: #1d1d1f;
}

.hero-subtitle {
  font-size: 20px;
  line-height: 1.5;
  color: #86868b;
  margin: 0 0 40px 0;
  font-weight: 400;
}

.btn-primary {
  display: inline-block;
  background-color: #0071e3;
  color: #ffffff;
  font-size: 17px;
  font-weight: 600;
  padding: 16px 32px;
  border-radius: 30px;
  transition: background-color 0.2s ease;
}

.btn-primary:hover {
  background-color: #0077ed;
}

.features-section {
  max-width: 1080px;
  margin: 0 auto;
  padding: 0 20px 120px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.feature-card {
  background-color: #ffffff;
  border-radius: 20px;
  padding: 40px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.02);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.05);
}

.feature-icon {
  font-size: 40px;
  margin-bottom: 24px;
}

.feature-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 12px 0;
  color: #1d1d1f;
}

.feature-desc {
  font-size: 15px;
  line-height: 1.5;
  color: #86868b;
  margin: 0;
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 40px;
  }
  
  .feature-grid {
    grid-template-columns: 1fr;
  }
}
</style>