<template>

</template>

<script>
import axios from "axios";
import request from '../utils/request.js'
import { ElNotification } from 'element-plus'

export default {
  name: "info",
  data(){
    return{
    }
  },
  methods: {
    setHeaders() {
      let defaultHeaders = axios.defaults.headers.common;
      let addHeaders = {
        "token": localStorage.getItem('token'),
      }
      return {
        ...defaultHeaders,
        ...addHeaders
      };
    },
    getUser() {
      const headers = this.setHeaders();
      request.get("http://localhost:9090/userDetail", {headers:headers}).then(res=>{
        try {
          let code = res.code;
          if (code !== "200") {
            this.$router.push("/login")
            throw new Error(res.msg);
          }
        } catch (error) {
          this.$notify.error(res.msg)
        }
      })
    }
  },
  mounted() {
    this.getUser();
  }
}
</script>

<style>

</style>