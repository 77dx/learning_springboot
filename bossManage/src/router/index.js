import Vue from 'vue'
import Router from 'vue-router'
import login from '@/components/login'
import index from '@/components/index'
import blank from '@/components/blank'
import ocrConfirm from '@/components/businessManage/ocrConfirm'
import evaluationQuery from '@/components/businessManage/evaluationQuery'
import evaluationQueryDetails from '@/components/businessManage/evaluationQueryDetails'
import manualEvaluation from '@/components/businessManage/manualEvaluation'
import ocrDetails from '@/components/businessManage/ocrDetails'
import manualEvaluationDetails from '@/components/businessManage/manualEvaluationDetails'
import userList from '@/components/userCenter/userList'
import roleList from '@/components/userCenter/roleList'
import permission from '@/components/userCenter/perssion'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'login',
      component: login
    },
    {
      path: '/index',
      name: 'index',
      component: index,
      redirect: '/ocrConfirm',
      children: [
        {
          path:'/ocrConfirm',
          nam:'ocrConfirm',
          component:ocrConfirm
        },
        {
          path: '/evaluationQuery',
          name: 'evaluationQuery',
          component: evaluationQuery,
        },
        {
          path:'/manualEvaluation',
          nam:'manualEvaluation',
          component:manualEvaluation
        },
        {
          path:'/ocrDetails',
          nam:'ocrDetails',
          component:ocrDetails
        },
        {
          path:'/manualEvaluationDetails',
          nam:'manualEvaluationDetails',
          component:manualEvaluationDetails
        },
        {
          path:'/evaluationQueryDetails',
          nam:'evaluationQueryDetails',
          component:evaluationQueryDetails
        },
        {
          path:'/userList',
          nam:'userList',
          component:userList
        },
        {
          path:'/roleList',
          nam:'roleList',
          component:roleList
        },
        {
          path:'/permission',
          nam:'permission',
          component:permission
        },
        {
          path: '/blank',
          name: 'blank',
          component: blank
        },
      ]
    }
  ],
})
