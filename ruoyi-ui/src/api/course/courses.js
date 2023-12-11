import request from '@/utils/request'

// 查询课程列表
export function listCourses(query) {
  return request({
    url: '/course/courses/list',
    method: 'get',
    params: query
  })
}

// 查询课程详细
export function getCourses(courseId) {
  return request({
    url: '/course/courses/' + courseId,
    method: 'get'
  })
}

// 新增课程
export function addCourses(data) {
  return request({
    url: '/course/courses',
    method: 'post',
    data: data
  })
}

// 修改课程
export function updateCourses(data) {
  return request({
    url: '/course/courses',
    method: 'put',
    data: data
  })
}

// 删除课程
export function delCourses(courseId) {
  return request({
    url: '/course/courses/' + courseId,
    method: 'delete'
  })
}
