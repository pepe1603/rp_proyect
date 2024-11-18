// services/AuthService.js
import axios from 'axios';
import { useAuthStore } from '@/stores/authStore';  // Usamos el store de autenticación

const api = axios.create({
    baseURL: 'http://localhost:4200/api/v1/auth',  // Cambiar la URL según tu configuración
    headers: {
        'Content-Type': 'application/json',
    },
});

const authService = {
    // Método para registrarse
    signUp(userData) {
        return api.post('/sign_up', userData);
    },

    // Método para iniciar sesión
    signIn(credentials) {
        return api.post('/sign_in', credentials);
    },

    // Método para refrescar el token
    refreshToken(refreshToken) {
        return api.post('/refresh-token', { refreshToken });
    },

    // Método para solicitar el link de restablecimiento de contraseña
    requestPasswordReset(email) {
        return api.post(`/password-reset-request?email=${email}`);
    },

    // Método para restablecer la contraseña
    resetPassword(verificationCode, newPassword) {
        return api.post('/password-reset', { verificationCode, newPassword });
    },

    // Método para cerrar sesión
    logout(token) {
        return api.post('/logout', {}, { headers: { Authorization: `Bearer ${token}` } });
    },
};

export default authService;
