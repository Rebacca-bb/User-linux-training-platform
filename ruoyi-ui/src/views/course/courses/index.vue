<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="课程名称" prop="courseName">
        <el-input
          v-model="queryParams.courseName"
          placeholder="请输入课程名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程类型" prop="courseTypeid">
        <el-select v-model="queryParams.courseTypeid" placeholder="课程类型" clearable>
          <el-option
            v-for="dict in dict.type.course_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
<!--        <el-input-->
<!--          v-model="queryParams.courseTypeid"-->
<!--          placeholder="请输入课程类型"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="课程状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['course:courses:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['course:courses:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['course:courses:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['course:courses:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="coursesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" width="50" align="center">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="课程名称" align="center" prop="courseName" min-width="80"/>
      <el-table-column label="课程简介" align="center" prop="courseInfo" min-width="180"/>
      <el-table-column label="课程类型" align="center" prop="courseTypeid" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.course_type" :value="scope.row.courseTypeid"/>
        </template>
      </el-table-column>
      <el-table-column label="课程资源" align="center" prop="courseSource" min-width="150"/>
      <el-table-column label="课程状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" min-width="100" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['course:courses:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['course:courses:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-service"
            @click="toVideo(scope.row)"
          >播放视频及实操</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改课程对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="课程简介" prop="courseInfo">
          <el-input v-model="form.courseInfo" placeholder="请输入课程简介" />
        </el-form-item>
        <el-form-item label="课程类型" prop="courseTypeid">
          <el-select v-model="form.courseTypeid" placeholder="课程类型">
            <el-option
              v-for="dict in dict.type.course_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="课程资源" prop="courseSource">
<!--          <el-input v-model="form.courseSource" placeholder="请输入课程资源" />-->
          <el-upload
            ref="upload"
            :limit="1"
            accept=".mp4, .mov"
            :action="upload.url"
            :headers="upload.headers"
            :file-list="upload.fileList"
            :on-progress="handleFileUploadProgress"
            :on-success="handleFileSuccess"
            :auto-upload="false">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <el-button style="margin-left: 10px;" size="small" type="success" :loading="upload.isUploading" @click="submitUpload">上传到服务器</el-button>
            <div slot="tip" class="el-upload__tip">只能上传"mp4, avi,rmvb"文件，且不超过500Mb</div>
          </el-upload>

        </el-form-item>

        <el-form-item label="课程状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCourses, getCourses, delCourses, addCourses, updateCourses } from "@/api/course/courses";
import { getToken } from "@/utils/auth";

export default {
  name: "Courses",
  dicts: ['sys_normal_disable','course_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 课程表格数据
      coursesList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseName: null,
        courseInfo: null,
        courseTypeid: null,
        courseSource: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        courseName: [
          { required: true, message: "课程名称不能为空", trigger: "blur" }
        ],
        courseInfo: [
          { required: true, message: "课程简介不能为空", trigger: "blur" }
        ],
        courseTypeid: [
          { required: true, message: "课程类型不能为空", trigger: "blur" }
        ],
      },
      upload: {
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/common/upload",
        // 上传的文件列表
        fileList: []
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /**跳转到播放页面*/
    toVideo(id){
      this.$router.push({path:'/index'})
    },

    /** 查询课程列表 */
    getList() {
      this.loading = true;
      listCourses(this.queryParams).then(response => {
        this.coursesList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        courseId: null,
        courseName: null,
        courseInfo: null,
        courseTypeid: null,
        courseSource: null,
        status: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.courseId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加课程";
      this.upload.fileList = [];
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const courseId = row.courseId || this.ids
      getCourses(courseId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改课程";
      });
      this.upload.fileList = [{ name: this.form.fileName, url: this.form.courseSource }];
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {

          // 遍历 coursesList 数组，检查 courseName 是否已存在
          let courseExists = false;
          for (let course of this.coursesList) {
            if (course.courseName === this.form.courseName) {
              courseExists = true;
              break;
            }
          }
          // 如果 courseExists 为 true，表示 courseName 已存在
          // if (courseExists) {
          //   this.$modal.msgError("课程名称已存在，请重新输入");
          // }

            if (this.form.courseId != null) {
              updateCourses(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            }
            else if(courseExists){
                this.$modal.msgError("课程名称已存在，请重新输入");
            }
            else {
              addCourses(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }

        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const courseIds = row.courseId || this.ids;
      this.$modal.confirm('是否确认删除课程编号为"' + courseIds + '"的数据项？').then(function() {
        return delCourses(courseIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('course/courses/export', {
        ...this.queryParams
      }, `courses_${new Date().getTime()}.xlsx`)
    },

    // 新加的内容
    // 文件提交处理
    submitUpload() {
      this.$refs.upload.submit();
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.isUploading = false;
      this.form.courseSource = response.url;
      this.msgSuccess(response.msg);

    }

  },
};
</script>

