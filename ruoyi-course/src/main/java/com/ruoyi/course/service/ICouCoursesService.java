package com.ruoyi.course.service;

import java.util.List;
import com.ruoyi.course.domain.CouCourses;

/**
 * 课程Service接口
 * 
 * @author Achen
 * @date 2023-07-14
 */
public interface ICouCoursesService 
{
    /**
     * 查询课程
     * 
     * @param courseId 课程主键
     * @return 课程
     */
    public CouCourses selectCouCoursesByCourseId(Long courseId);

    /**
     * 查询课程列表
     * 
     * @param couCourses 课程
     * @return 课程集合
     */
    public List<CouCourses> selectCouCoursesList(CouCourses couCourses);

    /**
     * 新增课程
     * 
     * @param couCourses 课程
     * @return 结果
     */
    public int insertCouCourses(CouCourses couCourses);

    /**
     * 修改课程
     * 
     * @param couCourses 课程
     * @return 结果
     */
    public int updateCouCourses(CouCourses couCourses);

    /**
     * 批量删除课程
     * 
     * @param courseIds 需要删除的课程主键集合
     * @return 结果
     */
    public int deleteCouCoursesByCourseIds(Long[] courseIds);

    /**
     * 删除课程信息
     * 
     * @param courseId 课程主键
     * @return 结果
     */
    public int deleteCouCoursesByCourseId(Long courseId);
}
