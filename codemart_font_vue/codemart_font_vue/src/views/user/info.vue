<template>
<div class="context-background">
  <div class="context-item-back">
    <div class="context-item">
      <div class="left-item">
        <div style="height: 60px;display: flex;padding-left: 40px;
                    align-items: center;font-family: heiti;font-size: 14px;
                    border-bottom: solid 1px #e8e8e8;">
          个人中心
        </div>
        <div class="left-item-option">
          <ul>
            <div class="left-item-option-title">
              交易管理
            </div>
            <li class="left-item-option-op" @click="this.toThe('/myProject')">
              我发布的任务
            </li>
            <li class="left-item-option-op" @click="this.toThe('/myBidding')">
              我竞标的项目
            </li>
          </ul>
        </div>
        <div class="left-item-option">
          <ul>
            <div class="left-item-option-title">
              个人设置
            </div>
            <li class="left-item-option-op" @click="this.toThe('/updateUserDetail')">
              修改基本信息
            </li>
            <li class="left-item-option-op">
              安全管理
            </li>
          </ul>
        </div>
      </div>
      <div class="right-item">
        <div class="right-item-main">
          <div style="padding-left: 5%;padding-top: 5%">
              <el-descriptions title="个人信息">
                <el-descriptions-item label="用户名:">{{this.userDetail.username}}</el-descriptions-item>
                <el-descriptions-item label="昵称:">{{this.userDetail.nickname}}</el-descriptions-item>
                <el-descriptions-item label="手机号码:">{{this.userDetail.phone}}</el-descriptions-item>
                <el-descriptions-item label="生日:">{{this.userDetail.birthday}}</el-descriptions-item>
                <el-descriptions-item label="标签:">
                  <el-tag size="small" v-for="(sku) in this.userDetail.skuList" style="margin-right: 10px">
                    {{sku}}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="地址:">
                  {{this.userDetail.address}}
                </el-descriptions-item>
              </el-descriptions>

          </div>
        </div>
        <div class="right-item-context">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import axios from "axios";
import request from '../../utils/request.js'

export default {
  name: "info",
  data(){
    return{
      userDetail:{},
    }
  },
  methods:{
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
    toThe(url){
      this.$router.push(url)
    },
    getUser(){
      const headers = this.setHeaders()
      request.get("/api/userDetail",{ headers:headers }).then(res=>{
        if(res.code === "200"){
          this.userDetail = res.data
          if(this.userDetail.phone == null){
            this.userDetail.phone = "未设置"
          }
          if(this.userDetail.birthday == null){
            this.userDetail.birthday = "未设置"
          }else{
            this.userDetail.birthday = new Date(this.userDetail.birthday).toISOString().split("T")[0]
          }
          if(this.userDetail.address == null){
            this.userDetail.address = "未设置"
          }
        }else{
          this.$notify.error(res.msg);
          this.$router.push("/");
        }
      })
    }
  },
  mounted() {
    if(localStorage.getItem('token') != null){
      this.getUser();
    }else{
      this.$notify.error("错误,非法进入,请登陆后重新尝试进入");
      this.$router.push("/")
    }
  }
}
</script>

<style scoped>
.context-background{
  background-color: #f0f2f5;
  min-height: 100vh
}
.context-item-back{
  padding-top: 20px;
  display: flex;
  justify-content: center;
}
.context-item{
  width: 80%;
  min-height: 60px;
  display: flex;
  justify-content: center;
}
.left-item{
  background-color: white;
  width: 15%;
  border-radius: 5px;
  min-height: 200px;
  padding-bottom: 20px;
}
.left-item-option{
  min-height: 60px;
  font-family: "heiti";
  font-size: 14px;
}
ul{
  list-style-type: none;
}
ul li{
  cursor: pointer;
}
ul li:hover{
  color: cornflowerblue;
}
.left-item-option-title{
  padding-top: 20px;
  font-size: 16px;
  font-weight: bolder;
  font-family: heiti;
}
.left-item-option-op{
  padding-top: 20px;
  border: none;
}
.right-item{
  width: 60%;
  height: 200px;
  padding-left: 10px;
}
.right-item-main{
  background-color: white;
  width: 100%;
  min-height: 200px;
  border-radius: 5px;
}
.right-item-context{
  margin-top: 10px;
  background-color: white;
  width: 100%;
  min-height: 0px;
  border-radius: 5px;
}
</style>