<template>
    <div class="singer-album" >
        <div class="album-slide">
            <div class="singer-img">
                <img :src="getSingerUrl(singer.pic)">
            </div>
            <ul class="info">
                <li v-if="singer.sex == 0 || singer.sex == 1 || singer.sex == 2 || singer.sex == 3" style="color:#909399;">
                    {{attachSex(singer.sex)}}
                </li>
                <li style="color:#909399;">{{singer.location}}</li>                
                <li style="color:#909399;">{{attachBirth(singer.birth)}}</li>
            </ul>
        </div>
        <div class="album-content">
            <div class="intro">
                <h2>{{singer.name}}</h2>
                <span>{{singer.introduction}}</span>
            </div>
            <div class="content">
                <album-content :songList="listOfSongs">
                    <template slot="title">热门歌曲</template>
                </album-content>
            </div>
        </div>
    </div>
</template>

<script>
import {mixin} from '../mixins';
import {mapGetters} from 'vuex' ;
import { songOfSingerId } from '../api/index';
import AlbumContent from '../components/AlbumContent';
import comment from '../components/Comment';
export default {
    name:'singer-album',
    mixins:[mixin],
    components:{
        AlbumContent,
    },
    data(){
        return{
            singerId: '',       //歌手id
            singer:{},  //当前歌手
        }
    },
    computed:{
        ...mapGetters([
            'listOfSongs',  //当前播放列表
            'tempList', //当前歌手对象
            'loginIn',  //判断用户是否已经登陆
            'userId',   //当前登录的用户
        ])
    },
    created(){
        this.singerId = this.$route.params.id;
        this.singer = this.tempList;
        this.getSongOfSingerId();
    },
    methods:{
        //根据歌手id查询歌曲
        getSongOfSingerId(){
            songOfSingerId(this.singerId)
                .then(res => {
                    this.$store.commit('setListOfSongs',res);
                })
                .catch(err =>{
                    console.log(err)
                })
        },
        //歌手性别
        attachSex(value){
            if(value == 0){
                return '女';
            }else if(value == 1){
                return '男';
            }else if(value == 2){
                return '组合';
            }else if(value == 3){
                return '不明';
            }
            return '';
        }
    }
}
</script>

<style lang="scss" scoped> /* 引入的scss语法，所以需要加lang=scss */
@import '../assets/css/singer-album.scss';
</style>
