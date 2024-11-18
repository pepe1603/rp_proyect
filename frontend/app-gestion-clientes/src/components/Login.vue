<template>
    <div class="container mt-4 ">
        <form @submit.prevent="login" class=" m-2 p-4 bg-slate-00  shadow-lg rounded-md flex flex-col ">
            <div class="mt-2 flex gap-2">
                <label for="username">Username</label>
                <input class="outline outline-1 outline-offset-1" type="text" v-model="credentials.username" />
            </div>
            <div class="mt-2 flex gap-2">
                <label for="password">Password</label>
                <input class="outline outline-1 outline-offset-1" type="password" v-model="credentials.password" />
            </div>
            <button class="mt-4 py-2 px-4 text-gray-100 bg-teal-400 hover:bg-teal-700 rounded-lg" type="submit">Login</button>
        </form>
    </div>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/AuthStore";
import authService from "@/services/AuthService";

const authStore = useAuthStore();
const credentials = ref({
    username: "",
    password: "",
});

const login = async () => {
    try {
        const response = await authService.signIn(credentials.value);
        authStore.setUser(response.data.user);
        authStore.setAccessToken(response.data.token);
        // Redirigir a la página principal o dashboard
    } catch (error) {
        console.error("Error en login:", error);
        console.log("Infromacion del error: ", error.message);
    }
};
</script>
