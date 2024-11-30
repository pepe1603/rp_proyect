import { defineStore } from "pinia";
import { ref} from "vue";
import authService from "@/services/auth/authService";

export const useTokenStore = defineStore('token', () => {
    const tokenAccess = ref(localStorage.getItem('tokenAccess')    || null);
    const tokenRefresh = ref(localStorage.getItem('tokenRefresh')    || null);

    const fetchTokens = async () => {
        try {
            const data = await authService.signIn(username,password);
            tokenAccess.value = data.tokenAccess;
            tokenRefresh.value = data.tokenRefresh;
            localStorage.setItem('tokenAccess', data.tokenAccess);
            localStorage.setItem('tokenRefresh', data.toknAccess);
            
        } catch (error) {
            throw error;
        }

    };

    const tokenRefresh = async () => {
        try {
            const data = await authService.refreshToken();
            tokenAccess.value = data.tokenAccess;
            tokenRefresh.value = data.tokenRefresh;
            localStorage.setItem('tokenAccess', data.tokenAccess);
            localStorage.setItem('tokenrefresh', data.tokenRefesh);
        }catch(error){

        }
    };

    const logout = async () => {
        tokenAccess.value =null;
        tokenRefresh .value = null;
        localStorage.removeItem('tokenAccess');
        localStorage.removeItem('tokenRefresh');
    };

    return {
        tokenAccess,
        tokenRefresh,
        fetchTokens,
        logout,
    }


});