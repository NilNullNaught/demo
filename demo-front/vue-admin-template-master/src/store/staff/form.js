const getDefaultState = () => {
  return {
    formValidate: {
      id: "0",
      name: "",
      sex: 0,
      idcardNumber: "",
      department: 0,
      formalSchooling: 0,
      mail: ""
    }
  }
}

const state = getDefaultState()

const mutations = {
  SET_FORMVALIDATE(state, formValidate) {
    state.formValidate = formValidate
  },
  RESET_FORMVALIDATE(state) {
    state.formValidate = getDefaultState()
  },
}

const actions = {
  SET_FORMVALIDATE(context, formValidate) {
    context.commit('SET_FORMVALIDATE', formValidate)
  },
  RESET_FORMVALIDATE({ commit }) {
    commit('RESET_FORMVALIDATE')
  }
}

export default ({
  namespaced: true,
  state,
  mutations,
  actions,
})