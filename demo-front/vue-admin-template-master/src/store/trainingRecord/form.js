const getDefaultState = () => {
    return {
        formValidate: {
            id: '0',
            trainingTeacher: '',
            trainingDate: new Date(),
            trainContent: '',
        },
        list: []
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
    SET_LIST(state, list) {
        state.list = list
    },
    RESET_LIST(list) {
        state.list = getDefaultState()
    },
}

const actions = {
    SET_FORMVALIDATE(context, formValidate) {
        context.commit('SET_FORMVALIDATE', formValidate)
    },
    RESET_FORMVALIDATE({ commit }) {
        commit('RESET_FORMVALIDATE')
    },
    SET_LIST(context, list) {
        context.commit('SET_LIST', list)
    },
    RESET_LIST({ commit }) {
        commit('RESET_LIST')
    },
}

export default ({
    namespaced: true,
    state,
    mutations,
    actions,
})