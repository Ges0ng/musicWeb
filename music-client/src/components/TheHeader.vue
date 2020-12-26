<template>
  <div class="the-header">
    <div class="header-logo" @click="goHome" title="返回首页">
        <svg class="icon">
            <use xlink:href = "#icon-erji"></use>
        </svg>
        <span style="color:#FF9999;">&nbsp;&nbsp;Paracosm</span>
    </div>
    <ul class="navbar">
        <li :class="{active: item.name == activeName}" v-for="item in navMsg" :key="item.path" @click="goPage(item.path,item.name)">
            {{item.name}}
        </li>
        <li>
            <div class="header-search">
                <input type="text" placeholder="搜索音乐" @keyup.enter="goSearch()" v-model="keywords">
                <div class="search-btn" @click="goSearch()" title="搜索">
                    <svg class="icon" >
                        <use xlink:href = "#icon-sousuo" ></use>
                    </svg>
                </div>
            </div>
        </li>
        <li v-show="!loginIn" :class="{active: item.name == activeName}" v-for="item in loginMsg" :key="item.path" @click="goPage(item.path,item.name)">
            {{item.name}}
        </li>
    </ul>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <div class="btn-fullscreen" @click="handleFullScreen" >
      <el-tooltip :content="fullscreen?'取消全屏':'全屏'" placement="bottom">
        <i class="el-icon-rank"></i>
      </el-tooltip>                
    </div>

    <div class="header-right" v-show="loginIn">
        <div id='user'>
            <img :src='getUserAvator1(avator)'>
        </div>
        <ul class="menu">
            <li v-for="(item,index) in menuList" :key="index" @click="goMenuList(item.path)">{{item.name}}</li> 
        </ul>
    </div>

     

  </div>
</template>

<script>
import {mapGetters} from 'vuex';
import { loginIn } from '../api';
import {navMsg,loginMsg,menuList} from '../assets/data/header';

export default {
  name: 'the-header',
  data() {
      return {
          navMsg: [],    //左侧导航栏
          keywords: '',  //搜索关键字       
          loginMsg: [],  //右侧导航栏
          menuList: [], //用户下拉菜单
          fullscreen:'',    //全屏
      }
  },
  computed:{
      ...mapGetters([
          'activeName',
          'loginIn',
          'avator'
      ])
  },
  created() {
      this.navMsg = navMsg;
      this.loginMsg = loginMsg;
      this.menuList = menuList;
  },
  mounted(){
      document.querySelector('#user').addEventListener('click',function(e){
          document.querySelector('.menu').classList.add("show");
          e.stopPropagation()           //关键在于阻止冒泡
      },false);
      document.querySelector('.menu').addEventListener('click',function(e){
          e.stopPropagation()           //点击菜单内部时，阻止时间冒泡，这样，点击内部时，菜单不会关闭
      },false);
      document.addEventListener('click',function(){
        document.querySelector('.menu').classList.remove('show');
      },false);
  },
  
  methods: {
      //提示信息
        notify(title,type){
            this.$notify({
                title: title,
                type: type
            })
        },
      goHome() {
          this.$router.push({path: "/"});
      },
      goPage(path,name) {
          if(!this.loginIn && path ==  '/my-music'){
              this.notify('请先登录','warning')
          }else{
            this.$store.commit('setActiveName',name);
            this.$router.push({path: path});
          }
      },
      goSearch(){
          this.$router.push({path:'/search',query:{keywords: this.keywords}})
      },
      handleFullScreen(){
            if(this.fullscreen){    //进入全屏状态
                //浏览器识别
                if(document.exitFullscreen){
                    document.exitFullscreen();
                }else if(document.webkitCancelFullScreen){  //safari,chrome
                    document.webkitCancelFullScreen();
                }else if(document.mozCancelFullScreen){     //火狐
                    document.mozCancelFullScreen();
                }else if(document.msExitFullScreen){    //ie
                    document.msExitFullScreen();
                }
            }else{  //非全屏状态
                let element = document.documentElement; //整个页面元素
                if(element.requestFullscreen){
                    element.requestFullscreen();
                }else if(element.webkitRequestFullScreen){//safari,chrome
                    element.webkitRequestFullScreen();
                }else if(element.mozRequestFullScreen){ //firefox
                    element.mozRequestFullScreen();
                }else if(element.msRequestFullScreen){  //ie
                    element.msRequestFullScreen();
                }
            }
            this.fullscreen =!this.fullscreen;
        },
      //获取图片地址
        getUserAvator1(srcUrl){ 
            return srcUrl ? this.$store.state.configure.HOST+srcUrl : this.$store.state.configure.HOST+"/img/AvatorImages/1.png";
        },
      goMenuList(path){
          if(path == 0){
            this.$store.commit('setLoginIn',false);
            this.$router.go(0); 
            this.$store.commit('setIsActive',false); //收藏成功变颜色奥
          }else{
              this.$router.push({path:path});
          }
      }
  }

}
</script>

<style lang="scss" scoped>
@import '../assets/css/the-header.scss';
.navbar{
  position: relative;
  left: 25%;
}
/* 全屏设置 */
.btn-fullscreen{
    transform: rotate(45deg);
    margin: 2px;
    font-size: 30px;
    color:#FF9999;
}        
</style>
