<template>
    <div class="table">
        <div class="crums" style="font-size:25px;">
            
            <i class="el-icon-tickets"></i>
            歌曲资源
        </div>
        <div class="container">
            <div class="handle-box">
            <el-button type="primary" size="mini" @click="delAll" style="float: right;" class="el-icon-delete-solid">批量删除</el-button>
                <el-button type="primary" icon="el-icon-arrow-left" @click="back">上一页</el-button>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <el-input  prefix-icon="el-icon-search" v-model="select_word" size="mini" placeholder="请输入歌曲"  class="handle-input">
                </el-input>
                <el-button type="primary" size="mini" @click="centDialogVisible = true" style="float: right;" class="el-icon-circle-plus-outline">添加歌曲</el-button>
            </div>
        </div>

        <el-table size="mini" ref="multipleTable" border style="width:100%" height="710px" :data="data" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40"></el-table-column>

            <el-table-column label="歌曲图片" width="110" align="center">
                <template slot-scope="scope">
                    <div class="song-img">
                        <img :src="getSongPicUrl(scope.row.pic)" style="width:100%"/>
                    </div>
                    <div class="play" @click="setSongUrl(scope.row.url, scope.row.name)">
                        <div v-if="toggle == scope.row.name">
                            <svg class="icon">
                                <use xlink:href="#icon-zanting"></use>
                            </svg>
                        </div>
                        <div v-if="toggle != scope.row.name">
                            <svg class="icon">
                                <use xlink:href="#icon-bofanganniu"></use>
                            </svg>
                        </div>
                    </div>
<!--                     <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatorUpload" 
                        :on-success="handleAvatorSuccess">
                      <el-button size="mini" class="el-icon-picture">更新图片</el-button>
                    </el-upload> -->
                </template>
            </el-table-column>
            <el-table-column prop="name" label="歌手-歌名" width="120" align="center"></el-table-column>
            <el-table-column prop="introduction" label="专辑" width="150" align="center"></el-table-column>        
            <el-table-column prop="lyric" label="歌词" align="center">
                <template slot-scope="scope">
                    <ul style="height:150px;overflow:scroll;">
                        <li v-for="(item,index) in parseLyric(scope.row.lyric)" :key="index">
                            {{item}}
                        </li>
                    </ul>
                    
                </template>
            </el-table-column>

            <el-table-column prop="lyric" label="更新资源" align="center">
                <template slot-scope="scope">
                    <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatorUpload" 
                        :on-success="handleAvatorSuccess">
                      <el-button size="mini" class="el-icon-picture-outline">图 片</el-button>
                    </el-upload>   
                    <br/>             
                    <el-upload :action="uploadSongUrl(scope.row.id)" :before-upload="beforeSongUpload" 
                        :on-success="handleSongSuccess">
                      <el-button size="mini" class="el-icon-service">歌 曲</el-button>
                    </el-upload> 
                </template>
            </el-table-column>            
            
            <el-table-column label="操作" width="150" align="center">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.row)" class="el-icon-edit">编 辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)" class="el-icon-delete"></el-button>
                    <br/><br/>
                    <el-button size="mini" class="el-icon-video-play"> 试 听</el-button>
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


        <el-dialog title="添加歌曲" :visible.sync="centDialogVisible" width="400px" center > 
            <el-form :model="registerForm" ref="registerForm" label-width="80px"  id="tf">
                <div>
                    <label>歌名</label>
                    <el-input v-model="registerForm.name" type="text" name="name" ></el-input>                    
                </div>  
                <br/>                                        
                <div>
                    <label>专辑</label>
                    <el-input v-model="registerForm.introduction" type="text" name="introduction"></el-input>                    
                </div>      
                <br/> 
                <div>
                    <label>歌词</label>
                    <el-input v-model="registerForm.lyric" type="textarea" name="lyric"></el-input>                    
                </div>     
                <br/>
                <div>
                    <label>上传歌曲</label>
                    <br/>
                    <input type="file" name="file"/> 
                </div>                


            </el-form>
            <span slot="footer">
                <el-button size="mini" @click="centDialogVisible = false">取消</el-button>
                <el-button size="mini" @click="addSong">确定</el-button>                
            </span>
        </el-dialog>


        <el-dialog title="更新歌曲信息" :visible.sync="editVisble" width="400px" center > 
            <el-form :model="form" ref="form" label-width="80px">
                <el-form-item prop="name" label="歌手-歌名"  size="mini">
                <el-input v-model="form.name" placeholder="歌手-歌名"></el-input>                    
                </el-form-item>

                <el-form-item prop="introduction" label="专辑"  size="mini">
                    <el-input v-model="form.introduction" placeholder="专辑"></el-input>                 
                </el-form-item>        
                <el-form-item prop="lyric" label="歌词"  size="mini">
                    <el-input v-model="form.lyric" placeholder="歌词" type="textarea"></el-input>                 
                </el-form-item>                                                         
            </el-form>
            <span slot="footer">
            
                <el-button size="mini" @click=" editVisble = false">取消</el-button>
                <el-button size="mini" @click="editSave">确定</el-button>    
            </span>
        </el-dialog>

        <el-dialog title="删除歌曲" :visible.sync="delVisible" width="300px" center > 
            <div align="center">删除之后不可恢复,确定删除?</div>
            <span slot="footer">
                <el-button size="mini" @click="delVisible = false">取消</el-button>
                <el-button size="mini" @click="deleteRow">确定</el-button>    
            </span>
        </el-dialog>




    </div>
