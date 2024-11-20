import { createRouter, createWebHistory } from 'vue-router';

// Importar páginas
import HomePage from '@/pages/HomePage.vue';
import ServicesPage from '@/pages/ServicesPage.vue';

// Importar layouts
import MainLayout from '@/layouts/MainLayout.vue';
import DashboardLayout from '@/layouts/DashboardLayout.vue';
import LoginPage from '@/pages/LoginPage.vue';
import Register from '@/views/Register.vue';
import ForgotPassword from '@/views/ForgotPassword.vue';
import DashboardHome from '@/pages/DashboardHome.vue';
import UserManagement from '@/pages/UserManagement.vue';
import SettingsPage from '@/pages/SettingsPage.vue';
import NotFoundPage from '@/pages/NotFoundPage.vue';
import AuthLayout from '@/layouts/AuthLayout.vue';
import ErrorLayout from '@/layouts/ErrorLayout.vue'; // Importamos el nuevo layout

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // Rutas públicas (Main Layout)
    {
      path: '/',
      children: [
        {
          path: '',
          name: 'Home',
          component: HomePage,
        },
        {
          path: '/services',
          name: 'Services',
          component: ServicesPage,
        },
        {
          path: '/about',
          name: 'About',
          component: () => import('@/views/AboutView.vue'),
        },
      ],
      meta: { layout: 'MainLayout' }, // Definimos el layout
    },

    // Rutas de autenticación agrupadas bajo '/auth' (Auth Layout)
    {
      path: '/auth',
      children: [
        {
          path: 'login',
          name: 'Login',
          component: LoginPage,
        },
        {
          path: 'register',
          name: 'Register',
          component: Register,
        },
        {
          path: 'forgot-password',
          name: 'ForgotPassword',
          component: ForgotPassword,
        },
      ],
      meta: { layout: 'AuthLayout' }, // Definimos el layout
    },

    // Rutas del Dashboard (Dashboard Layout)
    {
      path: '/dashboard',
      children: [
        {
          path: '',
          name: 'DashboardHome',
          component: DashboardHome,
        },
        {
          path: 'users',
          name: 'UserManagement',
          component: UserManagement,
        },
        {
          path: 'settings',
          name: 'Settings',
          component: SettingsPage,
        },
      ],
      meta: { layout: 'DashboardLayout' }, // Definimos el layout
    },

    // Ruta para páginas no encontradas (usando ErrorLayout)
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: NotFoundPage,
      meta: { layout: 'ErrorLayout' }, // Usamos el layout ErrorLayout
    },
  ],
});

export default router;
