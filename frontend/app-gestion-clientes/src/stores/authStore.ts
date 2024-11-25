import { defineStore } from "pinia";
import { authService } from "@/services/authService";
import { ref } from "vue";

export const useAuthStore = defineStore("auth", () => {
    // Estado
    const isAuthenticated = ref(false);
    const token = ref(null);
    const refreshToken = ref(null);
    const user = ref(null);

    // Acciones
    const signIn = async (payload) => {
        try {
            const response = await authService.signIn(payload);

            // Procesar el JSON de la respuesta
            token.value = response.token;
            refreshToken.value = response.refreshToken;
            user.value = response.userData;

            isAuthenticated.value = true;

            // Guardar tokens en localStorage para persistencia
            saveTokensToLocalStorage(response.token, response.refreshToken);

            return { success: true };
        } catch (error) {
            return {
                success: false,
                message: error.response?.data?.message || "Credenciales inválidas",
            };
        }
    };

    const logout = async () => {
        try {
            if (token.value) {
                await authService.logout(token.value);
            }
            clearAuthState();
        } catch (error) {
            console.error("Error al cerrar sesión", error);
        }
    };

    const refreshAccessToken = async () => {
        try {
            const response = await authService.refreshToken(refreshToken.value);
            token.value = response.token;
            saveTokensToLocalStorage(response.token, refreshToken.value);
        } catch (error) {
            console.error("Error al refrescar el token", error);
            clearAuthState();
        }
    };

    // Métodos auxiliares
    const saveTokensToLocalStorage = (accessToken, refreshTokenValue) => {
        localStorage.setItem("authToken", accessToken);
        localStorage.setItem("refreshToken", refreshTokenValue);
    };

    const loadTokensFromLocalStorage = () => {
        const storedToken = localStorage.getItem("authToken");
        const storedRefreshToken = localStorage.getItem("refreshToken");

        if (storedToken && storedRefreshToken) {
            token.value = storedToken;
            refreshToken.value = storedRefreshToken;
            isAuthenticated.value = true;
        }
    };

    const clearAuthState = () => {
        token.value = null;
        refreshToken.value = null;
        user.value = null;
        isAuthenticated.value = false;
        localStorage.removeItem("authToken");
        localStorage.removeItem("refreshToken");
    };

    // Cargar tokens desde localStorage al iniciar
    loadTokensFromLocalStorage();

    return {
        isAuthenticated,
        token,
        refreshToken,
        user,
        signIn,
        logout,
        refreshAccessToken,
    };
});
