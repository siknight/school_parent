<!--登录vue-->
var userlogin = new Vue({
    el:"#loginApp",
    data:{
        user: {

        },
        result:{
            message:""   //在页面显示的错误信息
        },
        rememberme:false,  //是否记住用户名和密码，默认是不记住
        registerLoginFlag:true,  //true表示显示登陆 false表示显示修改密码
        show:true,   //验证码倒计时，默认是显示发送按钮，点击后显示倒计时，倒计时结束后，又变成发送按钮
        count: '30',  //验证码倒计时多少秒
        timer: null,  //定时器，用于发送验证码时倒计时
        verifyCode:"", //验证码
        toindexPage:"#"

    },
    methods:{
        //登录操作
        login: function () {
            this.result.message="";
            var regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
            if(this.user.mobile==null||this.user.mobile==""){
                this.result.message="邮箱不能为空";
            }else if(!regEmail.test(this.user.mobile)){
                this.result.message="邮箱格式不正确";
            }else if(this.user.password==null||this.user.password==""){
                this.result.message="密码不能为空";
            }else{
                // var url = "http://localhost:9001/user/login";
                var url =school_user_server+"/user/login"
                var _this = this;
                //post必须用这种方式
                var params = new URLSearchParams()
                params.append('mobile', this.user.mobile)
                params.append('password',this.user.password)
                //post必须用这种方式
                axios.post(url, params).then(function (response) {
                    _this.result = response.data;
                    if(_this.result.code==20000){
                        //开始时应将所有存的都删掉，以后再加

                        //表示登录成功
                        window.localStorage.setItem("userId_school",_this.result.data.userId);
                        window.localStorage.setItem("nickName_school",_this.result.data.name);
                        window.localStorage.setItem("mobile_school",_this.user.mobile);
                        window.localStorage.setItem("token_school",_this.result.data.token);
                        if (_this.rememberme==true){  //如果为true下次登录就直接显示了
                            window.localStorage.setItem("userpass_school",_this.user.password);
                        } else {  //如果为false，就将密码删除
                            window.localStorage.removeItem("userpass_school");
                        }

                        window.location.href=user_web_url+"/index.html";
                       // 下面这种是万能的，上面这种跳转好多浏览器不支持
                       // top.window.location=user_web_url+"/index.html";
                       //  var obj = document.getElementById('links');
                       //  obj.href = user_web_url+"/index.html";
                       //  obj.click();

                    }


                }).catch(function (err) {
                    console.log("网络错误");
                });
            }

        },  //login方法
        //登录初始化，判断是否有记住操作，有的话在输入框显示用户名，密码，没有的话不显示
        loadInit: function(){
            var userpass_school = window.localStorage.getItem("userpass_school");
            if (userpass_school!=null){
                this.user.mobile = window.localStorage.getItem("mobile_school");
                this.user.password = userpass_school;
                this.rememberme=true;
            }
        },
        registerLoginComeBack:function () {
            this.result.message="";
            this.registerLoginFlag = !this.registerLoginFlag;
        },
        //修改获得短信验证码
        updatePassCode:function () {
            this.result.message="";  //先置空
            var regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
            if(this.user.mobile==null||this.user.mobile==""){
                this.result.message="邮箱不能为空";
            }else if(!regEmail.test(this.user.mobile)){
                this.result.message="邮箱格式不正确"
            }else{
                this.show = false;
                var url = school_user_server+"/user/sendsms";
                var _this = this;
                //定时器开始
                var TIME_COUNT = 30;
                console.log(_this.timer);  //null
                if (!_this.timer) {  //非空
                    _this.count = TIME_COUNT;
                    _this.show = false;
                    _this.timer=setInterval(() =>{
                        if (_this.count > 0 && _this.count <= TIME_COUNT) {
                        _this.count--;
                    } else{
                        _this.show = true;
                        clearInterval(_this.timer);
                        _this.timer = null;
                    }
                },1000);
                }  //定时器结束
                //向服务器请求发送验证码
                var params = new URLSearchParams()
                params.append('mobile', this.user.mobile);
                axios.post(url,params).then(function (response) {

                }).catch(function (err) {
                    console.log("网络错误");
                });
            }

        },  //updatePassCode
        //修改密码方法
        updatePass:function () {
            this.result.message="";  //先置空
            var regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
            if(this.user.mobile==null||this.user.mobile==""){
                this.result.message="邮箱不能为空";
            }else if(!regEmail.test(this.user.mobile)){
                this.result.message="邮箱格式不正确";
            }else if(this.verifyCode==null||this.verifyCode==""){
                this.result.message="验证码不能为空";
            }else if(this.user.password==null||this.user.password==""){
                this.result.message="密码不能为空";
            }else{
                var url = school_user_server+"/user/pass/"+this.verifyCode;
                var _this = this;
                //json请求用默认方式
                axios.put(url, _this.user
                ).then(function (response) {
                    //将密码删除
                    window.localStorage.removeItem("userpass_school");
                    _this.result = response.data;
                    if(_this.result.code==20000){  //表示修改成功
                        //修改成功后跳转
                        _this.registerLoginFlag = true;
                    }

                }).catch(function (err) {
                    console.log("网络错误");
                });
            }
        }  //updatePass
    },  //methods
    created(){
        this.loadInit();
    }
});


