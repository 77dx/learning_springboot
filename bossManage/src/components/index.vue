<template>
  <div class="">
      <!-- 头部 -->
      <div class="menu_top clear">
        <div class="welcome">欢迎使用风林火山后台管理系统</div>
        <div class="clear menu_top_cz">
          <div class="logout" @click="logout"><span></span><label>退出</label></div>
          <div class="user"><span></span><label>{{name}}</label></div>
          <div class="updatepwd" @click="pwdShow"><span></span><label>修改密码</label></div>
        </div>
      </div>

      <!-- 主题 -->
      <div class="menu_cont" id="box">
        <!-- 左侧菜单 -->
        <div :class="menuClass" id="menuLeft">
          <span class="logo"></span>
          <el-menu
            :default-openeds="openeds"
            class="el-menu-vertical-demo"
            :default-active="witchTab"
            background-color="#334452"
            text-color="#fff"
            active-text-color="#ffd04b"
            >
            <el-submenu v-for="(item,index) in menuList" :index="String(index)" :key="item.id">
              <template slot="title">
                <span>{{item.name}}</span>
              </template>
              <el-menu-item-group v-for="(items,index2) in item.childBossPermission" :key="items.id">
                <router-link :to="items.url"><el-menu-item :index='index+"-"+index2' @click="reds(index,index2)">{{items.name}}</el-menu-item></router-link>
              </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </div>

        <!-- 右侧内容 -->
        <div class="menu_cont_right" style="background: #F7F7F7">
          <span class="operation" @click="retractClick"></span>
          <router-view></router-view>
        </div>
      </div>

      <!-- 修改密码 -->
      <div v-if="change">
        <div class="shade"></div>
        <div class="change_password">
          <i @click="pwdHide"></i>
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" >
            <el-form-item prop="oriPwd">
              <el-input v-model="ruleForm.oriPwd" type="password" placeholder="请输入旧密码"></el-input>
            </el-form-item>
            <el-form-item prop="pwd">
              <el-input v-model="ruleForm.pwd" type="password" placeholder="请输入新密码"></el-input>
            </el-form-item>
            <el-form-item prop="confirmPwd">
              <el-input v-model="ruleForm.confirmPwd" type="password" placeholder="请重新输入新密码" @keyup.enter.native="resetPwd('ruleForm')"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="resetPwd('ruleForm')">确定</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

  </div>
</template>
<script>
export default {
  name:'index',
  data(){
    let validatePass1 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'));
      }else if (value.length< 6) {
        callback(new Error('密码长度至少在6位以上!'));
      } else if (/^[0-9]+$/.test(value)) {
        callback(new Error('新密码不能使用纯数字!'));
      } else {
        callback();
      }
    };
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请重新输入新密码'));
      } else if (value !== this.ruleForm.pwd) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return{
      openeds: ['0'],
      witchTab:"0-0",
      menuList:JSON.parse(sessionStorage.getItem('menuList')),//菜单
      name:sessionStorage.getItem('name'),//名称
      change:false,
      isWidth:'width:1024px',
      isRetract:true,//左侧菜单是否隐藏
      menuLeft:'',//左侧动画名
      menuClass:'menu_cont_left ',//左侧菜单class
      changeActive:0,
      ruleForm:{
        pwd:"",//新密码
        oriPwd:"",//旧密码
        confirmPwd:""//确认密码
      },
      rules: {
        oriPwd: [
          { required: true, message: '请输入旧密码', trigger: 'blur' },
        ],
        pwd: [
          { validator: validatePass1, trigger: 'blur' },
        ],
        confirmPwd: [
          { validator: validatePass2, trigger: 'blur' },
        ]
      }
    }
  },
  mounted(){
    if (sessionStorage.getItem("currentIndex1") && sessionStorage.getItem("currentIndex2")) {
      this.witchTab = sessionStorage.getItem("currentIndex1") + "-" + sessionStorage.getItem("currentIndex2")
      console.log(this.witchTab)
    } else {
      this.changeActive = "0-0";
    }
  },
  methods:{
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    reds: function(index, index2) {
      sessionStorage.setItem("currentIndex1", index);
      sessionStorage.setItem("currentIndex2", index2);
    },
    /**
     * 菜单收起展开
     */
    retractClick:function(){
      if(this.isRetract){
        this.isRetract=false
        this.isWidth='width:844px'
        this.menuLeft='menu_hide'
        this.menuClass='menu_cont_left '
        this.menuClass=this.menuClass+this.menuLeft
        console.log("当前class："+this.menuClass)
      }else{
        this.isRetract=true
        this.isWidth='width:1024px'
        this.menuLeft='menu_show'
        this.menuClass='menu_cont_left '
        this.menuClass=this.menuClass+this.menuLeft
        console.log("当前class："+this.menuClass)
      }
    },
    /**
     * 修改密码
     */
    resetPwd(formName){
      let that=this
      that.$refs[formName].validate((valid) => {
        if (valid) {
          console.log("验证通过")
          let params = {
            oriPwd:that.$md5(that.ruleForm.oriPwd),
            pwd:that.$md5(that.ruleForm.pwd),
            confirmPwd:that.$md5(that.ruleForm.confirmPwd)
          }
            that.$api.updatepwd(params).then(obj => {
            if(obj.data.code=="0000"){
              sessionStorage.removeItem('TOKEN')
              that.$message({
                message: '修改成功！',
                type: 'success'
              });
              that.$router.push('/')
              window.location.reload()
            }else{
              that.$alert(obj.data.desc)
            }
          })
        } else {
          return false;
        }
      });
    },
    /**
     * 登出
     */
    logout:function(){
      let that = this
      that.$confirm('确认退出吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
      }).then(() => {
        that.$api.logout().then(obj => {
          if(obj.data.code=="0000"){
            sessionStorage.removeItem('TOKEN')
              that.$message({
                type: 'success',
                message: '退出成功！'
              });
              that.$router.push('/');
              window.location.reload();
          }else{
            that.$alert(obj.data.desc)
          }
        })
      }).catch(() => {
          console.log('取消退出')
      });
    },
    pwdShow:function(){
      this.change=true
    },
    pwdHide:function(){
      this.change=false
    }
  }
}
</script>



