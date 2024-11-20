<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-100">
        <div class="w-full max-w-sm p-8 bg-white shadow-lg rounded-lg">
        <slot></slot>
        <!-- Aquí se renderizan las vistas de Login, Register, etc. -->
        </div>
    </div>
</template>

<script setup>
// Este es un layout simple sin barra de navegación ni footer
</script>

<style scoped>
/* Estilos para centrar el formulario de login, etc */
</style>
