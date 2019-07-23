<!-- ocr确认 -->
<template>
    <div>
        <!-- 通用头部 -->
        <div class="currency_top">用户管理>角色列表</div>

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
              <label style="width: 75px">角色名:</label>
              <input type="text" placeholder="请输入角色名" v-model="searchName" />
            </div>
          </div>
          <div class="clear currency_screen_div">
            <div class="clear currency_screen_input">
              <label style="margin-right: 15px;width:80px;">是否管理员:</label>
              <el-select v-model="isAdmin" placeholder="请选择">
                <el-option
                  v-for="item in isAdminOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </div>
            <el-button type="primary" icon="el-icon-search"  @click="getRoleList">查询</el-button>
            <el-button type="primary" @click="resetData">重置</el-button>
            <el-button v-if="createRoleBtn && isPermissionList" type="primary" style="float: right;" @click="addRoleBtn"> + 添加角色</el-button>
          </div>
        </div>

        <!-- 列表数据 -->
        <div class="currency_table">
          <el-table :data="dataList" style="width: 100%" border size="mini" >
            <el-table-column type="index" width="50" label="序号"></el-table-column>
            <el-table-column prop="name" label="角色名" :key="Math.random()"></el-table-column>
            <el-table-column prop="isAdmin" label="是否管理员" :key="Math.random()">
              <template slot-scope="scope">{{ scope.row.isAdmin ? '是' : '否'}}</template>
            </el-table-column>
            <el-table-column label="操作" :key="Math.random()">
              <template slot-scope="scope">
                <span v-if="isPermissionList && updateRoleBtn && scope.row.isAdmin == false" class="tableBtn" @click="oprateRole(scope.row)">修改权限</span>
                <span v-if="deleteRoleBtn" class="tableBtn" @click="deleteRole(scope.row.id)">删除</span>
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页 -->
          <div v-if="totalPage > 1" class="page">
            <el-pagination background layout="prev, pager, next" :current-page="currentPage" :page-size="pageSize" :total="total"  @current-change="currentChange"></el-pagination>
          </div>
        </div>

        <!--添加角色弹框-->
        <el-dialog
          :close-on-click-modal="false"
          title="新增角色"
          :visible.sync="createDialogVisible"
          width="50%">
          <el-form ref="form" :model="addform" label-width="150px" :rules="rules">
            <el-form-item label="角色名:" prop="name">
              <el-input onKeypress="javascript:if(event.keyCode == 32)event.returnValue = false;" maxlength="20" v-model="addform.name" placeholder="请输入角色名称" clearable></el-input>
            </el-form-item>
            <el-form-item label="编码:" prop="code">
              <el-input v-model="addform.code" placeholder="请输入数字或字母编码" onKeypress="javascript:if(event.keyCode == 32)event.returnValue = false;" onkeyup="value=value.replace(/[\u4E00-\u9FA5]/g,'')" clearable></el-input>
            </el-form-item>
            <el-form-item label="排序号:" prop="sort">
              <el-input v-model="addform.sort" placeholder="请输入纯数字排序号" onKeypress="javascript:if(event.keyCode == 32)event.returnValue = false;" clearable onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');"></el-input>
            </el-form-item>
            <el-form-item label="是否管理员:">
              <el-radio v-model="addform.isAdmin" label="true">是</el-radio>
              <el-radio v-model="addform.isAdmin" label="false">否</el-radio>
            </el-form-item>
            <el-form-item label="选择权限:" v-if="addform.isAdmin == 'false'">
              <el-tree
                :props="props"
                :data="hadAccessData"
                show-checkbox
                ref="tree"
                node-key="id"
                default-expand-all
                :render-content="renderContent"
                :check-on-click-node="true"
                :check-strictly="true"
                :expand-on-click-node="false"
                @node-click="nodeClick"
                @check="handleCheckChange"
              >
              </el-tree>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
              <el-button type="info" @click="createDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="addRole('form')">确 定</el-button>
            </span>
        </el-dialog>

      <!--修改角色权限弹框-->
      <el-dialog
        :close-on-click-modal="false"
        title="修改权限"
        :visible.sync="updateDialogVisible"
        width="50%">
        <el-form ref="form2" label-width="150px">
          <el-form-item label="选择权限:">
            <el-tree v-if="checkeAccess1.length > 0"
              :props="props"
              :data="hadAccessData"
              show-checkbox
              node-key="id"
              ref="tree2"
              default-expand-all
              :render-content="renderContent"
              :check-on-click-node="true"
              :check-strictly="true"
              :default-checked-keys="checkeAccess1"
              @node-click="nodeClick"
              :expand-on-click-node="false"
              @check="handleCheckChange2"
            >
            </el-tree>
            <el-tree v-if="checkeAccess1.length  == 0"
               :props="props"
               :data="hadAccessData"
               show-checkbox
               node-key="id"
               ref="tree2"
               :render-content="renderContent"
               :check-on-click-node="true"
               :check-strictly="true"
               default-expand-all
               :expand-on-click-node="false"
               @node-click="nodeClick"
               @check="handleCheckChange2"
            >
            </el-tree>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
              <el-button type="info" @click="updateDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="updatePro">确 定</el-button>
            </span>
      </el-dialog>
    </div>
