<template>
<div style="background-color: #f0f2f5;min-height: 100vh">
  <div>
    <div style="display: flex;justify-content: center;">
      <div style="background-color: #ffffff;width: 60%;margin-top: 40px;border-radius: 4px;box-shadow: 0 0 2px 0 #bdbdbd;">
        <div style="height: 140px;background-color: #f7fafc;">
            <div style="padding-top: 30px;padding-left: 30px">
              <div>
                <div style="display: flex">
                  <div style="font-size: 28px;color: #3b3b3b;">
                    {{this.items.title}}
                  </div>
                  <div class="info-no">
                    No.{{this.items.id}}
                  </div>
                </div>
                <div style="float: right;font-size: 14px;margin-right: 24px;color: #3b3b3b;">
                  {{this.items.status}}
                </div>
              </div>
              <div style="margin-top: 28px;display: flex">
                <div style="color: #a1a1a1">
                  发布人/单位 :
                </div>
                <div class="info-header-text">
                  {{this.items.nickname}}
                </div>
                <div style="color: #a1a1a1;margin-left: 15px">
                  金额 :
                </div>
                <div class="info-header-text">
                  ￥{{this.items.money}}
                </div>
                <div style="color: #a1a1a1;margin-left: 15px">
                  类型 :
                </div>
                <div class="info-header-text">
                  {{this.items.sku}}
                </div>
                <div style="color: #a1a1a1;margin-left: 15px">
                  周期 :
                </div>
                <div class="info-header-text">
                  {{this.items.term_day}} 天
                </div>
              </div>
            </div>
        </div>
        <div style="min-height:300px;display: flex;justify-content: center;">
          <div v-if="(this.items === 0) || (this.items.status === '已取消')"
               style="display: flex;justify-content: center;align-items: center;flex-direction: column;">
            <div style="margin-top: 5%;font-size: 22px;color: #a8a8a8;font-weight: bolder;">
              需求方已取消了该项目
            </div>
            <div class="info-other" @click="this.$router.push('/project')">
              查看其他项目
            </div>
          </div>
          <div style="width: 100%" v-if="this.items.length !== 0 && this.items.status !== '已取消'">
            <div class="project-context-t">
              需求描述:
            </div>
            <div style="display: flex;justify-content: center;padding-top: 20px">
              <div style="width: 95%;font-size: 14px">
                {{this.items.context}}
              </div>
            </div>
          </div>
        </div>
        <div>
          <div class="close-project" v-if="this.items.pageStatus && items.status !== '已取消' &&
                                           items.status !== '已截止'">
            <span @click="this.sureCloseProject()">
              取消需求
            </span>
          </div>
          <div class="project-plan"
               v-if="(this.items !== 0) && (this.items.status !== '已截止') && (this.items.status !== '已取消')">
            <div style="width: 100%">
              <el-steps :active="this.items.plan_id" finish-status="success" align-center>
                <el-step title="已托管"/>
                <el-step title="正在招标"/>
                <el-step title="已选标"/>
                <el-step title="正在进行交易"/>
                <el-step title="交易完成"/>
                <el-step title="已结单"/>
              </el-steps>
            </div>
          </div>
          <div style="display: flex;align-items: center;padding-bottom: 20px;
                      padding-top: 10px; border-top: solid 1px #e0e0e0;"
                      v-if="(!this.items.pageStatus) &&
                            (this.items.status !== '已截止') &&
                            (this.items.status !== '已取消') &&
                            this.items.plan_id < 3">
            <button class="join-project" @click="this.joinProject()"
                    :class="{ 'hover': isHovered }"
                    @mouseover="isHovered = true"
                    @mouseleave="isHovered = false">
              {{ isHovered ? '中标后平台将会收取 0.2% 手续费,不足1元的按1元计算,最高收取200元' : '参与竞标' }}
            </button>
          </div>
        </div>
      </div>
    </div>
    <div style="display: flex;justify-content: center;" v-if="this.items != 0">
      <div class="project-comment">
        <div style="min-height: 60px;display: flex;align-items: center;border-bottom: solid 1px #e0e0e0">
          <div style="margin-left: 20px;font-size: 18px;font-weight: bolder;color: #2f2f2f">
            竞标人公示
          </div>
        </div>
        <div v-if="this.personList == 0" style="display: flex;justify-content: center;margin-top: 60px">
          <div style="font-weight: bolder;font-size: 20px;color: #a1a1a1">
            暂时没有人参与竞标
          </div>
        </div>
        <div v-if="!this.personList == 0">
          <div style="border-bottom: solid 1px #d5d5d5" v-for="(person,index) in this.personList" :key="index">
            <div class="person-username">
              <span class="uname-text" @click="this.$router.push('/selectUserInfo')">
                {{person.nickname}}
              </span>
            </div>
            <div v-if="this.userInfo.u_id == this.items.author_id && person.dsod_dui_id == null">
              <div class="this-person"
                   v-if="(this.items !== 0) && (this.items.status !== '已截止') && (this.items.status !== '已取消')"
                   @click="this.isBid(this.items.id,person.u_id)">
                选标
              </div>
              <div class="call-person"
                   v-if="(this.items !== 0) && (this.items.status !== '已截止') && (this.items.status !== '已取消')">
                联系
              </div>
            </div>
            <div class="close-person" v-if="this.userInfo.u_id == person.u_id && person.dsod_dui_id == null &&
                                            (this.items.status !== '已截止') && (this.items.status !== '已取消')"
                 @click="closeProJoin(person.dui_id,person.u_id)">
              取消竞标
            </div>
            <div class="is-bid" v-if="person.dsod_dui_id === person.u_id">
              已选标
            </div>
            <div style="display: flex;padding-bottom: 20px;">
              <div style="padding-left: 20px;padding-top: 10px;display: flex;align-items: center;
                          font-weight: bolder;color: #939393;font-size: 16px" v-if="person.skusList">
                职业标签 :
                <div style="font-weight: normal;font-size: 12px;padding-top: 5px;padding-bottom: 5px;
                            border-radius: 5px;margin-left: 10px;padding-left: 10px;padding-right: 10px;
                            background-color: cornflowerblue;color: white"
                     v-for="(sku,index) in person.skusList" :key="index">
                  {{sku}}
                </div>
              </div>
              <div style="padding-left: 20px;padding-top: 10px;font-weight: bolder;color: #939393;
              font-size: 16px;display: flex;align-items: center;">
                参与时间 : {{new Date(person.join_time).toISOString().split('T')[0]}}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div style="margin-top: 20px;display: flex;justify-content: center" v-if="this.items != 0">
      <div style="min-height: 60px;width: 60%;background-color: white;  border-radius: 4px;
                  box-shadow: 0 0 2px 0 #bdbdbd;">
        <div style="min-height: 60px;display: flex;align-items: center;border-bottom: solid 1px #e0e0e0">
          <div style="margin-left: 20px;font-size: 18px;font-weight: bolder;color: #2f2f2f">
            推荐项目
          </div>
        </div>
        <div style="min-height: 100px">
          <div v-if="this.suggest == 0" style="display: flex;justify-content: center;margin-top: 60px">
            <div style="font-weight: bolder;font-size: 20px;color: #a1a1a1">
              暂时没有更多推荐
            </div>
          </div>
        </div>
      </div>
    </div>
    <div style="height: 100px;margin-top: 60px;">
      <div>
        结尾（备案号、联系方式、公司地址等）
      </div>
    </div>
  </div>
