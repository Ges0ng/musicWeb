<template>
    <div class="table">
        <div class="container">
            <div class="handle-box">
            <el-button type="primary" size="mini" @click="delAll" style="float: right;" class="el-icon-delete-solid">批量删除</el-button>

                <el-input  prefix-icon="el-icon-search" v-model="select_word" size="mini" placeholder="筛选关键字"  class="handle-input">
                </el-input>
                <el-button type="primary" size="mini" @click="centDialogVisible = true" style="float: right;" class="el-icon-circle-plus-outline">添加歌单</el-button>
            </div>
        </div>

        <el-table size="mini" ref="multipleTable" border style="width:100%" height="700px" :data="data" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40"></el-table-column>

            <el-table-column label="歌单图片" width="110" align="center">
                <template slot-scope="scope">
                    <div class="songList-img">
                        <img :src="getSongListPicUrl(scope.row.pic)" style="width:100%"/>
                    </div>
                    <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatorUpload" 
                        :on-success="handleAvatorSuccess">
                      <el-button size="mini" class="el-icon-picture">修改头像</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column prop="title" label="标题" width="120" align="center"></el-table-column>

            <el-table-column label="简介" align="center">
                <template slot-scope="scope">
                    <p style="height:100px;overflow:scroll" align="center">{{scope.row.introduction}}</p>
                </template>
            </el-table-column>
            <el-table-column prop="title" label="风格" width="120" align="center"></el-table-column>            
            <el-table-column label="歌曲管理" align="center" width="110">
                <template slot-scope="scope">
                    <el-button size="mini" @click="songEdit(scope.row.id)" >歌曲管理</el-button>
                </template>
            </el-table-column>

            <el-table-column label="评论" width="80" align="center">
                 <template slot-scope="scope">
                <el-button size="mini" @click="getComment(data[scope.$index].id)" >评论</el-button>
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


        <el-dialog title="添加歌单" :visible.sync="centDialogVisible" width="400px" center > 
            <el-form :model="registerForm" ref="registerForm" label-width="80px">
                <el-form-item prop="title" label="歌单"  size="mini">
                <el-input v-model="registerForm.title" placeholder="歌单名"></el-input>                    
                </el-form-item>
               
                <el-form-item prop="introduction" label="简介"  size="mini">
                    <el-input v-model="registerForm.introduction" placeholder="简介" type="textarea"></el-input>                 
                </el-form-item>

                <el-form-item prop="style" label="风格"  size="mini">
                <el-input v-model="registerForm.style" placeholder="风格"></el-input>                    
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click=" centDialogVisible = false">取消</el-button>                
                <el-button size="mini" @click="addSongList">确定</el-button>                
            </span>
        </el-dialog>


        <el-dialog title="更新歌单信息" :visible.sync="editVisble" width="400px" center > 
            <el-form :model="form" ref="form" label-width="80px">
                <el-form-item prop="title" label="歌单"  size="mini">
                <el-input v-model="form.title" placeholder="歌单名"></el-input>                    
                </el-form-item>
                
                <el-form-item prop="introduction" label="简介"  size="mini">
                    <el-input v-model="form.introduction" placeholder="简介" type="textarea"></el-input>                 
                </el-form-item>                                  

                 <el-form-item prop="style" label="风格"  size="mini">
                <el-input v-model="form.style" placeholder="风格"></el-input>                    
                </el-form-item>            
            </el-form>
            <span slot="footer">
            
                <el-button size="mini" @click="editVisble = false">取消</el-button>
                <el-button size="mini" @click="editSave">确定</el-button>    
            </span>
        </el-dialog>

        <el-dialog title="删除歌单" :visible.sync="delVisible" width="300px" center > 
            <div align="center">删除之后不可恢复,确定删除?</div>
            <span slot="footer">
                <el-button size="mini" @click="delVisible = false">取消</el-button>
                <el-button size="mini" @click="deleteRow">确定</el-button>    
            </span>
        </el-dialog>




    </div>
</template>


<script>
import {getAllSongList,setSongList, updateSongList,delSongList} from '../api/index';
import {mixin} from "../mixins/index";
import axios from 'axios';
export default {    
    mixins: [mixin],
    data(){
        return{          
            centDialogVisible:false,   //添加弹窗
            editVisble: false,  //编辑弹窗
            delVisible:false,   //删除弹窗是否显示
            registerForm:{
                title:'',
                introduction:'',
                style:''
            },
            form:{
                id:'',
                title:'',
                introduction:'',
                style:''
            },
            tableData : [ ],
            tempData : [ ],
            select_word:'',
            pageSize: 4,    //分页每页大小
            currentPage:1,   //当前页
            idx: -1,   //当前选择项,
            multipleSelection: [ ] //哪些项已经打勾了
        }
    },
    computed:{  //页面渲染完
        //计算当前搜索结果表里的数据
        data(){
            return this.tableData.slice((this.currentPage - 1) * this.pageSize , this.currentPage * this.pageSize)
        }
    },
    watch:{ //监控
        select_word:function(){ //根据关键字进行过滤
            if(this.select_word == ''){
                this.tableData = this.tempData;
            }else{ 
                this.tableData = [];
                for(let item of this.tempData){
                    if(item.title.includes(this.select_word)){
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
        //查询所有歌单
        getData(){
            this.tempData = [ ];
            this.tableData = [ ];
            getAllSongList().then(res => {
                this.tableData = res;
                this.tempData = res;
                this.currentPage = 1;
            })
        },
        //添加歌单
        addSongList(){
            let params = new URLSearchParams();         
            params.append('title',this.registerForm.title);
            params.append('pic','1.jpg');
            params.append('introduction',this.registerForm.introduction);
            params.append('style',this.registerForm.style);

            setSongList(params).then(res => {
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
        },
        handleEdit(row){    //弹出编辑页面
            this.editVisble = true;
            this.form={
                id: row.id,
                title : row.title,
                introduction: row.introduction,
                style: row.style

            }
        },
        editSave(){   //保存编辑页面修改的数据
            let params = new URLSearchParams();
            params.append('id',this.form.id);               
            params.append('title',this.form.title);
            params.append('introduction',this.form.introduction);
            params.append('style',this.form.style);

            updateSongList(params).then(res => {
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
            this.editVisble = false;
        },
        //更新图片
        uploadUrl(id){
            return `${this.$store.state.HOST}/songList/updateSongListPic?id=${id}`;
        },
        //删除一个歌单
        deleteRow(){
            delSongList(this.idx).then(res => {
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
        //转向歌曲管理页面
        songEdit(id,name){
            this.$router.push({path:`/ListSong`,query:{id}});
        },
        //转向评论管理页面
        getComment(id){
            this.$router.push({path: '/comment',query:{id}});
        }
    }
}
</script>

<style scoped>
.songList-img{
    width: 100%;
    height: auto;
    border-radius: 5px;
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