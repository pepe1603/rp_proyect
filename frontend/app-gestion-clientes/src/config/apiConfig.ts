// services/api.js
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/v1',  // Cambia esta URL por la base de tu API
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor para añadir el token de autenticación en cada solicitud
api.interceptors.request.use(config => {
    const token = localStorage.getItem('authToken');  // Suponemos que el token se guarda en localStorage
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

export default api;
