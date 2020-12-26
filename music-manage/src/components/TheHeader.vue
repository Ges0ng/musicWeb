<template>
    <div class="header" >
        <!-- 折叠图片按钮 -->
        <div class="collapse-btn" @click="collapseChange">
            <i class="el-icon-menu" title="侧边栏"></i>
        </div>
        <div class="logo" >
            <a href="https://github.com/Ges0ng" title="Github网址" style="color:white">
            Paracosm
            </a> 
            后台管理页面 
            </div>
        <div class="header-right">
            <div class="btn-fullscreen" @click="handleFullScreen">
                <el-tooltip :content="fullscreen?'取消全屏':'全屏'" placement="bottom">
                    <i class="el-icon-rank"></i>
                </el-tooltip>
            </div>
<!--             &nbsp;&nbsp;  -->
<!--            <el-dropdown  class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                    {{username}}
                    <i class="el-icon-caret-bottom"></i>
                    </span>

                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="logout">退出登录</el-dropdown-item>

                    </el-dropdown-menu>
            </el-dropdown>
            <div class="user-avator">
                <img src="../assets/img/user.jpg"/>        
            </div>
 -->
            <div class="user-avator" >
                <el-dropdown  class="user-name" trigger="hover" @command="handleCommand" >  <!-- trigger="click" 可以设置点击 -->
                        <img src="../assets/img/user.jpg" class="el-icon-caret-bottom" title="个人信息" />

                        <el-dropdown-menu slot="dropdown"  >
                            <!-- 跳转到个人页面 -->
                            <el-dropdown-item class="el-dropdown-link">&nbsp;{{username}}</el-dropdown-item>
                            <el-dropdown-item command="logout" class="el-icon-switch-button">注销</el-dropdown-item>
                        </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>
    </div>

</template>


<script>
import bus from "../assets/js/bus"
export default {
    data(){
        return{
            collapse:false,
            fullscreen:false
        }
    },
    computed:{
        username(){
            if(localStorage.getItem('username')){
                return localStorage.getItem('username');
            }else{
                this.$router.push("/");
                alert("请先登录！");
            }
        }
    },
    methods:{
        //侧边栏折叠
        collapseChange(){   //改变其状态
            this.collapse = !this.collapse;
            bus.$emit('collapse',this.collapse);
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
        handleCommand(command){
            if(command == "logout"){
                localStorage.removeItem('username');
                this.$router.push("/");
            }
        }
    }
}
</script>


<style scoped>
.el-dropdown-link{
    color: #FF6666;
    cursor: pointer;
    font-size: 20px;
}
/* 头像下拉框 */
.user-name{
    margin-left: 10px;

}
/* 头像 */
.user-avator img{
    display: block;
    width: 40px;
    height: 40px;
    border-radius: 50%;

}
.user-avator{
    margin-left: 20px; 
    
}
/* 全屏设置 */
.btn-fullscreen{
    transform: rotate(45deg);
    margin-right: 5px;
    font-size: 24px;
}
/* 头标签 */
.header-right{
    float: right;
    padding-right: 50px;
    display: flex;
    height: 70px;
    align-items: center;
}
.header{
    position: relative;
    background-color:#303133;
    box-sizing: border-box; /* 带阴影 */
    width: 100%;
    height: 70px;
    font-size: 22px;
    color: #FFFFFF;
    font-family:"微软雅黑";
}
.collapse-btn{
    float: left;
    padding: 0 21px;
    cursor: pointer;
    line-height: 70px;
}
.header .logo{
    float: left;
    font-family:"微软雅黑";
    line-height: 70px;
}
</style>