import Vue from 'vue'
import Vuex from 'vuex'
import * as actions from './actions'
import * as getters from './getters'
Vue.use(Vuex)

// 应用初始状态
const state = {
  buttonList:[]
};
// 定义所需的 mutations
const mutations = {
  getAccess(state, info) {
    console.log(info)
    state.buttonList = info
  }
}
// 创建 store 实例
export default new Vuex.Store({
  actions,
  getters,
  state,
  mutations,
})
