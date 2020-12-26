<template>
    <div class="sidebar">
        <el-menu
            class="sidebar-el-menu"
            :default-active="onRoutes"
            :collapse="collapse"
            background-color="#DCDFE6"
            text-color="#003366"
            active-text-color="#FF6666"
            router
            >
            <template v-for="item in items">
                <template>                    
                    <el-menu-item :index="item.index" :key="item.index">
                        <i :class="item.icon"></i>&nbsp;&nbsp;{{item.title}}
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
import bus from "../assets/js/bus"
export default {
    data(){
        return{
            collapse:false,
            items:[
                {
                    icon: 'el-icon-s-home',
                    index: 'Info',
                    title: '系统首页',
                },
                                {
                    icon: 'el-icon-s-custom',
                    index: 'Consumer',
                    title: '用户管理',
                },
                                {
                    icon: 'el-icon-service',
                    index: 'Singer',
                    title: '歌手管理',
                },
                                {
                    icon: 'el-icon-notebook-2',
                    index: 'SongList',
                    title: '歌单管理',
                }
            ]
        }
    },
    computed:{
        onRoutes(){
            return this.$route.path.replace('/','');    //转向到/info
        }
    },
    created(){
        //通过Bus进行组件之间的通信来折叠侧边栏
        bus.$on('collapse',msg =>{
            this.collapse = msg;    /* 收到信息之后让collapse属性发生变化 */
        })
    }
}
</script>


<style scoped>
.el-menu--collapse{ /* 当菜单收缩时element会给菜单添加el-menu--collapse类，这时候把文字和下拉图标隐藏掉即可 */
    width: 48px;

}
.sidebar-el-menu:not(.el-menu--collapse){
    width: 150px;
}
.sidebar::-webkit-scrollbar{
    width: 0px;
}
.sidebar > ul{
    height: 100%;
}
.sidebar {
    display: block; /* 块状 */
    position: absolute; /* 绝对定位 */
    left: 0;
    top: 70px;
    bottom: 0;
    background-color: #CCCCCC;
    overflow-y: scroll;
    font-family:"微软雅黑";   
     border-radius: 0px;
}
</style>