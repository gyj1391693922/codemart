<template>
  <div class="my-project-main">
    <div style="height: 60px;display: flex;align-items: center;padding-left: 20px;
                border-bottom: solid 1px #e8e8e8;font-weight: bolder;color: #646464">
      我发布的任务
    </div>
    <div style="width: 100%;min-height: 200px;display: flex;justify-content: center;
                  align-items: center;" v-if="this.projects.length == 0">
      <div style="font-size: 20px;font-weight: bolder;color: #83868a">
          没有发布任务
      </div>
    </div>
    <div style="width: 100%;min-height: 200px;" v-if="this.projects.length > 0">
        <div style="border-bottom: solid 1px #e5e5e5"
             v-for="(item,index) in this.projects" :key="index">
            <div style="font-size: 16px;padding-left: 20px;
                        padding-top: 10px;padding-bottom: 10px">
              <div class="my-project-title" @click="this.toInfo(item.de_id)">
                {{item.title}}
              </div>
              <div style="display: flex;">
                <div class="my-project-tags">
                  周期:
                  {{item.term_day}}
                  天
                </div>
                <div class="my-project-tags">
                  创建日期:
                  {{item.create_time}}
                </div>
                <div class="my-project-tags" v-if="item.status_name !== '已取消' && item.status_name !== '已截止'">
                  还剩
                  {{item.term_day - item.surplus_time - 1}}
                  天
                </div>
                <div class="my-project-tags">
                  {{item.status_name}}
                </div>
                <div class="my-project-tags">
                  金额:
                  {{item.money}}
                </div>
                <div class="my-project-tags">
                  竞标人数:
                  {{item.person}}
                </div>
              </div>
            </div>
        </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import request from '../../../utils/request.js'

export default {
  name: "myProject.vue",
  data(){
    return{
      projects:[]
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
    getMyProject(){
      let headers = this.setHeaders();
      request.get("/api/user/getMyProject",{headers:headers}).then(res=>{
        let data = res.data
        let code = res.code
        if(code === "200"){
          for(let i in data){
            if (Number.parseInt(data[i].term_day) < 10) {
              let t = data[i].term_day.padStart(2, '0');
              data[i].term_day = t;
            }

            let createTime = new Date(data[i].create_time)

            let surplus = Number.parseFloat(((new Date() - createTime)/1000/60/60/24)).toFixed(0)

            let year = createTime.getFullYear();
            let month = String(createTime.getMonth() + 1).padStart(2, '0');
            let day = String(createTime.getDate()).padStart(2, '0');
            let formattedDate = `${year}-${month}-${day}`;
            data[i].create_time = formattedDate
            data[i].surplus_time = surplus - 1
          }
          this.projects = data
        }else if(code === "777"){
          localStorage.removeItem("token");
          this.$notify.error(res.data.msg);
          this.$router.push("/");
        }else{
          this.$notify.error(res.data.msg);
          this.$router.push("/");
        }
      })
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
    if(localStorage.getItem("token") !== null){
      this.getMyProject();
    }
  }
}
</script>

<style scoped>
.my-project-main{
  width: 100%;
  min-height: 20px;
}
.my-project-title{
  cursor: pointer;
  font-size: 18px;
  font-weight: bolder;
  color: #606060;
}
.my-project-title:hover{
  color: cornflowerblue;
}
.my-project-tags{
  font-size: 12px;
  padding-top: 15px;
  padding-right: 20px;
  color: #858585;
}
</style>