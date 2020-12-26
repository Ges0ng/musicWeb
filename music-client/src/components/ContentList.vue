<template>
    <div class="content-list">
        <ul class="section-content">
            <li class="content-item" v-for="(item,index) in contentList" :key="index">
                <div class="kuo" @click="goAlbum(item,item.name)">
                        <img class="item-img" :src="attachImageUrl(item.pic)"/>
                        <div class="mask">
                            <svg class="icon">
                            <use xlink:href="#icon-bofang"></use>
                            </svg>
                        </div>
                </div>
                <p class="item-name">{{item.name||item.title}}</p>
            </li>
        </ul>
    </div>
</template>

<script>
import {mixin} from '../mixins';
export default {
    name: 'content-list',
    mixins:[mixin],
    props: ['contentList'],
    methods:{
        //配路径
        goAlbum(item,type){
            this.$store.commit("setTempList",item);
            if(type){       //歌手
                this.$router.push({path:`singer-album/${item.id}`});
            }else{      //歌单
                this.$router.push({path:`song-list-album/${item.id}`});
            }
        }
    },
}
</script>

<style lang="scss" scoped> /* 引入的scss语法，所以需要加lang=scss */
@import '../assets/css/content-list.scss';
.item-name{
    align-items: center;
    font-size: 15px;
    color: #91a0bd;
}
</style>