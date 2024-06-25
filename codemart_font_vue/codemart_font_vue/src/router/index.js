import { createRouter, createWebHistory, onBeforeRouteLeave } from 'vue-router'
import axios from 'axios'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/Home/index.vue')
    },
    {
      path: '/pages',
      name: 'pages',
      component: () => import('../views/PageApp/pageApp.vue'),
      children:[
        {
          path: '/project',
          name: 'project',
          component: () => import('../views/project/index.vue')
        },
        {
          path: '/projectInfo',
          name: 'projectInfo',
          component: () => import('../views/project/info.vue'),
          props: true,
          // meta: { requiresAuth: true }
        },
        {
          path: '/projectSubmit',
          name: 'projectSubmit',
          component: () => import('../views/project/submit.vue'),
        },
        {
          path: '/test',
          name: 'test',
          component: () => import('../components/test.vue'),
        },
        {
          path: '/userInfo',
          name: 'userInfo',
          component: () => import('../views/user/info.vue'),
          redirect: '/myProject',
          children:[
            {
              path: '/myProject',
              name: 'myProject',
              component: () => import('../views/user/project/myProject.vue')
            },
            {
              path: '/myBidding',
              name: 'myBidding',
              component: () => import('../views/user/project/myBidding.vue')
            },
            {
              path: '/updateUserDetail',
              name: 'updateUserDetail',
              component: () => import('../views/user/project/updateUserDetail.vue')
            }
          ]
        },
        {
          path: '/selectUserInfo',
          name: 'selectUserInfo',
          component: () => import('../views/user/selectUserInfo.vue')
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/index/Login.vue')
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('../views/index/Register.vue')
    },
  ]
})

// router.beforeEach((to,from,next) => {
//   axios.get("http://localhost:9090/getRole",)
// })
// router.beforeEach((to,from,next)=>{
//     const tk = localStorage.getItem("token");
//     if(tk == null){
//
//     }
// })
// router.beforeEach((to, from, next) => {
//   const isAuthenticated = localStorage.getItem('token'); // 示例：检查是否存在 token
//   if (to.matched.some(record => record.meta.requiresAuth)) {
//     if (!isAuthenticated) {
//       alert("请先登录")
//       next({
//         path: from.path,
//       });
//     } else {
//       next();
//     }
//   } else {
//     next();
//   }
// });
export default router
