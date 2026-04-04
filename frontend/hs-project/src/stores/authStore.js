import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('useAuth', () => {
    const AUTH_KEY = 'user_auth'

    const token = ref(null)
    const refreshToken = ref(null)
    const role = ref(null)
    const member_id = ref(0)
    const username = ref(null)

    const init = () => {
        const savedData = localStorage.getItem(AUTH_KEY)
        if (savedData) {
            try {
                const data = JSON.parse(savedData)
                token.value = data.token
                refreshToken.value = data.refreshToken
                role.value = data.role
                member_id.value = data.member_id
                username.value = data.username
            } catch (e) {
                localStorage.removeItem(AUTH_KEY)
            }
        }
    }

    init()

    const isAuthenticated = computed(() => !!token.value)
    const isAdmin = computed(() => role.value === 'ADMIN' || role.value === 'EMP')

    const login = (data) => {
        token.value = data.accessToken
        refreshToken.value = data.refreshToken
        role.value = data.role
        member_id.value = data.member_id
        username.value = data.name

        localStorage.setItem(AUTH_KEY, JSON.stringify({
            token: token.value,
            refreshToken: refreshToken.value,
            role: role.value,
            member_id: member_id.value,
            username: username.value
        }))
    }

    const updateToken = (newToken) => {
        token.value = newToken
        localStorage.setItem(AUTH_KEY, JSON.stringify({
            token: token.value,
            refreshToken: refreshToken.value,
            role: role.value,
            member_id: member_id.value,
            username: username.value
        }))
    }

    const logout = () => {
        token.value = null
        refreshToken.value = null
        role.value = null
        member_id.value = 0
        username.value = null
        localStorage.removeItem(AUTH_KEY)
    }

    return { 
        token, refreshToken, role, member_id, username,
        isAuthenticated, isAdmin,
        login, updateToken, logout 
    }
})