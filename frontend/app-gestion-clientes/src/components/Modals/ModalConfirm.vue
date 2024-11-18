<!-- src/components/modals/ModalConfirm.vue -->
<template>
    <Modal :isOpen="isOpen" @update:isOpen="isOpen = $event" @confirm="handleConfirm">
      <template #default>
        <p>{{ message }}</p>
      </template>
    </Modal>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import Modal from './Modal.vue'
  
  const props = defineProps({
    message: { type: String, required: true },
    isOpen: { type: Boolean, default: false }
  })
  
  const emit = defineEmits(['confirm'])
  
  const handleConfirm = () => {
    emit('confirm')
  }
  </script>
  