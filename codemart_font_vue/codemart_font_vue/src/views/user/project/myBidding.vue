<template>
  <div class="my-bidding-main">
    <div style="height: 60px;display: flex;align-items: center;padding-left: 20px;
                border-bottom: solid 1px #e8e8e8;font-weight: bolder;color: #444444">
      我曾经竞标过的项目
    </div>
    <div style="width: 100%;min-height: 200px;display: flex;justify-content: center;
                  align-items: center;" v-if="this.list === 0">
      <div style="font-size: 20px;font-weight: bolder;color: #83868a">
        没有竞标项目
      </div>
    </div>
    <div style="width: 100%;min-height: 200px;" v-if="this.list !== 0">
      <div style="border-bottom: solid 1px #e5e5e5"
           v-for="(item,index) in this.list" :key="index">
        <div style="font-size: 16px;padding-left: 20px;
                        padding-top: 10px;padding-bottom: 10px">
          <div class="my-project-title">
            <span @click="this.toInfo(item.de_id)">
              {{item.title}}
            </span>
          </div>
          <div style="display: flex;">
            <div class="my-project-tags">
              创建日期:
              {{item.join_time}}
            </div>
            <div class="my-project-tags">
              {{item.status_name}}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from '../../../utils/request.js';
import axios from "axios";
export default {
  name: "myBidding.vue",
  data(){
    return{
      list: [],
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
    toInfo(id){
      const token = localStorage.getItem('token');
      if(token == null){
        this.$notify.error("请先登录后尝试");
      }else{
        this.$router.push("/projectInfo?id="+id)
      }
    },
  },
  mounted() {
    let headers = this.setHeaders()
    request.post("/api/listMyBidding",{},{headers:headers}).then(res=>{
      if(res.code === "200"){
        this.list = res.data
      }else{
        this.$notify.error("异常");
      }
    })
  }
}
</script>

<style scoped>
.my-bidding-main{
  width: 100%;
  min-height: 20px;
}
.my-project-title{
  font-size: 18px;
  font-weight: bolder;
}
.my-project-title span{
  cursor: pointer;
  color: #606060;
}
.my-project-title span:hover{
  color: cornflowerblue;
}
.my-project-tags{
  font-size: 12px;
  padding-top: 15px;
  padding-right: 20px;
  color: #858585;
}
</style>