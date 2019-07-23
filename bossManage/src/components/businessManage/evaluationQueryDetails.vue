<!-- 人工处理详情 -->
<template>
    <div>
        <!-- 通用头部 -->
        <div class="currency_top">业务管理>人工测评>处理</div>
        <div class="ocr_details">
            <div class="clear ocr_details_title">
                <div><label class="title_font">测评编号：</label>{{evImgUpload.evRecordNo}}</div>
                <div class="underline" v-if="evRiskRecords.length>0"><label class="title_font">问卷：</label><a v-if="qadetailBtn" href="JavaScript:;" @click="qadetail">问卷详情</a></div>
            </div>
            <div class="details_textarea details_top" v-if="evRiskRecords.length>0">根据用户测评问卷，<span v-if="tubercle!='' && tubercle!=null">患结节的可能性为："{{tubercle}}"，</span>癌变的可能性为："{{canceration}}"</div>
            <div class="details_img_xz">
                <img :src="evImgUpload.imgUrl" alt="">
            </div>
            <div  class="details_textarea details_top">{{evImgUpload.auditContent}}</div>
            <!-- 转让备注 -->
            <div class="auditAssigns">
                <label class="title_font">转让记录：</label>
                <span v-if="!auditAssigns.length>0">无</span>
                <div v-else>
                    <div class="auditAssigns_div" v-for="(item, index) in auditAssigns" :key="item.id">
                        <div v-if="!isAssignsShow && index==0">
                            <span>{{item.transferTimeDesc}}</span>
                            <label><span class="title_font">{{item.assignFromName}}</span>&nbsp;转给&nbsp;<span class="title_font">{{item.assignToName}}</span></label>
                            <label style="text-align: justify" v-if="item.remark!=null && item.remark!=''">转让备注：{{item.remark}}</label>
                            <label v-else>转让备注：无</label>
                            <a href="javascript:;" v-if="index==0 && !isAssignsShow" @click="isOpen(1)">展开更多>></a>
                        </div>
                        <div  v-if="isAssignsShow">
                            <span>{{item.transferTimeDesc}}</span>
                            <label><span class="title_font">{{item.assignFromName}}</span>&nbsp;转给&nbsp;<span class="title_font">{{item.assignToName}}</span></label>
                            <label style="text-align: justify" v-if="item.remark!=null && item.remark!=''">转让备注：{{item.remark}}</label>
                            <label v-else>转让备注：无</label>
                            <a href="javascript:;" v-if="index==0 && auditAssigns.length>2" @click="isOpen(0)">收起>></a>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- 惠每诊断结果 -->
            <div  class="currency_table nanual_table">
                <div class="hm_div">
                    <span class="title_font">惠每诊断结果：</span>
                    <label v-if="hmThyroidTiDto==null || hmThyroidTiDto.status!=1">惠每数据获取失败</label>
                    <div v-else>
                        <a href="javascript:;" @click="hmOpen(1)" v-if="!ishmThyroidTiDto">展开>></a>
                        <a href="javascript:;" @click="hmOpen(0)" v-else>收起>></a>
                    </div>
                </div>
                <table class="cont_border" cellspacing="0" cellpadding="0" v-if="hmThyroidTiDto!=null && hmThyroidTiDto.status==1 && ishmThyroidTiDto">
                    <tbody>
                        <td width='40'>序号</td>
                        <td>诊断名称</td>
                        <td>备注文案</td>
                        <td>处理意见</td>
                        <td width='100'>恶性程度级别</td>
                        <td>恶性风险</td>
                        <td width='80'>诊断要点</td>
                        <td width='80'>ACR等级</td>
                    </tbody>
                    <tr v-for="(hmThyroidTiDtoItem, hmThyroidTiDtoIndex) in hmThyroidTiDto.list" :key="hmThyroidTiDtoItem.id">
                        <td>{{hmThyroidTiDtoIndex+1}}</td>
                        <td>{{hmThyroidTiDtoItem.diagnosticSuggest}}</td>
                        <td>{{hmThyroidTiDtoItem.interpretPostil}}</td>
                        <td>{{hmThyroidTiDtoItem.opinion}}</td>
                        <td>{{hmThyroidTiDtoItem.viciousGrade}}</td>
                        <td>{{hmThyroidTiDtoItem.viciousRisk}}</td>
                        <td>{{hmThyroidTiDtoItem.diagnosticPoints}}</td>
                        <td>{{hmThyroidTiDtoItem.acrCategory}}</td>
                        
                    </tr>
                </table>
            </div>

            <!-- 机器识别结果 -->
            <div  class="currency_table nanual_table">
                <div class=""><span class="title_font">机器识别结果：</span><label v-if="!autoNoduleList.length>0">机器诊断失败</label></div>
                <table cellspacing="0" cellpadding="0" class="cont_border"  v-if="autoNoduleList.length>0">
                    <tbody>
                        <td>序号</td>
                        <td>位置</td>
                        <td>成分</td>
                        <td>回声</td>
                        <td>形态</td>
                        <td>边缘</td>
                        <td>强回声(多选)</td>
                        <td>最大经线</td>
                        <td>总分值</td>
                        <td>ACR分类</td>
                    </tbody>
                    <tr v-for="(machine, machineIndex) in autoNoduleList" :key="machine.id">
                        <td>{{machineIndex+1}}</td>
                        <td>{{machine.part}}</td>
                        <td>{{machine.ingredient}}</td>
                        <td>{{machine.echo}}</td>
                        <td>{{machine.form}}</td>
                        <td>{{machine.edge}}</td>
                        <td>{{machine.strongEcho}}</td>
                        <td>{{machine.diameter}}</td>
                        <td>{{machine.totalScore}}</td>
                        <td>{{machine.acrCategory}}</td>
                        
                    </tr>
                </table>
            </div>
            <!-- 人工确认结果 -->
            <div  class="currency_table nanual_table">
                <div class=""><span class="title_font">人工确认：<label v-if="evImgUpload.isNormal">无结节</label></span></div>
                <table cellspacing="0" cellpadding="0" class="cont_border" v-if="noduleList.length>0 && evImgUpload.isNormal==false">
                    <tbody>
                        <td>序号</td>
                        <td>位置</td>
                        <td>成分</td>
                        <td>回声</td>
                        <td>形态</td>
                        <td>边缘</td>
                        <td>强回声(多选)</td>
                        <td>最大经线</td>
                        <td>总分值</td>
                        <td>ACR分类</td>
                    </tbody>
                    <tr v-for="(machine, machineIndex) in noduleList" :key="machine.id">
                        <td>{{machineIndex+1}}</td>
                        <td>{{machine.part}}</td>
                        <td>{{machine.ingredient}}</td>
                        <td>{{machine.echo}}</td>
                        <td>{{machine.form}}</td>
                        <td>{{machine.edge}}</td>
                        <td>{{machine.strongEcho}}</td>
                        <td>{{machine.diameter}}</td>
                        <td>{{machine.totalScore}}</td>
                        <td>{{machine.acrCategory}}</td>
                        
                    </tr>
                </table>
            </div>
        </div>

        <!-- 问卷详情 -->
        <div v-if="isQuestionnaire">
            <div class="shade"></div>
            <div class="questionnaireDetails">
                <a href="javascript:;" class="close" @click="colse"></a>
                <div class="questionnaireDetails_cont">
                    <div v-for="item in qadetailList" :key="item.id" class="question_list">
                        <label>{{item.sortNo}}、{{item.question}}</label>
                        <div v-if="item.questType!=3">
                            <span v-for="itemAnswer in item.qaQuestOptionDtos" :key="itemAnswer.id" :class="{check:itemAnswer.checked}">{{itemAnswer.key}}、{{itemAnswer.desc}}</span>
                        </div>
                        <div v-else>
                            <span class="check">{{item.answer}}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    name:'ocrDetails',
    data(){
        return{
            qadetailBtn:false,//问卷详情
            id:this.$route.query.id,
            ishmThyroidTiDto:false,//是否显示惠每诊断数据
            isAssignsShow:true,//多余的是否显示
            evImgUpload:'',//OCR处理详情
            auditAssigns:[],//转让记录
            hmThyroidTiDto:'',//惠每诊断数据
            autoNoduleList:[],//机器识别
            evRiskRecords:[],//风险等级，根据type字段判断，1结节、2癌变
            tubercle:'',//结节等级
            canceration:'',//癌变等级
            noduleList:[],//人工处理
            isQuestionnaire:false,//是否显示问卷详情
            qadetailList:[],//问卷题目列表
        }
    },
    mounted(){
        this.getApiAccess()
        this.evOcrDetailP()
    },
    methods:{
        //获取用户操作按钮权限
        getApiAccess(){
          if(this.$util.getapiAccess('ev/qadetail')){//问卷详情查询
            this.qadetailBtn = true
          }
        },
        /**
         * 惠每诊断数据展开收起
         */
        hmOpen:function(obj){
            let that = this
            if(obj==1){
                that.ishmThyroidTiDto=true
            }else{
                that.ishmThyroidTiDto=false
            }
        },
        /**
         * 转让记录展开收起
         */
        isOpen:function(obj){
            let that = this
            if(obj==1){
                that.isAssignsShow=true
            }else{
                that.isAssignsShow=false
            }
        },
        /**
         * 惠每诊断数据展开收起
         */
        hmOpen:function(obj){
            let that = this
            if(obj==1){
                that.ishmThyroidTiDto=true
            }else{
                that.ishmThyroidTiDto=false
            }
        },
        /**
         * 根据id查询详情
         */
        evOcrDetailP:function(){
            let that=this
            let params = {
              uploadId:parseInt(that.id)
            }
            that.$api.uploaddetail(params).then(obj => {
                if(obj.data.code=="0000"){
                    that.evImgUpload=obj.data.data.evImgUpload//ocr处理详情
                    that.auditAssigns=obj.data.data.auditAssigns//转让记录
                    if(that.auditAssigns.length>2){
                        that.isAssignsShow=false
                    }
                    that.hmThyroidTiDto=obj.data.data.hmThyroidTiDto//惠每诊断数据
                    that.autoNoduleList=obj.data.data.autoNoduleList//机器识别
                    that.noduleList=obj.data.data.noduleList//人工处理
                    that.evRiskRecords=obj.data.data.evRiskRecords//风险等级
                    if(obj.data.data.evRiskRecords && obj.data.data.evRiskRecords.length>0){
                        for(var i=0;i<obj.data.data.evRiskRecords.length;i++){
                            if(obj.data.data.evRiskRecords[i].type==1){//结节
                                that.tubercle=obj.data.data.evRiskRecords[i].grade
                            }else{
                                that.canceration=obj.data.data.evRiskRecords[i].grade
                            }
                        }
                    }
                }else{
                    that.$alert(obj.data.desc)
                }
            })
        },
        /**
         * 根据ID查询问卷详情
         */
        qadetail:function(){
            let that=this
            that.isQuestionnaire=true
            let params = {
              uploadId:parseInt(that.id)
            }
            that.$api.qadetail(params).then(obj => {
                if(obj.data.code=="0000"){
                    that.qadetailList=obj.data.data

                }else{
                    that.$alert(obj.data.desc)
                }
            })
        },
        /**
         * 关闭问卷详情
         */
        colse:function(){
            this.isQuestionnaire=false
        }
    }
    
}
</script>
