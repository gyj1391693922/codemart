<template>
  <div class="body">
    <div class="body-left">
      <div class="body-head">
        <div style="height: 200px;width: 800px;">
          <div class="pj-options">
            <div style="color: #99a4a6;font-weight: bolder;display: inline-block;float: left;padding-top: 10px">
              角色:
            </div>
            <div style="padding-left: 8rem;height: 100%;width: 80%;position: relative">
              <div style="height: 100%;width: 100%;display: flex">
                <div class="option-query" :class="{ 'active': optionSelected === '全部' }"
                     @click="selectOption('全部');this.getProjectList()">
                  <div class="option-query-font">
                    全部
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelected === '开发团队' }"
                     @click="selectOption('开发团队');this.getProjectList('开发团队');selectTopicOption('全部')">
                  <div class="option-query-font">
                    开发团队
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelected === '开发工程师' }"
                     @click="selectOption('开发工程师');this.getProjectList('开发工程师');selectTopicOption('全部')">
                  <div class="option-query-font">
                    开发工程师
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelected === '设计师' }"
                     @click="selectOption('设计师');this.getProjectList('设计师');selectTopicOption('全部')">
                  <div class="option-query-font">
                    设计师
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelected === '产品经理' }"
                     @click="selectOption('产品经理');this.getProjectList('产品经理');selectTopicOption('全部')">
                  <div class="option-query-font">
                    产品经理
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelected === '测试工程师' }"
                     @click="selectOption('测试工程师');this.getProjectList('测试工程师');selectTopicOption('全部')">
                  <div class="option-query-font">
                    测试工程师
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="pj-options">
            <div style="color: #99a4a6;font-weight: bolder;display: inline-block;float: left;padding-top: 10px">
              项目标签:
            </div>
            <div style="padding-left: 8rem;height: 100%;width: 80%;position: relative">
              <div style="height: 100%;width: 120%;display: flex;flex-wrap: wrap;">
                <div class="option-query" :class="{ 'active': optionSelectedTopic === '全部' }"
                     @click="selectTopicOption('全部');this.getProjectList();selectOption('全部')">
                  <div class="option-query-font">
                    全部
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelectedTopic === '需求明确' }"
                     @click="selectTopicOption('需求明确');this.getProjectList('需求明确');selectOption('全部')">
                  <div class="option-query-font">
                    需求明确
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelectedTopic === '全新产品' }"
                     @click="selectTopicOption('全新产品');this.getProjectList('全新产品');selectOption('全部')">
                  <div class="option-query-font">
                    全新产品
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelectedTopic === '运维支持' }"
                     @click="selectTopicOption('运维支持');this.getProjectList('运维支持');selectOption('全部')">
                  <div class="option-query-font">
                    运维支持
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelectedTopic === '产品优化' }"
                     @click="selectTopicOption('产品优化');this.getProjectList('产品优化');selectOption('全部')">
                  <div class="option-query-font">
                    产品优化
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelectedTopic === '技术支持' }"
                     @click="selectTopicOption('技术支持');this.getProjectList('技术支持');selectOption('全部')">
                  <div class="option-query-font">
                    技术支持
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelectedTopic === '工具插件' }"
                     @click="selectTopicOption('工具插件');this.getProjectList('工具插件');selectOption('全部')">
                  <div class="option-query-font">
                    工具插件
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelectedTopic === '二次开发' }"
                     @click="selectTopicOption('二次开发');this.getProjectList('二次开发');selectOption('全部')">
                  <div class="option-query-font">
                    二次开发
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelectedTopic === '项目管理' }"
                     @click="selectTopicOption('项目管理');this.getProjectList('项目管理');selectOption('全部')">
                  <div class="option-query-font">
                    项目管理
                  </div>
                </div>
                <div class="option-query" :class="{ 'active': optionSelectedTopic === '测试' }"
                     @click="selectTopicOption('测试');this.getProjectList('测试');selectOption('全部')">
                  <div class="option-query-font">
                    测试
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="body-content">
        <div style="width: 90%;margin-left: 5%">
          <div class="body-content-top">
            <div style="padding-top: 40px;">
              <input class="search" v-model="tag"/>
              <button class="search-button" @click="this.getProjectList(this.tag)">搜索</button>
            </div>
          </div>
          <div class="project-list-container">
            <div class="project-list" v-for="(item, index) in items" :key="index" v-if="this.items.length > 0">
              <div class="project-title" @click="this.toInfo(item.de_id)">
                <span>{{item.title}}</span>
              </div>
              <div style="display: flex;margin-top: 10px">
                <div v-for="(i,index) in item.skuList" :key="index" style="background-color: #8c8dcb;width: 80px;
                                                                display: flex;justify-content: center;
                                                                align-items: center;height: 30px;border-radius: 5px;
                                                                margin-right: 10px;font-size: 12px;color: white">
                  {{i}}
                </div>
              </div>
              <div style="padding-top: 20px;max-width: 990px">
                {{item.context}}
              </div>
              <div style="display: flex">
                <div class="joinPerson">
                  竞标人数 : {{item.person}} 人
                </div>
                <div class="report" style="">
                  举报
                </div>
              </div>
            </div>
            <div class="project-list-none" v-if="this.items.length === 0">
              暂无更多订单
            </div>
          </div>
        </div>
      </div>
    </div>
    <div style="width: 20%;">
      <div class="body-right">
        <div style="padding-left: 25px;padding-top: 15px;padding-bottom: 15px;border-bottom: solid 1px #eaeaea;color: #313131">开发者工作指南
          <div style="float: right;margin-right: 25px;color: #3e6fb6;cursor: pointer;" @click="">
            了解更多
          </div>
        </div>
        <div style="border-left: solid 2px #eaeaea;margin-left: 25px;padding-left: 25px;margin-top: 20px;">
          <div>
            <div class="maker-teach">
              1. 注册并认证身份
            </div>
            <div class="maker-teach">
              2. 报名参与项目
            </div>
            <div class="maker-teach">
              3. 确认合作需求方

            </div>
            <div class="maker-teach">
              4. 项目阶段划分并启动
            </div>
            <div class="maker-teach">
              5. 阶段交付收款
            </div>
          </div>
        </div>
      </div>
      <div>
        <button class="submit-want" @click="this.goSubmit()">
          发布需求
        </button>
        <button class="submit-seam">
          寻找团队
        </button>
      </div>
    </div>
  </div>

