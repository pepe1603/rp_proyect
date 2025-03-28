<template>
    <div class="w-64 bg-teal-600 text-white flex flex-col">
        <!-- Logo / Título -->
        <div class="p-6 text-2xl text-center font-bold">
        <router-link to="/cPanel">
            <span>Dashboard</span>
        </router-link>
        </div>

        <!-- Menú -->
        <nav class="flex-1 px-2 py-4">
        <ul>
            <li>
            <router-link to="dashboard" class="block px-4 py-2 text-sm hover:bg-gray-700 rounded-lg">
                Dashboard
            </router-link>
            </li>
            <li>
            <router-link to="users" class="block px-4 py-2 text-sm hover:bg-gray-700 rounded-lg">
                Usuarios
            </router-link>
            </li>
            <li>
            <router-link to="settings" class="block px-4 py-2 text-sm hover:bg-gray-700 rounded-lg">
                Configuración
            </router-link>
            </li>
            <!-- Más enlaces si es necesario -->
        </ul>
        </nav>
    </div>
</template>

<script lang="ts" setup></script>

<style></style>
