import History from '@/components/History'
import { mount, createLocalVue } from '@vue/test-utils'
import chai from 'chai'
import { mutations } from '@/store/index'
import Vuex from 'vuex'
import Vue from 'vue'
import sinon from 'sinon'
import moxios from 'moxios'
import axios from 'axios'

Vue.use(Vuex)

const localVue = createLocalVue()
localVue.use(Vuex)
let store
let respond = [
  {
    id_commande: 4,
    display: false,
    date_commande: '2020-05-21T13:12:09.918+0000',
    code: 'code',
    commandeProduitsList: [
      {
        id_commandeProduit: 7,
        produit: {
          id_produit: 2,
          quantite_produit: 25,
          nom_produit: 'produit1',
          description_produit: 'description1',
          image_produit: 'https://i.pinimg.com/originals/d4/51/bd/d451bd6be0a4bdb720b8e3386c15a855.jpg',
          prix_produit: 1,
          marchand: {
            id_marchand: 1,
            identifiant_marchand: 'marchand1'
          }
        },
        quantite_commande_produit: 15
      }
    ],
    acheteur: null
  },
  {
    id_commande: 5,
    display: true,
    date_commande: '2020-05-22T13:12:09.918+0000',
    code: 'code',
    commandeProduitsList: [
      {
        id_commandeProduit: 8,
        produit: {
          id_produit: 2,
          quantite_produit: 20,
          nom_produit: 'produit1',
          description_produit: 'description1',
          image_produit: 'https://i.pinimg.com/originals/d4/51/bd/d451bd6be0a4bdb720b8e3386c15a855.jpg',
          prix_produit: 1,
          marchand: {
            id_marchand: 1,
            identifiant_marchand: 'marchand1'
          }
        },
        quantite_commande_produit: 15
      },
      {
        id_commandeProduit: 9,
        produit: {
          id_produit: 3,
          quantite_produit: 16,
          nom_produit: 'produit2',
          description_produit: 'description2',
          image_produit: 'https://i.pinimg.com/originals/d4/51/bd/d451bd6be0a4bdb720b8e3386c15a855.jpg',
          prix_produit: 2,
          marchand: {
            id_marchand: 1,
            identifiant_marchand: 'marchand1'
          }
        },
        quantite_commande_produit: 16
      }
    ],
    acheteur: null
  },
  {
    id_commande: 6,
    display: true,
    date_commande: '2020-05-23T13:12:09.918+0000',
    code: '',
    commandeProduitsList: [
      {
        id_commandeProduit: 10,
        produit: {
          id_produit: 3,
          quantite_produit: 20,
          nom_produit: 'produit2',
          description_produit: 'description2',
          image_produit: 'https://i.pinimg.com/originals/d4/51/bd/d451bd6be0a4bdb720b8e3386c15a855.jpg',
          prix_produit: 2,
          marchand: {
            id_marchand: 1,
            identifiant_marchand: 'marchand1'
          }
        },
        quantite_commande_produit: 16
      }
    ],
    acheteur: null
  }
]