</template>

<script>
import axios from 'axios'
import request from '../../utils/request.js'

export default {
  name: "index.vue",
  data(){
    return{
      optionSelected: "全部",
      optionSelectedTopic: "全部",
      items: [],
      tag:"",
      showError:false,
      message: ''
    }
  },
  methods:{
    selectOption(option) {
      this.optionSelected = option;
    },
    selectTopicOption(option){
      this.optionSelectedTopic = option;
    },
    getProjectList(tag){
      request.get("/api/project/list?tag="+tag).then(res=>{
        if(res.code === "200"){
          if(res.data != null){
            this.items = res.data
          }
        }else{
          this.$notify.error(res.msg);
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
    goSubmit(){
      let token = localStorage.getItem('token');
      if(token == null){
        this.$notify.error("请先登录后尝试");
      }else{
        this.$router.push("/projectSubmit")
      }
    },
  },
  mounted(){
    this.getProjectList();
  },
}
</script>

<style scoped>
.body{
  min-height: 100vh;
  background-color: #f6f6f6;
  display: flex;
}
.body-left{
  /*width: 60%;*/
  margin-left: 10%;
  margin-top: 20px;
}
.body-head{
  width: 80%;
  padding-left: 6rem;
  padding-right: 6rem;
  padding-top: 2rem;
  padding-bottom: 2rem;
  background-color: white;
  box-shadow: 0 1px 2px 0 #e0e0e0;
  border: solid 1px #f3f3f3;
  border-radius: 7px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.body-content{
  width: 99.5%;
  background-color: white;
  box-shadow: 0 1px 2px 0 #e0e0e0;
  border: solid 1px #f3f3f3;
  border-radius: 7px;
  margin-top: 20px;
  /*display: flex;*/
  /*justify-content: center;*/
  /*align-items: center;*/
}
.body-content-top{
  height: 100px;
  width: 100%;
  border-bottom: solid 1px #eaeaea;
}
.pj-options{
  width: 100%;
  height: 50%;
}
.option-query{
  display: flex;
  flex-wrap: wrap;
  height: 40px;
  justify-content: center;
  align-items: center;
  margin-right: 5px;
  color: #505050;
  border-radius: 3px;
}
.option-query-font{
  display: inline-block;
  padding-right: 1rem;
  padding-left: 1rem;
}
.option-query:hover{
  background-color: cornflowerblue;
  cursor: pointer;
  color: white;
}
.option-query.active {
  background-color: cornflowerblue;
  color: white;
}

.search{
  height: 30px;
  width: 200px;
  border-inline: none;
  border: solid 1px #ecf1f1;
  border-radius: 4px;
  background-color: #fafafa;
  outline: none;
  padding-left: 15px;
  caret-color: #5d5d5d;
  color: #676767;
  font-size: 16px;
}
.search-button{
  border: solid 1px #e5e8e8;
  height: 35px;
  width: 70px;
  margin-left: 10px;
  border-radius: 3px;
  cursor: pointer;
  background-color: #fcf8f8;
}
.search-button:hover{
  background-color: cornflowerblue;
  color: white;
}
.project-list-container {
  /*overflow-y: auto;*/
  display: flex;
  flex-direction: column;
}
.project-list{
  min-height: 150px;
  border-bottom: solid 1px #eaeaea;
  box-shadow: 0 0 2px 0 #e7e9ea;
}
.body-right{
  background-color: white;
  height: 400px;
  margin-top: 20px;
  margin-left: 10px;
  border-radius: 3px;
  box-shadow: 0 1px 2px 0 #e0e0e0;
}
.maker-teach{
  margin-top: 40px;
  color: #6c6c6c;
}
.project-title{
  padding-top: 10px;
  font-size: 20px;
  font-weight: bolder;
  color: #575757;
}
.project-title span{
  cursor: pointer
}
.project-title span:hover{
  color: cornflowerblue;
}
.report{
  padding-top: 20px;
  color: #b6b6b6;
  padding-bottom: 10px;
  font-size: 14px;
  cursor: pointer;
  width: 40px;
}
.report:hover{
  color: cornflowerblue;
}
.joinPerson{
  padding-top: 20px;
  color: #b6b6b6;
  padding-bottom: 10px;
  font-size: 14px;
  width: 120px;
}
.project-list-none{
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  color: #8f8f8f;
  font-size: 22px;
  font-weight: bolder;
}
.submit-want{
  margin-top: 10px;
  border: none;
  margin-left: 10px;
  background-color: cornflowerblue;
  border-radius: 5px;
  font-size: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 98%;
  color: white;
  height: 60px;
  cursor: pointer;
}
.submit-want:hover{
  background-color: #4266ab;
}
.submit-seam{
  margin-top: 10px;
  border: none;
  margin-left: 10px;
  background-color: #ed8464;
  border-radius: 5px;
  font-size: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 98%;
  color: white;
  height: 60px;
  cursor: pointer;
}
.submit-seam:hover{
  background-color: #d76642;
}
</style>