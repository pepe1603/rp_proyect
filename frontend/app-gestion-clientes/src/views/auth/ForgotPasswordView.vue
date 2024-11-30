<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="bg-white shadow-lg rounded-lg p-6 w-full max-w-md">
      <h1 class="text-2xl font-bold text-gray-800 text-center mb-4">
        Recupera tu contraseña
      </h1>

      <p class="text-gray-600 text-center mb-10">
        Introduce tu correo electrónico para recibir un enlace de restablecimiento.
      </p>

      <!-- Alerta de éxito o error -->
      <Alert
        v-if="alertMessage"
        :title="alertTitle"
        :intent="alertType"
        :show="showAlert"
        :onDismiss="dismiss"
      >{{ alertMessage }}</Alert>

      <!-- Formulario -->
      <form @submit.prevent="handleRequestPasswordReset" class="space-y-8 text-center">
        <div>
          <label for="email" class="block text-sm text-left font-medium text-gray-700">
            Correo Electrónico
          </label>
          <div class="mt-1">
            <input
              v-model="email"
              type="email"
              id="email"
              class="w-full border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 p-3 mt-2 text-gray-700"
              placeholder="ejemplo@correo.com"
            />
          </div>
        </div>
        <button
          type="submit"
          :disabled="loading"
          class="mx-auto mt-0 bg-blue-500 text-white font-semibold py-2 px-4 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-400 disabled:bg-blue-300 disabled:cursor-not-allowed"
        >
          <span v-if="loading" class="flex items-center justify-center">
            <ArrowPathIcon class="animate-spin h-5 w-5 mr-2 text-white " />
            Enviando...
          </span>
          <span v-else>Enviar Enlace</span>
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { authService } from "@/services/auth/authService";
import Alert from "@/components/notifications/Alert.vue";
import { ArrowPathIcon } from "@heroicons/vue/24/solid";
import { useRouter } from "vue-router";

// Estados
const email = ref("");
const loading = ref(false);
const alertTitle = ref("");
const alertMessage = ref(null);
const alertType = ref("info"); // "success", "danger", etc.
const showAlert = ref(false);
const router = useRouter();

// Rebibir emitr de la funcion Dissmiss del componente Alerta para ocultar el alerta
const dismiss = () => {
  showAlert.value = false;
};

// Función para manejar la solicitud
const handleRequestPasswordReset = async () => {
  loading.value = true;
  showAlert.value = false;

  try {
    await authService.requestPasswordReset(email.value);
    alertTitle.value = "Codigo de Verificacion Enviado"
    alertMessage.value = "¡Codigo de verificacion se enviado con éxito! Revisa tu correo.";
    alertType.value = "success";
    showAlert.value = true;
    //redirigri a al apgina de restablecimiento
    setTimeout( () => {
      router.push('reset-password');
    }, 7000);//esperar un timepo antes de redireccionar

  } catch (error) {
    alertMessage.value =
      error.response?.data?.message ||
      "Error al enviar el Codigo de Verificacion. Intenta de nuevo.";
    alertTitle.value = "¡Ups!, Ocurrio un Error";
    alertType.value = "danger";

    showAlert.value = true;

  } finally {
    loading.value = false;
    email.value = "";
  }
};
</script>