</div>
    <el-dialog v-model="isClose" width="30%" style="margin-top: 250px">
      <div>
        <div style="display: flex;justify-content: center;
                  align-items: center;margin-top: -20px;
                  font-size: 20px;font-weight: bolder">
          取消发布需求
        </div>
        <div style="display: flex;justify-content: center;
                    align-items: center;padding-top: 20px">
          您确定要取消发布该需求吗？
        </div>
      </div>
      <div style="padding-top: 20px;display: flex;justify-content: center;align-items: center;">
        <el-button @click="closeProject">确定</el-button>
        <el-button @click="closeDialog">取消</el-button>
      </div>
    </el-dialog>
</template>

<script>
import axios from 'axios'
import request from '../../utils/request.js'

export default {
  name: "info.vue",
  data(){
    return{
      id:"",
      items: [],
      suggest: [],
      plans: [],
      isClose: false,
      personList: [],
      isHovered: false,
      userInfo: {},
    }
  },
  methods:{
    getID(){
      this.id = this.$route.query.id
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
    logout(){
      localStorage.removeItem("token");
      this.$router.push("/login");
    },
    getProjectInfoByID(){
      let headers = this.setHeaders();
      request.get("/api/project/detail?id="+this.id,{ headers: headers }).then(res=>{
        if(res.code === "200"){
          if(res.data == null){
            this.$router.push("/project")
            this.$notify.error("没有找到该项目");
          }else{
            this.items = res.data
          }
        }else if(res.code === "407"){
        }else{
          this.$notify.error(res.msg)
          this.logout();
        }
      })
    },
    getPlans(){
      let headers = this.setHeaders();
      request.get("/api/project/plans",{ headers: headers }).then(res=>{
        this.plans = res.data
        if(res.code === "200"){

        }else if(res.code === "407"){
        }else{
          this.$notify.error(res.data);
        }
      })
    },
    closeProject(){
//取消发布逻辑
      let headers = this.setHeaders();
      request.post("/api/closeProject",{de_id:this.id},{headers:headers}).then(res=>{
        if(res.code === "200"){
          this.$notify.success("取消成功,可以进入个人中心查看该需求");

          setTimeout(() => {
            location.reload();
          }, 3000);
        }else{
          this.$notify.error(res.msg);
        }
      })
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
    sureCloseProject(){
      this.isClose = true;
    },
    closeDialog(){
      this.isClose = false;
    },
    joinProject(){
      let headers = this.setHeaders();
      //加入竞标逻辑
      request.post("/api/joinProject",
              {"t":localStorage.getItem("token"),"deId":this.items.id},{headers:headers}).then(res=>{
        if(res.code === "200"){
          this.$notify.success("竞标成功");
        }else{
          this.$notify.error(res.msg)
        }
      })
    },
    getPersonList(){
      let headers = this.setHeaders();
      request.get("/api/listJoinPerson?de_id="+this.id,{headers:headers}).then(res=>{
        if(res.code === "200"){
          console.log(res.data)
          this.personList = res.data
        }else if(res.code === "407"){
          this.$notify.error(res.msg);
          this.$router.push("/project")
        }else{
          this.$notify.error(res.msg)
        }
      })
    },
    closeProJoin(id,uid){
      let headers = this.setHeaders();
      request.post("/api/closeJoin",{"cj":{"dui":id,"uid":uid}},{headers:headers}).then(res=>{
        if(res.code==="200"){
          this.$notify.success("取消竞标成功");
        }else{
          this.$notify.error(res.data)
        }
      })
    },
    isBid(de_id,u_id){
      let headers = this.setHeaders();
      request.post("/api/isBid",{"obj":{"de_id":de_id,"u_id":u_id}},{headers:headers}).then(res=>{
        if(res.code === "200"){
          this.$notify.success(res.data);
        }else{
          this.$notify.error(res.data);
        }
      })
    }
  },
  mounted(){
    if(localStorage.getItem("token") == null){
      this.$notify.error("错误,非法进入,请登陆后重新尝试进入");
      this.$router.push("/login")
    }else{
      this.getID();
      this.getProjectInfoByID();
      this.getPlans();
      this.getPersonList();
      this.getUser();
    }
  },
}
</script>

<style scoped>
.info-header-text{
  margin-left: 10px;
  border-right: solid 1px #a2a2a2;
  color: #525252;
  padding-right: 15px;
}
.info-other{
  color: white;
  background-color: cornflowerblue;
  padding-top: 8px;
  padding-bottom: 8px;
  padding-left: 15px;
  padding-right: 15px;
  border-radius: 5px;
  font-size: 14px;
  margin-top: 35px;
  cursor: pointer;
}
.info-other:hover{
  background-color: #5a80c0;
}
.info-no{
  background-color: #8796a8;
  color: white;
  border-radius: 3px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-right: 5px;
  padding-left: 5px;
  margin-left: 30px;
  cursor: default;
}
.project-plan{
  min-height: 60px;
  width: 100%;
  padding-top: 30px;
  display: flex;
  justify-content: center;
  border-top: solid 1px #e0e0e0;
  padding-bottom: 10px;
}
/*.project-plan-info{*/
/*  display: flex;*/
/*  justify-content: center;*/
/*  align-items: center;*/
/*  width: 100px;*/
/*  height: 60px;*/
/*  font-size: 14px;*/
/*  padding-right: 2%;*/
/*  padding-left: 2%;*/
/*  cursor: default;*/
/*  background-color: #e3e4e5;*/
/*}*/
/*.project-plan-in{*/
/*  display: flex;*/
/*  justify-content: center;*/
/*  align-items: center;*/
/*  width: 100px;*/
/*  height: 60px;*/
/*  font-size: 14px;*/
/*  padding-right: 2%;*/
/*  padding-left: 2%;*/
/*  cursor: default;*/
/*  background-color: #83d57d;*/
/*}*/
.project-context-t{
  width: 100%;
  border-top: solid 1px #e0e0e0;
  padding-left: 25px;
  font-size: 16px;
  font-weight: bold;
  color: #2f2f2f;
  padding-top: 20px;
}
.project-comment{
  min-height: 200px;
  background-color: white;
  width: 60%;
  margin-top: 20px;
  border-radius: 4px;
  box-shadow: 0 0 2px 0 #bdbdbd;
}
.close-project{
  padding-left: 25px;
  padding-bottom: 20px;
}
.close-project span{
  cursor: pointer;
  color: #b6b6b6;
}
.close-project span:hover{
  color: crimson;
}
.join-project{
  margin-top: 10px;
  border: none;
  margin-left: 20px;
  background-color: cornflowerblue;
  border-radius: 5px;
  font-size: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 20%;
  color: white;
  height: 60px;
  cursor: pointer;
}
.join-project:hover{
  background-color: #7eaaf8;
  font-size: 12px;
}
.person-username{
  padding-left: 20px;
  padding-top: 10px;
  font-weight: bolder;
  font-size: 22px;
}
.uname-text{
  color: #656565;
  cursor: pointer;
}
.uname-text:hover{
  color: cornflowerblue;
}
.call-person{
  float: right;
  padding-right: 20px;
  margin-right: 20px;
  padding-left: 20px;
  padding-top: 5px;
  padding-bottom: 5px;
  border-radius: 5px;
  background-color: cornflowerblue;
  color: white;
  cursor: pointer;
}
.call-person:hover{
  background-color: #338efd;
}
.this-person{
  float: right;
  padding-right: 20px;
  margin-right: 20px;
  padding-left: 20px;
  padding-top: 5px;
  padding-bottom: 5px;
  border-radius: 5px;
  background-color: #55b273;
  color: white;
  cursor: pointer;
}
.this-person:hover{
  background-color: #16a949;
}
.close-person{
  float: right;
  padding-right: 20px;
  margin-right: 20px;
  padding-left: 20px;
  padding-top: 5px;
  padding-bottom: 5px;
  border-radius: 5px;
  background-color: #bdbdbd;
  color: white;
  cursor: pointer;
}
.close-person:hover{
  background-color: #858585;
}
.is-bid{
  float: right;
  padding-right: 20px;
  margin-right: 20px;
  padding-left: 20px;
  padding-top: 5px;
  padding-bottom: 5px;
  border-radius: 5px;
  color: #676767;
  font-weight: bolder;
  cursor: pointer;
  border: solid 1px #5d5d5d;
}
</style>