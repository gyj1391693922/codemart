<template>
    <div v-if="this.showMessage" class="message-box">{{ this.message }}
    </div>
</template>

<script>
export default {
  name: "tip",
  props:{
    showMessage:{
      type: Boolean,
      required: true
    },
    message:{
      type: String,
      required: true
    }
  }
}
</script>

<style scoped>
.message-box {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 10px 20px;
  background-color: #c24545;
  color: white;
  border-radius: 5px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  opacity: 1;
  transition: opacity 0.5s ease-in-out;
}
.message-hidden {
  opacity: 0;
}
.close-message{
  float: right;
  margin-left: 20px;
  cursor: default;
}
</style>