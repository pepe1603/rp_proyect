<template>
    <transition
        name="fade"
        leave-active-class="duration-700"
        leave-to-class="opacity-0"
        enter-active-class="duration-300 ease-out"
        enter-from-class="opacity-0"
        enter-to-class="opacity-100"
    >
        <div v-if="props.show" role="alert" class="hover:shadow-md absolute right-3 bottom-5" :class="containerClass">
            <div class="shrink-0">
                <!-- Icono de la alerta -->
                <component :is="iconComponent" :class="iconClass" />
            </div>
            <div class="flex-1 space-y-2">
                <h2 :class="titleClass">
                    {{ props.title }}
                </h2>
                <div :class="contentClass">
                    <slot />
                </div>
            </div>
            <!-- Botón de cerrar si se pasa la función onDismiss -->
            <div class="shrink-0" v-if="props.onDismiss">
                <button @click="dismiss" :class="closeButtonClass">
                    <!-- Icono de la cruz -->
                    <XMarkIcon class="w-6 h-6 " />
                </button>
            </div>
        </div>
    </transition>
</template>

<script setup>
import { InformationCircleIcon, XMarkIcon, CheckCircleIcon, ExclamationTriangleIcon, XCircleIcon, LightBulbIcon, ShieldExclamationIcon } from '@heroicons/vue/24/solid'
import { cva } from 'class-variance-authority'
import { computed, onBeforeUnmount, onMounted, watch } from 'vue'

const props = defineProps({
    intent: {
        type: String,
        validator(value) {
            return ["info", "success", "danger", "warning", "dark", "brand"].includes(value)
        },
        default: "info"
    },
    title: String,
    show: {
        type: Boolean,
        default: true
    },
    onDismiss: Function
})

const emit = defineEmits(['dismiss']);

const containerClass = computed(() => {
    const intentClasses = {
        info: "bg-blue-100",
        success: "bg-green-100",
        warning: "bg-orange-100",
        danger: "bg-red-100",
        dark: "bg-slate-600",
        brand: "bg-purple-200"
    }
    return `min-w-[275px] max-w-[325px] flex p-4 rounded-md space-x-3 ${intentClasses[props.intent] || ''}`
})

const iconClass = computed(() => {
    return cva("w-6 h-6", {
        variants: {
            intent: {
                info: "text-blue-700",
                success: "text-green-600",
                warning: "text-orange-400",
                danger: "text-red-400",
                dark: "text-white",
                brand: "text-purple-900"
            }
        }
    })( {
        intent: props.intent,
    })
})

const titleClass = computed(() => {
    return cva("font-bold", {
        variants: {
            intent: {
                info: "text-blue-900",
                success: "text-green-900",
                warning: "text-orange-900",
                danger: "text-red-900",
                dark: "text-white",
                brand: "text-purple-900"
            }
        }
    })( {
        intent: props.intent,
    })
})

const contentClass = computed(() => {
    return cva("text-xs", {
        variants: {
            intent: {
                info: "text-blue-800",
                success: "text-green-800",
                warning: "text-orange-800",
                danger: "text-red-800",
                dark: "text-slate-200",
                brand: "text-purple-800"
            }
        }
    })( {
        intent: props.intent,
    })
})

const closeButtonClass = computed(() => {
    return cva("p-0.5 rounded-md -m-1", {
        variants: {
            intent: {
                info: "text-blue-900/70 hover:text-blue-900 hover:bg-blue-300",
                success: "text-green-900/70 hover:text-green-900 hover:bg-green-300",
                warning: "text-orange-900/70 hover:text-orange-900 hover:bg-orange-300",
                danger: "text-red-900/70 hover:text-red-900 hover:bg-red-300",
                dark: "text-slate-100/70 hover:text-white hover:bg-slate-800",
                brand: "text-purple-900/70 hover:text-purple-900 hover:bg-purple-300"
            }
        }
    })( {
        intent: props.intent,
    })
})

const iconComponent = computed(() => {
    const icons = {
        success: CheckCircleIcon,
        info: InformationCircleIcon,
        danger: XCircleIcon,
        warning: ExclamationTriangleIcon,
        brand: LightBulbIcon,
        dark: ShieldExclamationIcon
    }

    return icons[props.intent]
})

const AutoHideDurations = computed(()=> {
    const durations = {
        success: 5300, // 5.3 segundos
        info: 5000,    // 5 segundos
        danger: 5500,  // 5.5 segundos
        warning: 4500, // 4.5 segundos
        dark: 4000,    // 4 segundos
        brand: 4000,   // 10 segundos
        default: 3000, // Tiempo por defecto
    }
    return durations[props.intent] || durations.default;
});


// Ocultamiento automático
let timer = null;

const handleAutoHide = () => {
    if (props.show) {
        timer = setTimeout(() => {
            dismiss();
        }, AutoHideDurations.value);
    }
};

watch(() => props.show, handleAutoHide);
onMounted(handleAutoHide);
onBeforeUnmount(() => {
    if (timer) {
        clearTimeout(timer);
    }
});


// Emitir el evento dismiss cuando se haga clic en el botón de cerrar (manualmente)
function dismiss() {
    if (props.onDismiss) {
        props.onDismiss() // Llama al callback onDismiss, es decir llamam al callback pasado dfesde el padre
    }
    emit('dismiss');
}
</script>

<style scoped>
</style>
