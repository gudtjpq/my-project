import axios from 'axios';
import { useAuthStore } from '@/stores/authStore';
import { useHostStore } from '@/stores/hostStore' 

const hostStore = useHostStore()

const http = axios.create({
    baseURL: `${hostStore.host}/api`, 
    timeout: 10000, 
    headers: {
        'Content-Type': 'application/json',
    },
});

http.interceptors.request.use(
    (config) => {
        const authStore = useAuthStore();
        if (authStore.token) {
            config.headers.Authorization = `Bearer ${authStore.token}`; 
        }
        return config;
    },
    (error) => Promise.reject(error)
);

http.interceptors.response.use(
    (response) => {
        if (response.config.responseType === 'blob') {
            return response;
        }
        return response.data;
    },
    async (error) => {
        const authStore = useAuthStore();
        const originalRequest = error.config;

       if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            try {
                const res = await axios.post(`${hostStore.host}/api/auth/refresh`, {
                    accessToken: authStore.token, 
                    refreshToken: authStore.refreshToken 
                });

                if (res.status === 200) {
                    const newToken = res.data.accessToken;
                    authStore.updateToken(newToken); 
                
                    originalRequest.headers.Authorization = `Bearer ${newToken}`;
                    return http(originalRequest); 
                }
            } catch (refreshError) {
                authStore.logout(); 
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);

export default http;