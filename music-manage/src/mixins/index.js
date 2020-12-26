export const mixin = {
    methods:{
        //提示信息
        notify(title,type){
            this.$notify({
                title: title,
                type: type
            })
        },
        //根据相对地址获取绝对地址
        getsingerPicUrl(url){   //歌手
            return `${this.$store.state.HOST}${url}`;
        },
        getSongPicUrl(url){ //歌曲
            return `${this.$store.state.HOST}${url}`;
        },
        getSongListPicUrl(url){ //歌单
            return `${this.$store.state.HOST}${url}`;
        },
        getConsumerAvatorUrl(url){ //用户
            return `${this.$store.state.HOST}${url}`;
        },
        //获取性别中文
        changeSex(value){
            if(value == 0){
                return '女';
            }
            if(value == 1){
                return '男';
            }
            if(value == 2){
                return '组合';
            }
            if(value == 3){
                return '不明';
            }
            return value;
        },
        //生日字符串截取
        attachBirth(val){
            return String(val).substr(0,10);
        },
        //上传图片之前的校验
        beforeAvatorUpload(file){
            const isJPG = (file.type == 'image/jpeg')||(file.type == 'image/png');
            if(!isJPG){
                this.$message.error('只能上传jpg/png格式的头像');
                return false; 
            }
            const isLt2M = (file.size /1024 /1024) < 2;
            if(!isLt2M){
                this.$message.error('上传头像图片不应超过2M');
                return false;
            }
            return true;
        },
        //上传成功之后
        handleAvatorSuccess(res){
            let _this = this;
            if(res.code == 1){
                //_this.imageUrl = URL.createObjectURL(file.raw);
                _this.getData();
                _this.notify(res.msg, 'success');
            }else{
                _this.notify(res.msg, 'error');
            }
        },
        handleDelete(id){
            this.idx = id;
            this.delVisible=true;
        },
        //把已经选择的项赋值给multipleSelection
        handleSelectionChange(val){
            this.multipleSelection = val;
        },
        //批量删除已经选择的项
        delAll(){
            for(let item of this.multipleSelection){
                this.handleDelete(item.id);
                this.deleteRow();
            }
            this.multipleSelection =[] ;
        }
    }
}