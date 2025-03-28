<template>
    <transition
        name="modal"
        enter-active-class="duration-500 ease-out"
        enter-from-class="opacity-0"
        enter-to-class="opacity-100"
        leave-active-class="duration-500 ease-in"
        leave-from-class="opacity-100"
        leave-to-class="opacity-0"
    >
        <div v-if="isOpen" class="modal-overlay fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
            <div class="modal bg-white p-6 rounded-lg w-96">
                <!-- Header Modal -->
                <div class="flex justify-between items-center">
                    <h2 class="text-xl font-semibold text-gray-800">{{ title }}</h2>
                    <button @click="close" class="text-gray-500 hover:text-gray-800">
                        <XMarkIcon class="h-6 w-6"/>
                    </button>
                </div>
        
                <!-- Content Modal -->
                <div class="my-4 text-gray-600">
                    <slot></slot> <!-- Content passed via slot -->
                </div>
        
                <!-- Footer Modal -->
                <div class="flex justify-end space-x-4">
                    <button 
                        @click="close" 
                        class="bg-rose-500 text-white px-4 py-2 rounded-md  hover:bg-rose-600">
                        Cancel
                    </button>
                    <button 
                        @click="confirm" 
                        class="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400">
                        Confirm
                    </button>
                </div>
            </div>
        </div>
    </transition>
</template>

<script setup>
import { ref } from 'vue'
import { XMarkIcon } from '@heroicons/vue/24/solid'

// Props para manejar el estado y los datos del modal
const props = defineProps({
    title: {
        type: String,
        default: 'Confirm Action'
    },
    isOpen: {
        type: Boolean,
        default: false
    },
    onDismiss: {
        type: Function,
        default: () => {}
    }
})

// Emitir eventos para controlar el estado del modal
const emit = defineEmits(['update:isOpen', 'confirm'])

const close = () => {
    emit('update:isOpen', false) // Cierra el modal al cambiar isOpen a false
    if (props.onDismiss) {
        props.onDismiss() // Llama al callback onDismiss si está definido
    }
}

const confirm = () => {
    emit('confirm') // Emite el evento 'confirm' cuando se presiona el botón Confirmar
    close() // Cierra el modal
}
</script>

<style scoped>
/* Animación de fade */
.fade-enter-active, .fade-leave-active {
    transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active en versiones anteriores de Vue */ {
    opacity: 0;
}
</style>
