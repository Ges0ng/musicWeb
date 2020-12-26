<template>
    <div class="search-songs"> 
        <album-content :songList="listOfSongs"></album-content>
    </div>
</template>


<script>
import {mapGetters} from 'vuex';
import {mixin} from '../../mixins';
import AlbumContent from '../AlbumContent';

export default {
    components: { AlbumContent },
    name: 'search-songs',
    mixins: [mixin],
    computed:{
        ...mapGetters([
            'listOfSongs'   //取得listofsongs歌曲列表放到缓存中，用computed监控，监控到会传送到albumcontent,在其中显示。//目的是为了albumcontent能通用。
        ])      
    },
    mounted(){
        this.getSong();
    }
}
</script>

<style lang="scss" scoped>
@import '../../assets/css/search-songs.scss';
</style>