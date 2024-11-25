// services/api.js
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:42000/api/v1',  // Cambia esta URL por la base de tu API
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor para añadir el token de autenticación en cada solicitud
api.interceptors.request.use(
    (response) => response,
    error => {
        console.error("API Error:", error.response || error.message);
    return Promise.reject(error);
});

export default api;
