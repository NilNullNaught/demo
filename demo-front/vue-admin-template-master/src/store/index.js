import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import app from './modules/app'
import settings from './modules/settings'
import user from './modules/user'
import staffForm from './staff/form'
import staffList from './staff/list'
import trainingRecord from './trainingRecord/form'

// 持久化插件
import createPersistedState from 'vuex-persistedstate'
Vue.use(Vuex)

const dataState = createPersistedState({
  key: 'demo-loaldata',
  paths: ['staffForm','staffList','trainingRecord']
})

const store = new Vuex.Store({
  modules: {
    app,
    settings,
    user,
    staffForm,
    staffList,
    trainingRecord
  },
  plugins: [
    dataState
  ],
  getters
})

export default store
