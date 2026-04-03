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
@import '@/assets/css/footer.css';
</style>