<!-- ocr确认 -->
<template>
    <div>
        <!-- 通用头部 -->
        <div class="currency_top">用户管理>用户列表</div>

        <!-- 用户信息展示-->
        <div class="currency_screen">
          <p class="userMsg"><span class="msgleft">姓名：</span><span class="msgRight">{{username}}</span></p>
          <p class="userMsg"><span class="msgleft">编号：</span><span class="msgRight">{{userId}}</span></p>
          <p class="userMsg"><span class="msgleft">账号：</span><span class="msgRight">{{userAccount}}</span></p>
        </div>

        <!-- 条件筛选区 -->
        <div class="currency_screen">
          <div class="clear currency_screen_div">
            <div class="clear currency_screen_input">
              <label>账号:</label>
              <input onKeypress="javascript:if(event.keyCode == 32)event.returnValue = false;" type="text" placeholder="请输入账号" v-model="searchAccount" />
            </div>
          </div>
          <div class="clear currency_screen_div">
            <div class="clear currency_screen_input">
              <label>姓名:</label>
              <input type="text" placeholder="请输入姓名" v-model="searchName" />
            </div>
          </div>
          <div class="clear currency_screen_div">
            <div class="clear currency_screen_input">
              <label style="margin-right: 15px;width:36px;">状态:</label>
              <el-select v-model="userStatus" placeholder="请选择">
                <el-option
                  v-for="item in statusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </div>
            <el-button type="primary" icon="el-icon-search"  @click="getUserList">查询</el-button>
            <el-button type="primary" @click="resetData">重置</el-button>
            <el-button v-if="isCreateBtn && isRoleList" type="primary" style="float: right;" @click="createBtn"> + 创建用户</el-button>
          </div>
        </div>

        <!-- 列表数据 -->
        <div class="currency_table">
          <el-table :data="dataList" :key="1" style="width: 100%" border size="mini" >
            <el-table-column type="index" width="50" label="序号"></el-table-column>
            <el-table-column prop="account" label="账号" :key="Math.random()"></el-table-column>
            <el-table-column prop="name" label="姓名" :key="Math.random()"></el-table-column>
            <!--<el-table-column prop="phone" label="手机号码" :key="Math.random()"></el-table-column>-->
            <!--<el-table-column prop="email" label="邮箱" :key="Math.random()"></el-table-column>-->
            <el-table-column prop="name" label="角色" :key="Math.random()" width="200">
              <template slot-scope="scope">
                <span v-if="scope.row.bossRoles.length > 0" v-for="item in (scope.row.bossRoles)"> {{item.name}} &nbsp;</span>
                <span v-if="scope.row.bossRoles.length  == 0">无</span>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="状态" :key="Math.random()">
              <template slot-scope="scope">
                <span v-if="scope.row.status == 0" style="color: red">已停用</span>
                <span v-if="scope.row.status == 1">正常</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" :key="Math.random()" width="200">
              <template slot-scope="scope">
                <span v-if="isRoleList && isUpdateRole && scope.row.status ==1" class="tableBtn" @click="oprateRole(scope.row.id)">修改角色</span>
                <span v-if="isResetPwd && scope.row.status ==1" class="tableBtn" @click="resetPwd(scope.row.id)">重置密码</span>
                <span v-if="isDeleteMember && scope.row.status ==1" class="tableBtn" @click="deleteUser(scope.row.id)">禁用</span>
                <span v-if="isRestore && scope.row.status ==0" class="tableBtn" @click="restoreUser(scope.row.id)">启用</span>
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页 -->
          <div v-if="totalPage > 1" class="page">
            <el-pagination background layout="prev, pager, next" :current-page="currentPage" :page-size="pageSize" :total="total"  @current-change="currentChange"></el-pagination>
          </div>
        </div>

        <!--创建用户弹框-->
        <el-dialog
          :close-on-click-modal="false"
          title="创建用户"
          :visible.sync="createDialogVisible"
          width="50%">
          <el-form :model="createForm" ref="ruleForm" label-width="150px">
            <el-form-item label="账号：" prop="account" :rules="[
           {required: true, message: '请输入非中文账号',trigger:'blur'},
            ]">
              <el-input v-model="createForm.account"  maxlength="15" onKeypress="javascript:if(event.keyCode == 32)event.returnValue = false;" onkeyup="value=value.replace(/[\u4E00-\u9FA5]/g,'')" placeholder="请输入账号" clearable></el-input>
            </el-form-item>
            <el-form-item label="姓名：" prop="name" :rules="[
           {required: true, message: '请输入姓名',trigger:'blur'},
            ]">
              <el-input v-model="createForm.name" maxlength="15" onKeypress="javascript:if(event.keyCode == 32)event.returnValue = false;"  placeholder="请输入姓名" clearable></el-input>
            </el-form-item>
            <el-form-item label="邮箱："  prop="email" :rules="[
            {pattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/, message: '邮箱格式不正确',trigger:'blur'}
            ]">
              <el-input v-model="createForm.email" placeholder="请输入邮箱" clearable></el-input>
            </el-form-item>
            <el-form-item label="手机号码："  prop="phone" :rules="[
            {pattern: /^1[3|4|5|7|8][0-9]{9}$/, message: '手机号码格式不正确',trigger:'blur'}
            ]">
              <el-input v-model="createForm.phone" placeholder="请输入手机号码" clearable></el-input>
            </el-form-item>
            <el-form-item label="密码：" prop="pwd" :rules="[
           {required: true, message: '请输入密码',trigger:'blur'},
           {pattern:/^\w{6,16}$/,message:'请输入6-16位字母、数字、下划线组合',trigger:'blur'}
            ]">
              <el-input v-model="createForm.pwd" type="password" placeholder="请输入密码" clearable></el-input>
            </el-form-item>
            <el-form-item label="选择角色：" prop="checkedroles1">
              <span>管理员：</span>
              <el-checkbox-group v-model="checkedroles1">
                <el-checkbox style="width: 150px" v-for="role in hadRolesData1" :label="role" :key="role">{{role}}</el-checkbox>
              </el-checkbox-group>
              <span>非管理员：</span>
              <el-checkbox-group v-model="checkedroles2">
                <el-checkbox style="width: 150px" v-for="role in hadRolesData2" :label="role" :key="role">{{role}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button type="info" @click="createDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="createUser('ruleForm')">确 定</el-button>
          </span>
        </el-dialog>

        <!--选择角色弹框-->
      <el-dialog
        :close-on-click-modal="false"
        title="分配角色"
        :visible.sync="roleDialogVisible"
        width="50%">
        <span style="line-height: 50px">管理员：</span>
        <el-checkbox-group v-model="checkedrolesData1" >
          <el-checkbox style="width: 150px;line-height: 40px" v-for="role in hadRolesData1" :label="role" :key="role">{{role}}</el-checkbox>
        </el-checkbox-group>
        <span style="line-height: 50px">非管理员：</span>
        <el-checkbox-group v-model="checkedrolesData2" >
          <el-checkbox style="width: 150px;line-height: 40px" v-for="role in hadRolesData2" :label="role" :key="role">{{role}}</el-checkbox>
        </el-checkbox-group>
        <span slot="footer" class="dialog-footer">
            <el-button type="info" @click="roleDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="seleteRole">确 定</el-button>
          </span>
      </el-dialog>

      <!--重置密码-->
      <el-dialog title="重置密码" :visible.sync="dialogPass" :close-on-click-modal="false">
        <el-form>
          <el-form-item label="密码:">
            <el-input type="password" v-model="resetPass" placeholder="请输入要重置的密码"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="info" @click="dialogPass = false">取 消</el-button>
          <el-button type="primary" @click="resetPassBtn">确 定</el-button>
        </div>
      </el-dialog>
    </div>
</template>
<script>
export default {
    name:'ocrConfirm',
    data(){
    return{
      isCreateBtn:false,//创建用户按钮
      isUpdateRole:false,//修改角色
      isResetPwd:false,//重置密码
      isDeleteMember:false,//删除用户
      isRestore:false,//启用用户
      isUserList:false,//用户列表
      isRoleList:false,//角色列表
      dialogPass:false,
      passId:'',
      currentPage:1,
      pageSize: 10,
      totalPage:1,
      total:0,
      currentPage1:1,
      pageSize1: 10,
      totalPage1:1,
      total1:0,
      dataList:[],
      roleId:'',
      createDialogVisible:false,
      roleDialogVisible:false,
      createForm:{
        account:'',
        name:'',
        email:'',
        phone:'',
        pwd:''
      },
      resetPass:'',
      searchAccount:'',
      username:'',
      userId:'',
      userAccount:'',
      searchName:'',
      checkedroles:[],
      checkedrolesData:[],
      checkedrolesData1:[],
      checkedrolesData2:[],
      checkedroles1:[],
      checkedroles2:[],
      hadRoles:[],
      hadRolesData:[],
      hadRolesData1:[],
      hadRolesData2:[],
      userStatus:'',
      statusOptions:[{
        value: 0,
        label: '已停用'
      },{
        value: 1,
        label: '正常'
      },{
        value: '',
        label: '全部'
      }],
    }
  },
  mounted(){
    this.username = sessionStorage.getItem("name")
    this.userId = sessionStorage.getItem("userId")
    this.userAccount = sessionStorage.getItem("account")
    this.getApiAccess()
    if(this.isUserList){
      this.getUserList()
    }
    if(this.isRoleList){
      this.getRoleList()
    }
  },
  methods:{
      //获取用户操作按钮权限
      getApiAccess(){
        if(this.$util.getapiAccess('user/list')){//用户列表
          this.isUserList = true
        }
        if(this.$util.getapiAccess('role/list')){//角色列表
          this.isRoleList = true
        }
        if(this.$util.getapiAccess('user/create')){//创建用户
          this.isCreateBtn = true
        }
        if(this.$util.getapiAccess('user/setroles')){//角色设置
          this.isUpdateRole = true
        }
        if(this.$util.getapiAccess('user/resetpwd')){//重置密码
          this.isResetPwd = true
        }
        if(this.$util.getapiAccess('user/delete')){//删除用户
          this.isDeleteMember = true
        }
        if(this.$util.getapiAccess('user/restore')){//启用用户
          this.isRestore = true
        }
      },
    refresh(){
      let that = this
      that.$api.loadpermission().then(obj => {
        if(obj.data.code=="0000"){
          that.$message({
            type: 'success',
            message: '刷新成功！'
          });
        }else{
          that.$alert(obj.data.desc)
        }
      })
    },
      //获取用户列表
    getUserList(){
      let that = this
      let params = {
        account:that.searchAccount,
        name:that.searchName,
        status:that.userStatus,
        page:{
          currentPage:that.currentPage,
          pageSize:that.pageSize
        }
      }
        that.$api.userList(params).then(obj => {
        if(obj.data.code=="0000"){
          that.dataList = obj.data.data.datas
          that.total = obj.data.data.totalRecord
          that.totalPage = obj.data.data.totalPage
        }else{
          that.$alert(obj.data.desc)
        }
      })
    },
    //获取角色列表
    getRoleList(){
      let that = this
      that.hadRoles = []
      let params = {
        isAdmin:'',
        name:'',
        page:{
          currentPage:that.currentPage1,
          pageSize:that.pageSize1
        }
      }
      that.$api.roleList(params).then(obj => {
        if(obj.data.code=="0000"){
          that.hadRoles = obj.data.data
          that.hadRoles.forEach(val =>{
            that.hadRolesData.push(val.name)
            if(val.isAdmin == false){//非管理员
              that.hadRolesData2.push(val.name)
            }else{//管理员
              that.hadRolesData1.push(val.name)
            }
          })
        }else{
          that.$alert(obj.data.desc)
        }
      })
    },
    //点击创建用户
    createBtn(){
      this.createDialogVisible = true
      this.checkedroles1 = []
      this.checkedroles2 = []
      this.createForm = {
        account:'',
        name:'',
        pwd:'',
        email:'',
        phone:''
      }
    },
    //创建用户
    createUser(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let that = this
          let ids=''
          let checkedAll = [...that.checkedroles1,...that.checkedroles2] //拼接数组
          if(checkedAll.length == 0){
            this.$message({
              showClose: true,
              message: '请选择用户角色！',
              type: 'error'
            });
            return false
          }
          that.hadRoles.forEach(val => {
            checkedAll.forEach(val1 =>{
              if(val.name == val1){
                ids += val.id + ','
              }
            })
          })
          ids=ids.substr(0, ids.length - 1);
          let params = {
            account:that.createForm.account,
            name:that.createForm.name,
            email:that.createForm.email,
            phone:that.createForm.phone,
            pwd:that.$md5(that.createForm.pwd),
            roleIds:ids
          }
          that.$api.create(params).then(obj => {
            if(obj.data.code=="0000"){
              that.$message({
                showClose: true,
                message: '创建成功！',
                type: 'success'
              });
              that.createDialogVisible = false
              that.getUserList()
            }else{
              that.$alert(obj.data.desc)
            }
          })
        } else {
          return false;
        }
      });
    },
    //修改角色
    seleteRole(){
      let that = this
      let ids=''
      let allCheckedRole = [...that.checkedrolesData1,...that.checkedrolesData2] //拼接数组
      if(allCheckedRole.length < 1){
        that.$message({
          message: '请分配角色!',
          type: 'error'
        });
        return false
      }
      that.hadRoles.forEach(val => {
        allCheckedRole.forEach(val1 =>{
          if(val.name == val1){
            ids += val.id + ','
          }
        })
      })
      ids=ids.substr(0, ids.length - 1);
      let params = {
        userId:that.roleId,
        roleIds:ids
      }
      that.$api.setroles(params).then(obj => {
        if(obj.data.code=="0000"){
          that.$message({
            showClose: true,
            message: '角色设置成功!',
            type: 'success'
          });
          that.getUserList()
        }else{
          that.$alert(obj.data.desc)
        }
      })
      that.roleDialogVisible = false
    },
    //点击修改角色（获取角色详情）
    oprateRole(id){
      let that = this
      that.roleId = id
      that.checkedrolesData = []
      that.checkedrolesData1 = []
      that.checkedrolesData2 = []
      that.roleDialogVisible = true
      let params = {
        userId: id
      }
      //获取用户角色列表
      that.$api.userRoleList(params).then(obj => {
        if(obj.data.code=="0000"){
          that.checkedroles = obj.data.data
          that.checkedroles.forEach(val => {
            if(val.isAdmin == false){//非管理员
              that.checkedrolesData2.push(val.name)
            }else if(val.isAdmin == true){//管理员
              that.checkedrolesData1.push(val.name)
            }
          })
        }else{
          that.$alert(obj.data.desc)
        }
      })
    },
    //管理员重置密码
    resetPwd(id){
      let that = this
      that.dialogPass = true
      that.passId = id
    },
    resetPassBtn(){
      let that = this
      if(that.resetPass == ''){
        that.$message({
          type: 'error',
          message: '密码不能为空，请输入密码！'
        });
        return false
      }
      let params = {
        id:that.passId,
        pwd:that.$md5(that.resetPass)
      }

      that.$confirm('确认重置密码吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        that.$api.resetpwd(params).then(obj => {
          if(obj.data.code=="0000"){
            that.$message({
              type: 'success',
              message: '密码已重置！'
            });
            that.dialogPass = false
            that.getUserList()
          }else{
            that.$alert(obj.data.desc)
          }
        })
      }).catch(() => {
        console.log('取消重置')
      });
    },
    //禁用用户
    deleteUser(id){
      let that = this
      let params = {
        id:id
      }
      that.$confirm('确认禁用吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        that.$api.deleteUser(params).then(obj => {
          if(obj.data.code=="0000"){
            that.$message({
              type: 'success',
              message: '禁用成功！'
            });
            that.getUserList()
          }else{
            that.$alert(obj.data.desc)
          }
        })
      }).catch(() => {
        console.log('取消')
      });
    },
    //启用用户
    restoreUser(id){
      let that = this
      let params = {
        id:id
      }
      that.$confirm('确认启用吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        that.$api.restore(params).then(obj => {
          if(obj.data.code=="0000"){
            that.$message({
              type: 'success',
              message: '启用成功！'
            });
            that.getUserList()
          }else{
            that.$alert(obj.data.desc)
          }
        })
      }).catch(() => {
        console.log('取消')
      });
    },
    //重置
    resetData(){
      this.searchAccount = ''
      this.searchName = ''
    },
    currentChange(val) {
      this.currentPage = Number(val)
      this.getUserList()
    }
  }
}
</script>
<style lang="scss">
  .el-checkbox+.el-checkbox {
    margin-left:0;
  }
</style>

