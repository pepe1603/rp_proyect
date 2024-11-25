import { defineStore  } from "pinia";
import { reactive } from "vue";
import authService from "@/services/AuthService";

export const userStore = defineStore('user', () => {
    const user = reactive(
        JSON.parse(localStorage.getItem('user')) || null);

    const login = async (username, password) => {
        try {
            const data = await authService.signIn(username, password);
            user.value = data.user;
            localStorage.setItem('user', JSON.stringify(data.user));
        } catch (error) {
            throw error;
        }
    };

    const logout= () =>{
        user.value = null;
        localStorage.removeItem('user');
    }

    return {
        user,
        logout,
        login
    }

});