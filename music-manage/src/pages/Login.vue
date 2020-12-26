<template>
  <div class="login-wrap" >
    <div class="ms-title">
      <a href="https://github.com/Ges0ng" title="Github">@Paracosm</a> 后台管理系统
    </div>
    <div class="ms-login" >
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
        <el-form-item prop="username">
          <el-input clearable v-model="ruleForm.username" placeholder="用户名" ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" clearable show-password v-model="ruleForm.password" placeholder="密码" 
              @keydown.enter.native="submitForm"></el-input>
        </el-form-item>
        <div class="login-btn" >
          <el-button type="primary" @click="submitForm">登录</el-button>  <!-- v-loading.fullscreen.lock="fullscreenLoading" -->
        </div>
      </el-form>
    </div>
<!--     <div class="github">
      <a href="https://github.com/Ges0ng">@Paracosm</a>
    </div> -->
  </div>
</template>

<script>
import {mixin} from "../mixins/index";
import {getLoginStatus} from "../api/index";
export default {
  mixins:[mixin],
  data: function(){
    return {
      ruleForm:{  //默认的
        username: "",
        password: ""
      },
      rules:{
        username:[
          {required:true,message:"请输入用户名",trigger:"blur"}
        ],
        password:[
          {required:true,message:"请输入密码",trigger:"blur"}
        ]
      },
      //读取页面转圈
      //fullscreenLoading: false,
    };
  },
  methods:{
    submitForm(){
      console.log("ok！");
      let params = new URLSearchParams();
      params.append("name",this.ruleForm.username);
      params.append("password",this.ruleForm.password);
      getLoginStatus(params)
        .then((res) =>{
          if(res.code == 1){
            localStorage.setItem('username',this.ruleForm.username);
            /* load转圈
              this.fullscreenLoading = true;
              setTimeout(() => {
                this.fullscreenLoading = false;
              }, 2000); */
            //跳转
            this.$router.push("/Info");
            this.notify(res.msg,"success");
          }else{
            this.notify(res.msg,"error");
          }
        });
        
    }
  }
}
</script>

<style scoped>
a{  /* 超链接颜色 */
  color: #fff;
}
/* .github{
  position: absolute;
  left: 54%;
  bottom: 41%;
  font-size: 20px;
  color: black;
} */
.ms-login{
  opacity: 0.6;
}
.login-wrap {
  position: relative;
  background: url("../assets/img/background.jpg");
  background-attachment: fixed;
  background-position: center;
  background-size: cover;
  width: 100%;
  height: 100%;
}
.ms-title {
  position: absolute;
  top: 50%;
  width: 100%;
  margin-top: -230px;
  text-align: center;
  font-size: 30px;
  font-weight: 600;
  color: #fff;
}
.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 300px;
  height: 160px;
  margin: -185px;
  margin-top: -150px;
  padding: 40px;
  border-radius: 5px;
  background: #abb1b4;
  border-radius: 50px;

}
.login-btn{
  text-align: center;
}
.login-btn button{
  width:100%;
  height: 36px;
}
</style>
