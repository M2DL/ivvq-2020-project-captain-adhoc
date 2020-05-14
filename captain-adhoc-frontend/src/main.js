// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import Vuelidate from 'vuelidate'
import store from './store'

import '@fortawesome/fontawesome-free/css/all.css'
import '@fortawesome/fontawesome-free/js/all.js'

import '../node_modules/bulma/css/bulma.css'
import '../node_modules/animate.css/animate.css'

import 'vuelidate/dist/vuelidate.min.js'
Vue.config.productionTip = false

Vue.use(Vuelidate)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  store
})
