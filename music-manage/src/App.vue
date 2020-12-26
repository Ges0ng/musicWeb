<template>
  <div id="app">
    <Listen/>
    <router-view></router-view>
    <el-backtop target=".page-component__scroll .el-scrollbar__wrap"></el-backtop>
  </div>
</template>

<script>
import Listen from './components/Listen';
export default {
  name: 'App',
  components: {
    Listen
  }
}
</script>

<style>
</style>
