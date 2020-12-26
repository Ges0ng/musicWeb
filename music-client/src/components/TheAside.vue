<template>
    <transition name="slide-face">
        <div class="the-aside" v-if="showAside">
            <h2 class="title">播放列表</h2>
             <li>&nbsp;&nbsp;&nbsp;歌曲&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;歌手</li>
            <ul class="menus">
                <li v-for="(item,index) in listOfSongs" :key="index" :class="{'is-play': id==item.id}" 
                    @click="toplay(item.id,item.url,item.pic,item.index,item.name,item.lyric)">
                    {{replaceFName(item.name)}}
                     &nbsp;&nbsp;&nbsp;
                    {{replaceLName(item.name)}}
                </li>
            </ul>
        </div>
    </transition>
</template>




<script>
import {mapGetters} from 'vuex';
import {getCollectOfUserId} from '../api/index';
export default {
    name : 'the-aside',  
    computed: {
        ...mapGetters([
            'showAside', //歌曲列表
            'listOfSongs',  //当前歌曲列表
            'id',   //正在播放的音乐id
            'userId',   //当前登录用户的id
            'loginIn',  //验证登录的信息            
            'isActive', //当前播放的歌曲是否已收藏
        ])
    },
    mounted(){
        let _this = this;
        document.addEventListener('click',function(){
            _this.$store.commit('setShowAside',false)
        },true);
    },
    methods:{
        //获取名字的前半部分.   歌手名
        replaceLName(str){
            let arr = str.split('-');
            return arr[0];
        },
        //后半部分  歌名
        replaceFName(str){
            let arr = str.split('-');
            return arr[1];
        },
        //播放
        toplay(id,url,pic,index,name,lyric){
            this.$store.commit('setId',id);
            this.$store.commit('setUrl',this.$store.state.configure.HOST+"/song/"+url);
            this.$store.commit('setPicUrl',this.$store.state.configure.HOST+pic);
            this.$store.commit('setListIndex',index);
            this.$store.commit('setTitle',this.replaceFName(name));
            this.$store.commit('setArtist',this.replaceLName(name));
            this.$store.commit('setLyric',this.parseLyric(lyric));

            this.$store.commit('setIsActive',false); //不管有没有登录首先设置成没有收藏,再判断是否被收藏来变色
            if(this.loginIn){
                getCollectOfUserId(this.userId).then(res => {
                    for(let item of res){   //遍历列表看有没有收藏,如果遍历到了,就变红
                        if(item.songId == id){  //当前歌曲的id和页面的id
                            this.$store.commit('setIsActive',true);
                            break;
                        }
                    }
                })
            }
        },
        //歌词解析成应有的格式
        parseLyric(text){
            let lines = text.split("\n");       //将歌词分解成数组
            let patten = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g;   
            let result = []; //返回值
            //对于歌词格式不对的直接返回
            if(!(/\[.+\]/.test(text))){
                return [[0,text]];
            }
            while(!patten.test(lines[0])){  //去掉前面格式不正确的行
                lines = lines.slice(1);
            }
            //遍历每一行,形成一个带着两个元素的数组, (秒)时间+歌词
            for(let item of lines){
                let time = item.match(patten);//存时间段
                let value = item.replace(patten,'');//存后面的歌词 遇到时间戳格式的替换成空

                for(let item1 of time){
                    let t = item1.slice(1,-1).split(":");   //取出时间,换算成数组
                    
                    if(value!=''){
                        result.push([parseInt(t[0],10)*60 +parseFloat(t[1]),value]);   //强制10进制转换
                    }
                }
            }
            //按照第一个元素 -- 时间 -- 排序
            result.sort(function(a,b){
                    return a[0] - b[0];
            });
            return result;
        },
    }
    
}
</script>

<style lang="scss" scoped>
@import '../assets/css/the-aside.scss';
.title{
    color:#FF9999;

}
.the-aside{
    background-color: Transparent;
    border-radius: 50px;
    box-shadow: 0 5px 10px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
}
.menus{
    background-color: Transparent;
    color:#FF9999;
}
</style>