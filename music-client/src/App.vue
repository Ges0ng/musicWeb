<template>
  <div id="app">    
    <the-header/>
    <router-view class="music-content"/>   
    <song-audio/> 
    <the-aside/> 
    <play-bar/>
    <scroll-top /> 
    <the-footer />

  </div>
</template>

<script>
import ScrollTop from './components/ScrollTop';
import TheHeader from './components/TheHeader';
import TheFooter from './components/TheFooter';
import SongAudio from './components/SongAudio';
import PlayBar   from './components/PlayBar';
import TheAside  from './components/TheAside';
export default {
  name: 'App',
  components: {
    TheHeader,
    ScrollTop,
    TheFooter,
    SongAudio,
    PlayBar,
    TheAside,
  }
}
</script>

<style  lang="scss" scoped>
@import './assets/css/app.scss';
</style>
