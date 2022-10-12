const getDefaultState = () => {
    return {
        tableColumnWidth: ['180','180','180','180','180','180','150']
    }
}

const state = getDefaultState()


const mutations = {
    SET_TABLECOLUMNWIDTH(state, tableColumnWidth) {
        state.tableColumnWidth = tableColumnWidth
    },
    RESET_TABLECOLUMNWIDTH(state) {
        state.tableColumnWidth = getDefaultState()
    },
}

const actions = {
    SET_TABLECOLUMNWIDTH(context, tableColumnWidth) {
        context.commit('SET_TABLECOLUMNWIDTH', tableColumnWidth)
    },
    RESET_TABLECOLUMNWIDTH({ commit }) {
        commit('RESET_TABLECOLUMNWIDTH')
    }
}

export default ({
    namespaced: true,
    state,
    mutations,
    actions,
})