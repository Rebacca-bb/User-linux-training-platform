package com.ruoyi.course.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.course.mapper.CouCoursesMapper;
import com.ruoyi.course.domain.CouCourses;
import com.ruoyi.course.service.ICouCoursesService;

/**
 * 课程Service业务层处理
 * 
 * @author Achen
 * @date 2023-07-14
 */
@Service
public class CouCoursesServiceImpl implements ICouCoursesService 
{
    @Autowired
    private CouCoursesMapper couCoursesMapper;

    /**
     * 查询课程
     * 
     * @param courseId 课程主键
     * @return 课程
     */
    @Override
    public CouCourses selectCouCoursesByCourseId(Long courseId)
    {
        return couCoursesMapper.selectCouCoursesByCourseId(courseId);
    }

    /**
     * 查询课程列表
     * 
     * @param couCourses 课程
     * @return 课程
     */
    @Override
    public List<CouCourses> selectCouCoursesList(CouCourses couCourses)
    {
        return couCoursesMapper.selectCouCoursesList(couCourses);
    }

    /**
     * 新增课程
     * 
     * @param couCourses 课程
     * @return 结果
     */
    @Override
    public int insertCouCourses(CouCourses couCourses)
    {
        couCourses.setCreateTime(DateUtils.getNowDate());
        return couCoursesMapper.insertCouCourses(couCourses);
    }

    /**
     * 修改课程
     * 
     * @param couCourses 课程
     * @return 结果
     */
    @Override
    public int updateCouCourses(CouCourses couCourses)
    {
        couCourses.setUpdateTime(DateUtils.getNowDate());
        return couCoursesMapper.updateCouCourses(couCourses);
    }

    /**
     * 批量删除课程
     * 
     * @param courseIds 需要删除的课程主键
     * @return 结果
     */
    @Override
    public int deleteCouCoursesByCourseIds(Long[] courseIds)
    {
        return couCoursesMapper.deleteCouCoursesByCourseIds(courseIds);
    }

    /**
     * 删除课程信息
     * 
     * @param courseId 课程主键
     * @return 结果
     */
    @Override
    public int deleteCouCoursesByCourseId(Long courseId)
    {
        return couCoursesMapper.deleteCouCoursesByCourseId(courseId);
    }
}
