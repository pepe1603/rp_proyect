// services/PublicInfoService.js
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:4200/api/v1/public',  // Cambiar la URL según tu configuración
    headers: {
        'Content-Type': 'application/json',
    },
});

const publicInfoService = {
    getPublicInfo() {
        return api.get('/info');
    },
};

export default publicInfoService;
