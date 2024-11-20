<template>
    <div
        class="min-h-screen flex flex-col items-center justify-center bg-gray-100"
    >
        <div class="text-center p-4">
        <router-view />
        <!-- Aquí se renderiza el componente NotFoundPage -->
        </div>
    </div>
</template>

<script setup>
// Este layout es muy simple para una página de error 404.
</script>

<style scoped>
/* Estilos específicos para la página de error */
</style>
