<template>
    <div class="min-h-screen bg-gray-100 grid items-center p-6">
        <!-- Alerta -->
        <Alert v-if="showAlert" :intent="alertType" :title="alertTitle" :onDismiss="dismiss">
            {{ alertMessage }}
        </Alert>

        <!-- Bienvenida -->
        <div class="text-center mb-4">
            <h1 class="text-3xl font-semibold text-teal-600">¡Bienvenido de nuevo!</h1>
            <p class="mt-3 text-gray-700">
                Inicia sesión para continuar con tu experiencia personalizada. Si no
                tienes cuenta, puedes crear una fácilmente.
            </p>
        </div>

        <!-- Formulario de Login -->
        <div class="max-w-[600px] min-w-[375px] mx-auto mt-6 p-8 rounded-xl shadow-lg bg-white">
            <h1 class="font-bold text-center mt-2 mb-8 text-3xl">Login</h1>
            <form @submit.prevent="login" class="w-full flex flex-col gap-4">
                <div>
                    <label for="username" class="block text-sm font-medium text-gray-700">Username</label>
                    <input id="username"
                    type="text" 
                    v-model="credentials.username"
                        
                    class="w-full p-3 mt-2 border text-gray-700 border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:outline-none focus:ring-blue-500"
                        
                    placeholder="Enter your username" 
                    required
                    />
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

                <div class="w-full text-xs justify-between flex text-left account">
                    <router-link to="/auth/forgot-password"
                        class="text-blue-500 hover:text-blue-700 hover:underline">Forgot your password?</router-link>
                    <span class="flex flex-col self-end">You don't have Account?
                        <router-link to="register"
                            class="text-right text-blue-500 hover:text-blue-700 hover:underline">Create one
                            now</router-link></span>
                </div>

                <button
                    class="font-semibold text-white bg-indigo-500 py-2 px-4 mx-auto mt-12 rounded-md hover:bg-indigo-600 focus:ring-2 focus:ring-indigo-400 disabled:bg-indigo-300 disabled:cursor-not-allowed"
                    type="submit">
                    <span v-if="loading" class="flex items-center justify-center">
                        <ArrowPathIcon class="animate-spin h-5 w-5 mr-2 text-white" />
                        Enviando..
                    </span>
                    <span v-else>Login</span>
                </button>
            </form>
        </div>

        <!-- Modal -->
        <Modal2 v-model="showModal">
            <div>
                <h2 class="text-2xl font-bold mb-4 text-gray-800">
                    🎉 ¡Bienvenido a la Plataforma!
                </h2>
                <p class="text-gray-600 mb-6">
                    Gracias por iniciar sesión. Este proyecto es una herramienta que
                    desarrollé para <strong>optimizar procesos en la empresa</strong>. Aquí
                    podrás gestionar información de manera eficiente y segura.
                </p>
                <button @click="showModal = false"
                    class="bg-indigo-500 text-white font-semibold py-2 px-4 rounded-md hover:bg-indigo-600">
                    ¡Entendido!
                </button>
            </div>
        </Modal2>
    </div>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "../../stores/authStore";
import { authService } from "../../services/auth/authService";
import { EyeIcon, EyeSlashIcon, ArrowPathIcon } from "@heroicons/vue/24/solid";
import { useRouter } from "vue-router";
import Alert from "../../components/notifications/Alert.vue";
import Modal2 from "@/components/Modals/Modal2.vue";

const router = useRouter();
const showModal = ref(false);

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
const loading = ref(false);

const dismiss = () => {
    showAlert.value = false;
};

// Contraseña visible
const isPasswordVisible = ref(false);
const togglePasswordVisibility = () => {
    isPasswordVisible.value = !isPasswordVisible.value;
};

const login = async () => {
    loading.value = true;
    showAlert.value = false;

    try {
        const response = await authService.signIn(credentials.value);
        authStore.setUser(response.data.user);
        authStore.setAccessToken(response.data.token);

        alertTitle.value = "Inicio de sesión completado";
        alertMessage.value = "¡Bienvenido de nuevo!";
        alertType.value = "success";
        showAlert.value = true;

        // Mostrar el modal antes de redirigir
        showModal.value = true;

        setTimeout(() => {
            router.push("/cPanel");
        }, 6000);
    } catch (error) {
        alertTitle.value = "¡Ups! Hubo un problema";
        alertMessage.value =
            error.response?.data?.message || "Ocurrió un error inesperado.";
        alertType.value = "danger";
        showAlert.value = true;
        // Mostrar el modal antes de redirigir
        showModal.value = true;
    } finally {
        loading.value = false;
        credentials.value.username = "";
        credentials.value.password = "";
    }
};
</script>

<style scoped>
.btn-icon {
    position: absolute;
    top: 70%;
    right: 0.5rem;
    transform: translateY(-50%);
}

.account {
    font-family: "Roboto", sans-serif;
}
</style>