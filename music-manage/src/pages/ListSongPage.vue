<template>
    <div class="table">
        <div class="crums" style="font-size:25px;">
            
            <i class="el-icon-tickets"></i>
            歌单歌曲管理
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

        <el-table size="mini" ref="multipleTable" border style="width:100%" height="710px" :data="tableData" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40"></el-table-column>

            <el-table-column prop="name" label="歌手-歌名" align="center"></el-table-column>
            
            <el-table-column label="操作" width="150" align="center">
                <template slot-scope="scope">
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)" class="el-icon-delete"></el-button>
                    
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="添加歌曲" :visible.sync="centDialogVisible" width="400px" center > 
            <el-form :model="registerForm" ref="registerForm" label-width="80px"  id="tf">
                <el-form-item prop="singerName" label="歌手"  size="mini">
                <el-input v-model="registerForm.singerName" placeholder="歌手名"></el-input>                    
                </el-form-item>
                <el-form-item prop="songName" label="歌名"  size="mini">
                <el-input v-model="registerForm.songName" placeholder="歌曲名"></el-input>                    
                </el-form-item>                
            </el-form>
            <span slot="footer">
                <el-button size="mini" @click="centDialogVisible = false">取消</el-button>
                <el-button size="mini" @click="getSongId">确定</el-button>                
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
import { ListSongDetail,songOfSongId,songOfSongName,ListSongAdd,delListSong } from '../api/index';
import {mixin} from "../mixins/index";
import axios from 'axios';

export default {    
    mixins: [mixin],
    data(){
        return{       
            centDialogVisible: false,   //添加弹窗
            delVisible:false,   //删除弹窗是否显示
            registerForm:{  //添加框
                singerName:'',  //歌手名字
                songName:''    //歌曲名字
            },
            tableData: [ ],
            tempData:[ ],
            select_word:'',
            idx: -1,   //当前选择项,
            multipleSelection: [], //哪些项已经打勾了
            songListId: ''  //歌单id
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
        this.songListId = this.$route.query.id;
        this.getData();
    },
    methods:{
        //查询所有歌曲
        getData(){
            this.tempData = [];
            this.tableData = [];
            /* axios.get("/singer/allSinger").then(res => {
                this.tableData = res.data;
            }) */
             ListSongDetail(this.songListId).then(res => {
                for(let item of res){
                    this.getSong(item.songId);
                }
            }) 
        },
        //根据歌曲id查询歌曲对象,放到tableData和tempData
        getSong(id){
            songOfSongId(id).then(res => {
                this.tempData.push(res);
                this.tableData.push(res);
            })
            .catch(err => {
                console.log(err);
            });
            
        },
        //添加歌曲前的准备=>获取歌曲id
        getSongId(){
            let _this = this;
            var songOfName = _this.registerForm.singerName+"-"+_this.registerForm.songName;
            songOfSongName(songOfName).then(res => {
                _this.addSong(res[0].id);
            });
        },
        //添加歌曲
        addSong(songId){
            let _this = this;
            let params = new URLSearchParams();
            params.append('songId',songId);               
            params.append('songListId',this.songListId);

            ListSongAdd(params).then(res => {
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
            _this.centDialogVisible = false;
        },
        //删除歌曲*1
        deleteRow(){
             delListSong(this.idx,this.songListId).then(res => {
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
            this.delVisible = false; 
        },
        back(){
            this.$router.push(`/SongList`);
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