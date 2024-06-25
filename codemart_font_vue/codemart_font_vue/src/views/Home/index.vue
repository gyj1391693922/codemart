<template>
  <div>
    <header id="header">
      <div id="banner-background">
        <div id="banner-icon">码市</div>
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
      <div id="user-options" v-if="this.isLogin">
        <div id="user-login">
          <div id="user-login-font" @click="this.go_to_login()">
            登录
          </div>
          <div id="user-login-font" @click="this.$router.push('/register')">
            注册
          </div>
        </div>
      </div>
      <div class="user-info" v-if="!this.isLogin">
        <div class="user-head" @click="this.toggleDropdown()">
          <div class="user-head-info">
            {{this.userInfo.nickname}}
          </div>
          <!--          <div class="user-head-info" ></div>-->
          <ul class="user-ul" v-show="isDropdownVisible">
            <li class="user-li" @click="this.$router.push('/userInfo')">
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

    <div id="bx-wrapper">
    </div>
    <div id="cm-nums-out">
      <cm_nums context="累计金额" request_url="/font/moneyCnt"/>
      <div class="cm-cent"></div>
      <cm_nums context="项目数量" request_url="/font/projectCnt"/>
      <div class="cm-cent"></div>
      <cm_nums context="注册开发者" request_url="/font/userCnt"/>
    </div>
    <div class="context">

    </div>
    <div style="">
      <div style="display: flex;justify-content: center;align-items: center;flex-direction: column;
    padding-top: 20px;font-size: 28px;font-weight: bolder;color: #626262">我们正在飞速发展中</div>
      <div style="display: flex;justify-content: center;align-items: center;flex-direction: column;
    padding-top: 5px;font-size: 16px;font-weight: bolder;color: #a2a2a2">月累计交易金额</div>
      <cm_charts/>
    </div>
  </div>
</template>

<script>
import cm_nums from '../../components/cm_nums.vue'
import cm_charts from "@/components/cm_charts.vue";
import axios from "axios";
import request from '../../utils/request.js'

export default {
  name: "Index",
  components:{
    cm_nums,
    cm_charts
  },
  data(){
    return{
      isLogin:false,
      isDropdownVisible: false,
      userInfo:{},
    }
  },
  methods:{
    go_to_login(){
      this.$router.push('/login')
    },
    getToken(){
      const token = localStorage.getItem('token');
      if(token == null){
        this.isLogin = true
      }else{
        this.isLogin = false
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
        }else if(res.code === "777") {
          localStorage.removeItem("token");
        }else{
          this.$notify.error(res.msg);
        }
      })
    },
    goSubmit(){
      const token = localStorage.getItem('token');
      if(token == null){
        this.$notify.error("请先登录");
      }else if(token != true){
        this.$router.push("/projectSubmit")
      }
    },
  },
  mounted() {
    window.addEventListener("scroll", function () {
      var header = document.querySelector("#header");
      header.classList.toggle("sticky", window.scrollY > 400);
    });
    this.getToken();
    this.getUser();
  }
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
#header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 65px;
  z-index: 1000;
  transition: background-color 0.3s;
  display: flex;
}

.sticky{
  background-color: rgb(54, 89, 220);
}

#banner {
  height: 65px;
  /*background-color: rgba(40,79,221,0.4);*/
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
}

#banner-background {
  background-size: cover;
  background-position: center;
  width: 100%;
  height: 100%;
  display: flex;
}
#banner-icon{
  color: #fff;
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
  color: #fff;
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
  color: white;
  margin-right: 10px;
  cursor: pointer;
}
#toWant{
  height: 80%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  margin-right: 10px;
  font-size: 20px;
}
#user-login :hover{
  border: solid rgba(222, 216, 216,0.5) 2px;
  border-radius: 7px;
}
#bx-wrapper{
  padding:0;
  margin:0;
  background-color: rgb(93, 92, 92);
  height: 660px;
  width: 100%;
  /*background-image: url("/banner-background-image.jpg");*/
}
#cm-nums-out{
  height: 200px;
  background-color: black;
  display: flex;
}
.cm-cent{
  width: 5%;
  background-color: #eeeeee;
}
.context{
  min-height: 0vh;
}
.user-info{
  width: 120px;
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
  color: white;
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
}
.user-li:hover{
  background-color: #e5e5e5;
}
.user-ul{
  position: absolute;
  list-style-type: none;
  border-radius: 7px;
  margin-top: 210px;
  margin-right: 50px;
  background-color: white;
}
</style>