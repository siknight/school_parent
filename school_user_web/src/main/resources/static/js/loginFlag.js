//判断是否登录，没有登录的话就跳转到登录页面
window.onload=function () {
    var token=window.localStorage.getItem("token_school");
    if (token==null||token==""){
        window.location.href=user_web_url+"/tologin_register.html";
    }
}