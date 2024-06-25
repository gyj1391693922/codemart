<template>
<div style="background-color: #f0f2f5;min-height: 100vh">
  <div style="padding-top:20px;width:100%;display: flex;justify-content: center;align-items: center;">
    <div style="background-color: #ffffff;min-height: 260px;width: 60%;padding-bottom: 30px;border-radius: 5px;
                box-shadow: 2px 2px 7px #d3d3d3">
      <div style="margin-top: 30px;margin-left: 40px;display: flex;align-items: center;">
        <div style="margin-top: 5px;color: #ff4646;">
          *
        </div>
        <div style="margin-left: 5px">
          项目分类
        </div>
        <el-select
            class="project-submit-select"
            v-model="this.project.sku"
            multiple
            placeholder="请选择项目分类"
            style="width: 240px"
            size="large"
        >
          <el-option
              v-for="item in this.skus"
              :key="item.sku_id"
              :label="item.sku_name"
              :value="item.sku_id"
          />
        </el-select>
<!--        <select class="project-submit-option" v-model="this.project.sku">-->
<!--          <option v-for="(option) in this.skus" :key="option.id" :value="option.id">{{ option.sku_name }}</option>-->
<!--        </select>-->
      </div>
      <div style="margin-top: 30px;margin-left: 40px;display: flex;align-items: center;">
        <div style="margin-top: 5px;color: #ff4646;">
          *
        </div>
        <div style="margin-left: 5px">
          需求标题
        </div>
        <input class="project-submit-title" placeholder="请简要概括需求内容,如: 打车APP开发" v-model="this.project.title"/>
      </div>
      <div style="margin-top: 30px;margin-left: 40px;display: flex;">
        <div style="margin-top: 5px;color: #ff4646;">
          *
        </div>
        <div style="margin-left: 5px">
          需求详情
        </div>
        <textarea class="project-submit-context" placeholder="详细描述修去内容,便于理解需求,如果不清楚,可以参考模板" v-model="this.project.context"/>
      </div>
      <div style="margin-top: 30px;margin-left: 40px;display: flex;align-items: center;">
        <div style="margin-top: 5px;color: #ff4646;">
          *
        </div>
        <div style="margin-left: 5px">
          具体金额
        </div>
        <input type="number" class="project-submit-title" style="width: 20%;" v-model="this.project.money"/>
      </div>
      <div style="margin-top: 30px;margin-left: 40px;display: flex;align-items: center;">
        <div style="margin-top: 5px;color: #ff4646;">
          *
        </div>
        <div style="margin-left: 5px">
          需求周期
        </div>
        <input type="number" class="project-submit-title" style="width: 5%;margin-right: 2%" v-model="this.project.days"/>天
      </div>
      <div style="padding-left: 4%;padding-top: 5px;color: #949494;font-size: 14px">
        注意 : 发布周期与等同于开发周期,如到达周期时间但没有完成订单,订单将会截止
      </div>
      <button class="project-submit-button" @click="this.submitProject(this.project)">
        发布需求
      </button>
      <div style="padding-left: 4%;padding-top: 5px;color: #949494;font-size: 14px">
        平台将会收取 0.2% 手续费,不足 1元 按 1元 计算,最高收取 200元
      </div>
    </div>
  </div>
</div>
</template>

<script>
import axios from "axios";
import request from '../../utils/request.js'

export default {
  name: "submit.vue",
  data(){
    return {
      skus:[],
      project:{
        title:'',
        sku:[],
        context:'',
        money:0,
        days:0,
      }
    }
  },
  methods:{
    goSubmit(){
      this.$router.push("/projectSubmit")
    },
    logout(){
      localStorage.removeItem("token");
      this.$router.push("/login")
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
    submitProject(project){
      let headers = this.setHeaders();
      request.post("/api/project/save",project,{headers: headers}).then(res=>{
        if(res.code === "200"){
          this.$notify.success(res.msg);
        }else{
          this.$notify.error(res.msg)
        }
      })
    }
  },
  mounted(){
    if(localStorage.getItem("token") == null){
      this.$notify.error("错误,非法进入,请登陆后重新尝试进入");
      this.$router.push("/login");
    }else{
      this.getSkuList();
    }
  },
}
</script>

<style scoped>
.project-submit-select{
  width: 40%;
  border-radius: 7px;
  box-shadow: 2px 2px 7px #e0e8e8;
  margin-left: 20px;
}
.project-submit-option{
  color: #595959;
  width: 40%;
  height: 50px;
  border: solid 1px #e0e0e0;
  outline: none;
  border-radius: 7px;
  box-shadow: 2px 2px 7px #e0e8e8;
  margin-left: 20px;
  padding-left: 10px;
}
.project-submit-option:focus{
  border: solid 1px coral;
}
.project-submit-title{
  color: #595959;
  width: 80%;
  height: 45px;
  border: solid 1px #e0e0e0;
  outline: none;
  border-radius: 7px;
  box-shadow: 2px 2px 7px #e0e8e8;
  margin-left: 20px;
  padding-left: 10px;
  font-family: heiti;
  font-size: 15px;
}
.project-submit-title::placeholder{
  color: #a8abb2;
}
.project-submit-title:focus{
  border: solid 1px coral;
}
.project-submit-context{
  color: #595959;
  width: 80%;
  min-height: 145px;
  border: solid 1px #e0e0e0;
  outline: none;
  border-radius: 7px;
  box-shadow: 2px 2px 7px #e0e8e8;
  margin-left: 20px;
  padding-left: 10px;
  resize: none;
  padding-top: 15px;
  font-family: heiti;
  font-size: 15px;
}
.project-submit-context::placeholder{
  color: #a8abb2;
}
.project-submit-context:focus{
  border: solid 1px coral;
}
input::-webkit-outer-spin-button,input::-webkit-inner-spin-button {
  -webkit-appearance: none;}
input[type="number"] {
  -moz-appearance: textfield;}
.project-submit-button{
  height: 50px;
  width: 20%;
  margin-top: 40px;
  margin-left: 40px;
  border: none;
  background-color: coral;
  font-size: 16px;
  font-weight: bolder;
  color: white;
  border-radius: 5px;
  box-shadow: 2px 2px 7px #e0e8e8;
  cursor: pointer;
}
.project-submit-button:hover{
  background-color: #f55c24;
}
</style>