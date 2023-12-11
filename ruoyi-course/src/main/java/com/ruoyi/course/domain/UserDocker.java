package com.ruoyi.course.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户-容器-映射端口信息对象 user_docker
 * 
 * @author Achen
 * @date 2023-07-19
 */
public class UserDocker extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表格中排序的id */
    private Long id;

    /** 用户名称 */
    @Excel(name = "用户名称")
    private String userName;

    /** 容器id */
    @Excel(name = "容器id")
    private String containerId;

    /** 映射端口号 */
    @Excel(name = "映射端口号")
    private Long mappingPort;

    /** 容器状态（0正常 1停用） */
    @Excel(name = "容器状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setContainerId(String containerId) 
    {
        this.containerId = containerId;
    }

    public String getContainerId() 
    {
        return containerId;
    }
    public void setMappingPort(Long mappingPort) 
    {
        this.mappingPort = mappingPort;
    }

    public Long getMappingPort() 
    {
        return mappingPort;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userName", getUserName())
            .append("containerId", getContainerId())
            .append("mappingPort", getMappingPort())
            .append("status", getStatus())
            .toString();
    }
}
