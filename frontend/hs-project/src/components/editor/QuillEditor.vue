<script setup>
import "quill/dist/quill.snow.css"

import "quill-resize-module/dist/resize.css"
import { initQuill } from "./quillModules"
import { onMounted, ref, watch, nextTick } from "vue"

const props = defineProps({
  modelValue: {
    type: String,
    default: ""
  }
})

const emit = defineEmits(["update:modelValue"])
const editor = ref(null)
let quill = null

onMounted(async () => {
  await nextTick()

  quill = initQuill(editor.value)
  if (props.modelValue) {
    quill.root.innerHTML = props.modelValue
  }

  quill.on("text-change", () => {
    const html = quill.root.innerHTML
    emit("update:modelValue", html)
  })
})

watch(
  () => props.modelValue,
  (val) => {
    if (quill && val !== quill.root.innerHTML) {
      quill.root.innerHTML = val
    }
  }
)
</script>

<template>
  <div class="quill-editor">
    <div id="editor" ref="editor"></div>
  </div>
</template>

<style>

</style>