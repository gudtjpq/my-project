<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();
</script>

<template>
  <footer id="footer" class="footer dark-background">
    <div class="container footer-top">
      <div class="row gy-4">
        <div class="col-lg-4 col-md-6 footer-about">
          <router-link to="/" class="logo d-flex align-items-center">
            <span class="sitename">SPRING</span>
          </router-link>
          <div class="footer-contact pt-3">
            <p>21, World Cup buk-ro</p>
            <p>Mapo-gu, Seoul, Republic of Korea</p>
            <p class="mt-3"><strong>Phone:</strong> <span>+82 1234 5648</span></p>
            <p><strong>Email:</strong> <span>info@example.com</span></p>
          </div>
          <div class="social-links d-flex mt-4">
            <a href="#"><i class="bi bi-twitter-x"></i></a>
            <a href="#"><i class="bi bi-facebook"></i></a>
            <a href="#"><i class="bi bi-instagram"></i></a>
            <a href="#"><i class="bi bi-linkedin"></i></a>
          </div>        
        </div>
        
        <div class="col-lg-2 col-md-3 footer-links">
          <h4>Useful Links</h4>
          <ul>
            <li><router-link to="/">Home</router-link></li>
            <li><router-link to="/about">About</router-link></li>
            <li><router-link to="/blog">Blog</router-link></li>
            <li><router-link to="/weather">Weather</router-link></li>
            <li><router-link to="/">Map</router-link></li>
          </ul>      
        </div>

        <div class="col-lg-2 col-md-3 footer-links">
          <h4>Help &amp; Information</h4>
          <ul>
            <li><router-link to="/">Help</router-link></li>
            <li><router-link to="/">Customer Service</router-link></li>          
            <li><router-link to="/">이용약관</router-link></li>
            <li><router-link to="/">개인정보처리방침</router-link></li>
            <li><router-link to="/">청소년보호정책</router-link></li>
          </ul>
        </div>
        
        <div class="col-lg-4 col-md-12 footer-guide">
          <h4>Our Newsletter</h4>
          <p>SPRING 회원이 되시면 최신 정보 및 서비스 소식을 받아볼수 있습니다.</p>
          </div>
      </div>
    </div>
    
    <div class="container copyright text-center mt-4">
      <p>© <span>Copyright</span> <strong class="px-1 sitename">SPRING</strong> <span>All Rights Reserved</span></p>
    </div>
  </footer>
</template>

<style scoped>
/* Vue Router 링크의 기본 밑줄을 제거 */
.footer-links ul li a {
  text-decoration: none;
  transition: 0.3s;
}

.footer-links ul li a:hover {
  color: var(--accent-color, #e84545);
}

/* 다크 배경에서 텍스트 색상 보정 */
.dark-background {
  --background-color: #060606;
  --default-color: #ffffff;
  --heading-color: #ffffff;
  background-color: var(--background-color);
  color: var(--default-color);
}
</style>