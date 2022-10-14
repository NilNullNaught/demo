import request from '@/utils/request'

const api_name = '/demo/training-record'

export default {
    // 新增一条培训记录
    add(formdata) {
        return request({
            url: `${api_name}`,
            method: 'post',
            data: formdata
        })
    },

    // 培训记录复杂查询
    trainingRecordComplexQuery(formdata) {
        return request({
            url: `${api_name}/trainingRecordComplexQuery`,
            method: 'get',
            params: formdata
        })
    },
    // 修改一条培训记录
    edit(formdata) {
        return request({
            url: `${api_name}`,
            method: 'put',
            data: formdata
        })
    },

    // 根据 ID 查询单条培训记录及其参与人员
    query(param) {
        return request({
            url: `${api_name}`,
            method: 'get',
            params: param
        })
    },
    // 删除一条培训记录及所有参与人员
    delete(formdata) {
        return request({
            url: `${api_name}`,
            method: 'delete',
            params: formdata
        })
    }
}