<template>
    <div class="table">
        <div class="container">
            <div class="handle-box">
            <el-button type="primary" size="mini" @click="delAll" style="float: right;" class="el-icon-delete-solid">批量删除</el-button>

                <el-input  prefix-icon="el-icon-search" v-model="select_word" size="mini" placeholder="筛选相关用户"  class="handle-input">
                </el-input>
                <el-button type="primary" size="mini" @click="centDialogVisible = true" style="float: right;" class="el-icon-circle-plus-outline">添加新用户</el-button>
            </div>
        </div>

        <el-table size="mini" ref="multipleTable" border style="width:100%" height="710px" :data="data" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40"></el-table-column>

            <el-table-column label="用户图片" width="110" align="center">
                <template slot-scope="scope">
                    <div class="singer-img">
                        <!--  -->
                            <img :src="getConsumerAvatorUrl(scope.row.avator)" style="width:100%"/>
                        <!--  -->
                    </div>
                    <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatorUpload" 
                        :on-success="handleAvatorSuccess">
                      <el-button size="mini" class="el-icon-picture">修改头像</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column prop="username" label="用户名" width="120" align="center"></el-table-column>
            <el-table-column label="性别" width="50px" align="center">
                <template slot-scope="scope">
                    {{changeSex(scope.row.sex)}}
                </template>
            </el-table-column>

            <el-table-column prop="birth" label="生日" width="120" align="center">
                <template slot-scope="scope">
                    {{attachBirth(scope.row.birth)}}
                </template>
            </el-table-column>
            <el-table-column prop="phoneNum" label="手机号" width="120" align="center"></el-table-column>
            <el-table-column prop="email" label="邮箱" width="240" align="center"></el-table-column>
            <el-table-column prop="introduction" label="签名" align="center"></el-table-column>
            <el-table-column prop="location" label="地区" width="100" align="center"></el-table-column>
            
            <el-table-column label="收藏" width="80" align="center">
                 <template slot-scope="scope">
                <el-button size="mini" @click="getCollect(data[scope.$index].id)" class="el-icon-star-off" circle></el-button>
                 </template>
            </el-table-column>

            <el-table-column label="操作" width="150" align="center">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.row)" class="el-icon-edit">编 辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)" class="el-icon-delete" circle></el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagination">
            <el-pagination 
                background 
                layout="total,prev,pager,next"
                :current-page="currentPage"
                :page-size="pageSize"
                :total="tableData.length"
                @current-change="handleCurrentChange">
            </el-pagination>
        </div>


        <el-dialog title="添加新用户" :visible.sync="centDialogVisible" width="400px" center > 
            <el-form :model="registerForm" ref="registerForm" label-width="80px" :rules="rules">
                <el-form-item prop="username" label="用户"  size="mini">
                <el-input v-model="registerForm.username" placeholder="用户名"></el-input>                    
                </el-form-item>
                <el-form-item prop="password" label="密码"  size="mini">
                <el-input type="password" v-model="registerForm.password" placeholder="密码"></el-input>                    
                </el-form-item>
                <el-form-item label="性别" size="medium">
                    <input type="radio" name="sex" value="1" v-model="registerForm.sex">&nbsp;男&nbsp;&nbsp;
                    <input type="radio" name="sex" value="0" v-model="registerForm.sex">&nbsp;女&nbsp;&nbsp;                                      
                </el-form-item>
                <el-form-item prop="phoneNum" label="手机"  size="mini">
                <el-input v-model="registerForm.phoneNum" placeholder="手机号码"></el-input>                    
                </el-form-item>
                <el-form-item prop="email" label="邮箱"  size="mini">
                <el-input v-model="registerForm.email" placeholder="邮箱地址"></el-input>                    
                </el-form-item>
                <el-form-item prop="birth" label="生日"  size="mini">
                    <el-date-picker type="date" v-model="registerForm.birth" placeholder="选择日期" style="width:100%"></el-date-picker>                 
                </el-form-item> 
                <el-form-item prop="introduction" label="签名"  size="mini">
                    <el-input v-model="registerForm.introduction" placeholder="签名"></el-input>                 
                </el-form-item>                   
                <el-form-item prop="location" label="地区"  size="mini">
                    <el-input v-model="registerForm.location" placeholder="地区"></el-input>                 
                </el-form-item>      
                                           
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click=" centDialogVisible = false">取消</el-button>                
                <el-button size="mini" @click="addConsumer">确定</el-button>                
            </span>
        </el-dialog>


        <el-dialog title="更新用户信息" :visible.sync="editVisble" width="400px" center > 
            <el-form :model="form" ref="form" label-width="80px" :rules="rules">
                <el-form-item prop="username" label="用户" size="mini">
                    <el-input v-model="form.username" placeholder="用户名"></el-input>
                </el-form-item>
                <el-form-item prop="password" label="密码" size="mini">
                    <el-input type="password" v-model="form.password" placeholder="密码"></el-input>
                </el-form-item>
                <el-form-item label="性别" size="mini">
                    <input type="radio" name="sex" value="0" v-model="form.sex">&nbsp;女&nbsp;&nbsp;
                    <input type="radio" name="sex" value="1" v-model="form.sex">&nbsp;男
                </el-form-item>
                <el-form-item prop="phoneNum" label="手机号" size="mini">
                    <el-input v-model="form.phoneNum" placeholder="手机号"></el-input>
                </el-form-item>
                <el-form-item prop="email" label="电子邮箱" size="mini">
                    <el-input v-model="form.email" placeholder="电子邮箱"></el-input>
                </el-form-item>
                <el-form-item prop="birth" label="生日" size="mini">
                    <el-date-picker type="date" placeholder="选择日期" v-model="form.birth" style="width:100%"></el-date-picker>
                </el-form-item>
                <el-form-item prop="introduction" label="签名" size="mini">
                    <el-input v-model="form.introduction" placeholder="签名"></el-input>
                </el-form-item>      
                <el-form-item prop="location" label="地区" size="mini">
                    <el-input v-model="form.location" placeholder="地区"></el-input>
                </el-form-item> 
            </el-form>
            <span slot="footer">
            
                <el-button size="mini" @click=" editVisble = false">取消</el-button>
                <el-button size="mini" @click="editSave">确定</el-button>    
            </span>
        </el-dialog>

        <el-dialog title="删除用户" :visible.sync="delVisible" width="300px" center > 
            <div align="center">删除之后不可恢复,确定删除?</div>
            <span slot="footer">
                <el-button size="mini" @click="delVisible = false">取消</el-button>
                <el-button size="mini" @click="deleteRow">确定</el-button>    
            </span>
        </el-dialog>
    </div>
