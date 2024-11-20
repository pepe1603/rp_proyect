<template>
    <!-- El layout se determina automáticamente con el valor de meta.layout -->
    <component :is="layoutComponent">
        <router-view />
    </component>
</template>

<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import MainLayout from "@/layouts/MainLayout.vue";
import AuthLayout from "@/layouts/AuthLayout.vue";
import DashboardLayout from "@/layouts/DashboardLayout.vue";
import ErrorLayout from "@/layouts/ErrorLayout.vue"; // Importamos el ErrorLayout

// Obtenemos la ruta actual
const route = useRoute();

// Computamos el layout basado en la propiedad meta de la ruta
const layoutComponent = computed(() => {
    const layout = route.meta.layout;

    if (layout === "DashboardLayout") {
        return DashboardLayout;
    } else if (layout === "AuthLayout") {
        return AuthLayout;
    } else if (layout === "ErrorLayout") {
        return ErrorLayout; // Si es la página no encontrada, usamos ErrorLayout
    } else {
        return MainLayout; // Layout predeterminado
    }
});
</script>
