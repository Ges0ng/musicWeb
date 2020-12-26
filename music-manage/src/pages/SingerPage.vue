<template>
    <div class="table">
        <div class="container">
            <div class="handle-box">
            <el-button type="primary" size="mini" @click="delAll" style="float: right;" class="el-icon-delete-solid">批量删除</el-button>

                <el-input  prefix-icon="el-icon-search" v-model="select_word" size="mini" placeholder="请输入歌手"  class="handle-input">
                </el-input>
                <el-button type="primary" size="mini" @click="centDialogVisible = true" style="float: right;" class="el-icon-circle-plus-outline">添加歌手</el-button>
            </div>
        </div>

        <el-table size="mini" ref="multipleTable" border style="width:100%" height="710px" :data="data" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40"></el-table-column>

            <el-table-column label="歌手图片" width="110" align="center">
                <template slot-scope="scope">
                    <div class="singer-img">
                        <img :src="getsingerPicUrl(scope.row.pic)" style="width:100%"/>
                    </div>
                    <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatorUpload" 
                        :on-success="handleAvatorSuccess">
                      <el-button size="mini" class="el-icon-picture">修改头像</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="歌手" width="120" align="center"></el-table-column>
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
            <el-table-column prop="location" label="出生地" width="100" align="center"></el-table-column>
            <el-table-column label="简介" align="center">
                <template slot-scope="scope">
                    <p style="height:100px;overflow:scroll" align="center">{{scope.row.introduction}}</p>
                </template>
            </el-table-column>

            <el-table-column label="歌曲管理" align="center" width="110">
                <template slot-scope="scope">
                    <el-button size="mini" @click="songEdit(scope.row.id,scope.row.name)" >查看歌曲</el-button>
                </template>
            </el-table-column>

            <el-table-column label="操作" width="150" align="center">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.row)" class="el-icon-edit">编 辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)" class="el-icon-delete"></el-button>
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


        <el-dialog title="添加歌手" :visible.sync="centDialogVisible" width="400px" center  > 
            <el-form :model="registerForm" ref="registerForm" label-width="80px" :rules="rules">
                <el-form-item prop="name" label="歌手"  size="mini">
                <el-input v-model="registerForm.name" placeholder="歌手名"></el-input>                    
                </el-form-item>
                <el-form-item label="性别" size="medium">
                <el-radio-group v-model="registerForm.sex">
                    <el-radio-button :label="1" >男</el-radio-button>
                    <el-radio-button :label="0">女</el-radio-button>
                    <el-radio-button :label="2">组合</el-radio-button>
                    <el-radio-button :label="3">不明</el-radio-button>
                </el-radio-group>                                        
                </el-form-item>

                <el-form-item prop="birth" label="生日"  size="mini">
                    <el-date-picker type="date" v-model="registerForm.birth" placeholder="选择日期" style="width:100%"></el-date-picker>                 
                </el-form-item>
                <el-form-item prop="location" label="地区"  size="mini">
                    <el-input v-model="registerForm.location" placeholder="地区"></el-input>                 
                </el-form-item>      
                <el-form-item prop="introduction" label="简介"  size="mini">
                    <el-input v-model="registerForm.introduction" placeholder="简介" type="textarea"></el-input>                 
                </el-form-item>                                              
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click=" centDialogVisible = false">取消</el-button>                
                <el-button size="mini" @click="addSinger">确定</el-button>                
            </span>
        </el-dialog>


        <el-dialog title="更新歌手信息" :visible.sync="editVisble" width="400px" center > 
            <el-form :model="form" ref="form" label-width="80px">
                <el-form-item prop="name" label="歌手"  size="mini">
                <el-input v-model="form.name" placeholder="歌手名"></el-input>                    
                </el-form-item>
                <el-form-item label="性别" size="medium">
                <el-radio-group v-model="form.sex">
                    <el-radio-button :label="1" >男</el-radio-button>
                    <el-radio-button :label="0">女</el-radio-button>
                    <el-radio-button :label="2">组合</el-radio-button>
                    <el-radio-button :label="3">不明</el-radio-button>
                </el-radio-group>                                        
                </el-form-item>

                <el-form-item  label="生日"  size="mini">
                    <el-date-picker type="date" v-model="form.birth" placeholder="选择日期" style="width:100%"></el-date-picker>                 
                </el-form-item>
                <el-form-item prop="location" label="地区"  size="mini">
                    <el-input v-model="form.location" placeholder="地区"></el-input>                 
                </el-form-item>      
                <el-form-item prop="introduction" label="简介"  size="mini">
                    <el-input v-model="form.introduction" placeholder="简介" type="textarea"></el-input>                 
                </el-form-item>                                              
            </el-form>
            <span slot="footer">
            
                <el-button size="mini" @click=" editVisble = false">取消</el-button>
                <el-button size="mini" @click="editSave">确定</el-button>    
            </span>
        </el-dialog>

        <el-dialog title="删除歌手" :visible.sync="delVisible" width="300px" center > 
            <div align="center">删除之后不可恢复,确定删除?</div>
            <span slot="footer">
                <el-button size="mini" @click="delVisible = false">取消</el-button>
                <el-button size="mini" @click="deleteRow">确定</el-button>    
            </span>
        </el-dialog>




    </div>
