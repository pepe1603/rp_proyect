<template>
        <nav class="bg-blue-600 p-3 shadow-md">
            <div class="max-w-screen-xl mx-auto flex items-center justify-between">
                <div class="text-white text-lg font-semibold">
                    Logo Company
                </div>
                <ul class="w-full flex list-none">
                    <li class="mr-5">
                        <router-link class="px-4 py-2 bg-teal-500 hover:bg-teal-700 text-white rounded-md" to="/">Inicio</router-link>
                    </li>
                    <li class="mr-5">
                        <router-link class="px-4 py-2 bg-teal-500 hover:bg-teal-700 text-white rounded-md" to="/services">Servicios</router-link>
                    </li>
                    <li class="mr-5">
                        <router-link class="px-4 py-2 bg-rosa-500 hover:bg-rosa-700 text-white rounded-md" to="/login">Login</router-link>
                    </li>
                </ul>
                <div>
                    <button class="bg-blue-700 text-white px-4 py-2 rounded-md hover:bg-blue-800">
                        Login
                    </button>
                </div>
            </div>
        </nav>
</template>

<script setup>


</script>

<style>

</style>