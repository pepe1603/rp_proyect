<template>
    <transition 
    leave-active-class="duration-700"
    leave-to-class="opacity-0"
    >
        <div v-if="props.show" :class="containerClass" >
        <div class="shrink-0">
            <component :is="iconComponent" :class="iconClass" /> 
        </div>
        <div class="flex-1 space-y-2">
            <h2 :class="tittleClass" >
                {{ props.title }}
            </h2>
            <div :class="contentClass">
                <slot />
            </div>
        </div>
        <div class="shrink-0" v-if="props.onDismiss">
            <button @click="dismiss()" :class="closeButtonClass">
                <XMarkIcon class="w-6 h-6"/>
            </button>
        </div>
    </div>
    </transition>
</template>

<script setup>

//import {InformationCirculation, XMarkIcon} from '@heroicons/vue/outline'
import { InformationCircleIcon, XMarkIcon, CheckCircleIcon, ExclamationTriangleIcon, XCircleIcon, LightBulbIcon, ShieldExclamationIcon } from '@heroicons/vue/24/solid/index.js'
import {cva} from 'class-variance-authority';
import { computed } from 'vue'


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
    onDismiss: Function,
});

const containerClass = computed( () => {
    return cva ("w-[315px] flex p-4 rounded-md space-x-3",{
        variants: {
            intent: {
                info: "bg-blue-100 ",
                success: "bg-green-100 ",
                warning: "bg-orange-100 ",
                danger: "bg-red-100 ",
                dark: "bg-slate-600 ",
                brand: "bg-purple-200 "
            }
        }
    })
    ({
        intent: 
            props.intent,
        
    })
} )
const iconClass = computed( () => {
    return cva ("w-6 h-6",{
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
    })
    ({
        intent: 
            props.intent,
        
    })
} )

const tittleClass = computed( () => {
    return cva ("font-medium",{
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
    })
    ({
        intent: 
            props.intent,
        
    })
} )

const contentClass = computed( () => {
    return cva("text-sm", {
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
    })({
        intent: 
            props.intent,
        
    })
} );

const closeButtonClass = computed( () => {
    return cva ("p-0.5 rounded-md -m-1",{
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
    })
    ({
        intent: 
            props.intent,
        
    })
} );

const iconComponent =  computed (() => {
    const icons = {
        success: CheckCircleIcon,
        info: InformationCircleIcon,
        danger: XCircleIcon,
        warning: ExclamationTriangleIcon,
        brand: LightBulbIcon,
        dark: ShieldExclamationIcon
        
        

    };

    return icons[props.intent];
});

function dismiss() {
    if( props.onDismiss ){
        props.onDismiss();
    }
}


</script>

<style></style>
