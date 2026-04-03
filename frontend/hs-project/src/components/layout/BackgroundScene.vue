<template>
  <div class="background-3d-scene">
    <div class="sphere sphere-pink-large"></div>
    <div class="sphere sphere-pink-small"></div>
    <div class="sphere sphere-orange-large"></div>
    <div class="sphere sphere-orange-small"></div>
    <div class="sphere sphere-purple"></div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.background-3d-scene { position: fixed; top: 0; left: 0; width: 100%; height: 100%; z-index: -1; pointer-events: none; overflow: hidden; background-color: #121214; }
.sphere { position: absolute; border-radius: 50%; animation: floatAround 18s infinite alternate ease-in-out; }
@keyframes floatAround { 0% { transform: translate(0, 0); } 50% { transform: translate(40px, -40px); } 100% { transform: translate(-30px, 30px); } }
.sphere-pink-large { top: 10%; left: -5%; width: 350px; height: 350px; background: radial-gradient(circle at 30% 30%, #ffc0d8, #ff1361 40%, #c2185b 70%, #8a002b) !important; box-shadow: inset 10px 10px 30px rgba(255,255,255,0.2), inset -10px -10px 30px rgba(0,0,0,0.5), 0 20px 40px rgba(0,0,0,0.4) !important; animation-delay: 0s; z-index: 1; }
.sphere-pink-small { top: 40%; left: 10%; width: 80px; height: 80px; background: radial-gradient(circle at 30% 30%, #ffc0d8, #ff1361 40%, #c2185b 70%, #8a002b) !important; box-shadow: inset 5px 5px 15px rgba(255,255,255,0.2), inset -5px -5px 15px rgba(0,0,0,0.5), 0 10px 20px rgba(0,0,0,0.4) !important; animation-delay: -2s; z-index: 3; }
.sphere-orange-large { top: 40%; right: -5%; width: 400px; height: 400px; background: radial-gradient(circle at 30% 30%, #ffdf9e, #ff9800 40%, #f57c00 70%, #b26a00) !important; box-shadow: inset 15px 15px 35px rgba(255,255,255,0.2), inset -15px -15px 35px rgba(0,0,0,0.5), 0 30px 50px rgba(0,0,0,0.4) !important; animation-delay: -4s; z-index: 0; }
.sphere-orange-small { top: 30%; right: 18%; width: 60px; height: 60px; background: radial-gradient(circle at 30% 30%, #ffdf9e, #ff9800 40%, #f57c00 70%, #b26a00) !important; box-shadow: inset 4px 4px 12px rgba(255,255,255,0.2), inset -4px -4px 12px rgba(0,0,0,0.5), 0 10px 20px rgba(0,0,0,0.4) !important; animation-delay: -6s; z-index: 3; }
.sphere-purple { bottom: -10%; left: 35%; width: 300px; height: 300px; background: radial-gradient(circle at 30% 30%, #e8bbff, #9c27b0 40%, #7b1fa2 70%, #4a0059) !important; box-shadow: inset 12px 12px 30px rgba(255,255,255,0.2), inset -12px -12px 30px rgba(0,0,0,0.5), 0 25px 40px rgba(0,0,0,0.4) !important; animation-delay: -8s; z-index: 0; }
</style>