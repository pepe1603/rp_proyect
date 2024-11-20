<!-- src/layouts/MainLayout.vue -->
<template>
    <div class="flex flex-col min-h-screen bg-gray-100 border border-red-500">
    <Navbar />
        <main class="flex-1 p-6 bg-green-50">
            <router-view />
            <!-- Aquí se renderizan las páginas -->
        </main>
        <Footer />
    </div>
</template>

<script setup>
import Navbar from "../components/Navbar.vue";
import Footer from "../components/Footer.vue";
</script>
    
<style scoped>
/* Aquí puedes agregar estilos para el layout */

</style>
