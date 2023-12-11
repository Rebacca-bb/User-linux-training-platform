<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="容器id" prop="containerId">
        <el-input
          v-model="queryParams.containerId"
          placeholder="请输入容器id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="映射端口号" prop="mappingPort">
        <el-input
          v-model="queryParams.mappingPort"
          placeholder="请输入映射端口号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="容器状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="容器状态" clearable>
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
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['course:docker:add']"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['course:docker:edit']"
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
          v-hasPermi="['course:docker:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['course:docker:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dockerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="数据库id" align="center" prop="id" width="100"/>
      <el-table-column label="用户名称" align="center" prop="userName" />
      <el-table-column label="容器id" align="center" prop="containerId" />
      <el-table-column label="映射端口号" align="center" prop="mappingPort" width="100" />
      <el-table-column label="容器状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['course:docker:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['course:docker:remove']"
          >删除</el-button>
<!--          <el-progress v-show="progressBar" type="circle" :percentage="100"></el-progress>-->
          <div class="overlay" v-show="progressBar" style="z-index: 9999;"></div>
          <div class="progress-container" v-show="progressBar" style="z-index: 10000;">
            <el-progress class="my-progress" :stroke-width="10" :percentage="100" type="circle" :color="progressBarColor" :text-inside="true"></el-progress>
            <div class="progress-text" v-if="progressBar" style="font-weight: bold; font-size: 20px; color: #409EFF;">修改中...</div>
          </div>

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

    <!-- 添加或修改用户-容器-映射端口信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body style="z-index: 1100;">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="form.userName"  readonly/>
        </el-form-item>
        <el-form-item label="容器id" prop="containerId">
<!--          <span style="display: block; text-align: center; font-size: 18px;">{{form.containerId}}</span>-->
          <el-input v-model="form.containerId"  readonly/>
        </el-form-item>
        <el-form-item label="映射端口号" prop="mappingPort">
<!--          <span style="display: block; text-align: center; font-size: 22px;">{{form.mappingPort}}</span>-->
          <el-input v-model="form.mappingPort"  readonly/>
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import { listDocker, getDocker, delDocker, addDocker, updateDocker } from "@/api/course/docker";

export default {
  name: "Docker",
  dicts: ['sys_normal_disable'],
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
      // 用户-容器-映射端口信息表格数据
      dockerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        containerId: null,
        mappingPort: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      progressBar: false,  // Progress bar visibility flag
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户-容器-映射端口信息列表 */
    getList() {
      this.loading = true;
      listDocker(this.queryParams).then(response => {
        this.dockerList = response.rows;
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
        id: null,
        userName: null,
        containerId: null,
        mappingPort: null,
        status: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户-容器-映射端口信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDocker(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            this.showProgressBar(); // 显示进度条
            updateDocker(this.form).then(response => {
              this.hideProgressBar(); // 隐藏进度条
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDocker(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
      /** 删除按钮操作（异步执行） */
      async handleDelete(row) {
        const ids = row.id || this.ids;
        try {
          await this.$modal.confirm('是否确认删除用户-容器-映射端口信息编号为"' + ids + '"的数据项？');

          // 显示进度条
          this.progressBar = true;
          this.progressBarColor = '#67C23A'; // 设置进度条颜色为绿色

          await delDocker(ids, this);

          // 隐藏进度条
          this.progressBar = false;
          this.progressBarColor = '#409EFF'; // 设置进度条颜色为蓝色

          this.getList();
          this.$modal.msgSuccess("删除成功");
        } catch (error) {
          // 隐藏进度条
          this.progressBar = false;
          this.progressBarColor = '#409EFF'; // 设置进度条颜色为蓝色

          // 处理错误
        }
      },

    /** 导出按钮操作 */
    handleExport() {
      this.download('course/docker/export', {
        ...this.queryParams
      }, `docker_${new Date().getTime()}.xlsx`)
    },

    showProgressBar() {
      this.progressBar = true;
    },
    hideProgressBar() {
      this.progressBar = false;
    }


  }
};
</script>

<style>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
}

.progress-container {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}

.my-progress {
  width: 150px;
  height: 150px;
  border: none; /* 去掉进度条的白圈 */
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.my-progress .el-progress__text {
  display: none; /* 隐藏默认的进度文本 */
}

.my-progress .el-icon-circle-check {
  font-size: 40px;
  color: #67C23A;
  margin-bottom: 10px;
}
</style>
