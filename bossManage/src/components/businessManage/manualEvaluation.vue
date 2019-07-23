<!-- 人工测评 -->
<template>
    <div >
        <!-- 通用头部 -->
        <div class="currency_top">业务管理>人工测评</div>

        <!-- 条件筛选区 -->
        <div class="currency_screen">
            <div class="clear currency_screen_div">
                <div class="clear currency_screen_input">
                    <label>测评编号:</label>
                    <input type="text" placeholder="请输入测评编号" v-model="evRecordNo">
                </div>
            </div>
            <div class="clear currency_screen_div">
                 <div class="block currency_screen_time">
                    <span class="demonstration">提交时间:</span>
                    <el-date-picker
                        v-model="time1"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        type="datetimerange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                    </el-date-picker>
                </div>
              <el-button type="primary" icon="el-icon-search"  @click="screenQuer">查询</el-button>
              <el-button type="primary" @click="resetData">重置</el-button>
            </div>
            
        </div>

        <!-- 列表数据 -->
        <div class="currency_table">
            <div class="clear currency_table_a">
                <a href="javascript:;" v-if="PersonReceivelist == true" :class="{check:status==2}" @click="evRelist()">待领取</a>
                <a href="javascript:;" v-if="PersonUnmanagelist == true" :class="{check:auditManageStatus==1 && status!=2}" @click="ocrProlist(1)">待处理</a>
                <a href="javascript:;" v-if="PersonManagelist == true" :class="{check:auditManageStatus==2 && status!=2}" @click="ocrProlist(2)">已处理</a>
                <label v-if="batchreceiveBtn ==true && status==2 && dataList!=null && dataList.length>0" @click="batchreceive">批量领取</label>
            </div>

            <!--表格渲染-->
            <el-table :data="dataList" :key="1" style="width: 100%" border size="mini" >
              <el-table-column type="index" width="50" label="序号"></el-table-column>
              <el-table-column prop="evRecordNo" label="测评编号" :key="Math.random()"></el-table-column>
              <el-table-column prop="createTimeDesc" label="提交时间" :key="Math.random()"></el-table-column>
              <el-table-column prop="ocrStatus" label="机器识别" width="100" :key="Math.random()">
                <template slot-scope="scope">{{ scope.row.ocrStatus ? '是' : '否'}}</template>
              </el-table-column>
              <el-table-column prop="isOwner" label="是否本人" width="100" :key="Math.random()">
                <template slot-scope="scope">{{ scope.row.isOwner ? '是' : '否' }}</template>
              </el-table-column>
              <el-table-column prop="publishBazaar" v-if="auditManageStatus==1 && status!=2" label="操作" :key="Math.random()">
                <template slot-scope="scope">
                  <span v-if="dealBtn" class="tableBtn" @click="deal(scope.row.id)">处理</span>
                  <span v-if="assignedBtn" class="tableBtn" @click="pwdShow(scope.row.id)">指派</span>
                </template>
              </el-table-column>
              <el-table-column prop="proTimeDesc" v-if="auditManageStatus==2 && status!=2" label="处理时间" :key="Math.random()"></el-table-column>
            </el-table>

            <!--分页-->
            <div v-if="totalPage > 1" class="page">
                <el-pagination background layout="prev, pager, next" :current-page="currentPage" :page-size="pageSize" :total="total"  @current-change="currentChange">
                </el-pagination>
            </div>
        </div>

        
      <!-- 指派 -->
      <div v-if="change">
        <div class="shade"></div>
        <div class="transfer">
            <i @click="pwdHide" class="colse"></i>
            <div class="clear">
                <label>指派用户：</label>
                <el-select class="" v-model="user" @change="userClick"  placeholder="请选择指派人">
                    <el-option v-for="item in userIdList" :key="item.id" :label="item.name" :value="item.id"> </el-option>
                </el-select>
            </div>
            <div class="clear" style="margin-top: 20px;">
                <label>指派备注：</label>
                <textarea class="transfer_textarea" v-model="remark" placeholder="请输入指派备注,不超过50个文字" maxlength="50"></textarea>
            </div>
            <div>

            </div>
          <a href="javascript:;" @click="auditTransferee">确定</a>
        </div>
      </div>

    </div>
