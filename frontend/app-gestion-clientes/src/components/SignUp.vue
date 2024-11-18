<template>
    <div>
        <form @submit.prevent="signUp">
            <div>
                <label for="username">Username</label>
                <input type="text" v-model="userData.username" />
            </div>
            <div>
                <label for="password">Password</label>
                <input type="password" v-model="userData.password" />
            </div>
            <div>
                <label for="email">Email</label>
                <input type="email" v-model="userData.email" />
            </div>
            <button type="submit">Register</button>
        </form>
    </div>
</template>

<script setup>
import { ref } from "vue";
import authService from "@/services/AuthService";

const userData = ref({
    username: "",
    password: "",
    email: "",
});

const signUp = async () => {
    try {
        const response = await authService.signUp(userData.value);
        // Redirigir a la página de login después de un registro exitoso
    } catch (error) {
        console.error("Error en sign up:", error);
    }
};
</script>
