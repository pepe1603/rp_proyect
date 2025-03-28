<template>
    <div class="relative w-full max-w-md mx-auto">
      <!-- Campo de búsqueda --><!-- Solo se busca al presionar Enter -->
      <input
        v-model="searchQuery"
        type="text"
        class="w-full p-3 pl-10 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
        placeholder="Buscar..."
        @input="onInputChange"
        @keydown.enter="onSearchEnter" 
      />
  
      <!-- Icono de búsqueda -->
      <MagnifyingGlassIcon class="absolute left-3 top-3 w-5 h-5 text-gray-500" />

      
      <!-- Mostrar sugerencias solo si la búsqueda no está vacía -->
      <div v-if="showSuggestions && filteredResults.length > 0" class="absolute w-full bg-white shadow-lg rounded-lg mt-2 z-10">
        <ul>
          <li
            v-for="(item, index) in filteredResults"
            :key="index"
            class="p-3 hover:bg-gray-200 cursor-pointer"
            @click="onItemSelect(item)"
          >
            {{ item }}
          </li>
        </ul>
      </div>

      
      <!--  spinner de carga -->
    <div v-if="loading" class="absolute right-3 top-3">
        <ArrowPathIcon 
        class="w-5 h-5 animate-spin text-blue-500"
    />
    </div>

  
      <!-- Mensaje de no resultados solo si la búsqueda no está vacía -->
      <div v-if="filteredResults.length === 0 && searchQuery !== '' && !isSearching" class="absolute w-full bg-white shadow-lg rounded-lg mt-2 z-10">
        <p class="text-center p-3 text-gray-500">No se encontraron resultados</p>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, defineEmits, defineProps, watch } from 'vue';
  import { MagnifyingGlassIcon, ArrowPathIcon } from '@heroicons/vue/24/solid';
  
  // Props
  const props = defineProps({
    items: {
      type: Array,
      required: true,
    },
    query: {
      type: String,
      default: '',
    },
  });
  
  // Emitters
  const emit = defineEmits(['update:modelValue', 'update:filteredResults']);
  
  // Variables reactivas
  const searchQuery = ref(props.query);  // Almacena el valor del input de búsqueda
  const filteredResults = ref([]);  // Resultados filtrados
  const showSuggestions = ref(false);  // Controla si se deben mostrar las sugerencias
  const isSearching = ref(false);  // Controla si se está buscando al presionar enter
  const loading = ref(false);
  
  // Función para manejar la entrada del usuario (filtrar resultados)
  const onInputChange = () => {
    if (searchQuery.value === '') {
      filteredResults.value = [];  // Limpiar resultados si el input está vacío
      emit('update:filteredResults', []);  // Emitir resultados vacíos
    } else {
        loading.value = true;
      showSuggestions.value = true;  // Mostrar sugerencias mientras el usuario escribe
      filteredResults.value = props.items.filter(item =>
        item.toLowerCase().includes(searchQuery.value.toLowerCase())
      );
      emit('update:modelValue', searchQuery.value);  // Emitir el valor de búsqueda al padre
      emit('update:filteredResults', filteredResults.value);  // Emitir los resultados filtrados
    }
  };
  
  // Función para manejar la búsqueda cuando se presiona Enter
  const onSearchEnter = () => {
    // Solo realizar la búsqueda si el campo de búsqueda no está vacío
    if (searchQuery.value.trim() !== '') {
      showSuggestions.value = false;  // Dejar de mostrar sugerencias al presionar Enter
      isSearching.value = true;  // Marcar que se está buscando
      filteredResults.value = props.items.filter(item =>
        item.toLowerCase().includes(searchQuery.value.toLowerCase())
      );
      emit('update:modelValue', searchQuery.value);  // Emitir el valor de búsqueda al padre
      emit('update:filteredResults', filteredResults.value);  // Emitir los resultados filtrados
    } else {
      // Si el campo está vacío, limpiar resultados y no emitir nada
      filteredResults.value = [];
      emit('update:modelValue', '');  // Emitir un valor vacío al padre
      emit('update:filteredResults', []);  // Emitir un array vacío de resultados
    }
    loading.value= false;
  };
  
  // Función para seleccionar un ítem de las sugerencias
  const onItemSelect = (item) => {
    searchQuery.value = item;
    filteredResults.value = [];  // Limpiar los resultados de sugerencias
    emit('update:modelValue', item);  // Emitir el ítem seleccionado
    emit('update:filteredResults', []);  // Limpiar los resultados
    showSuggestions.value = false;  // Dejar de mostrar las sugerencias
    loading.value = false;
  };
  
  // Sincronizar con el valor del padre
  watch(() => props.query, (newQuery) => {
    searchQuery.value = newQuery;
  });
  </script>
  
  <style scoped>
  /* Aquí pueden ir estilos personalizados si se requieren */
  </style>
  