<template>
  <nav class="navbar is-white" role="navigation" aria-label="main navigation" :key="navKey">

    <div class="navbar-brand">
      <router-link to="/home" class="navbar-item is-flex">
        <div>
          <img src="../assets/brand-icon.png" alt="Accueil">
        </div>
        <div>
          <span><b>Accueil</b></span>
        </div>
      </router-link>

      <a v-on:click="burgerClick"
          role="button"
          class="navbar-burger burger"
          aria-label="menu"
          aria-expanded="false"
          data-target="navbar-menu">
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
      </a>
    </div>

  <div id="navbar-menu" class="navbar-menu">
    <div class="navbar-start">
      <div class="is-flex" v-if="isLogged">
        <router-link to="/products" class="navbar-item is-flex">
          <div class="fontawesome-icon">
            <span class="fas fa-grip-horizontal"></span>
          </div>
          <div>
            <span>Produits</span>
          </div>
        </router-link>
      </div>
      <div class="is-flex" v-if="isLogged && user.isAdmin">
        <router-link to="/inventory" class="navbar-item is-flex">
          <div class="fontawesome-icon">
            <i class="fas fa-suitcase"></i>
          </div>
          <div>
            <span>Inventaire</span>
          </div>
        </router-link>
      </div>
    </div>

    <div class="navbar-end">
      <div v-if="!isLogged" class="buttons is-flex">
        <router-link to="/log-in" class="navbar-item button is-flex">
          <div class="fontawesome-icon">
            <span class="fas fa-sign-in-alt"></span>
          </div>
          <div>
            <span>Connexion</span>
          </div>
        </router-link>

        <router-link to="/sign-up" class="navbar-item button is-flex">
          <div class="fontawesome-icon">
            <span class="fas fa-user-plus"></span>
          </div>
          <div>
            <span>Inscription</span>
          </div>
        </router-link>

      </div>
      <div v-else class="buttons is-flex">
        <router-link to="/cart" class="navbar-item button is-flex" aria-label="Your cart">
          <div class="fontawesome-icon">
            <span class="fas fa-cart-arrow-down"></span>
          </div>
          <div>
            <span>Panier</span>
          </div>
        </router-link>

        <router-link to="/purchases-history" class="navbar-item button is-flex">
          <div class="fontawesome-icon">
            <span class="fas fa-history"></span>
          </div>
          <div>
            <span>Commandes</span>
          </div>
        </router-link>

        <button @click="openLogoutModal" class="navbar-item button is-flex">
          <div class="fontawesome-icon">
            <span class="fas fa-sign-out-alt"></span>
          </div>
          <div>
            <span>Déconnexion</span>
          </div>
        </button>
      </div>
    </div>
  </div>
  </nav>
</template>

<script>
import LogoutModal from '@/components/LogoutModal'
export default {
  name: 'Navbar',
  components: { LogoutModal },
  data () {
    return {
      isLogged: '',
      user: '',
      navKey: 0
    }
  },
  created () {
    this.unwatch = this.$store.watch(
      (state, getters) => getters.isLoggedStore,
      (userStatus) => {
        this.isLogged = userStatus
        this.forceRerender()
      }
    )
    this.unwatchUser = this.$store.watch(
      (state, getters) => getters.userStore,
      (userStore) => {
        this.user = userStore
      }
    )
  },

  beforeDestroy () {
    this.unwatch()
    this.unwatchUser()
  },
  methods: {
    burgerClick: function () {
      const burger = document.querySelector('.burger')
      const navMenu = document.querySelector('#' + burger.dataset.target)

      burger.classList.toggle('is-active')
      navMenu.classList.toggle('is-active')

      const buttons = navMenu.querySelector('.buttons')
      buttons.classList.toggle('animated')
      buttons.classList.toggle('slideInLeft')

      document.querySelector('.navbar').classList.toggle('is-mobile')
    },
    openLogoutModal () {
      this.$buefy.modal.open({
        parent: this,
        component: LogoutModal,
        hasModalCard: true,
        trapFocus: true
      })
    },
    forceRerender () {
      this.$forceUpdate()
      this.navKey += 1
    }
  }
}

</script>

<style scoped>
img {
  padding-right: 0.325rem;
}

.buttons {
  padding-right: 0.325rem;
}

div.fontawesome-icon {
  padding-right: 0.325rem;
  font-size: 1.175rem;
}

nav.is-mobile  {
  display: flex;
  flex-direction: column;
}
.navbar-menu.is-active {
  display: flex;
  justify-content: space-between;
  flex-direction: row;
}

</style>