</template>
<script>
export default {
    name:'ocrConfirm',
    data(){
    return{
      createRoleBtn:false,//新增角色
      updateRoleBtn:false,//修改角色权限
      deleteRoleBtn:false,//删除角色
      isRoleList:false,//角色列表
      isPermissionList:false,//权限列表
      isRelated:false,
      username:'',
      userId:'',
      userAccount:'',
      roleList:[],
      searchName:'',
      isAdmin:'',
      createDialogVisible:false,
      updateDialogVisible:false,
      currentPage:1,
      pageSize: 10,
      totalPage:1,
      total:0,
      dataList:[],
      checkeAccess:[],
      checkeAccess1:[],
      checkeAccess2:[],
      changeFlag:0,//是否更改权限
      checkedId:[],
      checkedParentId:[],
      addform:{
        name:'',
        code:'',
        sort:'',
        isAdmin:'false'
      },
      props: {
        label: 'name',
        children: 'childBossPermission'
      },
      updateId:'',
      hadAccess:[],
      hadAccessData:[],
      isAdminOptions:[{
        value: true,
        label: '是'
      },{
        value: false,
        label: '否'
      },{
        value: '',
        label: '全部'
      }],
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请请输入唯一编码', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入纯数字排序号', trigger: 'blur' }
        ]
      }
    }
  },
  mounted(){
    this.username = sessionStorage.getItem("name")
    this.userId = sessionStorage.getItem("userId")
    this.userAccount = sessionStorage.getItem("account")
    this.getApiAccess()
    if(this.isRoleList){
      this.getRoleList()
    }
    if(this.isPermissionList){
      this.getAccessList()
    }
    // 底层树结构横排显示
    let _levelname = document.getElementsByClassName("levelname")  //levelname是上面的最底层节点的名字，查询，新增。。
    setTimeout(function(){   //等到树都加载完了再去执行的这里
      for(var i = 0; i <_levelname.length; i++){
        console.log(_levelname)
        _levelname[i].parentNode.style.cssFloat = 'left'    //最底层的节点，包括多选框和名字都让他左浮动
        _levelname[i].parentNode.style.styleFloat = 'left'
      }
    },1000)
  },
  methods: {
    //获取用户操作按钮权限
    getApiAccess(){
      if(this.$util.getapiAccess('role/list')){//角色列表
        this.isRoleList = true
      }
      if(this.$util.getapiAccess('permission/list')){//权限列表
        this.isPermissionList = true
      }
      if(this.$util.getapiAccess('role/save')){//创建角色
        this.createRoleBtn = true
      }
      if(this.$util.getapiAccess('permission/setpermission')){//修改权限
        this.updateRoleBtn = true
      }
      if(this.$util.getapiAccess('role/delete')){//删除角色
        this.deleteRoleBtn = true
      }
    },
    //选中子节点或孙子节点，必须选中父节点及祖父节点，取消选中子节点或孙子节点，父节点及祖父节点可以不取消选中；
    nodeClick(data, node){
      console.log("node",node)
      this.childNodesChange(node);
      this.parentNodesChange(node);
    },
    childNodesChange(node){
      let that = this
      let len = node.childNodes.length;
      for(let i = 0; i < len; i++){
        node.childNodes[i].checked = false;
        that.childNodesChange(node.childNodes[i]);
      }
    },
    parentNodesChange(node){
      // console.log(node)
      let that = this
      if(node.parent){
        for(let key in node){
          if(key == "parent"){
            node[key].checked = true;
            that.parentNodesChange(node[key]);
          }
        }
      }
    },
    // 内容区渲染后执行 判断底层 赋 class（底层树横排显示）
    renderContent(h, { node, data, store }) {
      let classname = ''
      if (node.childNodes.length === 0) {
        classname = 'levelname'
      }else{
        classname = 'fatherlevelname'
      }
      return (
        <span class={classname}>
        <span>{node.label}</span>
        </span>);
    },
    handleCheckChange(nodeObj, treeObj) {
      this.changeFlag = 1
      console.log("nodeObj",nodeObj)
      this.disabled = false
      this.checkeAccess = treeObj.checkedNodes
      console.log(this.checkeAccess)
    },
    handleCheckChange2(nodeObj, treeObj) {
      this.changeFlag = 1
      this.checkeAccess2 = treeObj.checkedNodes
      this.checkedId = treeObj.checkedKeys //选中节点id
      this.checkedParentId = treeObj.halfCheckedKeys //选中节点的父节点id
    },
      //获取角色列表（有分页）
    getRoleList(){
      let that = this
      let params = {
        isAdmin: that.isAdmin,
        name: that.searchName,
        page: {
          currentPage: that.currentPage,
          pageSize: that.pageSize
        }
      }
      that.$api.rolePageList(params).then(obj => {
        if(obj.data.code=="0000"){
          that.dataList = obj.data.data.datas
          that.total = obj.data.data.totalRecord
          that.totalPage = obj.data.data.totalPage
        }else{
          that.$alert(obj.data.desc)
        }
      })
    },
    //获取权限列表
    getAccessList(){
      let that = this
      that.$api.permissionList().then(obj => {
        if(obj.data.code=="0000"){
          that.hadAccessData = obj.data.data
          console.log(that.hadAccessData)
        }else{
          that.$alert(obj.data.desc)
        }
      })
    },
    //重置
    resetData(){
      this.searchName = ''
      this.isAdmin = ''
    },
    //分配权限（获取角色权限列表）
    oprateRole(data){
      let that = this
      that.updateId = data.id
      that.checkeAccess1 = []
      // 重置选中项为空
      if (that.$refs.tree2) {
        that.$refs.tree2.setCheckedKeys([]);
      }
      this.changeFlag = 0
      that.updateDialogVisible = true
      let params = {
        roleId: data.id
      }
      that.$api.getAuthorizationData(params).then(obj => {
        if(obj.data.code=="0000"){
          obj.data.data.forEach(val => {
            that.checkeAccess1.push(val.id + '')
          })
          console.log(that.checkeAccess1)
        }else{
          that.$alert(obj.data.desc)
        }
      })
    },
    //角色赋权
    updatePro(){
      let that = this
      let ids = ''
      let idsArr = []
      if(that.changeFlag == 1){
        that.checkedId.forEach(val => {
          idsArr.push(val)
        })
        that.checkedParentId.forEach(val => {
          idsArr.push(val)
        })
      }else if(that.changeFlag == 0){
        that.checkeAccess1.forEach(val => {
          idsArr.push(val)
        })
      }
      let tt  = [...new Set(idsArr)] // 数组去重
      ids=tt.join(',')
      let params = {
        roleId: that.updateId,
        permissions: ids
      }
      that.$api.setpermission(params).then(obj => {
        if(obj.data.code=="0000"){
          that.$message({
            type: 'success',
            message: '赋权成功！'
          });
          that.updateDialogVisible = false
        }else{
          that.$alert(obj.data.desc)
        }
      })
    },
    //删除角色
    deleteRole(id){
      let that = this
      let params = {
        id:id
      }
      // 查询角色是否关联用户
      that.$api.relation(params).then(obj => {
        if(obj.data.code=="0000"){
          that.isRelated = obj.data.data.isRelated
          let message = ''
          if(that.isRelated == false){//无角色关联
            message = "确认删除吗?"
          }else if(that.isRelated == true){//有角色关联
            message = "此角色有用户关联，确认删除？"
          }
          // 删除角色
          that.$confirm(message, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            that.$api.roleDelete(params).then(obj => {
              if(obj.data.code=="0000"){
                that.$message({
                  type: 'success',
                  message: '删除成功！'
                });
                that.getRoleList()
              }else{
                that.$alert(obj.data.desc)
              }
            })
          }).catch(() => {
            console.log('取消')
          });
        }else{
          that.$alert(obj.data.desc)
        }
      })
    },
    currentChange(value){
      this.currentPage = value
      this.getRoleList()
    },
    //点击新建角色
    addRoleBtn(){
      let that = this
      that.createDialogVisible = true
      that.checkeAccess = []
      that.addform =  {
        name:'',
        code:'',
        sort:'',
        isAdmin:'false'
      }
      if (that.$refs.tree) {
        that.$refs.tree.setCheckedKeys([]);
      }
      console.log(that.checkeAccess)
    },
    //新增角色
    addRole(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let that = this
          if(that.checkeAccess.length == 0 && that.addform.isAdmin == 'false'){
            this.$message({
              showClose: true,
              message: '请给角色分配权限！',
              type: 'error'
            });
            return false
          }
          let ids=''
          that.checkeAccess.forEach(val => {
            ids += val.id + ','
          })
          // 去除最后的逗号
          ids=ids.substr(0, ids.length - 1);
          let isAdmin
          that.addform.isAdmin == 'false' ? isAdmin = false : isAdmin = true
          let params = {
            name: that.addform.name,
            isAdmin: isAdmin,
            code: that.addform.code,
            sortNo: that.addform.sort,
            permissions: ids
          }
          that.$api.saveRole(params).then(obj => {
            if(obj.data.code=="0000"){
              that.$message({
                type: 'success',
                message: '角色创建成功！'
              });
              that.getRoleList()
              that.createDialogVisible = false
            }else{
              that.$alert(obj.data.desc)
            }
          })
        } else {
          return false;
        }
      });
    }
  }
}
</script>
<style>
  .el-tree-node__label{
    position: relative;
  }
  .el-tree-node__label:before{
    content: '';
    width: 20px;
    height: 20px;
    display: block;
    position: absolute;
    top: 9px;
    left: -27px;
    z-index: 999;
  }
  .levelname{
    position: relative;
    min-width: 135px;
  }
  .levelname:before{
    content: '';
    width: 20px;
    height: 20px;
    display: block;
    position: absolute;
    top: 9px;
    left: -27px;
    z-index: 999;
  }
  .fatherlevelname{
    position: relative;
    min-width: 135px;
  }
  .fatherlevelname:before{
    content: '';
    width: 20px;
    height: 20px;
    display: block;
    position: absolute;
    top: 9px;
    left: -27px;
    z-index: 999;
  }
  .el-checkbox__inner{
    top:0;
  }
</style>


