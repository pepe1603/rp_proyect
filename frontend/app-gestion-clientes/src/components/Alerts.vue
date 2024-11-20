<template>
    <div>
      <SearchBar v-model="searchQuery" :items="productList" @update:filteredResults="setFilteredResults" />
  
      <!-- Mostrar los resultados solo cuando hay algo seleccionado o no está vacío -->
      <div v-if="filteredResults.length > 0">
        <p>Resultados para: {{ searchQuery }}</p>
        <ul>
          <li v-for="(item, index) in filteredResults" :key="index">{{ item }}</li>
        </ul>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import SearchBar from './SearchBar.vue';
  
  const productList = ref([
    'Manzana', 'Banana', 'Cereza', 'Durazno', 'Frutilla', 'Kiwi', 'Limon', 'Mango', 'Naranja',
  ]);
  
  const searchQuery = ref('');
  const filteredResults = ref([]);
  
  // Actualizar los resultados filtrados
  const setFilteredResults = (results) => {
    filteredResults.value = results;
  };
  //---------------------------------
  </script>
  