</template>


<script>
import {setSinger,getAllSinger, updateSinger,delSinger} from '../api/index';
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
                name:'',
                sex:'1',
                birth:'',
                location:'',
                introduction:''
            },
            form:{
                id:'',
                name:'',
                sex:'',
                birth:'',
                location:'',
                introduction:''
            },
            tableData: [ ],
            tempData:[ ],
            select_word:'',
            pageSize: 4,    //分页每页大小
            currentPage:1,   //当前页
            idx: -1,   //当前选择项,
            multipleSelection: [], //哪些项已经打勾了
            rules:{
                name:[
                    {required:true, message:'请输入歌手名',trigger:'blur'},
                ],
                location:[
                    {required:true, message:'要记得输入地区喔',trigger:'blur'},
                ],
                introduction:[
                    {required:true, message:'要记得简介喔',trigger:'blur'},
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
        select_word:function(){
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
        //获取当前页
        handleCurrentChange(val){
            this.currentPage = val;
        },
        //查询所有歌手
        getData(){
            this.tempData = [];
            this.tableData = [];
            /* axios.get("/singer/allSinger").then(res => {
                this.tableData = res.data;
            }) */
            getAllSinger().then(res => {
                this.tableData = res;
                this.tempData = res;
                this.currentPage = 1;
            })
        },
        //添加歌手
        addSinger(){
            this.$refs['registerForm'].validate(valid => {
                if(valid){
            let d = this.registerForm.birth;
            let dateTime = d.getFullYear()+'-'+(d.getMonth()+1)+'-'+d.getDate();        
            let params = new URLSearchParams();         
            params.append('name',this.registerForm.name);
            params.append('sex',this.registerForm.sex);
            params.append('pic','/img/singerPic/1.png');
            //params.append('pic',this.registerForm.pic);
            params.append('birth',dateTime);
            params.append('location',this.registerForm.location);
            params.append('introduction',this.registerForm.introduction);

            setSinger(params).then(res => {
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
            this.form={
                id: row.id,
                name : row.name,
                sex: row.sex,
                birth: row.birth,
                location: row.location,
                introduction: row.introduction
            }
        },
        editSave(){   //保存编辑页面修改的数据
            this.$refs['form'].validate(valid =>{
            if(valid){        
            let d = new Date(this.form.birth);
            let dateTime = d.getFullYear()+'-'+(d.getMonth()+1)+'-'+d.getDate();        
            let params = new URLSearchParams();
            params.append('id',this.form.id);               
            params.append('name',this.form.name);
            params.append('sex',this.form.sex);
            params.append('birth',dateTime);
            params.append('location',this.form.location);
            params.append('introduction',this.form.introduction);

            updateSinger(params).then(res => {
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
            return `${this.$store.state.HOST}/singer/updateSingerPic?id=${id}`;
        },
        deleteRow(){
            delSinger(this.idx).then(res => {
                if(res == 1){
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
        songEdit(id,name){
            this.$router.push({path:`/Song`,query:{id,name}});
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