<template>
  <div class="setNew">
    <el-form
        :label-position="labelPosition"
        label-width="auto"
        :model="formLabelAlign"
        style="max-width: 600px"
    >
      <el-form-item label="昵称">
        <el-input v-model="this.userDetail.nickname" />
      </el-form-item>
      <el-form-item label="手机号码">
        <el-input v-model="this.userDetail.phone" />
      </el-form-item>
      <el-form-item label="生日">
        <el-date-picker
            v-model="this.userDetail.birthday"
            type="date"
            placeholder="选择生日日期"
            :size="size"
        />
      </el-form-item>
      <el-form-item label="标签">
        <el-select
            v-model="this.userDetail.skuList"
            multiple
            placeholder="选择职业标签"
            style="width: 240px"
        >
          <el-option
              v-for="sku in skus"
              :key="sku.sku_id"
              :label="sku.sku_name"
              :value="sku.sku_id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="this.userDetail.address" />
      </el-form-item>
      <el-form-item>
        <el-button style="margin-left: 4%" type="primary" @click="this.submitUpdate(this.userDetail)">修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from "axios";
import request from '../../../utils/request.js'

export default {
  name: "updateUserDetail",
  data(){
    return{
      userDetail: {},
      skus: [],
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
    getSkuList(){
      let headers = this.setHeaders();
      request.get("/api/project/skus",{ headers: headers }).then(res=>{
        if(res.code === "200"){
          this.skus = res.data
        }else{
          this.$router.push("/project");
          this.$notify.error(res.msg)
        }
      })
    },
    getUser(){
      const headers = this.setHeaders()
      request.get("/api/userDetail",{ headers:headers }).then(res=>{
        console.log(res)
        if(res.code === "200"){
          this.userDetail = res.data
          if(this.userDetail.phone == null){
            this.userDetail.phone = "未设置"
          }
          if(this.userDetail.birthday == null){
            this.userDetail.birthday = "未设置"
          }
          if(this.userDetail.address == null){
            this.userDetail.address = "未设置"
          }
        }else if(res.code === "777"){
          this.$router.push("/");
          this.$notify.error(res.msg);
          localStorage.removeItem("token");
        }else{
          this.$router.push("/");
          this.$notify.error(res.msg);
        }
      })
    },
    submitUpdate(ud){
      let headers = this.setHeaders();
      ud.birthday = new Date(ud.birthday).toISOString().split("T")[0]
      console.log(ud)
      request.post("/api/updateUserDetail",ud,{headers:headers}).then(res=>{
        console.log(res)
        if(res.code === "200"){
          this.$notify.success(res.msg)
          setTimeout(() => {
            location.reload();
          }, 3000);
        }else{
          this.$notify.error(res.msg)
        }
      })
    }
  },
  mounted() {
    if(localStorage.getItem("token") !== null){
      this.getUser();
      this.getSkuList();
    }
  }
}
</script>

<style scoped>
.setNew{
  padding-top: 20px;
  padding-bottom: 20px;
  padding-left: 5%;
  min-height: 20px;
}
</style>