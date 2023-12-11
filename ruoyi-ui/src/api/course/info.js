import request from '@/utils/request'

// 查询文件信息列表
export function listInfo(query) {
  return request({
    url: '/course/info/list',
    method: 'get',
    params: query
  })
}

// 查询文件信息详细
export function getInfo(fileId) {
  return request({
    url: '/course/info/' + fileId,
    method: 'get'
  })
}

// 新增文件信息
export function addInfo(data) {
  return request({
    url: '/course/info',
    method: 'post',
    data: data
  })
}

// 修改文件信息
export function updateInfo(data) {
  return request({
    url: '/course/info',
    method: 'put',
    data: data
  })
}

// 删除文件信息
export function delInfo(fileId) {
  return request({
    url: '/course/info/' + fileId,
    method: 'delete'
  })
}
