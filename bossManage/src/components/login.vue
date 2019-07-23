<template>
  <div class="student-wrap-app">
    <div class="login_box" id="login_box">
      <div class="login_bj">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" >
          <el-form-item prop="account">
            <el-input v-model="ruleForm.account" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item prop="pwd">
            <el-input v-model="ruleForm.pwd" type="password" placeholder="请输入密码" @keyup.enter.native="onSubmit('ruleForm')"></el-input>
          </el-form-item>
          <el-form-item prop="captcha" v-if="isVerification">
            <div class="verification clear">
              <el-input type="text" placeholder="请输入验证码" v-model="captcha" @keyup.enter.native="onSubmit('ruleForm')"></el-input>
              <img v-bind:src="img" alt="" @click="authCaptcha">
            </div>
          </el-form-item>
          <el-form-item>
            <el-button class="login_but" @click="onSubmit('ruleForm')">立即登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
  import store from './../vuex/store'
export default {
  name:'login',
  store,
  data(){
    return{
      captchaToken:"",
      isVerification:false,
      img:"",
      captcha:"",
      ruleForm:{
        account:"",
        pwd:""
      },
      rules: {
        account: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        pwd: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      }
    }
  },
  mounted() {
    this.authCaptcha()
  },
  methods: {
    /**
     * 用户登录
     */
    onSubmit(formName){
      let that=this
      that.$refs[formName].validate((valid) => {
        if (valid) {
          let params = {
            account:that.ruleForm.account,
            pwd:that.$md5(that.ruleForm.pwd),
            captchaToken:that.captchaToken,
            captcha:that.captcha
          }
          that.$api.login(params).then(obj => {
            if (obj.data.code == '0000') {
              //放入token值
              sessionStorage.setItem("TOKEN", obj.data.data.token);
              sessionStorage.setItem("buttonList",obj.data.data.buttonList);
              sessionStorage.setItem("menuList", JSON.stringify(obj.data.data.menuList));
              sessionStorage.setItem("name", obj.data.data.name);
              sessionStorage.setItem("userId", obj.data.data.id);
              sessionStorage.setItem("account", obj.data.data.account);
              sessionStorage.setItem("currentIndex1", "0");
              sessionStorage.setItem("currentIndex2", "0");
              if(!obj.data.data.menuList){
                that.$router.push({path: "/blank"});
              }else{
                that.$router.push({path: "/"+obj.data.data.menuList[0].childBossPermission[0].url});
              }
            } else {
              if(obj.data.code=="1005"||obj.data.code=="1004"){//验证码已过期或不正确
                that.$alert(obj.data.desc, {
                  callback: action => {
                    that.authCaptcha()
                  }
                })
              }else{
                alert(obj.data.desc)
              }
            }
          })
        } else {
          return false;
        }
      });
    },

    /**
     * 获取图片验证码
     */
    authCaptcha:function(){
      let that=this
      that.$api.captcha().then(obj => {
          if(obj.data.code=="0000"){
            that.isVerification=true
            that.img ="data:image/png;base64,"+obj.data.data.img
            that.captchaToken = obj.data.data.captchaToken
          }else if(obj.data.code=="1051"){//不需要验证码
            that.isVerification=false
          }else{
            alert(obj.data.desc)
          }
      })
    }
  }
}
</script>