</template>


<script>
import {delConsumer, getAllConsumer,setConsumer, updateConsumer} from '../api/index';
import {mixin} from "../mixins/index";
import axios from 'axios';
export default {    
    mixins: [mixin],
    data(){
        return{          
            centDialogVisible: false,   //添加弹窗
            editVisble: false,  //编辑弹窗
            delVisible:false,   //删除弹窗是否显示
            registerForm:{
                username:'',
                password:'',
                sex:'1',
                phoneNum:'',
                email:'',
                birth:'',
                introduction:'',
                location:'',
            },
            form:{
                id:'',
                username:'',
                password:'',
                sex:'',
                phoneNum:'',
                email:'',
                birth:'',
                introduction:'',
                location:'',
            },
            tableData: [ ],
            tempData:[ ],
            select_word:'',
            pageSize: 4,    //分页每页大小
            currentPage:1,   //当前页
            idx: -1,   //当前选择项,
            multipleSelection: [], //哪些项已经打勾了
            rules:{
                username:[
                    {required:true, message:'请输入用户名',trigger:'blur'},
                ],
                password:[
                    {required:true, message:'请输入密码',trigger:'blur'},
                ],
                phoneNum:[
                    {required:true, message:'请输入有效的手机号码',trigger:'blur'},
                ],
                email:[
                    {required:true, message:'请输入有效的邮箱',trigger:'blur'},
                ],
                introduction:[
                    {required:true, message:'要记得个性签名喔',trigger:'blur'},
                ],
                location:[
                    {required:true, message:'要记得输入地区喔',trigger:'blur'},
                ],
            }
        }
    },
    computed:{  //页面渲染完
        //计算当前搜索结果表里的数据
        data(){
            return this.tableData.slice((this.currentPage-1)*this.pageSize,this.currentPage*this.pageSize)
        }
    },
    watch:{ //监控
        //搜索框里面的内容发生变化的时候，搜索结果table列表的内容跟着它的内容发生变化
        select_word: function(){
            if(this.select_word == ''){
                this.tableData = this.tempData;
            }else{
                this.tableData = [];
                for(let item of this.tempData){
                    if(item.name.includes(this.select_word)){
                        this.tableData.push(item);
                    }
                }
            }
        }
    },
     created(){
        this.getData();
    },
    methods:{
        errorHandler() {    //图片加载失败默认
            return true;
        },
        //获取当前页
        handleCurrentChange(val){
            this.currentPage = val;
        },
        //查询所有用户
        getData(){
            this.tempData = [];
            this.tableData = [];

            getAllConsumer().then(res => {
                this.tableData = res;
                this.tempData = res;
                this.currentPage = 1;
            })
        },
        //添加用户
        addConsumer(){

            this.$$refs['registerForm'].validate(valid => {
                if(valid){
            let d = this.registerForm.birth;
            let dateTime = d.getFullYear()+'-'+(d.getMonth()+1)+'-'+d.getDate();        
            let params = new URLSearchParams();         
            params.append('username',this.registerForm.username);
            params.append('password',this.registerForm.password);
            params.append('sex',this.registerForm.sex);
            params.append('phoneNum',this.registerForm.phoneNum);
            params.append('email',this.registerForm.email);
            params.append('birth',dateTime);           
            params.append('introduction',this.registerForm.introduction);  
            params.append('location',this.registerForm.location);                       
            params.append('avator','1.png');

            setConsumer(params).then(res => {
                if(res.code == 1){
                    this.getData();                    
                    this.notify(res.msg,"success");
                }else{
                    this.notify(res.msg,"error");                    
                }
            })
            .catch(err => {
                console.log(err);
            });
            this.centDialogVisible =false;
                }
            })

            
        },
        handleEdit(row){    //弹出编辑页面
            this.editVisble = true;
            this.form = {
                id: row.id,
                username: row.username,
                password: row.password,
                sex: row.sex,
                phoneNum: row.phoneNum,
                email: row.email,
                birth: row.birth,
                introduction: row.introduction,
                location: row.location
            }
        },
        editSave(){   //保存编辑页面修改的数据
            this.$refs['form'].validate(valid =>{
                if(valid){
                    let d = new Date(this.form.birth);
                    let datetime = d.getFullYear()+'-'+(d.getMonth()+1)+'-'+d.getDate();
                    let params = new URLSearchParams();
                    params.append('id',this.form.id);
                    params.append('username',this.form.username);
                    params.append('password',this.form.password);
                    params.append('sex',this.form.sex);
                    params.append('phoneNum',this.form.phoneNum);
                    params.append('email',this.form.email);
                    params.append('birth',datetime);
                    params.append('introduction',this.form.introduction);
                    params.append('location',this.form.location);

            updateConsumer(params).then(res => {
                if(res.code == 1){
                    this.getData();
                    this.notify(res.msg,"success");
                }else{
                    this.notify(res.msg,"error");                    
                }
            })
            .catch(err => {
                console.log(err);
            });
            this.editVisble =false;
                }
            })
        },
        uploadUrl(id){
            return `${this.$store.state.HOST}/consumer/updateConsumerAvator?id=${id}`;
        },
        deleteRow(){
            delConsumer(this.idx).then(res => {
                if(res.code == 1){
                    this.getData();
                    this.notify("删除成功","success");
                }else{
                    this.notify("删除失败","error");                    
                }
            })
            .catch(err => {
                console.log(err);
            });
            this.delVisible =false;
        },
        //转向收藏列表
        getCollect(id){
            this.$router.push({path: '/collect',query:{id}});
        }
    }
}
</script>

<style scoped>
.singer-img{
    width: 100%;
    height: auto;
    border-radius: 50%;
    margin-bottom: 5px;
    overflow: hidden;
}
.handle-box{
    margin-bottom: 20px;
    
}
.handle-input{
    width: 300px;
    display: inline-block;
}
.pagination{
    display: flex;
    justify-content: center;
}
</style>