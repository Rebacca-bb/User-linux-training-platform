<template>

  <div id="container" class="flex-container">
    <div id="main-video-container" class="flex-item">
      <!-- 不变的部分 -->
      <video src="https://300188.cn/upfiles/onepage/202203/1646378068989.mp4" controls class="main-video" muted="muted"></video>
      <h3 class="main-vid-title">欢迎来到映雪实训演练平台！</h3>
      <h3 class="main-vid-text">
        映雪，是一款专为Linux实训而打造的一站式平台。无论您是初学者还是有经验的Linux爱好者，映雪平台都能够为您提供简洁、易用的操作界面和丰富、实用的功能，帮助您轻松掌握Linux实训的核心技能。
        <br/>
        <br/>
      </h3>
    </div>

    <div id="dynamicSection" class="flex-item">
      <!-- 可切换的部分 -->

      <div class="video-list-container"  v-show="currentPage === 'page1'">
        <div class="list" v-for="(video, index) in videoList" :key="index" :class="{ active: index === activeIndex }">
          <video :src="video.src" class="list-video"></video>
          <h3 class="list-title">{{ video.title }}</h3>
<!--          list-info 这一项要有在watch中才能调用，但是要隐藏起来（同时不占用页面空间）-->
          <div class="list-info" 　style="display: none;">{{video.info}}</div>
        </div>
      </div>

      <div class="page2" v-show="currentPage === 'page2'">

        <img src="../assets/images/loading.gif" alt="Loading animation" class="loading-gif" />

        <iframe :src="containerURL"></iframe>

        <el-button
            class="loading-button"
            @click="getContainer"
            v-hasPermi="['course:docker:add']"
            >实操
        </el-button>

      </div>

      <div class="button-container">
        <button @click="showPage('page1')">
          <svg-icon class-name="icon-contents" icon-class="contents"/>
        </button>
        <button @click="showPage('page2')">
          <svg-icon class-name="icon-CLI" icon-class="CLI"/>
        </button>
      </div>

    </div>
  </div>
</template>


<script>
import {listCourses} from "@/api/course/courses";
import {listDocker, getDocker, addDocker} from "@/api/course/docker";
import "./style.css";

