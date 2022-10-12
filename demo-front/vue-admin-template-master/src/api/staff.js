import request from '@/utils/request'

const api_name = '/demo/staff-info'

export default {
  // 根据 id 查询一条员工资料
  query(param) {
    return request({
      url: `${api_name}`,
      method: 'get',
      params: param
    })
  },

  // 查询所有员工名
  queryAllName(){
    return request({
      url: `${api_name}/queryAllName`,
      method: 'get'
    })
  },

  // 员工资料复杂查询
  staffComplexQuery(formdata) {
    return request({
      url: `${api_name}/staffComplexQuery`,
      method: 'get',
      params: formdata
    })
  },

  // 新增一条员工资料
  add(formdata) {
    return request({
      url: `${api_name}`,
      method: 'post',
      data: formdata
    })
  },
  // 修改一条员工资料
  edit(formdata){
    return request({
      url: `${api_name}`,
      method: 'put',
      data: formdata
    })
  },
  // 删除一条员工资料
  delete(formdata) {
    return request({
      url: `${api_name}`,
      method: 'delete',
      params: formdata
    })
  }
}