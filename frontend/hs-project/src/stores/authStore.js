import { defineStore } from 'pinia'
import { reactive, computed } from 'vue';

export const useAuthStore = defineStore('useAuth', () => {
    const AUTH_KEY = 'user_auth';

    // 초기 상태
    const getInitialState = () => {
        const savedData = localStorage.getItem(AUTH_KEY);
        if (savedData) {
            try {
                return JSON.parse(savedData);
            } catch (e) {
            }
        }

        // 데이터가 없거나 에러 발생 시 기본값 반환
        return {
            token: null,
            refreshToken: null,
            role: null,
            member_id: 0,
            username: null,
        };
    };

    // state : 초기값은 localStorage 에서 가져와 페이지 새로고침 시에도 유지
    const auth = reactive(getInitialState());

    // getters
    const isAuthenticated = computed(() => !!auth.token);
    const isAdmin = computed(() => auth.role === 'ADMIN' || auth.role === 'EMP');

    // actions
    // 로그인 성공할 경우 정보 저장
    const login = (data) => {
        auth.token = data.accessToken;
        auth.refreshToken = data.refreshToken;
        auth.role = data.role;
        auth.member_id = data.member_id;
        auth.username = data.name;

        localStorage.setItem(AUTH_KEY, JSON.stringify(auth));
    }

    // 토큰 재발급 시 Access Token 만 업데이트
    const updateToken = (newToken) => {
        auth.token = newToken;
        localStorage.setItem(AUTH_KEY, JSON.stringify(auth));
    };

    // 로그아웃 시 모든 상태 초기화
    const logout = () => {
        auth.token = null;
        auth.refreshToken = null;
        auth.role = null;
        auth.member_id = 0;
        auth.username = null;

        localStorage.removeItem(AUTH_KEY);
    };

    return { 
        auth, 
        isAuthenticated, 
        isAdmin,
        login, 
        updateToken, 
        logout 
    };
});