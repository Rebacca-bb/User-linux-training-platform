package com.ruoyi.web.controller.course;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.course.domain.UserDocker;
import com.ruoyi.course.service.IUserDockerService;
import com.ruoyi.dao.docker.DockerClientUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户-容器-映射端口信息Controller
 *
 * @author Achen
 * @date 2023-07-19
 */
@RestController
@RequestMapping("/course/docker")
public class UserDockerController extends BaseController
{
    @Autowired
    private IUserDockerService userDockerService;

    // 创建DockerClientUtils对象
    DockerClientUtils dockerClientUtils =new DockerClientUtils();
    /** 连接Docker服务器 */
    DockerClient client = dockerClientUtils.connectDocker("tcp://192.168.0.18:2375");

    /** 获取当前用户的用户名 */
    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
    /** 根据用户名来获取当前用户的容器id */
    public String getCurrentUserContainerId() {
        // 1.获取当前用户用户名
        String userName = this.getCurrentUsername();

        // 2.找到该用户对应的容器id
        UserDocker userDocker = new UserDocker();
        userDocker.setUserName(userName);
        List<UserDocker> userDockers = userDockerService.selectUserDockerList(userDocker);
        if (userDockers != null && !userDockers.isEmpty()) {
            return userDockers.get(0).getContainerId();
        } else {
            return null;
        }
    }
    /** 根据用户名来获取当前用户的容器的状态 */
    public String getCurrentUserContainerStatus() {
        // 1.获取当前用户用户名
        String userName = this.getCurrentUsername();

        // 2.找到该用户对应的容器的状态
        UserDocker userDocker = new UserDocker();
        userDocker.setUserName(userName);
        List<UserDocker> userDockers = userDockerService.selectUserDockerList(userDocker);
        if (userDockers != null && !userDockers.isEmpty()) {
            return userDockers.get(0).getStatus();
        } else {
            return null;
        }
    }

    /**
     * 查询用户-容器-映射端口信息列表
     */
    @PreAuthorize("@ss.hasPermi('course:docker:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserDocker userDocker)
    {
        startPage();
        List<UserDocker> list = userDockerService.selectUserDockerList(userDocker);
        return getDataTable(list);
    }

    /**
     * 导出用户-容器-映射端口信息列表
     */
    @PreAuthorize("@ss.hasPermi('course:docker:export')")
    @Log(title = "用户-容器-映射端口信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserDocker userDocker)
    {
        List<UserDocker> list = userDockerService.selectUserDockerList(userDocker);
        ExcelUtil<UserDocker> util = new ExcelUtil<UserDocker>(UserDocker.class);
        util.exportExcel(response, list, "用户-容器-映射端口信息数据");
    }

    /**
     * 获取用户-容器-映射端口信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('course:docker:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userDockerService.selectUserDockerById(id));
    }

    /**
     * 新增用户-容器-映射端口信息
     */
    @PreAuthorize("@ss.hasPermi('course:docker:add')")
    @Log(title = "用户-容器-映射端口信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserDocker userDocker)
    {
        //  给传进来的userDocker加上映射端口和容器id，写入数据库，并打开容器
        // 初始化一个随机端口hostPort  --- （问题：端口冲突问题如何解决？先不管？）
        int hostPort = DockerClientUtils.generateRandomNumber();
        String containerName = this.getCurrentUsername() + "_" + String.valueOf(hostPort);
        //创建容器
        CreateContainerResponse container = dockerClientUtils.createContainers(client,containerName,"openeuler_general:20.03",hostPort);
        //启动容器
        dockerClientUtils.startContainer(client,container.getId());

        userDocker.setMappingPort((long)hostPort);
        userDocker.setContainerId(container.getId());
        userDocker.setStatus("0");
        userDocker.setUserName(this.getCurrentUsername());

        System.out.println(userDocker.toString());


        return toAjax(userDockerService.insertUserDocker(userDocker));
    }

    /**
     * 修改用户-容器-映射端口信息
     */
    @PreAuthorize("@ss.hasPermi('course:docker:edit')")
    @Log(title = "用户-容器-映射端口信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserDocker userDocker)
    {
        // 修改容器的状态 ---“正常 0”或“停用 1”
        // 先查看该用户的容器的原有状态
        String status = this.getCurrentUserContainerStatus();
        String containerId = userDocker.getContainerId();

        if(userDocker.getStatus().equals(status)){
            // 若修改后的状态与原状态一致
            System.out.println("要修改的状态与原状态一致，不进行操作");
        } else if (userDocker.getStatus().equals("1")) {
            // 若修改后的状态为1---“停止容器”
            dockerClientUtils.stopContainer(client,containerId);
        } else {
            // 若修改后的状态为0---“启动容器”
            dockerClientUtils.startContainer(client,containerId);
        }

        return toAjax(userDockerService.updateUserDocker(userDocker));
    }

    /**
     * 删除用户-容器-映射端口信息
     */
    @PreAuthorize("@ss.hasPermi('course:docker:remove')")
    @Log(title = "用户-容器-映射端口信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        // 要求做到同时删除某条信息的同时删除容器
        String containerId = this.getCurrentUserContainerId();
        System.out.println(containerId);
        if(this.getCurrentUserContainerStatus().equals("0")) {
            dockerClientUtils.stopContainer(client, containerId);
        }
        dockerClientUtils.removeContainer(client,containerId);

        return toAjax(userDockerService.deleteUserDockerByIds(ids));
    }
}
