<template>
    <div class="space-y-5 p-5">


        <Alert 
        :show="showAlert" 
        :on-dismiss= "() => (showAlert = false)"
        
        title="This is my awesome alert!"
        >
            <p>Some importtant message</p>
        </Alert>

        <Alert intent="success"
        :show="showAlert" 
        :on-dismiss= "() => (showAlert = false)"
        
        title="This is my awesome alert!"
        >
            <p>Some importtant message</p>
        </Alert>

        <Alert intent="warning"
        :show="showAlert" 
        :on-dismiss= "() => (showAlert = false)"
        
        title="This is my awesome alert!"
        >
            <p>Some importtant message</p>
        </Alert>

        <Alert intent="dark"
        :show="showAlert" 
        :on-dismiss= "() => (showAlert = false)"
        
        title="This is my awesome alert!"
        >
            <p>Some importtant message</p>
        </Alert>

        <Alert intent="danger"
        :show="showAlert" 
        :on-dismiss= "() => (showAlert = false)"
        
        title="This is my awesome alert!"
        >
            <p>Some importtant message</p>
        </Alert>

        <Alert intent="brand"
        :show="showAlert" 
        :on-dismiss= "() => (showAlert = false)"
        
        title="This is my awesome alert!"
        >
            <p>Some importtant message</p>
        </Alert>
        


        <div class="container p-5 mx-auto mt-5 border rounded-lg">
            <button class="my-5 border border-green-400 mx-auto bg-green-200 px-4 py-2 rounded font-medium text-green-700  hover:bg-green-100" @click="openModal">show modal</button>

            <br>
            <ModalConfirm :isOpen="showModal" @update:isOpen="showModal = $event" @confirm="onConfirm" />

        </div>


    </div>
</template>

<script setup>
import { ref } from "vue";
import Alert from '../components/notifications/Alert.vue'
import ModalConfirm from '@/components/modals/ModalConfirm.vue'

const showAlert = ref(true);
const showModal = ref(false);

// Método para cerrar las alertas/--- sustiruir por el on-dissmiss
const closeAlert = () => {
  showAlert.value = false;  // Cierra las alertas
}

// Método para abrir el modal
const openModal = () => {
  showModal.value = true;  // Muestra el modal
}

// Método para manejar la confirmación del modal
const onConfirm = () => {
  alert('Confirmed!');
  showModal.value = false; // Cierra el modal al confirmar
}

</script>

<style>

</style>