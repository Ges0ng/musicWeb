<template>
    <div >
        <div class="comment" >
            <h2 style="color:#909399;">评论</h2>
            <div class="comment-msg" >
                <br/>
                <div class="comment-img"  >
                    <img :src="getUserAvator(avator)"/>
                </div>
                <br/>                
                <el-input v-model="textarea" class="comment-input" type="textarea" rows="3" placeholder="发表评论 ^^"></el-input>
            </div>
            <br/>      
            <el-button type="primary" class="sub-btn" @click="postComment" >评论</el-button>
        </div>
        <p>共 <span style="font-size:25px; color:#909399">{{commentList.length}}</span> 条评论</p>
        <br/>
        <ul class="popular" v-for="(item,index) in commentList" :key="index">
            <li>
                <div class="popular-img">
                    <img :src="attachImageUrl(userPic[index])"/>
                </div>
                <div class="popular-msg">
                    <ul>
                        <li class="name">{{userName[index]}}</li>
                        <li></li>
                        <li class="content">{{item.content}}</li>
                        <li class="time">{{item.createTime}}</li>

                    </ul>
                </div>
                <div class="up" ref="up" @click="postUp(item.id,item.up,index)">
                    <svg class="icon">
                        <use xlink:href="#icon-zan"></use>
                    </svg>
                    {{item.up}}
                </div>
            </li>
        </ul>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { getAllComment, getUserOfid, setComment, setLike } from '../api';
import {mixin} from '../mixins';
export default {
    name: 'comment',
    mixins:[mixin],
    props: [
        'playId',   //歌曲或者歌单的id
        'type', //0歌曲, 1歌单
    ],
    computed:{
        ...mapGetters([
            'id',    //当前歌曲/歌单id
            'loginIn',  //验证用户是否一登陆
            'userId',   //登录用户id
            'avator',   //用户头像
        ])
    },
    data(){
        return{
            textarea: '', //存放评论的内容
            commentList: [],    //存放评论列表
            userPic: [],    //用户头像
            userName: [],   //用户昵称
        }
    },
    mounted(){
        this.getComment();
    },
    methods:{
        //提交评论
       postComment(){
           if(this.loginIn){
               let params = new URLSearchParams();
               if(this.type == 0){
                   params.append('songId',this.playId);
               }else{
                params.append('songListId',this.playId)
               }
               params.append('userId',this.userId);
               params.append('type',this.type);
               params.append('content',this.textarea);
               setComment(params).then(res => {
                    if(res.code == 1){
                        this.notify(res.msg,'success');
                        this.textarea='';
                        this.getComment();
                    }else{
                        this.notify("评论失败",'error');
                    }
                }).catch(err =>{
                    this.notify("评论失败",'error');
                })
            }else{
                this.rank = null;
                this.notify('请先登录','warning');
            }
        },
        //评论列表
        getComment(){
            getAllComment(this.type,this.playId).then(res => {
                    this.commentList = res;
                    for(let item of res){
                        this.getUsers(item.userId);
                    }
                }).catch(err =>{
                    this.notify("加载失败",'error');
                })
        },
        //获取用户的头像和昵称
        getUsers(id){
            getUserOfid(id).then(res => {
                this.userPic.push(res.avator);
                this.userName.push(res.username);
            }).catch(err =>{
                    this.notify("出错了",'error');
                })
        },
        //给某一个评论点赞
        postUp(id,up,index){
            if(this.loginIn){
               let params = new URLSearchParams();
               
               params.append('id',id);
               params.append('up',up+1);
               setLike(params).then(res => {
                    if(res.code == 1){
                        this.$refs.up[index].children[0].style.color = '#2796cd';
                        this.getComment();
                    }
                }).catch(err =>{
                    this.notify("点赞失败",'error');
                })
            }else{
                this.rank = null;
                this.notify('请先登录','warning');
            }
        }
    },
}
</script>

<style lang="scss" scoped>
@import '../assets/css/comment.scss';
</style>