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
        <!--<el-button type="primary" style="margin-top: 20px" @click="refresh">权限刷新</el-button>-->
        <!--权限树-->
        <div class="treeContent">
          <el-tree v-if="accessData.length > 0"
              :props="props"
              :data="accessData"
              ref="tree"
              node-key="id"
              default-expand-all
              @node-click="nodeClick"
              :expand-on-click-node="false"
          >
          </el-tree>
        </div>
        <!--查看操作权限框-->
        <el-dialog
          :close-on-click-modal="false"
          title="权限详情"
          :visible.sync="seePermission"
          width="50%">
          <el-form label-width="80px" v-model="permissionDetail">
            <el-form-item style="text-align: right">
              <el-button v-if="isPermissionSave" type="primary" @click="createPermission">新增权限</el-button>
              <el-button v-if="isPermissionDelete" type="danger" @click="deletePermission">删除权限</el-button>
            </el-form-item>
            <el-form-item label="权限名称:">
              <el-input v-model="permissionDetail.name"></el-input>
            </el-form-item>
            <el-form-item label="权限类型:">
              <el-select v-model="permissionDetail.type" placeholder="请选择">
                <el-option label="目录" :value="0"></el-option>
                <el-option label="菜单" :value="1"></el-option>
                <el-option label="按钮" :value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="权限标志:">
              <el-input v-model="permissionDetail.permission"></el-input>
            </el-form-item>
            <el-form-item label="请求路径:">
              <el-input v-model="permissionDetail.url"></el-input>
            </el-form-item>
            <el-form-item label="排序号:">
              <el-input v-model="permissionDetail.sortNo"></el-input>
            </el-form-item>
            <el-form-item label="父ID:">
              <el-input v-model="permissionDetail.parentId"></el-input>
            </el-form-item>
            <el-form-item label="创建时间:">
              <el-input disabled v-model="permissionDetail.createTimeDesc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button v-if="isPermissionUpdate" type="primary" @click="updatePermission()">保存</el-button>
            </el-form-item>
          </el-form>
        </el-dialog>
        <!--新增权限-->
      <el-dialog
        :close-on-click-modal="false"
        title="新增权限"
        :visible.sync="addPermissionBox"
        width="50%">
        <el-form label-width="80px" v-model="addPermission" ref="addPermission">
          <el-form-item label="权限名称:" prop="name">
            <el-input v-model="addPermission.name"></el-input>
          </el-form-item>
          <el-form-item label="权限类型:" prop="type">
            <el-select v-model="addPermission.type" placeholder="请选择">
              <el-option label="目录" :value="0"></el-option>
              <el-option label="菜单" :value="1"></el-option>
              <el-option label="按钮" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="权限标志:" prop="permission">
            <el-input v-model="addPermission.permission"></el-input>
          </el-form-item>
          <el-form-item label="请求路径:" prop="url">
            <el-input v-model="addPermission.url"></el-input>
          </el-form-item>
          <el-form-item label="排序号:" prop="sortNo">
            <el-input v-model="addPermission.sortNo"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="addPermissionBtn()">保存</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
</template>
<script>
export default {
    name:'ocrConfirm',
    data(){
    return {
      isPermissionList:false,//权限列表
      isPermissionSelectbyid:false,
      isPermissionSave:false,
      isPermissionUpdate:false,
      isPermissionDelete:false,
      username:'',
      userId:'',
      userAccount:'',
      accessData:[],
      props: {
        label: 'name',
        children: 'childBossPermission'
      },
      nodeId:'',
      seePermission:false,
      addPermissionBox:false,
      permissionDetail:{},
      addPermission:{
        name:'',
        type:'',
        permission:'',
        url:'',
        sortNo:'',
        parentId:'',
      },
      isRelated:false
    }
  },
  mounted(){
    this.username = sessionStorage.getItem("name")
    this.userId = sessionStorage.getItem("userId")
    this.userAccount = sessionStorage.getItem("account")
    this.getApiAccess()
    if(this.isPermissionList){
      this.getAccessList()
    }
  },
  methods: {
    //获取用户操作按钮权限
    getApiAccess(){
      if(this.$util.getapiAccess('permission/list')){//权限列表
        this.isPermissionList = true
      }
      if(this.$util.getapiAccess('permission/selectbyid')){//权限详情
        this.isPermissionSelectbyid = true
      }
      if(this.$util.getapiAccess('permission/save')){//新增权限
        this.isPermissionSave = true
      }
      if(this.$util.getapiAccess('permission/update')){//修改权限
        this.isPermissionUpdate = true
      }
      if(this.$util.getapiAccess('permission/delete')){//删除权限
        this.isPermissionDelete = true
      }


    },
      // 点击节点
    nodeClick(data, node){
      let that = this
      that.nodeId = data.id
      that.addPermission.parentId = data.id
      if(this.isPermissionSelectbyid){
        // 根据id获取权限
        let params = {
          id: data.id
        }
        that.$api.selectbyid(params).then(obj => {
          if(obj.data.code=="0000"){
            that.permissionDetail = obj.data.data
            console.log(that.permissionDetail)
          }else{
            that.$alert(obj.data.desc)
          }
        })
        that.seePermission = true
      }
    },
    //获取权限列表
    getAccessList(){
      let that = this
      that.$api.permissionList().then(obj => {
        if(obj.data.code=="0000"){
          that.accessData = obj.data.data
        }else{
          that.$alert(obj.data.desc)
        }
      })
    },
    // 刷新权限
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
    // 新增权限
    createPermission(){
      this.addPermission = {
          name:'',
          type:'',
          permission:'',
          url:'',
          sortNo:'',
          parentId:this.addPermission.parentId
      }
      this.addPermissionBox = true
    },
    addPermissionBtn(){
      let that = this
      let params = that.addPermission
      that.$api.permissionSave(params).then(obj => {
        if(obj.data.code=="0000"){
          that.$message({
            type: 'success',
            message: '新增成功！'
          });
          that.addPermissionBox = false
          that.getAccessList()
        }else{
          that.$alert(obj.data.desc)
        }
      })
    },
    // 编辑权限
    updatePermission(){
      let that = this
      let params = {
        id: that.permissionDetail.id,
        name: that.permissionDetail.name,
        type: that.permissionDetail.type,
        permission: that.permissionDetail.permission,
        url: that.permissionDetail.url,
        parentId: that.permissionDetail.parentId,
        parentIdPath: that.permissionDetail.parentIdPath,
        sortNo: that.permissionDetail.sortNo
      }
      that.$api.PermissionUpdate(params).then(obj => {
        if(obj.data.code=="0000"){
          that.$message({
            type: 'success',
            message: '权限更新成功！'
          });
          that.seePermission = false
          that.getAccessList()
        }else{
          that.$alert(obj.data.desc)
        }
      })
    },
    // 删除权限
    deletePermission(){
      let that = this
      let params = {
        id:that.permissionDetail.id
      }
      // 查询权限是否有角色关联
      that.$api.PermissionRelation(params).then(obj => {
        if(obj.data.code=="0000"){
          that.isRelated = obj.data.data.isRelated
          let message = ''
          if(that.isRelated == false){//无角色关联
            message = "确认删除吗?"
          }else if(that.isRelated == true){//有角色关联
            message = "此权限有角色关联，确认删除？"
          }
          // 删除权限
          that.$confirm(message, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            that.$api.PermissionDelete(params).then(obj => {
              if(obj.data.code=="0000"){
                that.$message({
                  type: 'success',
                  message: '删除成功！'
                });
                that.seePermission = false
                that.getAccessList()
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
    }
  }
}
</script>

