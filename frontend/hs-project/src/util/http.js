import axios from 'axios';
import { useAuthStore } from '@/stores/authStore';
import { useHostStore } from '@/stores/hostStore' 

const hostStore = useHostStore()

// Axios 인스턴스 생성
const http = axios.create({
    baseURL: `${hostStore.host}/api`, // Spring Boot 서버 주소
    timeout: 10000, // 10초 이상 응답 없으면 에러
    headers: {
        'Content-Type': 'application/json',
    },
});

// 요청 인터셉터 (요청 전 공통 작업)
http.interceptors.request.use(
    (config) => {
        const authStore = useAuthStore();

        if (authStore.auth.token) {
            config.headers.Authorization = `Bearer ${authStore.auth.token}`; // JWT 토큰 추가
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 응답 인터셉터 (응답 후 공통 에러 처리)
http.interceptors.response.use(
    (response) => {
        // 파일 다운로드일 경우 그대로 반환
        if (response.config.responseType === 'blob') {
            return response;
        }
        
        return response.data;   // 데이터를 한 단계 미리 추출해서 전달
    },
    async (error) => {
        const authStore = useAuthStore();
        const originalRequest = error.config;

       if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            try {
                // 서버의 /api/auth/refresh 엔드 포인트 호출
                const res = await axios.post(`${hostStore.host}/api/auth/refresh`, {
                    accessToken: authStore.auth.token,
                    refreshToken: authStore.auth.refreshToken
                });

                if (res.status === 200) {
                    const newToken = res.data.accessToken;
                    authStore.updateToken(newToken); // 스토어 업데이트
                
                    originalRequest.headers.Authorization = `Bearer ${newToken}`;
                    return http(originalRequest); // 실패했던 요청 재시도
                }
            } catch (refreshError) {
                authStore.logout(); // 갱신 실패 시 로그아웃
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);

export default http;