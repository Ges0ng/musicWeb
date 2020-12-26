<template>
<div>
    <loginLogo/>
    <div class="signUp">
        <div class="signUp-head">
            <span>登 录</span>
        </div>
        <el-form :model="loginForm" ref="loginForm" label-width="80px" class="demo-ruleForm" :rules="rules">
            <el-form-item prop="username" label="用户名:" >
                <el-input placeholder="用户名" v-model="loginForm.username" ></el-input>
            </el-form-item>

            <el-form-item prop="password" label="密码:" >
                <el-input type="password" placeholder="密码" v-model="loginForm.password" ></el-input>
            </el-form-item>

            <!-- <el-form-item prop="code" label="验证码:" >
                <tr>
					<td valign="middle" align="left">
						<el-input type="text" v-model="code" class="inputgri" name="number" />
					</td>
					<td>
						<img id="num" :src="imagePath" />
						<a href="javascript:;" @click="getImg()">换一张</a>
					</td>
				</tr>
            </el-form-item> -->
            

            <div class="login-btn">
                <el-button @click="goSignUp">注册</el-button>
                <el-button type="primary" @click="handleLoginIn">登录</el-button>
            </div>
        
        </el-form>
    </div>
</div>
</template>

<script>
import loginLogo from '../components/LoginLogo'
import {mixin} from '../mixins/index'
import {loginIn} from '../api/index'
export default {
    name : 'login-in',
    mixins:[mixin],
    components:{
        loginLogo,
    },
    data(){
        return {
            imagePath: "",
            loginForm:{
                username:'',
                password:'',
            },
            rules:{
                username: [
                    { required: true, message: '请输入正确的用户名喔', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入正确的密码喔', trigger: 'blur' }
                ],
            },
            
        }
    },
    created(){
    },
    mounted(){
        this.changeIndex('登录');
    },
    methods:{
            /* //更换验证码
			getImg(){
				this.getSrc();
			},
			//获取验证码
			getSrc(){
				var _this = this;
				axios.get("/consumer/getImage?time="+Date.now()).then(res => {
					_this.imagePath = res.data;

				});
			}, */
        //登录
        handleLoginIn(){
            let _this = this;
            let params = new URLSearchParams();
            params.append('username',this.loginForm.username);
            params.append('password',this.loginForm.password);
            loginIn(params).then( res => {
                if(res.code == 1){
                    _this.notify(res.msg,'success');
                    _this.$store.commit('setLoginIn',true);
                    _this.$store.commit('setUserId',res.userMsg.id);
                    _this.$store.commit('setUsername',res.userMsg.username);
                    _this.$store.commit('setAvator',res.userMsg.avator);
                    
                    setTimeout(function(){  //等待2秒钟返回首页
                        _this.changeIndex('首页');
                        _this.$router.push({path:'/'});
                    },1000);
                }else{
                    _this.notify(res.msg,'error');
                }
            }).catch(err => {
                _this.notify("用户名或密码错误",'error');
            })
        },
        goSignUp(index){
            this.changeIndex('注册');
            this.$router.push({path:'/sign-up'});
        },
        changeIndex(value){
            this.$store.commit('setActiveName',value);
        },
    }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/sign-up.scss';
</style>