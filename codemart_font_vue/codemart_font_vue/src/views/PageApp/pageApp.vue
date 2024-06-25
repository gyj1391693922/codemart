<template>
  <header id="header">
    <div id="banner-background">
      <div id="banner-icon" @click="this.go_to_home()" style="cursor: pointer;">码市</div>
      <div id="banner-option">
        <div id="banner-option-font" @click="this.go_to_project()">找项目</div>
      </div>
      <div id="banner-option">
        <div id="banner-option-font">找团队</div>
      </div>
      <div id="banner-option">
        <div id="banner-option-font">找人才</div>
      </div>
      <div id="banner-option">
        <div id="banner-option-font">原型广场</div>
      </div>
      <div id="banner-option">
        <div id="banner-option-font">帮助中心</div>
      </div>
    </div>
    <div id="user-options" v-if="!this.isLogin">
      <div id="user-login">
        <div id="user-login-font" @click="this.go_to_login()">
          登录
        </div>
        <div id="user-login-font" @click="this.$router.push('/register')">
          注册
        </div>
      </div>
    </div>
    <div class="user-info" v-if="this.isLogin">
      <div class="user-head" @click="this.toggleDropdown()">
        <div class="user-head-info">
          {{this.userInfo.nickname}}
        </div>
        <!--          <div class="user-head-info" ></div>-->
        <ul class="user-ul" v-show="isDropdownVisible">
          <li class="user-li" @click="this.to_userInfo()">
            个人中心
          </li>
          <li class="user-li" @click="this.goSubmit()">
            发布需求
          </li>
          <li class="user-li">
            设置
          </li>
          <li class="user-li" @click="this.logout()">
            退出登录
          </li>
        </ul>
      </div>
    </div>
  </header>
  <router-view/>
</template>

<script>
import axios from "axios";
import request from '../../utils/request.js'

export default {
  name: "pageApp.vue",
  data(){
    return{
      isLogin:false,
      isDropdownVisible: false,
      userInfo: {},
    }
  },
  methods:{
    go_to_login(){
      this.$router.push('/login')
    },
    getToken(){
      const token = localStorage.getItem('token');
      if(token == null){
        this.isLogin = false
      }else{
        this.isLogin = true
      }
    },
    toggleDropdown() {
      this.isDropdownVisible = !this.isDropdownVisible;
    },
    logout(){
      localStorage.removeItem("token");
      this.$router.push("/login")
    },
    go_to_project(){
      this.$router.push("/project")
    },
    go_to_home(){
      this.$router.push("/")
    },
    to_userInfo(){
      this.$router.push("/userInfo")
    },
    setHeaders(){
      let defaultHeaders = axios.defaults.headers.common;
      let addHeaders = {
        "token":localStorage.getItem('token'),
      }
      return {
        ...defaultHeaders,
        ...addHeaders
      };
    },
    getUser(){
      const headers = this.setHeaders()
      request.get("/api/userInfo",{ headers:headers }).then(res=>{
        if(res.code === "200"){
          this.userInfo = res.data
        }else{
          this.$notify.error(res.msg);
        }
      })
    },
    goSubmit(){
      const token = localStorage.getItem('token');
      if(token == null){
        this.$notify.error("请先登录")
      }else if(token != true){
        this.$router.push("/projectSubmit")
      }
    },
  },
  mounted() {
    this.getToken();
    this.getUser();
  }
}
</script>

<style scoped>
#header {
  top: 0;
  left: 0;
  width: 100%;
  height: 65px;
  z-index: 1000;
  transition: background-color 0.3s;
  display: flex;
  background-color: rgb(255, 255, 255);
  box-shadow: 2px 0 3px 0 #7e7e7e;
}
#banner-background {
  background-size: cover;
  background-position: center;
  width: 100%;
  height: 100%;
  display: flex;
}
#banner-icon{
  color: #69a6ff;
  font-size: 36px;
  font-weight: bold;
  margin-top: 5px;
  margin-left: 5%;
  cursor:default;
}
#banner-option{
  width: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 5px;
  margin-left: 2%;
  cursor:default;
}
#banner-option :hover{
  color: #78a5f3;
}
#banner-option-font{
  color: #232323;
  font-size: 16px;
  cursor: pointer;
}
#user-login{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 280px;
  height: 100%;
  cursor:default;
}
#user-login-font{
  height: 60%;
  width: 80%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #232323;
  margin-right: 10px;
  cursor: pointer;
}
#user-login :hover{
  border: solid rgba(222, 216, 216,0.5) 2px;
  border-radius: 7px;
}
.user-info{
  width: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 5px;
  margin-left: 2%;
  cursor:default;
}
.user-head{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 280px;
  height: 100%;

}
.user-head :hover{
  border: solid rgba(222, 216, 216,0.5) 2px;
  border-radius: 7px;
}
.user-head-info{
  height: 80%;
  min-width: 100%;
  padding-left: 10px;
  padding-right: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #232323;
  margin-right: 60px;
}
.user-li{
  width: 100px;
  height: 35px;
  list-style-type: none;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor:default;
  margin-left: -40px;
}
.user-li:hover{
  background-color: #e5e5e5;
}
.user-ul{
  position: absolute;
  list-style-type: none;
  border-radius: 7px;
  margin-top: 225px;
  margin-right: 70px;
  background-color: white;
}
</style>