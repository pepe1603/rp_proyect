<template>
        <!-- Contenido visible solo en desktop -->
        <div class="hidden md:block">
            <!-- El layout se determina automáticamente con el valor de meta.layout -->
            <component :is="layoutComponent">
                <router-view />
            </component>
        </div>
        
        <!-- Contenido visible solo en móviles (opcional) -->
        <div class="block md:hidden">
            <div class="flex items-center justify-center h-screen bg-gray-100">
                <div class="text-center p-6 bg-white shadow-lg rounded-lg">
                    <h1 class="text-xl font-semibold mb-4">¡Ups! Esta página no está disponible en dispositivos móviles</h1>
                    <p>Por razones de accesibilidad, te recomendamos acceder a esta página desde una computadora de escritorio o laptop.</p>
                </div>
            </div> 
        </div>

</template>

<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import MainLayout from "@/layouts/MainLayout.vue";
import CPanelLayout from '@/layouts/CPanelLayout.vue'
import AuthLayout from "@/layouts/AuthLayout.vue";
import ErrorLayout from "@/layouts/ErrorLayout.vue"; // Importamos el ErrorLayout

// Obtenemos la ruta actual
const route = useRoute();

// Computamos el layout basado en la propiedad meta de la ruta
const layoutComponent = computed(() => {
    const layout = route.meta.layout;

    if (layout === "cPanelLayout") {
        return CPanelLayout;
    } else if (layout === "AuthLayout") {
        return AuthLayout;
    } else if (layout === "ErrorLayout") {
        return ErrorLayout; // Si es la página no encontrada, usamos ErrorLayout
    } else {
        return MainLayout; // Layout predeterminado
    }
});
</script>
