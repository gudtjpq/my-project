<template>
  <router-view />
</template>

<script setup>
</script>

<style>
@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');

html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  font-family: 'Pretendard', sans-serif;
  background-color: #fbfbfd;
  color: #1d1d1f;
  -webkit-font-smoothing: antialiased;
}

a {
  text-decoration: none;
  color: inherit;
}

* {
  box-sizing: border-box;
}
</style>