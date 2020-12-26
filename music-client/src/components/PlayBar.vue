<template>
    <div class="play-bar" :class="{show:!toggle}">
        <div @click="toggle=!toggle" class="item-up" :class="{turn: toggle}" >
            <svg class="icon">
                  <use xlink:href="#icon-jiantou-xia-cuxiantiao"></use>  
                </svg>
        </div>
        <div class="kongjian">
            <!-- 上一首 -->
            <div class="item" @click="prev" title="上一首">
                <svg class="icon">
                  <use xlink:href="#icon-ziyuanldpi"></use>  
                </svg>
            </div>
            <!-- 播放 -->
            <div class="item" @click="togglePlay" title="播放/暂停" >
                <svg class="icon">
                  <use :xlink:href="playButtonUrl"></use>  
                </svg>
            </div>    
            <!-- 下一首 -->        
            <div class="item" title="下一首" @click="next" >
                <svg class="icon">
                  <use xlink:href="#icon-ziyuanldpi1"></use>  
                </svg>
                
            </div>     
              &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; 
            <!-- 歌曲图片 -->    
            <div class="item-img" @click="toLyric" title="跳转到歌词">
                <img :src="picUrl"/>
            </div>            
            <!-- 播放进度 -->
            <div class="playing-speed">
                <!-- 播放开始时间 -->
                <div class="current-time" >{{nowTime}}</div>
                <div class="progress-box">
                    <div class="item-song-title">
                        <div style="color:#F56C6C;">{{this.title}}</div>
                        <div>{{this.artist}}</div>
                    </div>
                    <div  title="请尽情调戏进度条吧" ref="progress" class="progress" @mousemove="mousemove" @mousedown="mousedown" @mouseup="mouseup">
                        <!-- 进度条 -->
                        <div ref="bg" class="bg" @click="updatemove" >
                            <div ref="curProgress"  class="cur-progress" :style="{width:curentLength+'%'}"></div>
                        </div>
                        <!-- 拖动点 -->
                        <div class="idot" ref="idot" :style="{left:curentLength+'%'}"  ></div>
                    </div>
                </div>
                <!-- 播放结束时间 -->
                <div class="left-time">{{songTime}}</div>
                <!-- 音量 -->
                <div class="item item-volume" title="音量">
                    <svg v-if="volume == 0" class="icon">
                        <use xlink:href="#icon-yinliangjingyinheix"></use>  
                    </svg>
                    <svg v-else class="icon">
                        <use xlink:href="#icon-yinliang"></use>  
                    </svg>
                    <el-slider class="volume" v-model="volume" :vertical="true"></el-slider>
                </div>
                <!-- 收藏 -->
                <div class="item" title="喜欢" @click="collection"> 
                    <svg class="icon" :class="{active:isActive }">
                        <use xlink:href="#icon-xihuan-shi"></use>  
                    </svg>
                </div>

                <!-- 下载 -->
                <div class="item" title="下载" @click="download">
                    <svg class="icon">
                        <use xlink:href="#icon-xiazai"></use>  
                    </svg>
                </div>
                <!-- 当前播放歌曲列表 -->
                <div class="item" @click="changeAside" title="播放列表">
                    <svg class="icon">
                        <use xlink:href="#icon-liebiao"></use>  
                    </svg>
                </div>
            </div>            
        </div>
    </div>
</template>

