import Vue from "vue";
import Vuex from 'vuex';
Vue.use(Vuex)

// 在应用程序初始化时从本地存储中还原状态
const savedState = localStorage.getItem('storeState');
const initialState = savedState ? JSON.parse(savedState) : {};

const store = new Vuex.Store({
    state: {
        needLogin:false,
    },
    mutations: {
      Login (state, payload) {
        state.username = payload.authorName;
        state.password = payload.authorKey;
        state.uid = payload.authorId;
        state.userStatus = payload.userStatus;
      }
    }
  })

  export default store
