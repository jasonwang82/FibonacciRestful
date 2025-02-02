<template>
  <div class="app">
    <nav class="nav">
      <router-link to="/" class="nav-link">Calculator</router-link>
      <router-link to="/history" class="nav-link">History</router-link>
    </nav>
    <router-view></router-view>
  </div>
</template>

<script setup lang="ts">
</script>

<style>
.app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 20px;
  padding: 20px;
}

.nav {
  padding: 20px 0;
  margin-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.nav-link {
  display: inline-block;
  padding: 10px 20px;
  color: #2c3e50;
  text-decoration: none;
  margin: 0 10px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.nav-link:hover {
  background-color: #f5f5f5;
}

.router-link-active {
  color: #42b983;
  font-weight: bold;
}
</style>