</template>


<script>
import '@/assets/js/iconfont.js';
import { songOfSingerId, updateSong, delSong } from '../api/index';
import {mixin} from "../mixins/index";
import axios from 'axios';
import{mapGetters} from 'vuex';

export default {    
    mixins: [mixin],
    data(){
        return{       
            singerId:'',    //歌手id
            singerName:'',  //歌手名
            centDialogVisible: false,   //添加弹窗
            editVisble: false,  //编辑弹窗
            delVisible:false,   //删除弹窗是否显示
            registerForm:{  //添加框
                name:'',
                singerName:'1',
                introduction:'',
                lyric: ''
            },
            form:{  //编辑框
                id:'',
                name:'',
                introduction:'',
                lyric:''
            },
            tableData: [ ],
            tempData:[ ],
            select_word:'',
            pageSize: 4,    //分页每页大小
            currentPage:1,   //当前页
            idx: -1,   //当前选择项,
            multipleSelection: [], //哪些项已经打勾了
            toggle: false   //播放器显示图标状态

        }
    },
    computed:{  //页面渲染完
        ...mapGetters([
            'isPlay'
        ]),
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
        this.singerId = this.$route.query.id;
        this.singerName = this.$route.query.name;
        this.getData();
    },
    destroyed(){
         this.$store.commit('setIsPlay',false);
    },
    methods:{
        //解析歌词
        parseLyric(text){
            let lines = text.split("\n");
            let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g;
            let result = [];
            for(let item of lines){
                let value = item.replace(pattern,'');
                result.push(value);
            }
            return result;
        },
        //获取当前页
        handleCurrentChange(val){
            this.currentPage = val;
        },
        //查询所有歌曲
        getData(){
            this.tempData = [];
            this.tableData = [];
            /* axios.get("/singer/allSinger").then(res => {
                this.tableData = res.data;
            }) */
             songOfSingerId(this.singerId).then(res => {
                this.tableData = res;
                this.tempData = res;
                this.currentPage = 1;
            }) 
        },
        //添加歌曲
        addSong(){
            //传统方法
            let _this = this;
            var form = new FormData(document.getElementById('tf'));
            form.append('singerId',this.singerId);
            form.set('name',this.singerName+'-'+form.get('name'));
            if(!form.get('lyric')){
                form.set('lyric','[00:00:00]暂无歌词');
            }
            var req = new XMLHttpRequest();
            req.onreadystatechange = function(){
                //req.readyState == 4 获取到返回的完整数据
                //req.status == 200 和后台正常交互完成
                if(req.readyState == 4 && req.status == 200){
                    let res = JSON.parse(req.response);
                    if(res.code){
                        _this.getData();
                        _this.registerForm = {};
                        _this.notify(res.msg,'success');
                    }else{
                         _this.notify('保存失败','error');
                    }
                }
            }
            req.open('post',`${_this.$store.state.HOST}/song/add`,false);
            req.send(form);
            _this.centDialogVisible = false;
        },
        handleEdit(row){    //弹出编辑页面
            this.editVisble = true;
            this.form={
                id: row.id,
                name : row.name,
                introduction:row.introduction,
                lyric:row.lyric,
            } 
        },
        editSave(){   //保存编辑页面修改的数据
             let d = new Date(this.form.birth);
            let dateTime = d.getFullYear()+'-'+(d.getMonth()+1)+'-'+d.getDate();        
            let params = new URLSearchParams();
            params.append('id',this.form.id);               
            params.append('name',this.form.name);
            params.append('introduction',this.form.introduction);
            params.append('lyric',this.form.lyric);

            updateSong(params).then(res => {
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
            this.centDialogVisible = false; 
        },
        //更新图片
        uploadUrl(id){
            return `${this.$store.state.HOST}/song/updateSongPic?id=${id}`;
        },
        //删除歌曲*1
        deleteRow(){
             delSong(this.idx).then(res => {
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
            this.delVisible =false; 
        },
        back(){
            this.$router.push(`/Singer`);
        },
                //上传歌曲之前的校验
        beforeSongUpload(file){
            var testMsg = file.name.substring(file.name.lastIndexOf('.') + 1);
            if(testMsg != 'mp3'){
                this.$message({
                    message:'上传文件只能是mp3格式',
                    type:'error'
                });
                return false;
            }
            return true;
        },
        //上传歌曲成功之后
        handleSongSuccess(res){
            let _this = this;
            if(res.code == 1){
                _this.getData();
                _this.notify(res.msg, 'success');
            }else{
                _this.notify(res.msg, 'error');
            }
        },
        //更新歌曲url
        uploadSongUrl(id){
            return `${this.$store.state.HOST}/song/updateSongUrl?id=${id}`;
        },
        //切换播放歌曲
        setSongUrl(url,name) {
            this.toggle = name;
            this.$store.commit('setUrl',this.$store.state.HOST + encodeURI("/song/"+url));
            if(this.isPlay){
                this.$store.commit('setIsPlay',false);
            }else{
                this.$store.commit('setIsPlay',true);
            }
        }
    }
}
</script>

<style scoped>
.song-img{
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
.play{
    position: absolute;
    z-index: 100;
    width: 80px;
    height: 80px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    top: 40px;
    left: 15px;
}
.icon{
    width: 2em;
    height: 2em;
    color: white;
    fill: currentColor;
    overflow: hidden;
}
</style>