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
            this.result.token_school = window.localStorage.getItem("mobile_school");
            if (this.user.mobile==null||this.user.mobile==""){
                this.show=true;
            }else{
                this.show=false;
            }
        }
    },
    created(){
        this.showloginManager();
    }
});