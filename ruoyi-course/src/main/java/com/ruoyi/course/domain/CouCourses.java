package com.ruoyi.course.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课程对象 cou_courses
 * 
 * @author Achen
 * @date 2023-07-14
 */
public class CouCourses extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 课程ID */
    private Long courseId;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 课程简介 */
    @Excel(name = "课程简介")
    private String courseInfo;

    /** 课程类型id */
    @Excel(name = "课程类型id")
    private Long courseTypeid;

    /** 课程资源（URL？） */
    @Excel(name = "课程资源", readConverterExp = "U=RL？")
    private String courseSource;

    /** 课程状态（0正常 1停用） */
    @Excel(name = "课程状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }
    public void setCourseName(String courseName) 
    {
        this.courseName = courseName;
    }

    public String getCourseName() 
    {
        return courseName;
    }
    public void setCourseInfo(String courseInfo) 
    {
        this.courseInfo = courseInfo;
    }

    public String getCourseInfo() 
    {
        return courseInfo;
    }
    public void setCourseTypeid(Long courseTypeid) 
    {
        this.courseTypeid = courseTypeid;
    }

    public Long getCourseTypeid() 
    {
        return courseTypeid;
    }
    public void setCourseSource(String courseSource) 
    {
        this.courseSource = courseSource;
    }

    public String getCourseSource() 
    {
        return courseSource;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("courseId", getCourseId())
            .append("courseName", getCourseName())
            .append("courseInfo", getCourseInfo())
            .append("courseTypeid", getCourseTypeid())
            .append("courseSource", getCourseSource())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
