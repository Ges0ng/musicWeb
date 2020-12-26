import { likeSongOfName, loginIn,getCollectOfUserId } from "../api/index";
import {mapGetters} from 'vuex';
export const mixin = {
    computed: {
        ...mapGetters([
            'userId',   //当前登录用户的id
            'loginIn',  //验证登录的信息            
        ])
    },
    methods: {
        //提示信息
        notify(title,type){
            this.$notify({
                title: title,
                type: type
            })
        },
        //获取图片地址
        attachImageUrl(srcUrl){ 
            return srcUrl ? this.$store.state.configure.HOST+srcUrl : this.$store.state.configure.HOST+"/img/songListPic/1.jpg";
        },
        //获取用户头像
        getUserAvator(srcUrl){ 
            return srcUrl ? this.$store.state.configure.HOST+srcUrl : this.$store.state.configure.HOST+"/img/AvatorImages/1.png";
        },
        //获取歌手头像
        getSingerUrl(srcUrl){ 
            return srcUrl ? this.$store.state.configure.HOST+srcUrl : this.$store.state.configure.HOST+"/img/singerPic/1.jpg";
        },
        //根据歌手名字模糊查询歌曲
        getSong(){  //取参数的时候用route
            if(!this.$route.query.keywords){
                this.$store.commit('setListOfSongs',[]);
                this.notify('您输入的内容为空','warning');
            }else{
                likeSongOfName(this.$route.query.keywords).then(res => {
                    if(!res.length){    //res返回的是一个list列表，不是空的话说明参数是存在的。
                        this.$store.commit('setListOfSongs',[]);
                        this.notify('无符合条件的歌曲','warning');
                    }else{
                        this.$store.commit('setListOfSongs',res);
                    }
                }).catch(err => {
                    console.log(err);
                }) 
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
        //播放
        toplay:function(id,url,pic,index,name,lyric){
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
            while(!patten.test(lines[0])){  //去掉前面格式不正确的行,这里test如果是中括号会进入循环造成假死
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
        //歌手生日
        attachBirth(val){
            return String(val).substr(0,10); //数据库传过来的生日是带时分秒的 不需要
            
        }
    }
}