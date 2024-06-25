<template>
  <div id="cm-nums">
    <span class="cm-num">{{this.cnt}}</span>
    <span class="cm-font">{{this.context}}</span>
  </div>
</template>

<script>
import axios from 'axios';
import request from '../utils/request.js'

export default {
  name: "cm_nums.vue",
  props:{
    request_url:{
      type:String,
      required:true
    },
    context:{
      type:String,
      required: true
    }
  },
  data() {
    return {
      cnt: 0.0
    };
  },
  methods:{
    getTheNum(URL){
      // const url = "http://localhost:9090"+URL
      request.get("/api"+URL).then(res=>{
        this.cnt = res.data.cnt
      })
    }
  },
  mounted(){
    this.getTheNum(this.request_url)
  }
}
</script>

<style scoped>
#cm-nums{
  background-color: #eaeaea;
  height: 100%;
  width: 30%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.cm-num{
  color: #418cfd;
  font-size: 34px;
  font-weight: bold;
}
.cm-font{
  color: #6b6b6b;
  font-size: 14px;
  font-weight: bold;
}
</style>