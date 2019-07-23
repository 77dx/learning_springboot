//import Axios from 'axios';
import util from '@/plugins/util/util'
import axios from '@/plugins/util/servuce'
//获取接口地址
export const headApi = util.headUrl

export default {
  login(params) { //登录
      return axios.post(`${headApi}/auth/login`, params)
  },
  logout() { //登出
    return axios.post(`${headApi}/auth/logout`)
  },
  captcha() { //获取验证码
    return axios.post(`${headApi}/auth/captcha`)
  },
  create(params) { //创建用户(并初始化化角色)
    return axios.post(`${headApi}/user/create`,params)
  },
  userList(params) { //用户列表
    return axios.post(`${headApi}/user/list`, params)
  },
  updatepwd(params) { //修改密码（自己）
    return axios.post(`${headApi}/user/updatepwd`, params)
  },
  resetpwd(params) { //重置密码（管理员）
    return axios.post(`${headApi}/user/resetpwd`, params)
  },
  saveRole(params) { //新增角色(并初始化权限)
    return axios.post(`${headApi}/role/save`, params)
  },
  roleList(params) { //角色列表(无分页)
    return axios.post(`${headApi}/role/list`, params)
  },
  rolePageList(params) { //角色列表(有分页)
    return axios.post(`${headApi}/role/pagelist`, params)
  },
  roleDelete(params) { //角色删除(修改状态)
    return axios.post(`${headApi}/role/delete`, params)
  },
  userRoleList(params) { //用户的角色列表
    return axios.post(`${headApi}/role/user/list`, params)
  },
  setroles(params) { //更新用户角色
    return axios.post(`${headApi}/user/setroles`, params)
  },
  loadpermission() { //权限刷新
    return axios.post(`${headApi}/permission/loadpermission`)
  },
  pagelist(params) { //权限列表(有分页)
    return axios.post(`${headApi}/permission/pagelist`, params)
  },
  permissionList(params) { //权限列表(无分页)
    return axios.post(`${headApi}/permission/list`, params)
  },
  getAuthorizationData(params) { //角色的权限列表
    return axios.post(`${headApi}/role/getAuthorizationData`, params)
  },
  evList(params) { //测评查询
    return axios.post(`${headApi}/ev/list`, params)
  },
  evReList(params) { //待领取列表
    return axios.post(`${headApi}/ev/re/list`, params)
  },
  batchreceive(params) { //OCR批量领取
    return axios.post(`${headApi}/ev/ocr/batchreceive`, params)
  },
  // OCRprolist(params) { //OCR处理列表
  //   return axios.post(`${headApi}/ev/ocr/prolist`, params)
  // },
  OCRdetail(params) { //OCR人工处理详情
    return axios.post(`${headApi}/ev/ocr/detail`, params)
  },
  imgPreviewAndDownload(params) { //图片预览和下载url获取
    return axios.post(`${headApi}/ev/imgPreviewAndDownload`, params)
  },
  evOCRsave(params) { //OCR人工处理结果保存
    return axios.post(`${headApi}/ev/ocr/save`, params)
  },
  evBatchreceive(params) { //人工测评批量领取
    return axios.post(`${headApi}/ev/audit/batchreceive`, params)
  },
  // auditProlist(params) { //人工测评处理列表
  //   return axios.post(`${headApi}/ev/audit/prolist`, params)
  // },
  evDetail(params) { //人工测评详情
    return axios.post(`${headApi}/ev/detail`, params)
  },
  evSave(params) { //人工测评保存
    return axios.post(`${headApi}/ev/save`, params)
  },
  thyroidacrhandle(params) { //人工测评acr
    return axios.post(`${headApi}/ev/thyroidacrhandle`, params)
  },
  OCRsave(params) { //OCR人工处理结果保存
    return axios.post(`${headApi}/ev/ocr/save`, params)
  },
  transferees() { //转派人员列表
    return axios.post(`${headApi}/ev/transferees`)
  },
  OCRtransferees(params) { //ocr转让
    return axios.post(`${headApi}/ev/ocr/transferee`, params)
  },
  auditTransferee(params) { //人工测评转让
    return axios.post(`${headApi}/ev/audit/transferee`, params)
  },
  uploaddetail(params) { //测评详情查询
    return axios.post(`${headApi}/ev/uploaddetail`, params)
  },
  qadetail(params) { //问卷测评详情
    return axios.post(`${headApi}/ev/qadetail`, params)
  },
  invalidreporthandle(params) { //处理？
    return axios.post(`${headApi}/ev/invalidreporthandle`, params)
  },
  deleteUser(params) { //删除用户
    return axios.post(`${headApi}/user/delete`, params)
  },
  setpermission(params) { //角色赋权
    return axios.post(`${headApi}/permission/setpermission`, params)
  },
  // 5.5新增接口
  OCRUnmanagelist(params) { //OCR待处理列表
    return axios.post(`${headApi}/ev/ocr/unmanagelist`, params)
  },
  OCRManagelist(params) { //OCR已处理列表
    return axios.post(`${headApi}/ev/ocr/managelist`, params)
  },
  PersonUnmanagelist(params) { //人工测评待处理列表
    return axios.post(`${headApi}/ev/audit/unmanagelist`, params)
  },
  PersonManagelist(params) { //人工测评已处理列表
    return axios.post(`${headApi}/ev/audit/managelist`, params)
  },
  OCRreceivelist(params) { //OCR待领取列表
    return axios.post(`${headApi}/ev/ocr/receivelist`, params)
  },
  PersonReceivelist(params) { //人工测评待领取列表
    return axios.post(`${headApi}/ev/audit/receivelist`, params)
  },
  restore(params) { //恢复用户
    return axios.post(`${headApi}/user/restore`, params)
  },
  relation(params) { //角色是否有用户关联
    return axios.post(`${headApi}/role/relation`, params)
  },
  permissionSave(params) { //新增权限
    return axios.post(`${headApi}/permission/save`, params)
  },
  selectbyid(params) { //根据id权限查询
    return axios.post(`${headApi}/permission/selectbyid`, params)
  },
  PermissionRelation(params) { //权限是否有角色关联
    return axios.post(`${headApi}/permission/relation`, params)
  },
  PermissionDelete(params) { //删除权限
    return axios.post(`${headApi}/permission/delete`, params)
  },
  PermissionUpdate(params) { //修改权限
    return axios.post(`${headApi}/permission/update`, params)
  },

};
