<!-- src/components/modals/Modal.vue -->
<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="close">
    <div class="modal">
        <div class="modal-icon"></div>
      <div class="modal-header">
        <h2>{{ title }}</h2>
        <button class="close-button" @click="close">
          <XMarkIcon class="h-6 w-6 rounded-sm" />
        </button>
      </div>
      <div class="modal-body">
        <slot></slot>
        
      </div>
      <div class="modal-footer">
        <button class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 hover:bg-red-500 text-base font-medium text-white" @click="close">Cancel</button>
        <button @click="confirm">Confirm</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { XMarkIcon } from "@heroicons/vue/24/solid/index.js";

// Propiedades y estado del modal
const props = defineProps({
  title: { type: String, default: "Modal Title" },
  isOpen: { type: Boolean, default: false },
});

const emit = defineEmits(["update:isOpen", "confirm"]);

const close = () => {
  emit("update:isOpen", false);
};

const confirm = () => {
  emit("confirm");
  close();
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 500px;
  width: 100%;
}

.modal-header {
  display: flex;
  justify-content: space-between;
}

.modal-body {
  margin-top: 20px;
}

.modal-footer {
  margin-top: 20px;
  text-align: right;
}
</style>
