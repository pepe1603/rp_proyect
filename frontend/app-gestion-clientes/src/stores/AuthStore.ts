// stores/authStore.js
import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
    state: () => ({
        user: null,
        accessToken: null,
        refreshToken: null,
    }),

    actions: {
        setUser(userData) {
            this.user = userData;
        },

        setAccessToken(token) {
            this.accessToken = token;
            localStorage.setItem("authToken", token); // Guardamos el token en localStorage
        },

        setRefreshToken(token) {
            this.refreshToken = token;
        },

        logout() {
            this.user = null;
            this.accessToken = null;
            this.refreshToken = null;
            localStorage.removeItem("authToken"); // Limpiamos el token de localStorage
        },

        // Método para refrescar el token de acceso
        async refreshAccessToken() {
            const response = await this.$store.dispatch(
                "refreshToken",
                this.refreshToken
            );
            this.setAccessToken(response.data.accessToken); // Asumimos que la respuesta tiene el nuevo token
        },
    },

    getters: {
        isAuthenticated: (state) => !!state.accessToken, // Verificamos si el usuario está autenticado
    },
});
