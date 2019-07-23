<!-- ocr确认 -->
<template>
    <div>
        <!-- 通用头部 -->
        <div class="currency_top">业务管理>OCR确认</div>

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
                <a href="javascript:;" v-if="OCRreceivelist == true" :class="{check:status==3}" @click="evRelist()">待领取</a>
                <a href="javascript:;" v-if="OCRUnmanagelist == true" :class="{check:ocrManageStatus==1 && status==0}" @click="ocrProlist(1)">待处理</a>
                <a href="javascript:;" v-if="OCRManagelist == true" :class="{check:ocrManageStatus==2 && status==0}" @click="ocrProlist(2)">已处理</a>
                <label v-if="batchReceiveBtn==true && status==3 &&  dataList!=null && dataList.length>0" @click="batchreceive">批量领取</label>
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
              <el-table-column prop="publishBazaar" v-if="ocrManageStatus==1 && status==0" label="操作" :key="Math.random()">
                <template slot-scope="scope">
                  <span v-if="dealBtn" class="tableBtn" @click="deal(scope.row.id)">处理</span>
                  <span v-if="assignedBtn" class="tableBtn" @click="pwdShow(scope.row.id)">指派</span>
                </template>
              </el-table-column>
              <el-table-column prop="evImgUploadOcr.proTimeDec" v-if="ocrManageStatus==2 && status==0" label="处理时间" :key="Math.random()"></el-table-column>
            </el-table>
          <!-- 分页 -->
          <div v-if="totalPage > 1" class="page">
            <el-pagination background layout="prev, pager, next" :current-page="currentPage" :page-size="pageSize" :total="total"  @current-change="currentChange"></el-pagination>
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
    </div>
