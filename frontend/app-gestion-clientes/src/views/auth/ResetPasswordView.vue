<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="bg-white min-w-[575px] shadow-lg rounded-lg p-6 w-full max-w-md">
      <!-- Título -->
      <h1 class="text-2xl font-bold text-gray-800 text-center mb-4">
        Restablecimiento de Contraseña
      </h1>

      <!-- Alerta -->
      <Alert
        v-if="alertVisible"
        :intent="alertIntent"
        :title="alertTitle"
        @dismiss="alertVisible = false"
      >
      {{ alertMessage }}
      </Alert>

      <template v-if="!isCodeVerified">
        <!-- Descripción -->
        <p class="text-gray-600 text-center mb-24">
          Introduce el código de verificación que hemos enviado a tu correo electrónico.
        </p>

        <!-- Grupo de campos con etiqueta flotante -->
        <div class="relative w-full flex justify-center gap-2">
          <!-- Etiqueta flotante -->
          <label
            for="verification-code"
            class="absolute -top-12 left-1/2 transform font-semibold text-lg -translate-x-1/2 text-gray-600 bg-white px-2"
          >
            Código de Verificación
          </label>

          <!-- Inputs para cada carácter -->
          <input
            v-for="(digit, index) in codeArray"
            :key="index"
            v-model="codeArray[index]"
            type="text"
            maxlength="1"
            id="verification-code"
            class="w-12 h-12 text-center border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 text-gray-700 text-lg"
            @input="handleInput(index)"
          />
        </div>
      </template>

      <template v-else>
        <div class="w-5/6 mx-auto">
          <!-- Formulario para nueva contraseña -->
          <p class="text-gray-600 text-center mb-8">
            Introduce una nueva contraseña para tu cuenta.
          </p>
          <form @submit.prevent="handlePasswordReset">
            <div class="space-y-4">
              <div>
                <label for="new-password" class="block text-sm font-medium text-gray-700">
                  Nueva Contraseña
                </label>
                <input
                  v-model="newPassword"
                  type="password"
                  id="new-password"
                  class="w-full border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 p-3 mt-2"
                  placeholder="••••••••"
                  required
                />
                <p class="text-xs text-gray-500 mt-1">
                  Usa al menos 8 caracteres, incluyendo mayúsculas, números y símbolos.
                </p>
              </div>
              <div>
                <label for="confirm-password" class="block text-sm font-medium text-gray-700">
                  Confirmar Contraseña
                </label>
                <input
                  v-model="confirmPassword"
                  type="password"
                  id="confirm-password"
                  class="w-full border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 p-3 mt-2"
                  placeholder="••••••••"
                  required
                />
              </div>
            </div>

            <!-- Mensaje de error si las contraseñas no coinciden -->
            <p v-if="passwordMismatch" class="text-sm text-red-500 mt-2">
              Las contraseñas no coinciden. Intenta de nuevo.
            </p>
                      <!-- Mensaje de error si la contraseña es demasiado corta -->
            <p v-if="passwordTooShort" class="text-sm text-yellow-500 mt-2">
              La contraseña debe tener al menos 8 caracteres.
            </p>

            <!-- Botón para restablecer contraseña -->
            <button
              type="submit"
              class="mt-6 bg-blue-500 text-white font-semibold py-2 px-4 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-400 disabled:bg-blue-300 disabled:cursor-not-allowed"
            >
              Reset Password
            </button>
          </form>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from "vue";
import Alert from "../../components/notifications/Alert.vue";
import { authService } from "../../services/auth/authService";  // Importando el servicio
import { useRouter } from "vue-router";

// Código de verificación
const codeArray = ref(new Array(6).fill(""));
const isCodeVerified = ref(false);
const newPassword = ref("");
const confirmPassword = ref("");

// Estados para la alerta
const alertVisible = ref(false);
const alertIntent = ref("info");
const alertTitle = ref("");
const alertMessage = ref("");

//uso del router para redirigir
const router = useRouter();

// Verifica si todos los campos del código están llenos
const isCodeComplete = computed(() => codeArray.value.every((digit) => digit.trim() !== ""));

// Verifica si las contraseñas coinciden
const passwordMismatch = computed(() => newPassword.value !== confirmPassword.value);

//verifica que la cont5raseña tenga almenos 78 caracteres
const passwordTooShort = computed( () => newPassword.value.length < 8 );

// Detecta automáticamente cuando el código está completo
watch(isCodeComplete, (complete) => {
  if (complete) {
    isCodeVerified.value = true;
    alertTitle.value = "Código Verificado";
    alertMessage.value = "El código se ha verificado correctamente.";
    alertIntent.value = "success";
    alertVisible.value = true;
  }
});

// Manejar la entrada y avanzar al siguiente campo
const handleInput = (index) => {
  if (codeArray.value[index].length === 1 && index < codeArray.value.length - 1) {
    const nextInput = document.querySelectorAll("input")[index + 1];
    nextInput.focus();
  }
};

// Manejar el restablecimiento de contraseña
const handlePasswordReset = async () => {
  if (passwordMismatch.value) {
    alertTitle.value = "Error";
    alertMessage.value = "Las contraseñas no coinciden. Intenta nuevamente.";
    alertIntent.value = "danger";
    alertVisible.value = true;
    return;
  }

  if (passwordTooShort.value){
    alertTitle.value = "Password is Short";
    alertMessage.value = "La contraseña debe ttener al menos 8 caracteres.";
    alertIntent.value = "danger";
    alertVisible.value = true;
    return;
  }

  try {
    const response = await authService.resetPassword(codeArray.value.join(""), newPassword.value);
    alertTitle.value = "Éxito";
    alertMessage.value = response.message || "Contraseña restablecida con éxito.";
    alertIntent.value = "success";
    alertVisible.value = true;
    
    // Redirigir al login después de mostrar el mensaje
    setTimeout(() => {
      router.push("login");  // O usa vue-router si es el caso
    }, 3000);
    
  } catch (error) {
    alertTitle.value = "Error";
    alertMessage.value = error.response?.data?.message || "Hubo un problema al restablecer la contraseña. Intentalo de nuevo";
    alertIntent.value = "danger";
    alertVisible.value = true;

        // Redirigir al login después de mostrar el mensaje
        setTimeout(() => {
      router.push("forgot-password");  // O usa vue-router si es el caso
    }, 6000);
  }
  finally{
    newPassword.value = "";
    confirmPassword.value = "";
  }
};
</script>
