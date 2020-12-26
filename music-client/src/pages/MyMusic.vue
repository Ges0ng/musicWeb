<template>
    <div class="my-music">
        <div class="album-slide">
            <div class="album-img" @click="goSetting" title="个人信息">
                <img :src="getUserAvator(avator)">
            </div>
            <ul class="album-info">
                <li>昵 称：<span style="color:#E6A23C;font-size:40px;">{{username}}</span></li>        
                <li></li>   
                <li>性 别：<span style="font-size:25px;">{{userSex}}</span></li>
                <li></li>                
                <li>生 日：<span style="font-size:25px;">{{birth}}</span></li>   
                <li></li>                             
                <li>地 区：<span style="font-size:25px;">{{location}}</span></li>
            </ul>
        </div>
        <div class="album-content">
            <div class="album-title">
                <p>个性签名：</p>
                <span style="color:#F08080;">&nbsp;&nbsp;{{introduction}}</span>
            </div>
            <div class="songs-body">
                <album-content :songList="collectList">
                    <template slot="title">我的收藏</template>
                </album-content>
            </div>
        </div>
    </div>
</template>

<script>
import {mixin} from '../mixins';
import {mapGetters} from 'vuex';
import {getUserOfid,getCollectOfUserId,songOfSongId} from '../api/index';
import AlbumContent from '../components/AlbumContent';

export default {
    name:'my-music',
    mixins:[mixin],
    components:{
        AlbumContent
    },
    data(){
        return{
            avator: '',       //头像
            username:'',  //用户名
            userSex:'',   //性别
            birth: '',  //生日
            location: '' , //地区
            introduction:'', //签名
            collection:[], //收藏的歌曲列表
            collectList:[] , // 收藏的歌曲列表(带歌曲详情)
        }
    },
    computed:{
        ...mapGetters([
            'listOfSongs',  //当前播放列表
            'userId',   //当前登录的用户
        ])
    },
    mounted(){
        this.getMsg(this.userId);
        this.getColltion(this.userId);
    },    
    methods:{
        getMsg(userId){
            getUserOfid(userId)
                .then(res =>{
                    this.avator = res.avator;
                    this.username = res.username;
                    if(res.sex==0){
                        this.userSex = '女';
                    }else if (res.sex==1){
                        this.userSex = '男';
                    }
                    this.birth = this.attachBirth(res.birth);
                    this.location = res.location;
                    this.introduction = res.introduction;          
                             
                })
                .catch(err => {
                    console.log(err);
                })
        },
        //获取我的收藏列表
        getColltion(userId){
            getCollectOfUserId(userId).then(res => {
                this.collection = res; 
                //通过歌曲id获取歌曲信息
                for(let item of this.collection){
                    this.getSongsOfIds(item.songId);
                }
            }).catch(err => {
                console.log(err);
            })
        },
        //通过歌曲id获取歌曲信息
        getSongsOfIds(id){
            songOfSongId(id).then(res => {
                this.collectList.push(res);
            }).catch(err => {
                console.log(err);
            })
        },
        goSetting(){
          this.$store.commit('setActiveName',name);
          this.$router.push({path: '/setting'});
      },
    }
}
</script>


<style lang="scss" scoped>
@import '../assets/css/my-music.scss';
</style>