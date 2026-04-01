<script setup>
import { reactive } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { useRouter, useRoute } from 'vue-router';
import http from '@/util/http';

const authStore = useAuthStore();
const router = useRouter();
const route = useRoute(); // 현재 라우트 정보를 담은 route 객체 생성

const loginData = reactive({
    login_id: '',
    password: ''
});

const handleLogin = async () => {
    try {
        const response = await http.post('/auth/login', loginData);
        authStore.login(response); 

        // 원래 가려던 페이지가 있으면 그리로 가고, 없으면 홈으로 이동
        const redirectPath = route.query.redirect || '/';
        router.push(redirectPath);
    } catch (error) {
        alert('로그인 실패: 아이디 또는 비밀번호를 확인하세요.');
    }
};
</script>

<template>
    <div class="row justify-content-center" data-aos="fade-up" data-aos-delay="200">
        <div class="col-md-5">
            <div class="bg-white box-shadow my-5 p-5">
                <h3 class="text-center pt-3"><i class="bi bi-lock"></i> 회원 로그인</h3>
                
                <form @submit.prevent="handleLogin" class="row g-3 mb-2">
                    <div class="col-12">
                        <label class="mb-1">아이디</label>
                        <input type="text" 
                            v-model="loginData.login_id"
                            class="form-control" placeholder="아이디"
                            required>
                    </div>
                    <div class="col-12">
                        <label class="mb-1">패스워드</label>
                        <input type="password" 
                            v-model="loginData.password"
                            class="form-control" autocomplete="off" 
                            placeholder="패스워드"
                            required>
                    </div>
                    <div class="col-12">
                        <div class="form-check">
                            <input class="form-check-input rememberMe" type="checkbox" id="rememberMe">
                            <label class="form-check-label" for="rememberMe"> 아이디 저장</label>
                        </div>
                    </div>
                    <div class="col-12 text-center">
                        <button type="submit" class="btn-accent btn-lg w-100">&nbsp;Login&nbsp;<i class="bi bi-check2"></i></button>
                    </div>
                    <div class="col-12 d-flex justify-content-between">
                        <button type="button" class="btn-light flex-fill me-2"><i class="bi bi-chat-fill kakao-icon"></i> 카카오</button>
                        <button type="button" class="btn-light flex-fill me-2"><span class="naver-icon">N</span> 네이버</button>
                        <button type="button" class="btn-light flex-fill"><i class="bi bi-google google-icon"></i> 구 글</button>
                    </div>
                </form>
                
                <div>
                    <p class="form-control-plaintext text-center text-danger"></p>
                </div>

                <div class="mt-3">
                    <p class="text-center">
                        <router-link to="/" class="me-2 border-link-right">아이디 찾기</router-link>
                        <router-link to="/" class="me-2 border-link-right">패스워드 찾기</router-link>
                        <router-link to="/signup" class="border-link-right">회원가입</router-link>
                    </p>
                </div>
                
            </div>
        </div>
    </div>
</template>

<style scoped>

</style>