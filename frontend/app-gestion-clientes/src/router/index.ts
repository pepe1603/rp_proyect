import { createRouter, createWebHistory } from 'vue-router';

// Importar páginas
import HomePage from '@/pages/publico/HomePage.vue';
import ServicesPage from '@/pages/publico/ServicesPage.vue';
import UserManagement from '@/pages/cPanel/UserManagement.vue';
import SettingsPage from '@/pages/cPanel/SettingsPage.vue';
import NotFoundPage from '@/pages/NotFoundPage.vue';
import DashboardPage from '@/pages/cPanel/DashboardPage.vue';
// Importar layouts
//se imprtan automaticamente en la etiqueta meta
//impoirtar vistas
import Register from '@/views/auth/Register.vue';
import ForgotPassword from '@/views/auth/ForgotPassword.vue';
import Login from '@/views/auth/Login.vue';
import Welcome from '@/pages/cPanel/Welcome.vue';
import WelcomePage from '@/views/publico/WelcomePage.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // Rutas públicas (Main Layout)
    {
      path: '/',
      children: [
        {
          path: '',
          name: 'Welcome',
          component: WelcomePage,
        },
        {
          path: 'home',
          name: 'Home',
          component: HomePage,
        },
        {
          path: 'services',
          name: 'Services',
          component: ServicesPage,
        },
        {
          path: 'about',
          name: 'About',
          component: () => import('@/views/publico/AboutView.vue'),
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
          component: Login,
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
      path: '/cPanel',
      children: [
        {
          path: '',
          name: 'welcome',
          component: Welcome
        },
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: DashboardPage,
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
      meta: { layout: 'cPanelLayout' }, // Definimos el layout
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
