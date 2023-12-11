package com.ruoyi.course.service;

import java.util.List;
import com.ruoyi.course.domain.UserDocker;

/**
 * 用户-容器-映射端口信息Service接口
 * 
 * @author Achen
 * @date 2023-07-19
 */
public interface IUserDockerService 
{
    /**
     * 查询用户-容器-映射端口信息
     * 
     * @param id 用户-容器-映射端口信息主键
     * @return 用户-容器-映射端口信息
     */
    public UserDocker selectUserDockerById(Long id);

    /**
     * 查询用户-容器-映射端口信息列表
     * 
     * @param userDocker 用户-容器-映射端口信息
     * @return 用户-容器-映射端口信息集合
     */
    public List<UserDocker> selectUserDockerList(UserDocker userDocker);

    /**
     * 新增用户-容器-映射端口信息
     * 
     * @param userDocker 用户-容器-映射端口信息
     * @return 结果
     */
    public int insertUserDocker(UserDocker userDocker);

    /**
     * 修改用户-容器-映射端口信息
     * 
     * @param userDocker 用户-容器-映射端口信息
     * @return 结果
     */
    public int updateUserDocker(UserDocker userDocker);

    /**
     * 批量删除用户-容器-映射端口信息
     * 
     * @param ids 需要删除的用户-容器-映射端口信息主键集合
     * @return 结果
     */
    public int deleteUserDockerByIds(Long[] ids);

    /**
     * 删除用户-容器-映射端口信息信息
     * 
     * @param id 用户-容器-映射端口信息主键
     * @return 结果
     */
    public int deleteUserDockerById(Long id);
}