describe('History.vue', () => {
  beforeEach(() => {
    moxios.install(axios)
  })

  afterEach(() => {
    moxios.uninstall(axios)
  })

  it('Should calculate good total ', (done) => {
    // Given
    moxios.withMock(function () {
      let spy = sinon.spy()
      axios.get('/commandes').then(spy)
      moxios.wait(() => {
        let request = moxios.requests.mostRecent()
        request.respondWith({
          status: 200,
          response: {
            commandes: respond
          }
        }).then(
          response => {
            submit(response.data.commandes)

            const wrapper = mount(History, {
              store,
              localVue
            })

            // When
            /*
              Commande 1:
                    quantite_commande_produit: 15,
                    prix_produit: 1,
                    total = 15 * 1 = 15
             */
            // Then
            chai.assert.strictEqual(wrapper.findAll('.card-header-title.total').at(0).text(), '15€')

            // When
            /*
              Commande 2:
                    quantite_commande_produit: 15,
                    prix_produit: 1,
                    total1 = 15 * 1 = 15
                    quantite_commande_produit: 16,
                    prix_produit: 2,
                    total = 16 * 2 = 32
                    total = total1 + total2 = 15 + 32 = 47
             */
            // Then
            chai.assert.strictEqual(wrapper.findAll('.card-header-title.total').at(1).text(), '47€')

            // When
            /*
                Commande 3:
                   quantite_commande_produit: 16,
                   prix_produit: 2,
                   total = 16 * 2 = 32
            */
            // Then
            chai.assert.strictEqual(wrapper.findAll('.card-header-title.total').at(2).text(), '32€')
            done()
          })
      })
    })
  })

  it('Should getTotalPrix calculate good total ', () => {
    // Given
    const spy = sinon.spy(History.methods, 'getTotalPrix')
    let commande = {
      commandeProduitsList: [
        {
          produit: {
            prix_produit: 5
          },
          quantite_commande_produit: 6
        },
        {
          produit: {
            prix_produit: 4
          },
          quantite_commande_produit: 36
        }
      ]
    }

    // When
    let total = History.methods.getTotalPrix(commande)

    // Then
    chai.assert.strictEqual(total, 174)
    chai.assert.strictEqual(spy.calledOnce, true)
  })

  it('Should display simple data ', (done) => {
    // Given
    moxios.withMock(function () {
      let spy = sinon.spy()
      axios.get('/commandes').then(spy)
      moxios.wait(() => {
        let request = moxios.requests.mostRecent()
        request.respondWith({
          status: 200,
          response: {
            commandes: respond
          }
        }).then(
          response => {
            submit(response.data.commandes)
            const wrapper = mount(History, {
              store,
              localVue
            })
            /*
              Commande 1:
                date_commande: '2020-05-21T13:12:09.918+0000',
             */
            chai.assert.strictEqual(wrapper.findAll('.card-header-title.date').at(0).text(), '2020-05-21 13:12')

            /*
              Commande 2:
                date_commande: '2020-05-22T13:12:09.918+0000',
             */
            chai.assert.strictEqual(wrapper.findAll('.card-header-title.date').at(1).text(), '2020-05-22 13:12')

            /*
              Commande 3:
                date_commande: '2020-05-23T13:12:09.918+0000',
            */
            chai.assert.strictEqual(wrapper.findAll('.card-header-title.date').at(2).text(), '2020-05-23 13:12')
            done()
          })
      })
    })
  })

  it('Should call store action displayContent when button is clicked', (done) => {
    // Given
    moxios.withMock(function () {
      let spy = sinon.spy()
      axios.get('/commandes').then(spy)
      moxios.wait(() => {
        const spy = sinon.spy(History.methods, 'displayContent')

        let request = moxios.requests.mostRecent()
        request.respondWith({
          status: 200,
          response: {
            commandes: respond
          }
        }).then(
          response => {
            submit(response.data.commandes)
            const wrapper = mount(History, {
              store,
              localVue
            })

            // When
            wrapper.find('header').trigger('click')

            // Then
            chai.assert.strictEqual(spy.calledOnce, true)
            done()
          })
      })
    })
  })

  it('Should be unfold after a clicked when it was fold', (done) => {
    // Given
    moxios.withMock(function () {
      let spy = sinon.spy()
      axios.get('/commandes').then(spy)
      moxios.wait(() => {
        let request = moxios.requests.mostRecent()
        request.respondWith({
          status: 200,
          response: {
            commandes: respond
          }
        }).then(
          response => {
            submit(response.data.commandes)
            const wrapper = mount(History, {
              store,
              localVue
            })

            // When
            wrapper.findAll('header').at(1).trigger('click')

            // Then
            chai.assert.strictEqual(store.state.commandes[1].display, false)
            chai.assert.strictEqual(store.state.commandes[2].display, true)
            done()
          })
      })
    })
  })

  it('Should be fold after a clicked when it was unfold', (done) => {
    // Given
    moxios.withMock(function () {
      let spy = sinon.spy()
      axios.get('/commandes').then(spy)
      moxios.wait(() => {
        let request = moxios.requests.mostRecent()
        request.respondWith({
          status: 200,
          response: {
            commandes: respond
          }
        }).then(
          response => {
            submit(response.data.commandes)
            const wrapper = mount(History, {
              store,
              localVue
            })

            // When
            wrapper.findAll('header').at(1).trigger('click')

            // Then
            chai.assert.strictEqual(store.state.commandes[1].display, true)
            chai.assert.strictEqual(store.state.commandes[2].display, true)
            done()
          })
      })
    })
  })

  it('Should show the code when there is a code', (done) => {
    // Given
    moxios.withMock(function () {
      let spy = sinon.spy()
      axios.get('/commandes').then(spy)
      moxios.wait(() => {
        let request = moxios.requests.mostRecent()
        request.respondWith({
          status: 200,
          response: {
            commandes: respond
          }
        }).then(
          response => {
            submit(response.data.commandes)
            const wrapper = mount(History, {
              store,
              localVue
            })

            // When
            // for commande 1 code: 'code'
            wrapper.findAll('header').at(0).trigger('click')

            // Then
            chai.assert.strictEqual(wrapper.findAll('.codeToDisplay').at(0).text(), 'Code code')
            done()
          })
      })
    })
  })

  it('Should not show the code when there is no code', (done) => {
    // Given
    moxios.withMock(function () {
      let spy = sinon.spy()
      axios.get('/commandes').then(spy)
      moxios.wait(() => {
        let request = moxios.requests.mostRecent()
        request.respondWith({
          status: 200,
          response: {
            commandes: respond
          }
        }).then(
          response => {
            submit(response.data.commandes)
            const wrapper = mount(History, {
              store,
              localVue
            })

            // When
            // for commande 3 code: ''
            wrapper.findAll('header').at(2).trigger('click')

            // Then
            chai.assert.strictEqual(wrapper.findAll('.noCodeToDisplay').at(0).text(), 'Aucun code utilisé')
            done()
          })
      })
    })
  })

  it('Should give (n/a) from null date', () => {
    // Given
    submit(respond)
    const wrapper = mount(History, {
      store,
      localVue
    })
    // When
    const dateReturn = wrapper.vm.date('')
    wrapper.vm.$forceUpdate()

    // Then
    chai.assert.strictEqual(dateReturn, '(n/a)')
  })

  it('Should give a date from conform date', () => {
    // Given
    submit(respond)
    const wrapper = mount(History, {
      store,
      localVue
    })
    // When
    const dateReturn = wrapper.vm.date('2020-10-09T13:12:20.918+0000')
    wrapper.vm.$forceUpdate()

    // Then
    chai.assert.strictEqual(dateReturn, '2020-10-09 13:12')
  })
})

function submit (commandes) {
  store = new Vuex.Store({
    mutations,
    state: {
      commandes: commandes
    }
  })
}
