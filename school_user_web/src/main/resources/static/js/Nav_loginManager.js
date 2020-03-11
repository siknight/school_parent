var loginManager = new Vue({
    el:"#index_header",
    data:{
        show:true,  //默认显示登陆和注册
        user: {}, //用户
        result:{}, //服务器返回结果

    },
    methods:{
        showloginManager:function () {
            //取值，判断是否登录
            this.user.mobile = window.localStorage.getItem("mobile_school");
            this.result.token_school = window.localStorage.getItem("token_school");
            //登录token校验
            var url =school_user_server+"/token/tokenVerify"
            var _this = this;
            axios.get(url,{
                headers:{
                    //请求时添加token，验证是否登录
                    'Authorization':"Bearer "+_this.result.token_school
                }
            }).then(function (response) {
                _this.result = response.data;
                if(_this.result.code==20000){
                    //开始时应将所有存的都删掉，以后再加
                    _this.show=false; //显示登录后信息
                }else{
                    _this.show=true;  //显示登陆和注册，没有等
                }
            }).catch(function (err) {
                console.log("网络错误");
            });
            // if (this.user.mobile==null||this.user.mobile==""){
            //     this.show=true;
            // }else{
            //     this.show=false;
            // }
        }
    },
    created(){
        this.showloginManager();
    }
});