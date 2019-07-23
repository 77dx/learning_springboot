/**
 * Created by Administrator on 2018/7/10.
 */
import Vue from 'vue'
import Vuex from 'vuex'
import store from '../../vuex/store'

Vue.use(Vuex)
//服务器路径
var headUrl = 'http://192.168.5.84:8080';//测试环境
// var headUrl = 'https://bus.flins.com.cn/boss';//迁移测试环境
//var headUrl='https://api.flins.com.cn/boss'//预生产环境
// var headUrl = 'https://bms.flins.com.cn/boss';//线上环境

export const isNumber = (val) => {
  var regPos = /^\d+(\.\d+)?$/; //非负浮点数
  var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
  if (regPos.test(val) || regNeg.test(val)) {
    return true;
  } else {
    return false;
  }

}

export default {
  getapiAccess: function (name) {
    let arr = sessionStorage.getItem("buttonList")
    let arr1 = arr.split(',')
    let arr2=[]
    for(let i = 0;i<arr1.length;i++){
      arr2.push(arr1[i].replace(/:/g, '/'))
    }
    if(arr2.indexOf(name) > -1){
      return true
    }else{
      return false
    }
  },
  headUrl: headUrl
}
