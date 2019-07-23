<!-- 测评查询 -->
<template>
    <div>
        <!-- 通用头部 -->
        <div class="currency_top">业务管理>测评查询</div>

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
            </div>
            <div class="clear currency_screen_div">
                 <div class="block currency_screen_time">
                    <span class="demonstration">处理时间:</span>
                    <el-date-picker
                        v-model="time2"
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
              <!--表格渲染-->
              <el-table class="smallTable" :data="dataList" :key="1" style="width: 100%" border size="mini" >
                <el-table-column type="index" width="50" label="序号"></el-table-column>
                <el-table-column prop="evRecordNo" label="测评编号" :key="Math.random()" width="130">
                  <template slot-scope="scope">
                    <template v-if="scope.row.status == 4">
                      <span v-if="uploaddetailBtn" style="color: #334452;text-decoration: underline;cursor: pointer;font-weight: bold;" @click="linkToNum(scope.row.id)">{{scope.row.evRecordNo}}</span>
                      <span v-if="!uploaddetailBtn" style="color: #334452;text-decoration: underline;cursor: pointer;font-weight: bold;">{{scope.row.evRecordNo}}</span>
                    </template>
                    <template v-else>{{scope.row.evRecordNo}}</template>
                  </template>
                </el-table-column>
                <el-table-column prop="createTimeDesc" label="提交时间" :key="Math.random()" width="130"></el-table-column>
                <el-table-column prop="ocrStatus" label="机器识别" width="60" :key="Math.random()">
                  <template slot-scope="scope">{{ scope.row.ocrStatus ? '是' : '否'}}</template>
                </el-table-column>
                <el-table-column prop="isOwner" label="是否本人" width="60" :key="Math.random()">
                  <template slot-scope="scope">{{ scope.row.isOwner ? '是' : '否' }}</template>
                </el-table-column>
                <el-table-column prop="status" label="状态"  :key="Math.random()">
                  <template slot-scope="scope">
                    <template v-if="scope.row.status == 0"><span>待ocr领取</span></template>
                    <template v-else-if="scope.row.status == 1"><span>ocr已领取</span></template>
                    <template v-else-if="scope.row.status == 2"><span>人工审核待领取</span></template>
                    <template v-else-if="scope.row.status == 3"><span>人工审核已领取</span></template>
                    <template v-else-if="scope.row.status == 4"><span>人工审核完成</span></template>
                    <template v-else-if="scope.row.status == 99"><span>B超图片无效</span></template>
                    <template v-else><span>未知</span></template>
                  </template>
                </el-table-column>
                <el-table-column prop="evImgUploadOcr" label="OCR核对" width="62" :key="Math.random()">
                  <template slot-scope="scope">
                    <template v-if="scope.row.evImgUploadOcr!=null">{{ scope.row.evImgUploadOcr.bossUserName}}</template>
                    <template v-else>无</template>
                  </template>
                </el-table-column>
                <el-table-column prop="evImgUploadAudit" label="超声评估" width="60" :key="Math.random()">
                  <template slot-scope="scope">
                    <template v-if="scope.row.evImgUploadAudit!=null">{{ scope.row.evImgUploadAudit.bossUserName}}</template>
                    <template v-else>无</template>
                  </template>
                </el-table-column>
                <el-table-column prop="proTimeDesc" label="完成时间" width="130" :key="Math.random()">
                  <template slot-scope="scope">
                    <template v-if="scope.row.proTimeDesc!=''">{{ scope.row.proTimeDesc}}</template>
                    <template v-else>无</template>
                  </template>
                </el-table-column>
              </el-table>
            <!-- 分页 -->
            <div v-if="totalPage > 1" class="page">
                <el-pagination background layout="prev, pager, next" :current-page="currentPage" :page-size="pageSize" :total="total"  @current-change="currentChange">
                </el-pagination>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    name:'ocrConfirm',
    data(){
    return{
        uploaddetailBtn:false,//测评详情查询
        dataList:[],
        evRecordNo:'',//测评编号
        time1: '',
        time2: '',
        total:0,
        pageSize:10,
        totalPage:1,
        currentPage:1,
        options: [{
          value: '0',
          label: '待领取'
        }, {
          value: '1',
          label: '待处理'
        }, {
          value: '2',
          label: '已处理'
        }],
        status: ''
    }
  },
  mounted(){
      this.getApiAccess()
      this.evList()
  },
  methods:{
    //获取用户操作按钮权限
    getApiAccess(){
      if(this.$util.getapiAccess('ev/uploaddetail')){//测评详情查询
        this.uploaddetailBtn = true
      }
    },
      linkToNum(id){
        this.$router.push({ path: '/evaluationQueryDetails', query: {id:id}})
      },
      evList:function(){
          let that=this
          let params = {
            page:{
              pageSize:that.pageSize,
              currentPage:that.currentPage
            },
            evRecordNo:that.evRecordNo,
            status:that.status,
            beginSubmitTime:that.time1[0],
            endSubmitTime:that.time1[1],
            beginAuditProTime:that.time2[0],
            endAuditProTime:that.time2[1]
          }
          that.$api.evList(params).then(obj => {
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
        if(that.time1==null){
            that.time1=''
        }
        if(that.time2==null){
            that.time2=''
        }
        that.evList()
    },
    resetData(){
      this.evRecordNo = ''
      this.time1 = ''
      this.time2 = ''
    },
    currentChange(val) {
        this.currentPage = Number(val)
        this.evList()
    }
  }
}
</script>

