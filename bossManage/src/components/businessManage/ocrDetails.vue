<!-- ocr详情处理 -->
<template>
    <div>
        <!-- 通用头部 -->
        <div class="currency_top">业务管理>OCR确认>处理</div>
        <div class="ocr_details">
            <div class="clear ocr_details_title">
                <div><label>测评编号：</label>{{evRecordNo}}</div>
                <div><label>提交时间：</label>{{createTimeDesc}}</div>
            </div>
            <div class="details_img">
                <img :src="img" alt=""  id="imgObjcet" :style="styleObj">
            </div>
            <div class="operation_img" style="">
                <a :href="img"  target="view_window">图片预览</a>
                <a href="javascript:;"  @click="downloadIamge('263440')">图片下载</a>
                <a href="javascript:;"  @click="rotate">图片旋转</a>
                <a href="javascript:;"  @click="enlarge">放大</a>
                <a href="javascript:;"  @click="narrow">缩小</a>
            </div>
            <textarea v-model="content" class="details_textarea"></textarea>
            <div class="clear details_but_box">
                <a v-if="invalidreporthandleBtn" href="javascript:;" @click="invalidreporthandle">无效处理</a>
                <a v-if="evSaveBtn" href="javascript:;" @click="evSave" >确认</a>
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
            evSaveBtn:false,//确认
            id:this.$route.query.id,
            img:'',
            evRecordNo:'',
            createTimeDesc:'',
            enlargeNumber:1,
            rotateNumber:0,
            width:0,
            height:0,
            styleObj:{
                'transform':'rotate(0deg);',
                'width':'auto',
                'height':'auto'
                },
            content:''
        }
    },
    mounted(){
        this.getApiAccess()
        this.evOcrDetailP()
    },
    methods:{
      //获取用户操作按钮权限
      getApiAccess(){
        if(this.$util.getapiAccess('ev/invalidreporthandle')){
          this.invalidreporthandleBtn = true
        }
        if(this.$util.getapiAccess('ev/ocr/save')){
          this.evSaveBtn = true
        }
      },
        /**
         * 旋转
         */
        rotate:function(){
            let that = this
            if(that.width==0){
                that.initializationImg()
            }
            if(that.rotateNumber==360){
                that.rotateNumber=0
            }else{
                that.rotateNumber=that.rotateNumber+90
            }
            that.styleObj.width=that.width*that.enlargeNumber+'px'
            that.styleObj.height=that.height*that.enlargeNumber+'px'
            that.styleObj['max-width']=that.width*that.enlargeNumber+'px'
            that.styleObj['max-height']=that.height*that.enlargeNumber+'px'
            that.styleObj.transform='rotate('+that.rotateNumber+'deg)'
        },
        /**
         * 放大
         */
        enlarge:function(){
            let that = this
            if(that.width==0){
                that.initializationImg()
            }
            if(that.enlargeNumber<2.5){
                that.enlargeNumber=that.enlargeNumber+0.5
                console.log("放大倍数："+that.enlargeNumber)
                
                that.styleObj.width=that.width*that.enlargeNumber+'px'
                that.styleObj.height=that.height*that.enlargeNumber+'px'
                that.styleObj['max-width']=that.width*that.enlargeNumber+'px'
                that.styleObj['max-height']=that.height*that.enlargeNumber+'px'
                console.log("图片宽度："+that.styleObj.width+"，图片高度："+that.styleObj.height)
            }else{
                this.$message({
                    message: '已经放到最大了',
                    type: 'warning'
                });
            }
        },
        /**
         * 缩小
         */
        narrow:function(){
            let that = this
            if(that.width==0){
                that.initializationImg()
            }
            console.log("放大倍数："+that.enlargeNumber)
            if(that.enlargeNumber>1){
                that.enlargeNumber=that.enlargeNumber-0.5
                console.log("放大倍数："+that.enlargeNumber)
                
                that.styleObj.width=that.width*that.enlargeNumber+'px'
                that.styleObj.height=that.height*that.enlargeNumber+'px'
                that.styleObj['max-width']=that.width*that.enlargeNumber+'px'
                that.styleObj['max-height']=that.height*that.enlargeNumber+'px'
                console.log("图片宽度："+that.styleObj.width+"，图片高度："+that.styleObj.height)
            }else{
                this.$message({
                    message: '已经放到最小了',
                    type: 'warning'
                });
            }
        },
        /**
         * 初始化图片宽高
         */
        initializationImg:function(){
            //初始化图片宽高
            let imgObjcet=document.getElementById("imgObjcet")
            console.log("原图高度："+imgObjcet.height)
            this.height=imgObjcet.height
            this.width=imgObjcet.width
        },
        /**
         * 根据id查询详情
         */
        evOcrDetailP:function(){
            let that=this
            let params = {
              evImgUploadId:parseInt(that.id)
            }
          that.$api.OCRdetail(params).then(obj => {
            if(obj.data.code=="0000"){
                that.img=obj.data.data.imgUrl
                that.evRecordNo=obj.data.data.evRecordNo
                that.createTimeDesc=obj.data.data.createTimeDesc
                that.content=obj.data.data.content
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
            let params = {
              auditContent:that.content,
              id:parseInt(that.id)
            }
             that.$api.evOCRsave(params).then(obj => {
                if(obj.data.code=="0000"){
                    that.$alert("保存成功", {
                        callback: action => {
                            that.$router.push({path: "index"});
                        }
                    })
                }else{
                    that.$alert(obj.data.desc)
                }
            })
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
                                  that.$router.push({path: "index"});
                              }
                          })
                      }else{
                          that.$alert(obj.data.desc)
                      }
                   })
                }).catch(() => {
                        
            });
            
        },

        downloadIamge:function(name) {//下载图片地址和图片名
          let imgsrc=this.img
          let image = new Image();
          // 解决跨域 Canvas 污染问题
          image.setAttribute("crossOrigin", "anonymous");
          image.onload = function() {
              let canvas = document.createElement("canvas");
              canvas.width = image.width;
              canvas.height = image.height;
              let context = canvas.getContext("2d");
              context.drawImage(image, 0, 0, image.width, image.height);
              let url = canvas.toDataURL("image/png"); //得到图片的base64编码数据

              let a = document.createElement("a"); // 生成一个a元素
              let event = new MouseEvent("click"); // 创建一个单击事件
              a.download = name || "photo"; // 设置图片名称
              a.href = url; // 将生成的URL设置为a.href属性
              a.dispatchEvent(event); // 触发a的单击事件
          };
          image.src = imgsrc;
        },
        downs(){
        this.downloadIamge(this.pic.url, 'pic')
            }
            
        }
}
</script>

