<!-- 人工处理详情 -->
<template>
    <div>
        <!-- 通用头部 -->
        <div class="currency_top">业务管理>人工测评>处理</div>
        <div class="ocr_details">
            <div class="clear ocr_details_title">
                <div><label class="title_font">测评编号：</label>{{evRecordNo}}</div>
            </div>
            <div  class="details_textarea details_top">{{auditContent}}</div>
            <!-- 转让备注 -->
            <div class="auditAssigns" v-if="auditAssigns.length>0">
                <label class="title_font">转让记录：</label>
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
            
            <!-- 惠每诊断结果 -->
            <div  class="currency_table nanual_table">
                <div class="hm_div">
                    <span class="title_font">惠每诊断结果：</span>
                    <label v-if="hmThyroidTiDto==null || hmThyroidTiDto.status!=1">惠每数据获取失败</label>
                    <a href="javascript:;" @click="hmOpen(1)" v-if="!ishmThyroidTiDto">展开>></a>
                    <a href="javascript:;" @click="hmOpen(0)" v-else>收起>></a>
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
                <div class=""><span class="title_font">机器识别结果：</span><label v-if="sysproStatus==2">机器诊断失败</label></div>
                <table cellspacing="0" cellpadding="0" v-if="isNormal==false && sysproStatus!=2">
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
                    <tr v-for="(machine, machineIndex) in machineList" :key="machine.id">
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
                <div class=""><span class="title_font">人工确认：</span>
                    <el-radio v-model="radio" label="1" @change="testIsNormal">有结节</el-radio>
                    <el-radio v-model="radio" label="0" @change="testIsNormal">无结节</el-radio>
                </div>

              <!--table重写-->
              <el-table class="smallTable" :data="artificialList" :key="1" style="width: 100%" border size="mini" v-if="radio==1">
                <el-table-column type="index" width="50" label="序号"></el-table-column>
                <el-table-column prop="evRecordNo" label="位置" :key="Math.random()">
                  <template slot-scope="scope">
                    <el-select class="currency_data_select" v-model="scope.row.part" @change="partListSel(scope.$index)" :placeholder="scope.row.part">
                      <el-option v-for="item in listObject.partList" :key="item.value" :label="item.label" :value="item.value"> </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="evRecordNo" label="方位" :key="Math.random()">
                  <template slot-scope="scope">
                    <el-select class="currency_data_select" v-model="scope.row.position" @change="positionListSel(scope.$index)" :placeholder="scope.row.position">
                      <el-option v-for="item in listObject.positionList" :key="item.value" :label="item.label" :value="item.value"> </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="evRecordNo" label="成分" :key="Math.random()">
                  <template slot-scope="scope">
                    <el-select class="currency_data_select" v-model="scope.row.ingredient" @change="ingredientSel(scope.$index)" :placeholder="scope.row.ingredient">
                      <el-option v-for="item in listObject.ingredientList" :key="item.value" :label="item.label" :value="item.value"> </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="evRecordNo" label="回声" :key="Math.random()">
                  <template slot-scope="scope">
                    <el-select class="currency_data_select" v-model="scope.row.echo" @change="echoSel(scope.$index)" :placeholder="scope.row.echo">
                      <el-option v-for="item in listObject.echoList" :key="item.value" :label="item.label" :value="item.value"> </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="evRecordNo" label="形态" :key="Math.random()">
                  <template slot-scope="scope">
                    <el-select class="currency_data_select" v-model="scope.row.form" @change="formSel(scope.$index)" :placeholder="scope.row.form">
                      <el-option v-for="item in listObject.formList" :key="item.value" :label="item.label" :value="item.value"> </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="evRecordNo" label="边缘" :key="Math.random()">
                  <template slot-scope="scope">
                    <el-select class="currency_data_select" v-model="scope.row.edge" @change="edgeSel(scope.$index)" :placeholder="scope.row.edge">
                      <el-option v-for="item in listObject.edgeList" :key="item.value" :label="item.label" :value="item.value"> </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="evRecordNo" label="强回声(多选)" :key="Math.random()">
                  <template slot-scope="scope">
                    <el-select class="currency_data_select" v-model="scope.row.strongEchoList" @change="strongEchoChang(scope.$index)" multiple>
                      <el-option v-for="item in listObject.strongEchoList" :key="item.value" :label="item.label" :value="item.value"> </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="evRecordNo" label="最大经线" :key="Math.random()">
                  <template slot-scope="scope">
                    <input class="currency_data_select diameter_input" type="number" v-model="scope.row.diameter" :placeholder="scope.row.diameter" >
                  </template>
                </el-table-column>
                <el-table-column prop="totalScore" label="总分值" :key="Math.random()"></el-table-column>
                <el-table-column prop="acrCategory" label="ACR分类" :key="Math.random()"></el-table-column>
                <el-table-column prop="evRecordNo" label="操作" :key="Math.random()">
                  <template slot-scope="scope">
                    <span class="tableBtn" @click="fraction(scope.$index)">评分</span>
                    <span class="tableBtn" @click="currencyDelect(scope.$index)">重置</span>
                  </template>
                </el-table-column>
              </el-table>


                <!--<table cellspacing="0" cellpadding="0" v-if="radio==1">-->
                    <!--<tbody>-->
                        <!--<td>序号</td>-->
                        <!--<td>位置</td>-->
                        <!--<td>方位</td>-->
                        <!--<td>成分</td>-->
                        <!--<td>回声</td>-->
                        <!--<td>形态</td>-->
                        <!--<td>边缘</td>-->
                        <!--<td>强回声(多选)</td>-->
                        <!--<td>最大经线(cm)</td>-->
                        <!--<td>总分值</td>-->
                        <!--<td>ACR分类</td>-->
                        <!--<td width='55'>操作</td>-->
                    <!--</tbody>-->

                    <!--<tr v-for="(artificial, index) in artificialList" :key="artificial.id">-->
                        <!--<td>{{index+1}}</td>-->
                        <!--&lt;!&ndash; 位置 &ndash;&gt;-->
                        <!--<td>-->
                            <!--<el-select class="currency_data_select" v-model="artificial.part" @change="partListSel(index)" :placeholder="artificial.part">-->
                                <!--<el-option v-for="item in listObject.partList" :key="item.value" :label="item.label" :value="item.value"> </el-option>-->
                            <!--</el-select>-->
                        <!--</td>-->
                        <!--&lt;!&ndash; 方位 &ndash;&gt;-->
                        <!--<td v-if="artificial.part=='峡部'"></td>-->
                        <!--<td v-else>-->
                            <!--<el-select class="currency_data_select" v-model="artificial.position" @change="positionListSel(index)" :placeholder="artificial.position">-->
                                <!--<el-option v-for="item in listObject.positionList" :key="item.value" :label="item.label" :value="item.value"> </el-option>-->
                            <!--</el-select>-->
                        <!--</td>-->
                        <!--&lt;!&ndash; 成分 &ndash;&gt;-->
                        <!--<td>-->
                            <!--<el-select class="currency_data_select" v-model="artificial.ingredient"  @change="ingredientSel(index)" :placeholder="artificial.ingredient">-->
                                <!--<el-option v-for="item in listObject.ingredientList" :key="item.value" :label="item.label" :value="item.value"> </el-option>-->
                            <!--</el-select>-->
                        <!--</td>-->
                        <!--&lt;!&ndash; 回声 &ndash;&gt;-->
                        <!--<td>-->
                            <!--<el-select class="currency_data_select" v-model="artificial.echo"  @change="echoSel(index)" :placeholder="artificial.echo">-->
                                <!--<el-option v-for="item in listObject.echoList" :key="item.value" :label="item.label" :value="item.value"> </el-option>-->
                            <!--</el-select>-->
                        <!--</td>-->
                        <!--&lt;!&ndash; 形态 &ndash;&gt;-->
                        <!--<td>-->
                            <!--<el-select class="currency_data_select" v-model="artificial.form" @change="formSel(index)" :placeholder="artificial.form">-->
                                <!--<el-option v-for="item in listObject.formList" :key="item.value" :label="item.label" :value="item.value"> </el-option>-->
                            <!--</el-select>-->
                        <!--</td>-->
                        <!--&lt;!&ndash; 边缘 &ndash;&gt;-->
                        <!--<td>-->
                            <!--<el-select class="currency_data_select" v-model="artificial.edge" @change="edgeSel(index)"  :placeholder="artificial.edge">-->
                                <!--<el-option v-for="item in listObject.edgeList" :key="item.value" :label="item.label" :value="item.value"> </el-option>-->
                            <!--</el-select>-->
                        <!--</td>-->
                        <!--&lt;!&ndash;强回声(多选) &ndash;&gt;-->
                        <!--<td>-->
                            <!--<el-select class="currency_data_select"  v-model="artificial.strongEchoList" @change="strongEchoChang(index)" multiple >-->
                                <!--<el-option v-for="item in listObject.strongEchoList" :key="item.value" :label="item.label" :value="item.value"> </el-option>-->
                            <!--</el-select>-->
                        <!--</td>-->
                        <!--&lt;!&ndash; 最大经线 &ndash;&gt;-->
                        <!--<td><input class="currency_data_select diameter_input" type="number" v-model="artificial.diameter"></td>-->
                        <!--&lt;!&ndash; 总分值 &ndash;&gt;-->
                        <!--<td>{{artificial.totalScore}}</td>-->
                        <!--&lt;!&ndash;ACR分类 &ndash;&gt;-->
                        <!--<td>{{artificial.acrCategory}}</td>-->
                        <!--<td class="handle">-->
                            <!--<a href="javascript:;" @click="fraction(index)">评分</a>-->
                            <!--<a href="javascript:;" @click="currencyDelect(index)">重置</a>-->
                        <!--</td>-->
                    <!--</tr>-->
                <!--</table>-->
            </div>
            <div class="clear details_but_box">
                <a href="javascript:;" v-if="invalidreporthandleBtn" @click="invalidreporthandle">无效处理</a>
                <a href="javascript:;" v-if="SaveBtn" @click="evSave">确认</a>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    name:'ocrDetails',
    data(){
        return{
            invalidreporthandleBtn:false,//无效处理
            SaveBtn:false,//保存
            id:this.$route.query.id,
            img:'',
            ishmThyroidTiDto:false,//是否显示惠每诊断数据
            hmThyroidTiDto:'',//惠每诊断数据
            auditAssigns:[],//转让记录
            isAssignsShow:true,//多余的是否显示
            evRecordNo:'',
            createTimeDesc:'',
            initialField:'',//初始化字段
            auditContent:'',//文本
            radio: '1',//人工选择：1、有结节，0、无结节
            isNormal:'',//是否有结节
            sysproStatus:'',//机器识别状态：1、诊断成功，2、失败
            machineList:[],//机器识别列表

            artificialList:[],//人工确认列表
            listObject:{
                //成分
                ingredientList:[{value: '囊性',label: '囊性'}, {value: '海绵样',label: '海绵样'}, {value: '囊实混合',label: '囊实混合'}, {value: '实质性',label: '实质性'}, {value: '无',label: '无'}],
                //位置
                partList:[{value: '左叶',label: '左叶'}, {value: '右叶',label: '右叶'}, {value: '峡部',label: '峡部'}, {value: '无',label: '无'}],
                //方位
                positionList:[{value: '上极',label: '上极'}, {value: '下极',label: '下极'}, {value: '中极',label: '中极'}, {value: '中上极',label: '中上极'}, {value: '中下极',label: '中下极'}, {value: '无',label: '无'}],
                
                //回声
                echoList:[{value: '无回声',label: '无回声'}, {value: '高回声或等回声',label: '高回声或等回声'}, {value: '低回声',label: '低回声'}, {value: '极低回声',label: '极低回声'}, {value: '其他回声',label: '其他回声'}, {value: '无',label: '无'}],
                
                //形态
                formList:[{value: '横径大于纵径',label: '横径大于纵径'}, {value: '纵径大于横径',label: '纵径大于横径'}, {value: '无',label: '无'}],
                //边缘
                edgeList:[{value: '光滑',label: '光滑'}, {value: '模糊',label: '模糊'}, {value: '分叶或不规则',label: '分叶或不规则'}, {value: '向甲状腺外延伸',label: '向甲状腺外延伸'}, {value: '无',label: '无'}],
                //强回声(多选)
                strongEchoList:[{value: '无强回声或大彗尾',label: '无强回声或大彗尾'}, {value: '粗钙化',label: '粗钙化'}, {value: '周围型钙化',label: '周围型钙化'}, {value: '点状强回声',label: '点状强回声'}, {value: '无',label: '无'}]
                
            },
            
        }
    },
    mounted(){
        this.getApiAccess()
        this.evOcrDetailP()
        //初始化字段赋值
        // var fieldObject = new Object
        // this.initialField=fieldObject
        // this.initialField['acrCategory']=''
        // this.initialField['advice']=''
        // this.initialField['aroundCalcification']=''
        // this.initialField['barCode']=''
        // this.initialField['bdiagnosis']=''
        // this.initialField['bigIris']=''
        // this.initialField['blurry']=''
        // this.initialField['cystic']=''
        // this.initialField['diameter']=null
        // this.initialField['echo']=''
        // this.initialField['edge']=''
        // this.initialField['evRecordId']=''
        // this.initialField['extend']=''
        // this.initialField['form']=''
        // this.initialField['highEqualEcho']=''
        // this.initialField['imgUploadId']=''
        // this.initialField['ingredient']=''
        // this.initialField['innerEcho']=''
        // this.initialField['irregularForm']=''
        // this.initialField['isAuto']=''
        // this.initialField['layeringCystic']=''
        // this.initialField['lowEcho']=''
        // this.initialField['lymphNode']=''
        // this.initialField['malignancyDegree']=''
        // this.initialField['malignantAnalysis']=''
        // this.initialField['neck']=''
        // this.initialField['noEcho']=''
        // this.initialField['nodule']=''
        // this.initialField['noduleDiagnosis']=''
        // this.initialField['number']=''
        // this.initialField['otherEcho']=''
        // this.initialField['part']=''
        // this.initialField['pointEcho']=''
        // this.initialField['quantity']=''
        // this.initialField['roughCalcification']=''
        // this.initialField['smooth']=''
        // this.initialField['sponge']=''
        // this.initialField['strongEcho']=''
        // this.initialField['substantive']=''
        // this.initialField['thyroid']=''
        // this.initialField['totalScore']=''
        // this.initialField['verticalBig']=''
        // this.initialField['verticalSmall']=''
        // this.initialField['veryLowEcho']=''
    },
    methods:{
      //获取用户操作按钮权限
      getApiAccess(){
        if(this.$util.getapiAccess('ev/invalidreporthandle')){
          this.invalidreporthandleBtn = true
        }
        if(this.$util.getapiAccess('ev/save')){
          this.SaveBtn = true
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
         * 结节测试
         */
        testIsNormal:function(){
            if(this.radio=='0'){
                this.isNormal=true
            }else{
                this.isNormal=false
            }
        },
        /**
         * 位置选择
         */
        partListSel:function(index){
            let that = this
            if(that.artificialList[index].part=='无'){
                that.artificialList[index].part=''
            }
        },
        /**
         * 方位选择
         */
        positionListSel:function(index){
            let that = this
            if(that.artificialList[index].position=='无'){
                that.artificialList[index].position=''
            }
        },
        /**
         * 强回声(多选)
         */
        strongEchoChang:function(index){
            let that = this
            console.log(JSON.stringify(that.machineList))
            if(that.artificialList[index].strongEchoList.indexOf("无强回声或大彗尾") != -1){
                that.artificialList[index].bigIris="无强回声或大彗尾"
            }else{
                that.artificialList[index].bigIris=null
            }
            if(that.artificialList[index].strongEchoList.indexOf("粗钙化") != -1){
                that.artificialList[index].roughCalcification="粗钙化"
            }else{
                that.artificialList[index].roughCalcification=null
            }
            if(that.artificialList[index].strongEchoList.indexOf("周围型钙化") != -1){
                that.artificialList[index].aroundCalcification="周围型钙化"
            }else{
                that.artificialList[index].aroundCalcification=null
            }
            if(that.artificialList[index].strongEchoList.indexOf("点状强回声") != -1){
                that.artificialList[index].pointEcho="点状强回声"
            }else{
                that.artificialList[index].pointEcho=null
            }
            //选择无，所有选项置空
            if(that.artificialList[index].strongEchoList.indexOf("无") != -1){
                that.artificialList[index].strongEchoList=['无']
                that.artificialList[index].bigIris=null
                that.artificialList[index].roughCalcification=null
                that.artificialList[index].aroundCalcification=null
                that.artificialList[index].pointEcho=null
            }

            console.log(that.artificialList[index].bigIris)
        },
        /**
         * 回声选择
         */
        echoSel:function(index){
            let that=this
            if(that.artificialList[index].echo=='无回声'){
                that.artificialList[index].noEcho=that.artificialList[index].echo
                //其它字段置空
                that.artificialList[index].highEqualEcho=''
                that.artificialList[index].lowEcho=''
                that.artificialList[index].veryLowEcho=''
                that.artificialList[index].otherEcho=''
            }else if(that.artificialList[index].echo=='高回声或等回声'){
                that.artificialList[index].highEqualEcho=that.artificialList[index].echo
                //其它字段置空
                that.artificialList[index].noEcho=''
                that.artificialList[index].lowEcho=''
                that.artificialList[index].veryLowEcho=''
                that.artificialList[index].otherEcho=''
            }else if(that.artificialList[index].echo=='低回声'){
                that.artificialList[index].lowEcho=that.artificialList[index].echo
                //其它字段置空
                that.artificialList[index].noEcho=''
                that.artificialList[index].highEqualEcho=''
                that.artificialList[index].veryLowEcho=''
                that.artificialList[index].otherEcho=''
            }else if(that.artificialList[index].echo=='极低回声'){
                that.artificialList[index].veryLowEcho=that.artificialList[index].echo
                //其它字段置空
                that.artificialList[index].noEcho=''
                that.artificialList[index].highEqualEcho=''
                that.artificialList[index].lowEcho=''
                that.artificialList[index].otherEcho=''
            }else if(that.artificialList[index].echo=='其他回声'){
                that.artificialList[index].otherEcho=that.artificialList[index].echo
                //其它字段置空
                that.artificialList[index].noEcho=''
                that.artificialList[index].highEqualEcho=''
                that.artificialList[index].lowEcho=''
                that.artificialList[index].veryLowEcho=''
            }else{
                that.artificialList[index].echo=''
                //所有字段置空
                that.artificialList[index].noEcho=''
                that.artificialList[index].highEqualEcho=''
                that.artificialList[index].lowEcho=''
                that.artificialList[index].veryLowEcho=''
                that.artificialList[index].otherEcho=''
            }
        },
        /**
         * 成分选择
         */
        ingredientSel:function(index){
            let that=this
            if(that.artificialList[index].ingredient=='囊性'){
                that.artificialList[index].cystic=that.artificialList[index].ingredient
                //其它字段置空
                that.artificialList[index].sponge=''
                that.artificialList[index].layeringCystic=''
                that.artificialList[index].substantive=''
            }else if(that.artificialList[index].ingredient=='海绵样'){
                that.artificialList[index].sponge=that.artificialList[index].ingredient
                //其它字段置空
                that.artificialList[index].cystic=''
                that.artificialList[index].layeringCystic=''
                that.artificialList[index].substantive=''
            }else if(that.artificialList[index].ingredient=='囊实混合'){
                that.artificialList[index].layeringCystic=that.artificialList[index].ingredient
                //其它字段置空
                that.artificialList[index].cystic=''
                that.artificialList[index].sponge=''
                that.artificialList[index].substantive=''
            }else if(that.artificialList[index].ingredient=='实质性'){
                that.artificialList[index].substantive=that.artificialList[index].ingredient
                //其它字段置空
                that.artificialList[index].cystic=''
                that.artificialList[index].sponge=''
                that.artificialList[index].layeringCystic=''
            }else{
                that.artificialList[index].ingredient=''
                //所有字段都置空
                that.artificialList[index].cystic=''
                that.artificialList[index].sponge=''
                that.artificialList[index].layeringCystic=''
                that.artificialList[index].substantive=''
            }
        },
         /**
         * 边缘
         */
        edgeSel:function(index){
            let that=this
            if(that.artificialList[index].edge=='光滑'){
                that.artificialList[index].smooth=that.artificialList[index].edge
                //其它字段置空
                that.artificialList[index].blurry=''
                that.artificialList[index].irregularForm=''
                that.artificialList[index].extend=''
            }else if(that.artificialList[index].edge=='模糊'){
                that.artificialList[index].blurry=that.artificialList[index].edge
                //其它字段置空
                that.artificialList[index].smooth=''
                that.artificialList[index].irregularForm=''
                that.artificialList[index].extend=''
            }else if(that.artificialList[index].edge=='分叶或不规则'){
                that.artificialList[index].irregularForm=that.artificialList[index].edge
                //其它字段置空
                that.artificialList[index].smooth=''
                that.artificialList[index].blurry=''
                that.artificialList[index].extend=''
            }else if(that.artificialList[index].edge=='向甲状腺外延伸'){
                that.artificialList[index].extend=that.artificialList[index].edge
                //其它字段置空
                that.artificialList[index].smooth=''
                that.artificialList[index].blurry=''
                that.artificialList[index].irregularForm=''
            }else{
                that.artificialList[index].edge=''
                //所有字段置空
                that.artificialList[index].smooth=''
                that.artificialList[index].blurry=''
                that.artificialList[index].irregularForm=''
                that.artificialList[index].extend=''
            }
        },
         /**
         * 形态
         */
        formSel:function(index){
            let that=this
            if(that.artificialList[index].form=='横径大于纵径'){
                that.artificialList[index].verticalSmall=that.artificialList[index].form
                //其它字段置空
                that.artificialList[index].verticalBig=''
            }else if(that.artificialList[index].form=='纵径大于横径'){
                that.artificialList[index].verticalBig=that.artificialList[index].form
                //其它字段置空
                that.artificialList[index].verticalSmall=''
            }else{
                that.artificialList[index].form=''
                //其它字段置空
                that.artificialList[index].verticalSmall=''
                that.artificialList[index].verticalBig=''
            }
        },
        /**
         * 根据id查询详情
         */
        evOcrDetailP:function(){
            let that=this
            let params = {
              id:parseInt(that.id)
            }
            that.$api.evDetail(params).then(obj => {
            if(obj.data.code=="0000"){
                that.evRecordNo=obj.data.data.evImgUpload.evRecordNo
                that.auditContent=obj.data.data.evImgUpload.auditContent
                that.sysproStatus=obj.data.data.evImgUpload.sysproStatus
                that.hmThyroidTiDto=obj.data.data.hmThyroidTiDto//惠每诊断数据
                that.auditAssigns=obj.data.data.auditAssigns//转让记录
                if(that.auditAssigns.length>2){
                    that.isAssignsShow=false
                }
                if(obj.data.data.evImgUpload.isNormal){
                    that.radio='0'
                    that.isNormal=true
                }else{
                    that.isNormal=false
                }
                if(obj.data.data.noduleList && obj.data.data.noduleList!=null && obj.data.data.noduleList.length>0){
                    that.machineList=obj.data.data.autoNoduleList

                    that.artificialList=obj.data.data.noduleList
                }
            }else{
                that.$alert(obj.data.desc)
            }
        })
        },
        /**
         * 保存
         */
        evSave:function(){
            let that=this
            let butList=[]
            let falg=true
            if(!that.isNormal){
                for(var i=0;i<that.artificialList.length;i++){
                    if((that.artificialList[i].part!='' && that.artificialList[i].part!=null)||(that.artificialList[i].form!='' && that.artificialList[i].form!=null)
                            ||(that.artificialList[i].position!='' && that.artificialList[i].position!=null)||(that.artificialList[i].ingredient!=''&&that.artificialList[i].ingredient!=null)
                            ||(that.artificialList[i].echo!=''&&that.artificialList[i].echo!=null)||(that.artificialList[i].edge!=''&&that.artificialList[i].edge!=null)
                            ||that.artificialList[i].strongEchoList.length>0){
                            console.log("分数："+that.artificialList[i].totalScore)
                        if((that.artificialList[i].totalScore==null || that.artificialList[i].totalScore=='' ) && that.artificialList[i].totalScore!='0'){
                            that.$alert("请给第"+(i+1)+"条结节进行评分！")
                            falg=false
                            return
                        }else{
                            butList.push(that.artificialList[i])
                        }
                    }
                }
            }
            if(falg){
                console.log("是否有结节："+that.isNormal)
                if(butList && butList.length==0 && !that.isNormal){//判断结节信息
                    that.$alert("您选择了有结节，结节信息不能为空！")
                }else{
                    if(!that.isNormal){
                        //获取用户获取分数最大值所对应的acr分类，新建一个数组，单独存储分数与分类，然后使用sort函数重新排序，取第一条数据就是最大值进行判断
                        var acrArr = []
                        for(var acrI=0;acrI<butList.length;acrI++){
                            console.log("增加数据："+butList[acrI].totalScore)
                            acrArr.push({"totalScore":butList[acrI].totalScore,"acrCategory":butList[acrI].acrCategory})
                        }
                        acrArr.sort(function(a,b){
                            return b.totalScore-a.totalScore;
                        })
                        console.log("打印用户填写最大的acr值为："+acrArr[0].acrCategory)
                    }
                    var isFlage=false
                    //选择了有结节，并且用户选择生成的数据与惠每的数据最大值不相等
                    if(!that.isNormal && that.hmThyroidTiDto!=null && that.hmThyroidTiDto.status==1 && acrArr[0].acrCategory!=that.hmThyroidTiDto.list[0].acrCategory){
                        isFlage=true
                    }
                    //选择了无结节，但是惠每返回了诊断数据
                    if(that.isNormal && that.hmThyroidTiDto!=null && that.hmThyroidTiDto.status==1){
                        isFlage=true
                    }
                    if(isFlage){//判断提交数据与惠每诊断结果是否一致
                        that.$confirm('您的结果与惠每诊断不一致，请再次确认！', '提示', {
                            confirmButtonText: '狠心提交',
                            cancelButtonText: '返回确认',
                            type: 'warning'
                            }).then(() => {
                                let params = {
                                  id:parseInt(that.id),
                                  isNormal:that.isNormal,
                                  nodules:butList
                                }
                                that.$api.evSave(params).then(obj => {
                                    if(obj.data.code=="0000"){
                                        that.$alert("保存成功", {
                                        callback: action => {
                                            that.$router.push({path: "manualEvaluation"});
                                            }
                                        })
                                    }else{
                                        that.$alert(obj.data.desc)
                                    }
                                })
                            }).catch(() => {
                                    
                        });
                    }else{
                        that.$confirm('是否确认提交？', '提示', {
                            confirmButtonText: '确认',
                            cancelButtonText: '取消',
                            type: 'warning'
                            }).then(() => {
                                let params = {
                                  id:parseInt(that.id),
                                  isNormal:that.isNormal,
                                  nodules:butList
                                }
                                that.$api.evSave(params).then(obj => {
                                    if(obj.data.code=="0000"){
                                        that.$alert("保存成功", {
                                        callback: action => {
                                            that.$router.push({path: "manualEvaluation"});
                                    }
                                })
                                    }else{
                                        that.$alert(obj.data.desc)
                                    }
                                })
                            }).catch(() => {
                                    
                        });
                    }
                }
            }
            
        },
        /**
         * 无效处理
         */
        invalidreporthandle:function(){
            let that=this
            that.$confirm('确认把该数据作为无效处理?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
                }).then(() => {
                    let params = {
                      id:parseInt(that.id)
                    }
                    that.$api.invalidreporthandle(params).then(obj => {
                        if(obj.data.code=="0000"){
                            that.$alert("处理成功", {
                                callback: action => {
                                    that.$router.push({path: "manualEvaluation"});
                                }
                            })
                        }else{
                            that.$alert(obj.data.desc)
                        }
                    })
                }).catch(() => {
                        
            });
            
        },
        /**
         * 重置
         */
        currencyDelect:function(index){
            let that = this
            that.$confirm('确认重置该行数据?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                console.log("重置成功")
                that.artificialList[index].part=''
                that.artificialList[index].form=''
                that.artificialList[index].position=''
                that.artificialList[index].ingredient=''
                that.artificialList[index].echo=''
                that.artificialList[index].edge=''
                that.artificialList[index].acrCategory=''
                that.artificialList[index].totalScore=''
                that.artificialList[index].diameter=null
                that.artificialList[index].acrCategory=''
                that.artificialList[index].strongEchoList=[]
                //下级
                that.artificialList[index].pointEcho=''
                that.artificialList[index].aroundCalcification=''
                that.artificialList[index].roughCalcification=''
                that.artificialList[index].bigIris=''
                that.artificialList[index].extend=''
                that.artificialList[index].irregularForm=''
                that.artificialList[index].blurry=''
                that.artificialList[index].smooth=''
                that.artificialList[index].verticalBig=''
                that.artificialList[index].verticalSmall=''
                that.artificialList[index].otherEcho=''
                that.artificialList[index].veryLowEcho=''
                that.artificialList[index].lowEcho=''
                that.artificialList[index].highEqualEcho=''
                that.artificialList[index].noEcho=''
                that.artificialList[index].substantive=''
                that.artificialList[index].layeringCystic=''
                that.artificialList[index].sponge=''
                that.artificialList[index].cystic=''
                console.log(JSON.stringify(that.artificialList[index]))
            }).catch(() => {
                console.log('取消重置')
            });
        },
        /**
         * 获取分数
         */
        fraction:function(index){
            let that=this
            if(that.artificialList[index].part!=''||that.artificialList[index].form!=''||that.artificialList[index].position!=''||that.artificialList[index].ingredient!=''||that.artificialList[index].echo!=''||that.artificialList[index].edge!=''||that.artificialList[index].strongEchoList.length>0){
              let paramsData = {};
              for(let i = 0;i < that.artificialList.length;i++){
                if(i == index){
                  paramsData = that.artificialList[i]
                }
              }
              let params = {
                nodule:paramsData
              }
              that.$api.thyroidacrhandle(params).then(obj => {
                if(obj.data.code=="0000"){
                  that.$message({
                    message: '获取成功！',
                    type: 'success'
                  });
                  that.artificialList[index].acrCategory=obj.data.data.acrCategory
                  that.artificialList[index].totalScore=obj.data.data.totalScore
                }else{
                  that.$alert(obj.data.desc)
                }
              });
            }else{
                that.$alert("请至少选择一项")
            }
             
        }
    }
    
}
</script>
