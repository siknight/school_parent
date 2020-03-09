var editor =  UE.getEditor('container');
var articleVue = new Vue({
    el:"#articleApp",
    data:{
        article:{},   //对象
        columnList:[],
        column:{
        },
        result:{},
        successMsg:""
    },
    methods:{
        findAllColumn:function () {
            var url = school_user_server+"/column";
            var _this = this;
            //json请求用默认方式
            axios.get(url).then(function (response) {
                _this.result = response.data;
                _this.columnList = _this.result.data;
                //如果没有这句代码，select中初始化会是空白的，默认选中就无法实现
                _this.article.columnid =  _this.columnList[0].id;
            }).catch(function (err) {
                console.log("网络错误");
            });
        },
        addArticle:function () {
            this.successMsg="";
            if(this.article.title==null||this.article.title==""){
                // this.result.message="邮箱不能为空";
                alert("标题不能为空");
            }else{
                //获取ueditor文章content赋值
                this.article.content = editor.getContent();
                //获取id，因为token里存的有，所以这里省略
                // this.article.userid = window.localStorage.getItem("userId_school");
                var token=window.localStorage.getItem("token_school");  //获取token
                console.info("userid=")
                var url = school_user_server+"/article";
                var _this = this;
                //json请求用默认方式
                axios.post(url,_this.article,{
                    headers:{
                        //请求时添加token，验证是否登录
                        'Authorization':"Bearer "+token
                    }
                }).then(function (response) {
                    _this.result = response.data;
                    _this.successMsg= _this.result.message;
                    alert( _this.successMsg);
                    window.location.href=user_web_url+"/index.html";

                }).catch(function (err) {
                    alert("网络错误");
                });
            }

        }

    },  //methods
    created(){
        this.findAllColumn();


    }
});