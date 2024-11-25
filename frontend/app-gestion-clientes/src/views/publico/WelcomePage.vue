<template>
    <div
        class="welcome-page relative p-5 h-screen bg-cover bg-center bg-no-repeat"
    >
        <!-- Contenido de la página -->

        <div
        class="flex flex-col items-center justify-center w-full h-full text-center text-white"
        >
        <h1 class="text-4xl font-bold mb-4 ">Bienvenido a la Página de Inicio</h1>
        <p class="text-lg mb-6">Explora nuestros servicios y mucho más.</p>
        <router-link to="services"
            class="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition duration-300"
        >
            Comenzar
        </router-link>
        </div>
    </div>
</template>

<script lang="ts" setup>

</script>

<style scope>
.welcome-page{
    background-image: url('../../assets/imgs/img-1.webp');
}

</style>