<script>
import {mapGetters} from 'vuex';
import {download,setCollect,getCollectOfUserId} from '../api/index';
import {mixin} from '../mixins';
export default {
    name : 'play-bar',  
    mixins:[mixin],
    data(){
        return {
            nowTime:'00:00', //当前默认的播放进度时间
            songTime : '00:00', //当前歌曲总时间
            curentLength: 0,    //进度条的位置
            progressLength: 0,  //进度条总长度
            mouseStartX: 0, //左右拖动
            tag: false, //拖拽开始结束的标记，拖拽的时候变成true
            volume: 50, //默认音量
            toggle:true, //控制播放栏的show/hidden. 默认是true
        }
    },
    computed:{
        ...mapGetters([
            'id',               //歌曲id
            'url',              //歌曲地址
            'isPlay',           //播放状态
            'playButtonUrl', //播放状态的图标
            'picUrl',   //当前歌曲图片
            'title',    //歌名
            'artist',    //歌手
            'duration' , //音乐时长
            'curTime',  //当前音乐播放的位置 
            'showAside',    //播放列表
            'listIndex',    //歌曲在歌单中的位置
            'listOfSongs',  //当前歌单列表
            'autoNext', //播放完切换下一首
            'userId',   //当前登录用户的id
            'loginIn',  //验证登录的信息
            'isActive', //当前播放的歌曲是否已收藏
        ])
    },
    watch: {
        //切换播放状态的图标
        isPlay(){
            if(this.isPlay){    //判断是否播放，如果true则返回false让他暂停
                this.$store.commit('setPlayButtonUrl','#icon-zanting');
            } else {
                this.$store.commit('setPlayButtonUrl','#icon-bofang');
            }
        },
        curTime(){
            this.nowTime = this.formatSeconds(this.curTime);
            this.songTime = this.formatSeconds(this.duration);
            this.curentLength = (this.curTime /this.duration) *100;
        },
        //音量变化
        volume(){
            this.$store.commit('setVolume',this.volume /100 );
        },
        //自动播放下一首
        autoNext(){ //默认是切换下一首false不自动播放，但是这里不判断，直接监控是否切换了歌，直接播放！
            this.next();
            
        }
    },
    mounted(){
        this.progressLength = this.$refs.progress.getBoundingClientRect().width;
        document.querySelector('.item-volume').addEventListener('click',function(e){
            document.querySelector('.volume').classList.add('show-volume');
            e.stopPropagation();
        },false);
        document.querySelector('.volume').addEventListener('click',function(e){
            e.stopPropagation();
        },false);
        document.addEventListener('click',function(){
            document.querySelector('.volume').classList.remove('show-volume');
        },false);
    },
    methods: {
        //控制音乐播放，暂停
        togglePlay(){
            if(this.isPlay){    //判断是否播放，如果true则返回false让他暂停
                this.$store.commit('setIsPlay',false);
            } else {
                this.$store.commit('setIsPlay',true);
            }
        },
        //时间格式
        formatSeconds(value){
            let theTime = parseInt(value);
            let result = '';    //返回值
            let hour = parseInt(theTime / 3600);    //小时
            let minute = parseInt((theTime / 60) % 60); //分钟
            let second = parseInt(theTime % 60);        //秒
            if(hour > 0){
                if(hour < 10){
                    result = '0' + hour + ":";
                }else{
                    result = hour + ":";
                }
            }
            if(minute > 0){
                if(minute < 10){
                    result += "0" + minute + ":";
                }else{
                    result += minute + ":";
                }
            }else{
                result += "00:";
            }
            if(second > 0){
                if(second < 10){
                    result += "0" + second;
                }else{
                    result += second;
                }
            }else{
                result += "00";
            }
            return result;
        },
        //拖拽开始
        mousedown(e){
            this.mouseStartX = e.clientX;
            this.tag = true;
        },
        //拖拽结束
        mouseup(){
            this.tag = false;
        },
        //拖拽中
        mousemove(e){
            if(!this.duration){
                return false;
            }
            if(this.tag){
                let movementX = e.clientX - this.mouseStartX;       //点点移动的距离
                let curLength = this.$refs.curProgress.getBoundingClientRect().width;
                let newPercent = ((movementX+curLength)/this.progressLength)*100;
                if(newPercent>100){
                    newPercent = 100;
                }
                this.curLength = newPercent;
                this.mouseStartX = e.clientX;
                this.changeTime(newPercent);
            }
        },
         //进度条
        changeTime(percent){
            let newCurTime = (percent*0.01)* this.duration;
            this.$store.commit('setChangeTime',newCurTime);
        },
        //点击进度条直接跳过去
        updatemove(e){
            if(!this.tag){  //非拖动状态
                //进度条左边坐标
                let curLength = this.$refs.bg.offsetLeft;
                let newPercent = ((e.clientX - curLength) / this.progressLength) * 100;
                if(newPercent > 100){
                    newPercent = 100;
                }
                if(newPercent < 0){
                    newPercent = 0;
                }
                this.curLength = newPercent;
                this.changeTime(newPercent);
            }
        },
        changeAside(){  //显示歌曲列表            
            this.$store.commit('setShowAside',true);
        },
        //上一首
        //上一首
        prev(){
            if(this.listIndex != -1 && this.listOfSongs.length > 1){    //当前处于不可能状态或者只有只有一首音乐的时候不执行）
                if(this.listIndex > 0){                                 //不是第一首音乐
                    this.$store.commit('setListIndex',this.listIndex - 1);  //直接返回上一首
                }else{                                                  //当前是第一首音乐
                    this.$store.commit('setListIndex',this.listOfSongs.length - 1);  //切换到倒数第一首
                }
                this.toplay(this.listOfSongs[this.listIndex].url);
            }
        },
        //下一首
        next(){
            if(this.listIndex != -1 && this.listOfSongs.length > 1){    //当前处于不可能状态或者只有只有一首音乐的时候不执行）
                if(this.listIndex < this.listOfSongs.length - 1){                                 //不是最后一首音乐
                    this.$store.commit('setListIndex',this.listIndex + 1);  //直接下一首
                }else{                                                  //当前是最后音乐
                    this.$store.commit('setListIndex',0);  //切换到倒数第一首
                }
                this.toplay(this.listOfSongs[this.listIndex].url);
            }
        },
        //播放
        toplay(url){
            if(url && url != this.url){
                this.$store.commit('setId',this.listOfSongs[this.listIndex].id);
                this.$store.commit('setUrl',this.$store.state.configure.HOST+"/song/"+url);
                this.$store.commit('setPicUrl',this.$store.state.configure.HOST+this.listOfSongs[this.listIndex].pic);
                this.$store.commit('setTitle',this.replaceFName(this.listOfSongs[this.listIndex].name));
                this.$store.commit('setArtist',this.replaceLName(this.listOfSongs[this.listIndex].name));
                this.$store.commit('setLyric',this.parseLyric(this.listOfSongs[this.listIndex].lyric));

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
            }
        },
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
        //打开歌词
        toLyric(){
            this.$router.push({path: `/lyric`});
        },
        //下载
        download(){
            download(this.url).then(res => {
                let content = res.data;
                let eleLink = document.createElement('a');
                eleLink.download = `${this.artist}-${this.title}.mp3`;
                eleLink.style.display = 'none';
                //字符串转换成blob地址
                let blob = new Blob([content]);
                eleLink.href = URL.createObjectURL(blob);
                //连接地址加入到document里
                document.body.appendChild(eleLink);
                //触发点击事件
                eleLink.click();
                //移除这个控件
                document.body.removeChild(eleLink);
            }).catch(err =>{
                console.log(err);
            })
        },
        //点击收藏
        collection(){
            if(this.loginIn){
                var params = new URLSearchParams();
                params.append('userId',this.userId);
                params.append('type',0);
                params.append('songId',this.id);
                setCollect(params).then(res => {
                    if(res.code == 1){  //收藏成功
                        this.$store.commit('setIsActive',true); //收藏成功变颜色奥
                        this.notify(res.msg,'success');
                    }else if(res.code == 2){
                        this.notify(res.msg,'warning');
                    }else{
                        this.notify(res.msg,'error');
                    }
                })
            }else{
                this.notify("请先登录!",'warning');
            }
        },
    }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/play-bar.scss';
.volume{
  @include box-shadow(0 0 20px 4px rgba(0, 0, 0, 0.2));
}
</style>