<template>
  <footer class="footer-container">
    <div class="footer-inner">
      <div class="footer-top">
        <div class="footer-brand">
          <h2>SubManager.</h2>
          <p>당신의 일상을 가볍게 만드는 구독 관리 솔루션</p>
        </div>
        <div class="footer-links">
          <div class="link-group">
            <h3>서비스</h3>
            <router-link to="/">기능 소개</router-link>
            <router-link to="/subscription">구독 관리</router-link>
          </div>
          <div class="link-group">
            <h3>고객지원</h3>
            <router-link to="/">공지사항</router-link>
            <router-link to="/">문의하기</router-link>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>© 2026 SubManager. All rights reserved.</p>
        <div class="legal-links">
          <router-link to="/">이용약관</router-link>
          <router-link to="/">개인정보처리방침</router-link>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
</script>

<style scoped>
.footer-container {
  width: 100%;
  background-color: #f5f5f7;
  padding: 60px 20px 40px;
  margin-top: auto;
  border-top: 1px solid #d2d2d7;
}

.footer-inner {
  max-width: 1080px;
  margin: 0 auto;
}

.footer-top {
  display: flex;
  justify-content: space-between;
  margin-bottom: 60px;
}

.footer-brand h2 {
  font-size: 20px;
  font-weight: 700;
  margin: 0 0 12px 0;
}

.footer-brand p {
  font-size: 14px;
  color: #86868b;
  margin: 0;
}

.footer-links {
  display: flex;
  gap: 80px;
}

.link-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.link-group h3 {
  font-size: 14px;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 4px 0;
}

.link-group a {
  font-size: 14px;
  color: #86868b;
  transition: color 0.2s ease;
}

.link-group a:hover {
  color: #1d1d1f;
}

.footer-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 24px;
  border-top: 1px solid #d2d2d7;
  font-size: 12px;
  color: #86868b;
}

.legal-links {
  display: flex;
  gap: 20px;
}

.legal-links a:hover {
  color: #1d1d1f;
}
</style>