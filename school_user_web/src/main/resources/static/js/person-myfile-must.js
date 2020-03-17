var personBaseInfoApp = new Vue({
    el:"#personBaseInfoApp",
    data:{
        qqFlag:true,  //用于判断qq是文本显示还是输入框显示
        schoolFlag:true,// 用于判断school是文本显示还是输入框显示
        professionalFlag:true,// 用于判断学院是文本显示还是输入框显示
        gradeFlag:true, //用于判断年级是文本显示还是输入框显示
        personalityFlag:true,  //简介
        imgFlag:true,  //是否显示修改头像框  true表示不显示
        user:{
            myqq:"",
            myschool:" ",
            mygrade:"",
            myprofessional:"",
            personality:""
        },
        user2:{  //用于中间值存储
            myqq:" 填写个人qq(必填)",
            myschool:" 填写所在学校(必填)",
            mygrade:" 填写所在年级(必填)",
            myprofessional:" 填写所在学院(必填)",
            personality:"暂时没有个人简介"
        },
        user3:{  //用于选择项赋值
            // myqq:" 填写个人qq(必填)",
            myschool:"天津城建大学",
            mygrade:"大一",  //年级
            myprofessional:"计算机学院"  //学院
        },
        result:{},

    },
    methods:{
        qqFlagChange:function () { //qq取反  这里是双击修改时用
            this.user2.myqq=this.user.myqq;
            this.qqFlag=!this.qqFlag;
        },
        schoolFlagChange:function () { //学校取反
            this.user2.myschool=this.user.myschool;
            this.schoolFlag  = !this.schoolFlag;
        },
        professionalFlagChange:function () {  //专业取反
            this.user2.myprofessional = this.user.myprofessional;
            this.professionalFlag = !this.professionalFlag;
        },
        gradeFlagChange:function () { // 年级取反
            this.user2.mygrade = this.user.mygrade;
            this.gradeFlag = !this.gradeFlag;
        },
        personalityFlagChange:function () { // 简介取反
            this.user2.personality = this.user.personality;
            this.personalityFlag = !this.personalityFlag;
        },
        qqFlagChange2:function () { //qq取反  底下是取消时用
            this.user.myqq= this.user2.myqq;
            this.qqFlag=!this.qqFlag;
        },
        schoolFlagChange2:function () { //学校取反
            this.user.myschool=  this.user2.myschool;
            this.schoolFlag  = !this.schoolFlag;
        },
        professionalFlagChange2:function () {  //专业取反
            this.user.myprofessional = this.user2.myprofessional ;
            this.professionalFlag = !this.professionalFlag;
        },
        gradeFlagChange2:function () { // 年级取反
            this.user.mygrade = this.user2.mygrade ;
            this.gradeFlag = !this.gradeFlag;
        },
        personalityFlagChange2:function () { // 取消
            this.user.personality = this.user2.personality ;
            this.personalityFlag = !this.personalityFlag;
        },
        qqUpdate:function () { //qq修改
            var reg = /^[1-9][0-9]{4,9}$/gim;
            if (!reg.test(this.user.myqq)) {
                alert("请输入正确的qq号")
            }else{
                var url = school_user_server+"/user/qq";
                var _this = this;
                var token=window.localStorage.getItem("token_school");  //获取token
                axios.put(url, _this.user,{
                    headers:{
                        //请求时添加token，验证是否登录
                        'Authorization':"Bearer "+token
                    }
                }).then(function (response) {

                    _this.qqFlag=!_this.qqFlag;
                    _this.result = response.data;
                    _this.user.myqq = _this.result.data.myqq;

                }).catch(function (err) {
                    // console.log("网络错误");
                });
            }
        },
        schoolUpdate:function () {  //school 修改
            if(this.user.myschool==" 填写所在学校(必填)"){
                this.user.myschool=this.user3.myschool;
            }
            var url = school_user_server+"/user/school";
            var _this = this;
            var token=window.localStorage.getItem("token_school");  //获取token
            axios.put(url, _this.user,{
                headers:{
                    //请求时添加token，验证是否登录
                    'Authorization':"Bearer "+token
                }
            }).then(function (response) {

                _this.schoolFlag  = !_this.schoolFlag;
                _this.result = response.data;
                _this.user.myschool = _this.result.data.myschool;

            }).catch(function (err) {
                // console.log("网络错误");
            });
        },
        professionalUpdate:function () {   //学院修改
            if(this.user.myschool==" 填写所在学院(必填)"){
                this.user.myprofessional = this.user3.myprofessional;
            }

            var url = school_user_server+"/user/professional";
            var _this = this;
            var token=window.localStorage.getItem("token_school");  //获取token
            axios.put(url, _this.user,{
                headers:{
                    //请求时添加token，验证是否登录
                    'Authorization':"Bearer "+token
                }
            }).then(function (response) {

                _this.professionalFlag = !_this.professionalFlag;
                _this.result = response.data;
                _this.user.myprofessional = _this.result.data.myprofessional;

            }).catch(function (err) {
                // console.log("网络错误");
            });
        },
        gradeUpdate:function () { //年级修改
            if(this.user.myschool==" 填写所在年级(必填)"){
                this.user.mygrade = this.user3.mygrade;
            }

            var url = school_user_server+"/user/grade";
            var _this = this;
            var token=window.localStorage.getItem("token_school");  //获取token
            axios.put(url, _this.user,{
                headers:{
                    //请求时添加token，验证是否登录
                    'Authorization':"Bearer "+token
                }
            }).then(function (response) {

                _this.gradeFlag = !_this.gradeFlag;
                _this.result = response.data;
                _this.user.mygrade = _this.result.data.mygrade;

            }).catch(function (err) {
                // console.log("网络错误");
            });
        },
        findUserById:function () {
            var url = school_user_server+"/user/findById";
            var _this = this;
            var token=window.localStorage.getItem("token_school");  //获取token
            axios.get(url,{
                headers:{
                    //请求时添加token，验证是否登录
                    'Authorization':"Bearer "+token
                }
            }).then(function (response) {
                _this.result = response.data;
                _this.user = _this.result.data;
                if (_this.user.myqq==null||_this.user.myqq==""){
                    _this.user.myqq=" 填写个人qq(必填)";
                }
                if (_this.user.mygrade==null||_this.user.mygrade==""){
                    _this.user.mygrade=" 填写所在年级(必填)";
                }
                if (_this.user.myschool==null||_this.user.myschool==""){
                    _this.user.myschool=" 填写所在学校(必填)";
                }
                if (_this.user.myprofessional==null||_this.user.myprofessional==""){
                    _this.user.myprofessional=" 填写所在学院(必填)";
                }
                if (_this.user.personality==null||_this.user.personality==""){
                    _this.user.personality=" 暂时没有个人简介";
                }
            }).catch(function (err) {
                // console.log("网络错误");
            });
        },
        updatePersonality:function () {  //修改个人简介
            // this.user.personality = this.user3.personality;
            var url = school_user_server+"/user/personality";
            var _this = this;
            var token=window.localStorage.getItem("token_school");  //获取token
            if ( this.user.personality==null|| this.user.personality=="") {
                this.user.personality= this.user2.personality;
            }else{
                axios.put(url, _this.user,{
                    headers:{
                        //请求时添加token，验证是否登录
                        'Authorization':"Bearer "+token
                    }
                }).then(function (response) {

                    _this.personalityFlag = !_this.personalityFlag;
                    _this.result = response.data;
                    _this.user.personality = _this.result.data.personality;

                }).catch(function (err) {
                    // console.log("网络错误");
                });
            }
        },
        updateImage:function () { //修改个人头像
            alert("你好");
            this.imageFlag = false;
        },

    },  //methods
    created(){
        this.findUserById();
    }

});