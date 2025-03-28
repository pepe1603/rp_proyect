import apiClient from "@/config/apiConfig";

export const authService = {
    async signUp(payload) {
        const response = await apiClient.post("/auth/sign_up", payload);
        return response.data;   //Retorna el JSOn Directamente
    },

    async signIn(payload) {
        const response = await apiClient.post("/auth/sign_in", payload);
        return response.data;
    },

    async requestPasswordReset(email) {
        const response = await apiClient.post("/auth/password-reset-request", null, {
            params: { email },
        });
        return response.data;
    },

    async resetPassword(verificationCode, newPassword) {
        const response = await apiClient.post("/auth/password-reset", null, {
            params: { verificationCode, newPassword },
        });
        return response.data;
    },

    async logout(token) {
        const response = await apiClient.post(
            "/auth/logout",
            {},
            {
                headers: { Authorization: `Bearer ${token}` },
            }
        );
        return response.data;
    },

    async refreshToken(refreshToken) {
        const response = await apiClient.post("/auth/refresh-token", { refreshToken });
        return response.data;
    },
};
