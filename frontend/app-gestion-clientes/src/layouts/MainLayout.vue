<template>
    <div class="flex flex-col min-h-screen bg-gray-100">
        <!-- Barra de navegación -->
        <Navbar />

        <!-- Contenido principal de la página (donde se renderizan las rutas hijas) -->
        <main class="flex-1 -mt-16">
        <router-view class="pt-16" />
        <!-- Aquí se renderizan las páginas hijas -->
        </main>

        <!-- Pie de página -->
        <Footer />
    </div>
</template>

<script setup>
import Navbar from "@/components/Navbar.vue";
import Footer from "@/components/Footer.vue";
</script>

<style scoped>
/* Estilos específicos para el layout principal si es necesario */
</style>