export default {
  name: 'VideoComponent',

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
      // docker表的总条数
      total_docker : 0,
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
        courseName: null,
        courseInfo: null,
        courseTypeid: null,
        courseSource: null,
        status: null,
      },
      // 查询参数2-- dockerList的
      queryParams2: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        containerId: null,
        mappingPort: null,
        status: null
      },

      activeIndex: 0,
      currentPage: 'page1',// 当前显示的页面
      // 其他数据...
      videoList: [],
      // 表单参数
      form: {},
      containerURL: "",
      hostname: "192.168.0.18",

    };
  },

  created() {
    this.getList();
    this.getDockerList();
  },

  methods: {
    /** 查询课程列表 */
    getList() {
      this.loading = true;
      listCourses(this.queryParams).then(response => {

        this.coursesList = response.rows;
        this.total = response.total;
        this.loading = false;
        this.videoList = Array.from({length: this.coursesList.length}, () => ({src: '', title: '',info:''}));
        this.mapCourseListToVideoList(this.coursesList, this.videoList);
      });
    },
    /** 得到docker的表格 */
    async getDockerList() {
      try {
        this.loading = true;
        const response = await listDocker(this.queryParams2);
        this.dockerList = response.rows;
        this.total_docker = response.total;
        this.loading = false;
      } catch (error) {
        // 处理错误
      }
    },

    showPage(page) {
      this.currentPage = page; // 切换页面
    },
    /** 映射courseList的部分值到VideoList中 */
    mapCourseListToVideoList(courseList, videoList) {
      let temp=0;
      courseList.forEach((course, index) => {
        //先过滤掉状态为1的课程（被停用的）
        if(course.status==1){
          this.total -= this.total;
          temp += 1;
          return;
        }
        const video = videoList[index-temp];
        if (video) {
          video.src = course.courseSource;
          video.title = course.courseName;
          video.info = course.courseInfo;
        }
      });
      if (temp > 0) {
        // 如果有videoList中有多余的项，就使用 splice() 方法删除它
        videoList.splice(videoList.length - temp, temp);
      }
    },

    /** 获取容器的映射端口
    /*  输入：用户名(直接获取)
        目标：能够返回用户的映射的端口号对应的wssh界面     */
    async getContainer() {
      // 隐藏 GIF 图片
      document.querySelector('.loading-gif').style.display = 'none';

      document.querySelector('.loading-button').style.display = 'none';

      var userName = this.$store.state.user.name;   // 获取当前登陆的用户名

      // 获取当前用户的端口号
      // 使用 find() 方法在 dockerList 数组中查找符合条件的元素
      const item = this.dockerList.find(item => item.userName === userName);

      // 如果找到了符合条件的元素，就返回它的 mappingPort 值
      if (item) {
        const mappingPort = item.mappingPort;
        console.log(mappingPort);
        // 拼接起来子页面中要访问的URL     因为在服务器上用nginx代理了webssh服务，所以有 192.168.0.18:8888 => 192.168.0.18/ssh
        this.containerURL = "http://192.168.0.18/ssh?hostname=" + this.hostname + "&port=" + mappingPort + "&username=root&password=MTIzNDU2" + "&fontcolor=black&bgcolor=%23F0ECE3&cursorcolor=black";
        // this.containerURL = "http://localhost:8888/?hostname=" + this.hostname + "&port=" + mappingPort + "&username=root&password=MTIzNDU2" + "&fontcolor=black&bgcolor=%23F0ECE3&cursorcolor=black";

      } else {
        // 若找不到符合条件的元素，则创建一个新的容器,再返回其mappingPort的值
        this.init_docker_form(userName);
        await addDocker(this.form);
        console.log("test1");

        //更新docker_list表格
        // 等待数据加载完成
        await this.getDockerList();

        const item_new = this.dockerList.find(item_new => item_new.userName === userName);
        if (item_new) {
          const mappingPort = item_new.mappingPort;
          console.log(mappingPort);
          setTimeout(function() {
            // Code to be executed after 1 second
          }, 1000); // 1000 milliseconds = 1 second
          // 拼接起来子页面中要访问的URL
          this.containerURL = "http://192.168.0.18/ssh?hostname=" + this.hostname + "&port=" + mappingPort + "&username=root&password=MTIzNDU2" + "&fontcolor=black&bgcolor=%23F0ECE3&cursorcolor=black";
          // this.containerURL = "http://localhost:8888/?hostname=" + this.hostname + "&port=" + mappingPort + "&username=root&password=MTIzNDU2" + "&fontcolor=black&bgcolor=%23F0ECE3&cursorcolor=black";

        } else {
          console.log("新的mappingport未定义的错误");
        }

      }
    },

    /** 表单初始化（提交的docker表单） */
    init_docker_form(id, userName){
      this.form = {
        id: null,
        userName: userName,
        containerId: null,
        mappingPort: null,
        status: null
      };
    },

  },

  mounted() {
    // 该方法将在 Vue 完成下一次 DOM 更新后被调用
    this.$nextTick(() => {
      const mainVideo = document.querySelector('.main-video');

      mainVideo.addEventListener('timeupdate', () => {
        const progressBar = document.querySelector('.progress-bar');
        if (progressBar) {
          const progress = (mainVideo.currentTime / mainVideo.duration) * 100;
          progressBar.value = progress;
        }
      });

      const progressBar = document.querySelector('.progress-bar');
      if (progressBar) {
        progressBar.addEventListener('input', () => {
          const videoTime = (mainVideo.duration * progressBar.value) / 100;
          mainVideo.currentTime = videoTime;
        });
      }

      // 在组件加载后自动播放视频
      mainVideo.play();
    });
  },

// 添加一个watcher来监听videoList
  watch: {
    videoList: {
      handler() {
        this.$nextTick(() => {
          const videoList = document.querySelectorAll('.video-list-container .list');

          videoList.forEach((vid) => {
            vid.onclick = () => {
              videoList.forEach((remove) => {
                remove.classList.remove('active');
              });
              vid.classList.add('active');
              const src = vid.querySelector('.list-video').src;
              const title = vid.querySelector('.list-title').innerHTML;
              const info =vid.querySelector('.list-info').innerHTML;


              const mainVideo = document.querySelector('.main-video');
              const mainTitle = document.querySelector('.main-vid-title');
              const mainInfo = document.querySelector('.main-vid-text');
              mainVideo.src = src;
              mainVideo.play();
              mainTitle.innerHTML = title;
              mainInfo.innerHTML = info;
            };
          });
        });
      },
      immediate: true, // 设置immediate属性为true，保证即便videoList已经初始化也会执行handler函数
    }
  },
    // 组件的其他选项和方法

}

</script>
