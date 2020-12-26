<template>
    <div class="setting">
        <div class="leftCol">
            <div class="settingMainHeader">
                设置
            </div>
            <br/>
            <ul class="setting-aside">
                <li v-for="(item,index) in settingList" :key="index" :class="{activeColor:activeName==item.name}"
                    @click="handleClike(item)">
                    {{item.name}}
                </li>
            </ul>
        </div>
        <div class="contentCol">
            <component :is="componentSrc"></component>
        </div>
    </div>
</template>


<script>
import Comment from '../components/Comment.vue'
import Info from '../components/Info'
import Upload from '../components/Upload'
export default {
    name:'setting',
    components:{
        Info,
        Upload,
    },
    data(){
        return{
            settingList:[
                {name: '个人信息',path: 'Info'},
                {name: '头像',path: 'Upload'},
            ],
            activeName:'个人信息',
            componentSrc: 'Info',
        }
    },
    mounted(){
    },    
    methods:{
        handleClike(item){
            this.activeName = item.name;
            this.componentSrc = item.path;
        }
    }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/setting.scss';
</style>