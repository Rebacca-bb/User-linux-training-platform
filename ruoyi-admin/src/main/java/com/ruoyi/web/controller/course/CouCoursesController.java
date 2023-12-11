package com.ruoyi.web.controller.course;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.course.domain.CouCourses;
import com.ruoyi.course.service.ICouCoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 课程Controller
 * 
 * @author Achen
 * @date 2023-07-14
 */
@RestController
@RequestMapping("/course/courses")
public class CouCoursesController extends BaseController
{
    @Autowired
    private ICouCoursesService couCoursesService;

    /**
     * 查询课程列表
     */
    @PreAuthorize("@ss.hasPermi('course:courses:list')")
    @GetMapping("/list")
    public TableDataInfo list(CouCourses couCourses)
    {
        startPage();
        List<CouCourses> list = couCoursesService.selectCouCoursesList(couCourses);
        return getDataTable(list);
    }

    /**
     * 导出课程列表
     */
    @PreAuthorize("@ss.hasPermi('course:courses:export')")
    @Log(title = "课程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CouCourses couCourses)
    {
        List<CouCourses> list = couCoursesService.selectCouCoursesList(couCourses);
        ExcelUtil<CouCourses> util = new ExcelUtil<CouCourses>(CouCourses.class);
        util.exportExcel(response, list, "课程数据");
    }

    /**
     * 获取课程详细信息
     */
    @PreAuthorize("@ss.hasPermi('course:courses:query')")
    @GetMapping(value = "/{courseId}")
    public AjaxResult getInfo(@PathVariable("courseId") Long courseId)
    {
        return success(couCoursesService.selectCouCoursesByCourseId(courseId));
    }

    /**
     * 新增课程
     */
    @PreAuthorize("@ss.hasPermi('course:courses:add')")
    @Log(title = "课程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CouCourses couCourses)
    {
        return toAjax(couCoursesService.insertCouCourses(couCourses));
    }

    /**
     * 修改课程
     */
    @PreAuthorize("@ss.hasPermi('course:courses:edit')")
    @Log(title = "课程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CouCourses couCourses)
    {
        return toAjax(couCoursesService.updateCouCourses(couCourses));
    }

    /**
     * 删除课程
     */
    @PreAuthorize("@ss.hasPermi('course:courses:remove')")
    @Log(title = "课程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{courseIds}")
    public AjaxResult remove(@PathVariable Long[] courseIds)
    {
        return toAjax(couCoursesService.deleteCouCoursesByCourseIds(courseIds));
    }
}
