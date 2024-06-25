import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import 'element-plus/dist/index.css'
import elementplus from 'element-plus'

const app = createApp(App)

app.use(router)


app.use(elementplus)
app.mount('#app')
