<template>
        <nav class="nav-dinamic w-full h-16 sticky top-0 z-50">
            <div class="h-full flex items-center justify-between px-5 ">
                <div class="logo flex-shrink-0  text-white flex items-center gap-1">
                    <BuildingOffice2Icon class="h-6 w-6 sm:h-8 sm:w-8 text-gray-400 hover:text-white" />
                    <span class="font-normal italic text-sm sm:text-md text-slate-300 hover:text-white"> Buenrostro & Asociados</span>
                </div>
                <ul class="flex  text-lg font-normal px-2">
                    <li class=" p-1">
                        <router-link to="/home" 
                            class="text-gray-100 hover:text-teal-300 hover:border-b-2 border-teal-300 px-5 py-1.5 " 
                            :class="{'border-b-2 border-teal-300 text-teal-300': isActive('/home')}">
                            Inicio
                        </router-link>
                    </li>
                    <li class=" p-1">
                        <router-link to="/services"
                            class="text-gray-100 hover:text-teal-300 hover:border-b-2 border-teal-300 px-5 py-1.5"
                            :class="{'border-b-2 border-teal-300 text-teal-300': isActive('/services')}" >
                            Servicios
                        </router-link>
                    </li>
                    <li class=" p-1">
                        <router-link 
                            to="/about"
                            class="text-gray-100  hover:text-teal-300 hover:border-b-2 border-teal-300 px-5 py-1.5" 
                            :class="{'border-b-2 border-teal-300 text-teal-300': isActive('/about')}">
                            about
                        </router-link>
                    </li>
                </ul>
                    <div v-if="!authStore.isAuthenticated">
                    <router-link to="auth/login" class="bg-blue-700 text-white px-4 py-2 rounded-md hover:bg-blue-800">
                        Login
                    </router-link>
                </div>
            </div>
        </nav>
</template>

<script setup>
import { BuildingOffice2Icon } from "@heroicons/vue/24/solid";
import { useRoute } from 'vue-router';
import { useAuthStore } from "@/stores/AuthStore";

const route = useRoute();
const authStore = useAuthStore();

// Función para determinar si la ruta actual es la activa
const isActive = (path) => {
    return route.path === path;
};


</script>

<style scoped>
.nav-dinamic{
    background-color: rgba(0, 0, 0, 0.4);
    font-family: 'Poppins', sans-serif;
    font-weight: 500;
}



</style>