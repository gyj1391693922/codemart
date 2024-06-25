<template>
  <header id="header">
    <div id="banner-background">
      <div id="banner-icon" @click="this.go_index()">码市</div>
    </div>
  </header>
  <div id="login-page">
    <div id="login-info">
      <div style="font-size: 22px;color: rgb(77,77,77);margin-bottom: 20px">账号密码登录</div>
      <div><input type="text" class="input-group" placeholder="用户名" v-model="this.username"/></div>
      <div><input type="password" class="input-group" placeholder="密码" v-model="this.password"/></div>
      <div><input type="password" class="input-group" placeholder="再次确认密码" v-model="this.repassword"/></div>
      <div style="display: flex">
        <!--        <div class="fondPassWord">-->
        <!--          <input type="checkbox" /> 记住我-->
        <!--        </div>-->
        <div class="fondPassWord">
          找回密码
        </div>
      </div>
      <div>
        <button class="login-button" style="background-color: #5d93f3;
                                            cursor: pointer;" @click="this.register()">注册</button>
        <button class="login-button" style="background-color: rgba(1,1,1,0);
                        color: black;border: solid 1px #b2b2b2;
                        cursor: pointer;" @click="this.$router.push('/login')">登录</button>
      </div>
    </div>
  </div>

</template>

<script>
import axios from 'axios'
import request from '../../utils/request.js'

export default {
  name: "Register.vue",
  data(){
    return{
      username: "",
      password: "",
      repassword: "",
    }
  },
  methods:{
    register(){
      if(this.password === "" || this.repassword === "" || this.username === ""){
        this.$notify.error("用户名或密码不能为空")
      }else{
        if(this.password !== this.repassword){
          this.$notify.error("两次密码不一致,请检查后重新输入");
        }else{
          request.post("/api/register",
              {
                username:this.username,
                password:this.password
              }).then(res=>{
            if(res.code === "200"){
              this.$router.push("/login")
            }else{
              this.$notify.error(res.msg);
            }
          })
        }
      }

    },
    go_index(){
      this.$router.push("/")
    }
  }
}
</script>

<style scoped>
#header{
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 65px;
  z-index: 1000;
  transition: background-color 0.3s;
  display: flex;
  background-color: cornflowerblue;
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
#login-page{
  height: 70vh;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
#login-info{
  height: 60%;
  width: 60%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.input-group{
  border: solid #b9b9b9 1px;
  height: 40px;
  width: 300px;
  border-radius: 5px;
  margin-top: 10px;
  margin-bottom: 10px;
  outline: none;
  padding-left: 15px;
}
.input-group::placeholder{
  color: #b9b9b9;
  font-size: 15px;
}
.login-button{
  border: none;
  width: 155px;
  height: 40px;
  margin-left: 5px;
  margin-right: 5px;
  border-radius: 5px;
  color: white;
}
.login-button:hover{
  box-shadow: 1px 1px 3px 0 #ababab
}
.fondPassWord{
  color: #5d676b;
  font-size: 14px;
  margin-bottom: 10px;
  margin-left: 100px;
  margin-right: 100px;
  cursor: default;
}

</style>