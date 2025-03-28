<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="bg-white min-w-[535px] shadow-lg rounded-lg p-6 w-full max-w-md">
      <!-- Título -->
      <h1 class="text-2xl font-bold text-gray-800 text-center mb-6">Registro</h1>

      <!-- Alerta -->
      <Alert
        v-if="showAlert"
        :intent="alertType"
        :title="alertTitle"
        :show="showAlert"
        :onDismiss="dismiss"
      >
        {{ alertMessage }}
      </Alert>

      <form @submit.prevent="handleRegister" class="mx-auto px-8 mt-6 text-center">
        <!-- Username -->
        <div class="mb-4 text-left">
          <label for="username" class="block text-sm font-medium text-gray-700">
            Nombre de Usuario
          </label>
          <input
            v-model="formData.username"
            type="text"
            id="username"
            class="w-full border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 p-3 mt-2"
            placeholder="Nombre de usuario"
            required
          />
        </div>

        <!-- Email -->
        <div class="mb-4 text-left">
          <label for="email" class="block text-sm font-medium text-gray-700">
            Correo Electrónico
          </label>
          <input
            v-model="formData.email"
            type="email"
            id="email"
            class="w-full border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 p-3 mt-2"
            placeholder="correo@ejemplo.com"
            
          />
          <p class="text-xs text-left text-blue-500">Este campo es opcional</p>
        </div>

        <!-- Password -->
        <div class="relative">
                    <label for="password" class="block text-left text-sm font-medium text-gray-700">Password</label>
                    <input id="password"
                    :type="isPasswordVisible ? 'text' : 'password'"
                    v-model="formData.password"
                        class="w-full p-3 my-2  mb-2 border text-gray-700 border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:outline-none focus:ring-blue-500"
                        placeholder="Enter your password" />
                    <button type="button" @click="togglePasswordVisibility" class="btn-icon">
                        <EyeIcon v-if="!isPasswordVisible" class="h-5 w-5" />
                        <EyeSlashIcon v-if="isPasswordVisible" class="h-5 w-5" />
                    </button>
                </div>

        <!-- Selección de rol -->
        <div class="mb-4 text-left">
          <label class="block text-sm font-medium text-gray-700">Rol</label>
          <div class="flex items-center space-x-4 mt-2">
            <label class="inline-flex items-center">
              <input
                type="radio"
                value="cliente"
                v-model="selectedRole"
                class="form-radio text-blue-600"
              />
              <span class="ml-2">Cliente</span>
            </label>
            <label class="inline-flex items-center">
              <input
                type="radio"
                value="administrador"
                v-model="selectedRole"
                class="form-radio text-blue-600"
              />
              <span class="ml-2">Administrador</span>
            </label>
          </div>
        </div>

        <!-- RFC o Clave Admin según el rol -->
        <div v-if="selectedRole === 'cliente'" class="my-4">
          <label for="rfc" class="block text-left text-sm font-medium text-gray-700">
            RFC (Cliente)
          </label>
          <input
            v-model="formData.rfc"
            type="text"
            id="rfc"
            class="w-full border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 p-3 mt-2"
            placeholder="RFC del cliente"
            required
          />
        </div>
        <div v-if="selectedRole === 'administrador'" class="my-4">
          <label for="claveAdmin" class="block text-left text-sm font-medium text-gray-700">
            Clave Administrador
          </label>
          <input
            v-model="formData.claveAdmin"
            type="text"
            id="claveAdmin"
            class="w-full border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 p-3 mt-2"
            placeholder="Clave de administrador"
            required
          />
        </div>

        <!-- Botón de Registro -->
        <button
          type="submit"
          :disabled="loading"
          class=" bg-blue-500 text-white font-semibold py-2 px-4 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-400 disabled:bg-blue-300 disabled:cursor-not-allowed"
        >
        <span v-if="loading" class="flex items-center justify-center">
            <ArrowPathIcon class="animate-spin h-5 w-5 mr-2 text-white" /> 
            Enviando...
          </span>
          <span v-else>
            Registrarse
          </span>
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Alert from "../../components/notifications/Alert.vue";
import { authService } from "../../services/auth/authService";
import { ArrowPathIcon, EyeIcon, EyeSlashIcon } from "@heroicons/vue/24/solid";
import { useRouter } from "vue-router";

const router = useRouter();

const formData = ref({
  username: "",
  email: "",
  password: "",
  rfc: "",
  claveAdmin: "",
});


// Contraseña visible
const isPasswordVisible = ref(false);
const togglePasswordVisibility = () => {
    isPasswordVisible.value = !isPasswordVisible.value;
};


const selectedRole = ref(null);

// Alerta
const showAlert = ref(false);
const alertType = ref("info");
const alertTitle = ref("");
const alertMessage = ref("");
const loading = ref(false);

//Recibir emitr de la funcion Dissmiss del componete Alerta para ocutar el alerta
const dismiss = () => {
  showAlert.value = false;
}

// Manejar el registro
const handleRegister = async () => {

  loading.value = true;
  showAlert.value = false;

  try {
    const payload = {
      username: formData.value.username,
      email: formData.value.email,
      password: formData.value.password,
      rfc: selectedRole.value === "alumno" ? formData.value.rfc : null,
      claveAdmin: selectedRole.value === "administrador" ? formData.value.claveAdmin : null,
    };

    if(selectedRole.value == null ){
      alertTitle.value = "¡Advertencia!";
      alertMessage.value = "Para continuar con el registro, debes seleccionar un rol.";
      alertType.value= "warning";
      showAlert.value = true;

      return;
    }

    const response = await authService.signUp(payload);
    alertTitle.value = "Registro completado";
    alertMessage.value = response.message || "Te haz registrado exitosamente";
    alertType.value = "success";
    showAlert.value = true;

    ///redirigir al login
    setTimeout( () => {
      router.push("login");
    }, 7000  );
  } catch (error) {
    alertTitle.value = "¿Ups!, ha ocurrido un error";
    alertMessage.value =
      error.response?.data?.message || "Hubo un problema al registrar el usuario.";
    alertType.value = "danger";
    showAlert.value = true;
  }
  finally {
    loading.value = false;
    formData.value.username = "";
    formData.value.password = "";
    formData.value.email = "";
    formData.value.claveAdmin = "";
    formData.value.rfc = "";
  }
};
</script>

<style scoped>
.btn-icon {
    position: absolute;
    top: 60%;
    right: 0.5rem;
    transform: translateY(-50%);
}

.account {
    font-family: "Roboto", sans-serif;
}
</style>