</template>
<script>
export default {
    name:'ocrConfirm',
    data(){
    return{
        dealBtn:false,//处理
        assignedBtn:false,//指派
        assignedBtn1:false,//指派人员列表
        batchreceiveBtn:false,//批量领取
        PersonUnmanagelist:false,//待处理
        PersonManagelist:false,//已处理
        PersonReceivelist:false,//待领取
        dataList:[],
        evRecordNo:'',
        status:0,//人工测评待领取
        auditManageStatus:1,//1:人工测评待处理 2:人工测评已处理
        time1: '',
        change:false,//指派显示隐藏
        uploadIds:[],//指派id数组
        total:0,
        pageSize:10,
        totalPage:1,
        currentPage:1,
        remark:'',
        userIdList: [],
        user:'',
        userId: '',
    }
  },
  mounted(){
    this.getApiAccess()
      if(this.assignedBtn1 == true){
        this.evTransferees()
      }
    if(this.PersonUnmanagelist == true){
      this.auditManageStatus = 1
      this.ocrProlist(this.auditManageStatus)
    }else if(this.PersonManagelist == true){
      this.auditManageStatus = 2
      this.ocrProlist(this.auditManageStatus)
    }else if(this.PersonReceivelist == true){
      this.status = 3
      this.evRelist()
    }
  },
  methods:{
    //获取用户操作按钮权限
    getApiAccess(){
      if(this.$util.getapiAccess('ev/audit/batchreceive')){//批量领取
        this.batchreceiveBtn = true
      }
      if(this.$util.getapiAccess('ev/transferees')){//指派人员列表
        this.assignedBtn1 = true
      }
      if(this.$util.getapiAccess('ev/audit/transferee')){//指派
        this.assignedBtn = true
      }
      if(this.$util.getapiAccess('ev/uploaddetail')){//处理
        this.dealBtn = true
      }
      if(this.$util.getapiAccess('ev/audit/unmanagelist')){//待处理
        this.PersonUnmanagelist = true
      }
      if(this.$util.getapiAccess('ev/audit/managelist')){//已处理
        this.PersonManagelist = true
      }
      if(this.$util.getapiAccess('ev/audit/receivelist')){//待领取
        this.PersonReceivelist = true
      }
    },
        pwdShow:function(id){
            this.change=true
            this.uploadIds=[]
            this.uploadIds.push(id)
        },
        deal(id){
          this.$router.push({ path: '/manualEvaluationDetails', query: {id:id}})
        },
        pwdHide:function(){
            this.change=false
            this.uploadIds=[]
            this.remark=''
            this.user=''
            this.userId=''
        },
        userClick:function(vId){
            this.userId=vId
        },
        auditTransferee:function(){
            let that = this
            if(that.userId==''){
                that.$alert("请选择指派人")
            }else{
                let params = {
                  uploadIds:that.uploadIds,
                  userId:that.userId,
                  remark:that.remark
                }
                that.$api.auditTransferee(params).then(obj => {
                    that.uploadIds=[]
                    that.remark=''
                    that.user=''
                    that.userId=''
                    if(obj.data.code=="0000"){
                        that.$message({
                            showClose: true,
                            message: '指派成功',
                            type: 'success'
                        });
                        that.change=false
                        that.currentPage=1//分页重置
                        if(that.status==2){
                            that.evRelist()
                        }else{
                            that.ocrProlist(that.auditManageStatus)
                        }
                    }else{
                        that.$alert(obj.data.desc)
                    }
                })
            }
        },
        /**
         * 查询装让人列表
         */
        evTransferees:function(){
            let that = this
            that.$api.transferees().then(obj => {
              if(obj.data.code=="0000"){
                that.userIdList=obj.data.data
              }else{
                that.$alert(obj.data.desc)
              }

            })
        },
      /**
       * 1:人工测评待处理 2:人工测评已处理
       */
      ocrProlist:function(auditManageStatus){
          let that=this
          that.status=0
          if(that.auditManageStatus!=auditManageStatus){
              that.currentPage=1//切换table数据时，分页重置
          }
          that.auditManageStatus=auditManageStatus
          let params = {
            // auditManageStatus:auditManageStatus,
            page:{
              pageSize:that.pageSize,
              currentPage:that.currentPage
            },
            evRecordNo:that.evRecordNo,
            beginSubmitTime:that.time1[0],
            endSubmitTime:that.time1[1]
          }
          if(auditManageStatus == 1){//1:人工测评待处理
            that.$api.PersonUnmanagelist(params).then(obj => {
              if(obj.data.code=="0000"){
                that.dataList=obj.data.data.datas
                that.total=obj.data.data.totalRecord
                that.currentPage=obj.data.data.currentPage
                that.totalPage=obj.data.data.totalPage
              }else{
                that.$alert(obj.data.desc)
              }
            })
          }else if(auditManageStatus == 2){//2:人工测评已处理
            that.$api.PersonManagelist(params).then(obj => {
              if(obj.data.code=="0000"){
                that.dataList=obj.data.data.datas
                that.total=obj.data.data.totalRecord
                that.currentPage=obj.data.data.currentPage
                that.totalPage=obj.data.data.totalPage
              }else{
                that.$alert(obj.data.desc)
              }
            })
          }
        //   that.$api.auditProlist(params).then(obj => {
        //   if(obj.data.code=="0000"){
        //     that.dataList=obj.data.data.datas
        //     that.total=obj.data.data.totalRecord
        //     that.currentPage=obj.data.data.currentPage
        //     that.totalPage=obj.data.data.totalPage
        //   }else{
        //     that.$alert(obj.data.desc)
        //   }
        // })
      },
        /**
         * 批量领取
         */
        batchreceive:function(){
            let that=this
            let params = {
              evImgUploadIds:[]
            }
            that.$api.evBatchreceive(params).then(obj => {
                if(obj.data.code=="0000"){
                    that.$alert("批量领取成功")
                    that.evRelist(that.status)
                }else{
                    that.$alert(obj.data.desc)
                }
            })
        },
      /**
       * 人工测评待领取，状态值：2、人工测评待领取
       */
      evRelist:function(){
          let that=this
          if(that.status!=2){
              that.currentPage=1//切换table数据时，分页重置
          }
          that.status=2
          let params = {
            // status:'2',
            page:{
              pageSize:that.pageSize,
              currentPage:that.currentPage
            },
            evRecordNo:that.evRecordNo,
            beginSubmitTime:that.time1[0],
            endSubmitTime:that.time1[1]
          }
          that.$api.PersonReceivelist(params).then(obj => {
          if(obj.data.code=="0000"){
            that.dataList=obj.data.data.datas

            that.total=obj.data.data.totalRecord
            that.currentPage=obj.data.data.currentPage
            that.totalPage=obj.data.data.totalPage
          }else{
            that.$alert(obj.data.desc)
          }
        })
      },
    /**
     * 增加筛选条件查询
     */
    screenQuer:function(){
        let that=this
        that.currentPage=1
        console.log(that.time1)
        if(that.time1==null){
            that.time1=''
        }
        if(that.status==2){
            that.evRelist()
        }else{
            that.ocrProlist(that.auditManageStatus)
        }
    },
    resetData(){
      this.evRecordNo = ''
      this.time1 = ''
    },
    currentChange(val) {
        this.currentPage = Number(val)
        if(this.status==2){
            this.evRelist()
        }else{
            this.ocrProlist(this.auditManageStatus)
        }
    }
  }
}
</script>


