<template>
    <div class="singer">
        <ul class="singer-header">
            <li v-for="(item,index) in singerStyle" :key="index" @click="handleChangeView(item)" :class="{active:item.name==activeName}">
                {{item.name}}
            </li>
        </ul>
        <div>
            <content-list :contentList="data"></content-list>
            <div class="pagination">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <el-pagination @current-change="handleCurrentChange" background layout="total,prev,pager,next"
                        :current-page="currentPage" :page-size="pageSize" :total="albumDatas.length">
                    </el-pagination>
            </div>            
        </div>


    </div>
</template>
<script>
import ContentList from '../components/ContentList'
import {getAllSinger,getSingerOfSex} from '../api/index'
import {mixin} from '../mixins'
import {singerStyle} from '../assets/data/singer'
export default {
    name: 'singer',
    components:{
        ContentList
    },
    data(){
        return{
            albumDatas: [], //歌手的数据
            pageSize: 10,   //一页展示的数据
            currentPage: 1,  //当前页 默认第一页
            singerStyle, //歌手风格
            activeName: '全部歌手', //默认歌手页面的标记 高亮
        }
    },
    mounted(){
        this.singerStyle = singerStyle;
        this.getSingerList();
    },
    computed:{
        //计算当前表格中的数据
        data(){
           return this.albumDatas.slice((this.currentPage - 1) * this.pageSize,this.currentPage * this.pageSize);
        }
    },
    methods:{
        getSingerList(){
                getAllSinger()
                    .then(res =>{
                        this.currentPage = 1;                        
                        this.albumDatas = res;
                })
        },
        //获取当前页
        handleCurrentChange(val){          
            this.currentPage = val;
        },
        //根据类型(性别)显示对应的歌手
        handleChangeView(item){
            this.activeName = item.name;
            this.albumDatas =  [];
            if(item.name == '全部歌手'){
                this.getSingerList();
            }else{
                this.getSingerListOfSex(item.type);
            }
        },
        //歌手性别
        getSingerListOfSex(sex){
            getSingerOfSex(sex).then(res =>{
                this.currentPage = 1;
                this.albumDatas = res ; 
            })
        }
    }
}

</script>

<style lang="scss" scoped>
@import '../assets/css/singer.scss';
</style>