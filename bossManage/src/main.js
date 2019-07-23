// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/style.css'//加载css
import router from './router'//加载路由
import axios from 'axios' //加载数据请求
import util from './plugins/util/util' //加载帮助类
import servuce from './plugins/util/servuce' //加载帮助类
import api from './services/api' //加载帮助类
import md5 from 'js-md5'//加载md5

Vue.use(element);
Vue.prototype.$md5 = md5;
Object.defineProperty(Vue.prototype, '$http', { value: axios }); //把axios 加入到Vue对象里面
Object.defineProperty(Vue.prototype, '$util', { value: util }); //把util 加入到Vue对象里面
Object.defineProperty(Vue.prototype, '$servuce', { value: servuce }); //servuce 加入到Vue对象里面
Object.defineProperty(Vue.prototype, '$api', { value: api }); //api 加入到Vue对象里面

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
