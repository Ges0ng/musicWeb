<template>
    <div class="song-list-album">
        <div class="album-slide">
            <div class="album-img">
                <img :src="attachImageUrl(tempList.pic)">
            </div>
            <div class="album-info">
                <h2 style="color:#67C23A;"> 简介: </h2>
                <span>
                    {{tempList.introduction}}
                </span>
            </div>
        </div>
        <div class="album-content">
            <div class="album-title">
                <p>{{tempList.title}}</p>
            </div>
            <div class="album-score">
                <div>
                    <h3 style="color:#409EFF;">歌单评分:</h3>
                    <div>
                        <el-rate v-model="average" disabled></el-rate>
                    </div>
                </div>
                <span style="color:pink;">
                    {{average * 2}}
                </span>
                <div></div>
                <div>
                    <h3 style="font-size:30px;color:#E6A23C;">评分:</h3>
                    <div @click="setRank">
                        <el-rate v-model="rank" allow-half show-text  ></el-rate>
                    </div>
                </div>
            </div>
            <div class="songs-body"> 
                <album-content :songList="listOfSongs">
                    <template slot="title">
                        <span  style="color:#909399;">
                        歌单
                        </span>
                    </template>                    
                </album-content>
                <comment :playId="songListId" :type="1"></comment>
            </div>
        </div>
    </div>
</template>

<script>
import {mixin} from '../mixins';
import {mapGetters} from 'vuex' ;
import { ListSongDetail, songOfSongId ,setRank ,getRankOfSongListId} from '../api/index';
import AlbumContent from '../components/AlbumContent';
import comment from '../components/Comment';
import Comment from '../components/Comment.vue';
export default {
    name:'song-list-album',
    mixins:[mixin],
    components:{
        AlbumContent,
        Comment,
    },
    data(){
        return{
            songLists: [],  //当前页面需要展示的歌曲列表
            songListId: '',       //前面传来的歌单id
            average: 0,   //评价计算的平均分
            rank: 0 , //评价分数
        }
    },
    computed:{
        ...mapGetters([
            'listOfSongs',  //当前播放列表
            'tempList', //当前歌曲列表
            'loginIn',  //判断用户是否已经登陆
            'userId', //当前登录的用户
        ])
    },
    created(){
        this.songListId = this.$route.params.id;
        this.getSongId();
        this.getRank(this.songListId);
    },
    methods:{
        //获取当前歌单的歌曲列表
        getSongId(){
            ListSongDetail(this.songListId).then(res =>{
                for(let item of res){
                    this.getSongList(item.songId);
                }
                this.$store.commit('setListOfSongs',this.songLists);
            }).catch(err =>{
                console.log(err);
            })
        },
        //根据歌曲id获取歌曲信息
        getSongList(id){
            songOfSongId(id).then(res => {
                this.songLists.push(res);
            }).catch(err =>{
                console.log(err);
            })
        },
       //获取歌单的分数
        getRank(id){
            getRankOfSongListId(id).then(res => {
                    this.average = res/2;
                }).catch(err =>{
                    console.log(err)
                })
        },
        //提交评分
        setRank(){
            if(this.loginIn){
                let params = new URLSearchParams();
                params.append('songListId',this.songListId);
                params.append('consumerId',this.userId);
                params.append('score',this.rank * 2);
                setRank(params).then(res => {
                    if(res.code == 1){
                        this.notify(res.msg,'success');
                        this.getRank(this.songListId);
                    }else{
                        this.notify(res.msg,'error');
                    }
                }).catch(err =>{
                    this.notify('您已经评过分啦','error');
                })
            }else{
                this.rank = null;
                this.notify('请先登录','warning');
            }
        }
    }

}
</script>

<style lang="scss" scoped> /* 引入的scss语法，所以需要加lang=scss */
@import '../assets/css/song-list-album.scss';
</style>