</template>
<script>
export default {
    name:'ocrConfirm',
    data(){
    return{
        batchReceiveBtn:false,//批量领取
        dealBtn:false,//处理
        assignedBtn:false,//指派
        OCRreceivelist:false,//待领取
        assignedBtn1:false,//指派人员列表
        OCRManagelist:false,//已处理
        OCRUnmanagelist:false,//待处理
        dataList:[],
        evRecordNo:'',//测评编号
        status:0,//0:ocr待领取
        ocrManageStatus:1,//1:ocr待处理 2:ocr已处理
        time1: '',
        total:0,
        pageSize:10,
        totalPage:1,
        currentPage:1,
        change:false,//指派显示隐藏
        uploadIds:[],//指派id数组
        remark:'',
        userIdList: [],
        user:'',
        userId: '',
        loginAccess:false,
    }
  },
  mounted(){
      this.getApiAccess()
      if(this.assignedBtn1 == true){
        this.evTransferees()
      }
      if(this.OCRUnmanagelist == true){
        this.ocrManageStatus = 1
        this.ocrProlist(this.ocrManageStatus)
      }else if(this.OCRManagelist == true){
        this.ocrManageStatus = 2
        this.ocrProlist(this.ocrManageStatus)
      }else if(this.OCRreceivelist == true){
        this.status = 3
        this.evRelist()
      }
  },
  methods:{
      //获取用户操作按钮权限
      getApiAccess(){
        if(this.$util.getapiAccess('ev/ocr/batchreceive')){//批量领取
          this.batchReceiveBtn = true
        }
        if(this.$util.getapiAccess('ev/ocr/detail')){//处理
          this.dealBtn = true
        }
        if(this.$util.getapiAccess('ev/transferees')){//指派人员列表
          this.assignedBtn1 = true
        }
        if(this.$util.getapiAccess('ev/ocr/transferee')){//指派
          this.assignedBtn = true
        }
        if(this.$util.getapiAccess('ev/ocr/unmanagelist')){//待处理
          this.OCRUnmanagelist = true
        }
        if(this.$util.getapiAccess('ev/ocr/managelist')){//已处理
          this.OCRManagelist = true
        }
        if(this.$util.getapiAccess('ev/ocr/receivelist')){//待领取
          this.OCRreceivelist = true
        }
      },
      pwdShow:function(id){
            this.change=true
            this.uploadIds=[]
            this.uploadIds.push(id)
        },
        deal(id){
          this.$router.push({ path: '/ocrDetails', query: {id:id}})
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
            //console.log("选择的userid是："+vId)
        },
        auditTransferee:function(){
            let that = this
            let params = {
              uploadIds:that.uploadIds,
              userId:that.userId,
              remark:that.remark
            }
            if(that.userId==''){
                that.$alert("请选择指派人")
            }else{
              that.$api.OCRtransferees(params).then(obj => {
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
                        if(that.status==3){
                            that.evRelist()
                        }else{
                            that.ocrProlist(that.ocrManageStatus)
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
     * 1:ocr待处理 2:ocr已处理（5.31，5.32）
     */
    ocrProlist(ocrManageStatus){
        let that=this
        that.status=0
        if(that.ocrManageStatus!=ocrManageStatus){
              that.currentPage=1//切换table数据时，分页重置
        }
        that.ocrManageStatus=ocrManageStatus
        let params = {
          // ocrManageStatus:ocrManageStatus,
          page:{
            pageSize:that.pageSize,
            currentPage:that.currentPage
          },
          evRecordNo:that.evRecordNo,
          beginSubmitTime:that.time1[0],
          endSubmitTime:that.time1[1]
        }
        if(ocrManageStatus == 1){//ocr待处理
          that.$api.OCRUnmanagelist(params).then(obj => {
            if(obj.data.code=="0000"){
              that.dataList=obj.data.data.datas
              // console.log(that.dataList)
              that.total=obj.data.data.totalRecord
              that.currentPage=obj.data.data.currentPage
              that.totalPage=obj.data.data.totalPage
            }else{
              that.$alert(obj.data.desc)
            }
          })
        }else if(ocrManageStatus == 2){//ocr已处理
          that.$api.OCRManagelist(params).then(obj => {
            if(obj.data.code=="0000"){
              that.dataList=obj.data.data.datas
              // console.log(that.dataList)
              that.total=obj.data.data.totalRecord
              that.currentPage=obj.data.data.currentPage
              that.totalPage=obj.data.data.totalPage
            }else{
              that.$alert(obj.data.desc)
            }
          })
        }
      // that.$api.OCRprolist(params).then(obj => {
      //   if(obj.data.code=="0000"){
      //     that.dataList=obj.data.data.datas
      //     // console.log(that.dataList)
      //     that.total=obj.data.data.totalRecord
      //     that.currentPage=obj.data.data.currentPage
      //     that.totalPage=obj.data.data.totalPage
      //   }else{
      //     that.$alert(obj.data.desc)
      //   }
      // })
    },
    /**
     * OCR待领取，状态值：0、OCR待领取
     */
    evRelist:function(){
        let that=this
        if(that.status!=3){
            that.currentPage=1//切换table数据时，分页重置
        }
        that.status=3
        let params = {
          // status:'0',
          page:{
            pageSize:that.pageSize,
            currentPage:that.currentPage
          },
          evRecordNo:that.evRecordNo,
          beginSubmitTime:that.time1[0],
          endSubmitTime:that.time1[1]
        }
        that.$api.OCRreceivelist(params).then(obj => {
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
        if(that.status==3){
            that.evRelist()
        }else{
            that.ocrProlist(that.ocrManageStatus)
        }
    },
    resetData(){
      this.evRecordNo = '';
      this.time1 = '';
    },
    /**
     * 批量领取
     */
    batchreceive:function(){
        let that=this
        let params = {
          evImgUploadIds:[]
        }
      that.$api.batchreceive(params).then(obj => {
        if(obj.data.code=="0000"){
            that.$alert("批量领取成功")
            that.evRelist()
        }else{
            that.$alert(obj.data.desc)
        }
    })
    },
    currentChange(val) {
        this.currentPage = Number(val)
        if(this.status==3){
            this.evRelist()
        }else{
            this.ocrProlist(this.ocrManageStatus)
        }
    }
  }
    
}
</script>

