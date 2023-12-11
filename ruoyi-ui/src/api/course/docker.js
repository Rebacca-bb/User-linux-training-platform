import request from '@/utils/request'

// 查询用户-容器-映射端口信息列表
export function listDocker(query) {
  return request({
    url: '/course/docker/list',
    method: 'get',
    params: query
  })
}

// 查询用户-容器-映射端口信息详细
export function getDocker(id) {
  return request({
    url: '/course/docker/' + id,
    method: 'get'
  })
}

// 新增用户-容器-映射端口信息
export function addDocker(data) {
  return request({
    url: '/course/docker',
    method: 'post',
    data: data
  })
}

// 修改用户-容器-映射端口信息
export function updateDocker(data) {
  return request({
    url: '/course/docker',
    method: 'put',
    data: data
  })
}

// // 删除用户-容器-映射端口信息
// export function delDocker(id) {
//   return request({
//     url: '/course/docker/' + id,
//     method: 'delete'
//   })
// }
// 删除用户-容器-映射端口信息
export function delDocker(id, vm) {
  // 显示进度条
  vm.progressBar = true;

  return request({
    url: '/course/docker/' + id,
    method: 'delete'
  }).then(() => {
    // 隐藏进度条
    vm.progressBar = false;
  }).catch(() => {
    // 隐藏进度条
    vm.progressBar = false;
  });
}
