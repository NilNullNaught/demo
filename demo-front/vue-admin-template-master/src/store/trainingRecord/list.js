const getDefaultState = () => {
    return {
        columnAttribute: [],
    }
}

const state = getDefaultState()


const mutations = {
    SET_COLUMNATTRIBUTE(state, columnAttribute) {
        state.columnAttribute = columnAttribute
    },
    RESET_COLUMNATTRIBUTE(state) {
        Object.assign(state, getDefaultState())
    },
}

const actions = {
    SET_COLUMNATTRIBUTE(context, columnAttribute) {
        context.commit('SET_COLUMNATTRIBUTE', columnAttribute)
    },
    RESET_COLUMNATTRIBUTE({ commit }) {
        commit('RESET_COLUMNATTRIBUTE')
    }
}

export default ({
    namespaced: true,
    state,
    mutations,
    actions,
})