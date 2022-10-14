const getDefaultState = () => {
    return {
        formValidate: {
            id: '0',
            trainingTeacher: '',
            trainingDate: new Date(),
            trainingContent: '',
            list:[]
        }
    }
}

const state = getDefaultState()

const mutations = {
    SET_FORMVALIDATE(state, formValidate) {
        state.formValidate = formValidate
    },
    RESET_FORMVALIDATE(state) {
        Object.assign(state, getDefaultState())
    },
}

const actions = {
    SET_FORMVALIDATE(context, formValidate) {
        context.commit('SET_FORMVALIDATE', formValidate)
    },
    RESET_FORMVALIDATE({ commit }) {
        commit('RESET_FORMVALIDATE')
    },
}

export default ({
    namespaced: true,
    state,
    mutations,
    actions,
})