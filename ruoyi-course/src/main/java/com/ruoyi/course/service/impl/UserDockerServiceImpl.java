package com.ruoyi.course.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.course.mapper.UserDockerMapper;
import com.ruoyi.course.domain.UserDocker;
import com.ruoyi.course.service.IUserDockerService;

/**
 * 用户-容器-映射端口信息Service业务层处理
 * 
 * @author Achen
 * @date 2023-07-19
 */
@Service
public class UserDockerServiceImpl implements IUserDockerService 
{
    @Autowired
    private UserDockerMapper userDockerMapper;

    /**
     * 查询用户-容器-映射端口信息
     * 
     * @param id 用户-容器-映射端口信息主键
     * @return 用户-容器-映射端口信息
     */
    @Override
    public UserDocker selectUserDockerById(Long id)
    {
        return userDockerMapper.selectUserDockerById(id);
    }

    /**
     * 查询用户-容器-映射端口信息列表
     * 
     * @param userDocker 用户-容器-映射端口信息
     * @return 用户-容器-映射端口信息
     */
    @Override
    public List<UserDocker> selectUserDockerList(UserDocker userDocker)
    {
        return userDockerMapper.selectUserDockerList(userDocker);
    }

    /**
     * 新增用户-容器-映射端口信息
     * 
     * @param userDocker 用户-容器-映射端口信息
     * @return 结果
     */
    @Override
    public int insertUserDocker(UserDocker userDocker)
    {
        return userDockerMapper.insertUserDocker(userDocker);
    }

    /**
     * 修改用户-容器-映射端口信息
     * 
     * @param userDocker 用户-容器-映射端口信息
     * @return 结果
     */
    @Override
    public int updateUserDocker(UserDocker userDocker)
    {
        return userDockerMapper.updateUserDocker(userDocker);
    }

    /**
     * 批量删除用户-容器-映射端口信息
     * 
     * @param ids 需要删除的用户-容器-映射端口信息主键
     * @return 结果
     */
    @Override
    public int deleteUserDockerByIds(Long[] ids)
    {
        return userDockerMapper.deleteUserDockerByIds(ids);
    }

    /**
     * 删除用户-容器-映射端口信息信息
     * 
     * @param id 用户-容器-映射端口信息主键
     * @return 结果
     */
    @Override
    public int deleteUserDockerById(Long id)
    {
        return userDockerMapper.deleteUserDockerById(id);
    }
}
