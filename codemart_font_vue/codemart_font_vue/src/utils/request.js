import axios from 'axios'
import router from '@/router'
import { ElNotification } from 'element-plus';

const request = axios.create({
    // baseURL: 'http://192.168.23.221:9090',
    timeout: 5000
})
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    // router.push('login')
    // config.headers['token'] = user.token;  // 设置请求头
    return config
}, error => {
    // ElNotification.error({
    //     title: 'Request Error',
    //     message: `Request error: ${error.message}`
    // });
    return Promise.reject(error)
});

request.interceptors.response.use(
    response => {
        let res = response.data;
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        console.log('err' + error)
        ElNotification.error({
            title: 'Response Error',
            message: `Response error: ${error.message}`
        });
        return Promise.reject(error)
    }
)

export default request