//  注册vue
var userRegister = new Vue({
    el:"#registerApp",
    data:{
        user: {},  //用户
        verifyCode:"", //验证码
        result:{}, //服务器返回结果
        checkflag:true,  //是否同意协议，默认同意
        show:true,   //验证码倒计时，默认是显示发送按钮，点击后显示倒计时，倒计时结束后，又变成发送按钮
        count: '',  //验证码倒计时秒数
        timer: null,  //定时器
        error:"" //在页面显示的错误信息
    },
    methods:{
        getCode:function () {
            this.error="";
            //邮箱正则表达式
            var regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
            if(this.user.mobile==null||this.user.mobile==""){
                this.error="邮箱不能为空";
            }else if(!regEmail.test(this.user.mobile)){
                this.error="邮箱格式不正确"
            }else{
                var url = school_user_server+"/user/sendsms";
                var _this = this;
                //定时器开始
                var TIME_COUNT = 30;
                console.log(_this.timer);  //null
                if (!_this.timer) {  //非空
                    _this.count = TIME_COUNT;
                    _this.show = false;
                    _this.timer=setInterval(() =>{
                        if (_this.count > 0 && _this.count <= TIME_COUNT) {
                        _this.count--;
                    } else{
                        _this.show = true;
                        clearInterval(_this.timer);
                        _this.timer = null;
                    }
                },1000);
                }  //定时器结束
                //向服务请求发送验证码
                var params = new URLSearchParams()
                params.append('mobile', this.user.mobile)
                axios.post(url,params).then(function (response) {

                }).catch(function (err) {
                    console.log("网络错误");
                });
            }  //else

        },  //getcode
        //注册方法
        register:function () {
            this.error="";
            var regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
            if(this.user.nickname==null||this.user.nickname==""){
                this.error="名字不能为空";
            }else if(this.user.mobile==null||this.user.mobile==""){
                this.error="邮箱不能为空";
            }else if(!regEmail.test(this.user.mobile)){
                this.error="邮箱格式不正确";
            }else if(this.verifyCode==null||this.verifyCode==""){
                this.error="验证码不能为空";
            }else if(this.user.password==null||this.user.password==""){
                this.error="密码不能为空";
            }else if(this.checkflag == false){
                this.error="你必须同意服务条款才能注册";
            }else{
                var url = school_user_server+"/user/register/"+this.verifyCode;
                var _this = this;
                //json请求用默认方式
                axios.post(url, _this.user
                ).then(function (response) {
                    _this.result = response.data;
                    _this.error = _this.result.message;
                }).catch(function (err) {
                    console.log("网络错误");
                });
            }



        }
    }
});
