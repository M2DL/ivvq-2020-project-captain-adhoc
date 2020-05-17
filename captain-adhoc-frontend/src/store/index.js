import Vuex from 'vuex'
import Vue from 'vue'
import axios from 'axios'
import { configs } from '../http-common'

Vue.use(Vuex)

const state = {
  commandes: [],
  cartProducts: [],
  products: []
}

export const mutations = {
  getData (state) {
    axios
      .get('/commandes', configs)
      .then(response => {
        response.data.forEach(commande => Vue.observable(commande.display = false))
        state.commandes = response.data
      })
  },
  storeProducts (state, productsToStore) {
    state.products = productsToStore
  },
  cartProducts (state, productsToCart) {
    state.cartProducts = productsToCart
  },
  displayContentCart (state, produit) {
    state.cartProducts.forEach(prod => {
      if (prod.id_produit === produit.id_produit) {
        prod.display = !prod.display
      }
    })
  },
  displayContent (state, commande) {
    state.commandes.forEach(com => {
      if (com.id_commande === commande.id_commande) {
        com.display = !com.display
      }
    })
  }
}

export const getters = {
  allProducts: state => {
    return state.produits
  }
}
export default new Vuex.Store({
  state,
  getters,
  mutations
})
