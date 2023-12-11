<template>

  <body>
  <!--背景-->
  <div class="login">
    <!--登录框-->
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <!--标题-->
      <p class="title">映雪实训演练平台</p>
      <!--用户名-->
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          auto-complete="off"
          placeholder="账号"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>

      <!--密码-->
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>

      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" class="custom-checkbox">记住密码</el-checkbox>

      <!--登录按钮-->
      <el-form-item>
        <el-button
          :loading="loading"
          @click.native.prevent="handleLogin"
          class="button"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>

  </div>
  </body>
</template>



<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: "Login",
  data() {
    return {
      codeUrl: "",
      loginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: true,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.getCode();
    this.getCookie();
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.loginForm.uuid = res.uuid;
        }
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{});
          }).catch(() => {
            this.loading = false;
            if (this.captchaEnabled) {
              this.getCode();
            }
          });
        }
      });
    }
  }
};
</script>




<style>
*{
  margin: 0;
  padding: 0;
}
/*更改默认值*/
body{
  width: 100%;
  height: 100%;

  /*修改后 不是预期效果 但是效果感觉更好*/
  /*background: url("../assets/images/back1.jpg") 100% 100% no-repeat;*/
  /*background-size: cover;*/
}
/*添加背景图片*/
.login{
  width: 100%;
  height: 100%;
  /*修改前*/
  /*background: url("../assets/images/back1.jpg") 100% 100% no-repeat;*/
  /*background-size: cover;*/
}
/*让输入框居中*/
.login-form{
  width: 700px;
  height: 400px;
  background: rgba(0,0,0,0.3);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin: auto ;
  /*添加边框圆角*/
  border-radius: 10px;

  /*添加边距挤下去*/
  padding: 50px 0;
  box-sizing: border-box;
  /*添加阴影*/
  box-shadow: 0 0 5px 5px rgba(0,0,0,0.4);

}
/*改变盒子下方的输入框位置*/
.login-form > div{
  width: 350px;
  height: 50px;
  margin: 40px auto;
  /*改变字体颜色*/
  color: white;
}

/*改变标题居中, 并且改颜色*/
.login-form > p{
  text-align: center;
  color: white;
  font-size: 25px;
}

/*改变输入框文字*/
.login-form > div span{
  display: inline-block;
  cursor: pointer;
  font-size: 20px;
}

/*改变输入框样式*/
.login-form > div input{
  width: 100%;
  height: 30px;
  /*透明色*/
  background: transparent;
  /*清除默认边框*/
  border: none;
  /*添加底部边框*/
  border-bottom: 1px solid white;
  /*清除蓝色点击框*/
  outline: none ;
  /*改变文字颜色*/
  color: white;
}
/*改变点击登录按钮样式*/
.login-form .button{
  width: 200px;
  height: 50px;
  /*添加圆角*/
  border-radius: 50px;
  /*设置背景颜色  渐变*/
  background-image: linear-gradient(to right , #15a805, #365fdc);
  text-align: center;
  padding-top: 10px;
  box-sizing: border-box;
  font-size: 20px;
  cursor: pointer;
  color: white;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

/*给登录按钮添加 鼠标放上效果 */
.login-form.button:hover{
  box-shadow: 0 0 10px rgba(0,0,0,0.5) inset;
}

/*给输入框提醒文字改变颜色*/
/*适配谷歌*/
input::-webkit-input-placeholder{
  color: #BDCADA;
}

svg{
  vertical-align: middle;
}

.login-code{
  margin-left: 250px; /* 将元素向右移动 250 像素 */
  margin-top: -60px; /* 将元素向上移动 60 像素 */
}

.custom-checkbox {
  font-size: 16px; /* 设置为 16 像素 */
  top: -24px; /* 将组件向上移动 24 像素 */
  left: 183px; /* 将元素向右移动 183 像素 */
}

</style>
