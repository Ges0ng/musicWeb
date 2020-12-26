<template >
    <div>
        <the-header></the-header>
        <the-aside></the-aside>
        <div class="content-box" :class="{'content-collapse':collapse}">
        <router-view></router-view>
        </div>
    </div>
</template>

<script>
import TheHeader from './TheHeader';
import TheAside from './TheAside';
import bus from "../assets/js/bus";
export default {
    components:{
        TheHeader,
        TheAside
    },
    data(){
        return{
            collapse:false
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
    
</style>