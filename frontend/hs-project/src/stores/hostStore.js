import { defineStore } from 'pinia'
import { ref } from 'vue';

export const useHostStore = defineStore('useHost', () => {
    // state
    const host = ref('http://localhost:9090');

    return {
        host
    };
});
