<template>
    <div class="max-w-[600px] min-w-[375px] mx-auto mt-6 p-8 rounded-xl shadow-lg bg-white">

        <!-- Alerta -->
        <Alert 
            v-if="showAlert" 
            :intent="alertType" 
            :title="alertTitle" 
            :onDismiss="dismiss"
        >
            {{ alertMessage }}
        </Alert>

        <h1 class="font-bold text-center mt-2 mb-8 text-3xl">Login</h1>
        <form @submit.prevent="login" class="w-full flex flex-col gap-4">
            <div>
                <label for="username" class="block text-sm font-medium text-gray-700">Username</label>
                <input id="username" type="text" v-model="credentials.username"
                    class="w-full p-3 mt-2 border text-gray-700 border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:outline-none focus:ring-blue-500"
                    placeholder="Enter your username" />

            </div>
            <div class="relative">
                <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
                <input id="password" :type="isPasswordVisible ? 'text' : 'password'" v-model="credentials.password"
                    class="w-full p-3 mt-2 border text-gray-700 border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:outline-none focus:ring-blue-500"
                    placeholder="Enter your password" />
                <button type="button" @click="togglePasswordVisibility" class="btn-icon">

                    <EyeIcon v-if="!isPasswordVisible" class="h-5 w-5" />
                    <EyeSlashIcon v-if="isPasswordVisible" class="h-5 w-5" />
                </button>

            </div>

            <div class="w-full text-xs justify-between flex text-left  account">
                <router-link to="/auth/forgot-password" class="text-blue-500 hover:text-blue-700 hover:underline">Forgot
                    your password?</router-link>
                <span class="flex flex-col self-end">You don't have Account? <router-link to="register"
                        class="text-right text-blue-500 hover:text-blue-700 hover:underline">Create one
                        now</router-link></span>

            </div>

            <button
                class=" font-semibold text-white bg-blue-500 py-2 px-4 mx-auto mt-12 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-400 disabled:bg-blue-300 disabled:cursor-not-allowed"
                type="submit">
                <span v-if="loading" class="flex items-center justify-center">
                    <ArrowPathIcon class="animate-spin h-5 w-5 mr-2 text-white"/>
                    Enviando..
                </span>
                <span v-else>
                    Login
                </span>
            </button>
        </form>
    </div>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "../stores/authStore";
import { authService } from "../services/auth/authService";
import { EyeIcon, EyeSlashIcon, ArrowPathIcon } from "@heroicons/vue/24/solid";
import { useRouter } from "vue-router";
import Alert from "./notifications/Alert.vue";


const router = useRouter();

const authStore = useAuthStore();
const credentials = ref({
    username: "",
    password: "",
});

// Alerta
const showAlert = ref(false);
const alertType = ref("info");
const alertTitle = ref("");
const alertMessage = ref("");
const loading =ref(false);

//funcion emit de al funcion emit dissmiss del componeten Alert para ocultar el alert
const dismiss = () =>{
    showAlert.value = false;
}

//variable reactiva para el controlar si la contrraseña es visible
const isPasswordVisible = ref(false);

//funcion apra alternar la visibilidad de lña contrtaseñla
const togglePasswordVisibility = () => {
    isPasswordVisible.value = !isPasswordVisible.value;
}



const login = async () => {
    loading.value = true;
    showAlert.value = false;

    try {
        const response = await authService.signIn(credentials.value);
        authStore.setUser(response.data.user);
        authStore.setAccessToken(response.data.token);

        alertTitle.value = "Inicio de sesión completado";
        alertMessage.value = response.message || "Haz inciado sesion éxitosamente";
        alertType.value = "success";
        showAlert.value = true;
        // Redirigir a la página principal o dashboard


        setTimeout( () => {
            router.push("/cPanel");
        }, 7000);

    } catch (error) {
        console.error("Error en login:", error);
        console.log("Informacion del error: ", error.message);
        alertTitle.value = "¡Ups!, ha Ocurrido un error";
        alertMessage.value = 
        error.response?.data?.message || "Hubo un problema al iniciar sesión";
        alertType.value = "danger";
        showAlert.value = true;
    }
    finally {
        loading.value=false;
        credentials.username.value ="";
        credentials.password.value = "";
    }
};
</script>

<style scoped>
.btn-icon {
    position: absolute;
    top: 45%;
    right: .5rem;
    transform: translateY(50%);
}

.account {
    font-family: 'Roboto', sans-serif;
}
</style>
