<template>
    <div class="flex h-screen bg-gray-100">
        <!-- Sidebar -->
        <Sidebar  />
        
        <div class="flex-1 flex flex-col">
            <!-- Main Section: donde se renderizan las vistas dinámicas -->
            <main class="flex-1 p-6 overflow-y-auto bg-gray-50">
                <router-view />
            </main>
            <!-- Footer -->
            <Footer />
        </div>
    </div>
</template>

<script lang="ts" setup>
import Footer from "../components/Footer.vue";
import Sidebar from "../components/Sidebar.vue";

</script>

<style>

</style>