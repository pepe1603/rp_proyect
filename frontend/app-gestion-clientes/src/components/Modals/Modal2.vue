<template>
    <teleport to="body">
        <!-- Overlay -->
        <transition name="fade">
            <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 z-40" @click.self="close"></div>
        </transition>

        <!-- Modal Content -->
        <transition name="slide-up">
            <div v-if="visible"
                class="fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white rounded-lg shadow-xl z-50 w-full max-w-lg p-6 space-y-4">
                <!-- Close Button -->
                <button @click="close"
                    class="absolute top-4 right-4 text-gray-400 hover:text-gray-700 transition-colors">
                    ✖
                </button>

                <!-- Slot for Modal Content -->
                <slot />
            </div>
        </transition>
    </teleport>
</template>

<script setup>
import { ref, watch } from "vue";
import { useEventListener } from "@vueuse/core";

const props = defineProps({
    modelValue: {
        type: Boolean,
        required: true,
    },
});

const emit = defineEmits(["update:modelValue"]);

const visible = ref(props.modelValue);

watch(
    () => props.modelValue,
    (newValue) => {
        visible.value = newValue;
    }
);

const close = () => {
    emit("update:modelValue", false);
};

// Close modal on Escape key press
useEventListener("keydown", (event) => {
    if (event.key === "Escape" && visible.value) {
        close();
    }
});
</script>

<style>
/* Fade Animation */
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}

/* Slide-Up Animation */
.slide-up-enter-active,
.slide-up-leave-active {
    transition: transform 0.3s, opacity 0.3s;
}

.slide-up-enter-from {
    transform: translateY(20px);
    opacity: 0;
}

.slide-up-leave-to {
    transform: translateY(20px);
    opacity: 0;
}
</style>