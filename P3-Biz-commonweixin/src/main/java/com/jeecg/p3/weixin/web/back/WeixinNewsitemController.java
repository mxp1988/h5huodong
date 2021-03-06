package com.jeecg.p3.weixin.web.back;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.p3.core.logger.Logger;
import org.jeecgframework.p3.core.logger.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.jeecgframework.p3.core.util.SystemTools;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.VelocityContext;
import org.jeecgframework.p3.core.util.plugin.ViewVelocity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import org.jeecgframework.p3.core.common.utils.AjaxJson;
import org.jeecgframework.p3.core.common.utils.StringUtil;
import org.jeecgframework.p3.core.utils.common.PageQuery;

import com.jeecg.p3.commonweixin.util.Constants;
import com.jeecg.p3.weixin.entity.WeixinNewsitem;
import com.jeecg.p3.weixin.entity.WeixinNewstemplate;
import com.jeecg.p3.weixin.entity.WeixinTemplate;
import com.jeecg.p3.weixin.enums.WeixinSheetTypeEnum;
import com.jeecg.p3.weixin.service.WeixinNewsitemService;
import com.jeecg.p3.weixin.service.WeixinNewstemplateService;
import com.jeecg.p3.weixin.service.WeixinTemplateService;

import org.jeecgframework.p3.core.web.BaseController;

 /**
 * 描述：</b>图文模板素材表<br>
 * @author weijian.zhang
 * @since：2018年07月13日 12时46分36秒 星期五 
 * @version:1.0
 */
@Controller
@RequestMapping("/weixin/back/weixinNewsitem")
public class WeixinNewsitemController extends BaseController{

  public final static Logger log = LoggerFactory.getLogger(WeixinNewsitemController.class);
  @Autowired
  private WeixinNewsitemService weixinNewsitemService;
  @Autowired
  private WeixinNewstemplateService weixinNewstemplateService;
  @Autowired
  private WeixinTemplateService weixinTemplateService;
  
/**
  * 列表页面
  * @return
  */
@RequestMapping(value="list",method = {RequestMethod.GET,RequestMethod.POST})
public void list(@ModelAttribute WeixinNewsitem query,HttpServletResponse response,HttpServletRequest request,
			@RequestParam(required = false, value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(required = false, value = "pageSize", defaultValue = "10") int pageSize) throws Exception{
	 	PageQuery<WeixinNewsitem> pageQuery = new PageQuery<WeixinNewsitem>();
	 	pageQuery.setPageNo(pageNo);
	 	pageQuery.setPageSize(pageSize);
	 	VelocityContext velocityContext = new VelocityContext();
		pageQuery.setQuery(query);
		velocityContext.put("weixinNewsitem",query);
		velocityContext.put("pageInfos",SystemTools.convertPaginatedList(weixinNewsitemService.queryPageList(pageQuery)));
		String viewName = "weixin/back/weixinNewsitem-list.vm";
		ViewVelocity.view(request,response,viewName,velocityContext);
}

 /**
  * 详情
  * @return
  */
@RequestMapping(value="toDetail",method = RequestMethod.GET)
public void weixinNewsitemDetail(@RequestParam(required = true, value = "id" ) String id,HttpServletResponse response,HttpServletRequest request)throws Exception{
		VelocityContext velocityContext = new VelocityContext();
		String viewName = "weixin/back/weixinNewsitem-detail.vm";
		WeixinNewsitem weixinNewsitem = weixinNewsitemService.queryById(id);
		velocityContext.put("weixinNewsitem",weixinNewsitem);
		ViewVelocity.view(request,response,viewName,velocityContext);
}

/**
 * 跳转到添加页面
 * @return
 */
@RequestMapping(value = "/toAdd",method ={RequestMethod.GET, RequestMethod.POST})
public void toAddDialog(@RequestParam(required = true, value = "newstemplateId" ) String newstemplateId,
		HttpServletRequest request,HttpServletResponse response,ModelMap model)throws Exception{
	 VelocityContext velocityContext = new VelocityContext();
	 String viewName = "weixin/back/weixinNewsitem-add.vm";
	 velocityContext.put("newstemplateId",newstemplateId);
	 ViewVelocity.view(request,response,viewName,velocityContext);
}

/**
 * 保存信息
 * @return
 */
@RequestMapping(value = "/doAdd",method ={RequestMethod.GET, RequestMethod.POST})
@ResponseBody
public AjaxJson doAdd(@ModelAttribute WeixinNewsitem weixinNewsitem){
	AjaxJson j = new AjaxJson();
	try {
		//update-begin--Author:zhangweijian  Date: 20180724 for：获取素材最大序号
		//获取素材最大序号
		String maxOrderNo=weixinNewsitemService.getMaxOrderNo(weixinNewsitem.getNewstemplateId());
		if(StringUtil.isEmpty(maxOrderNo)){
			maxOrderNo="1";
		}else{
			maxOrderNo=String.valueOf(Integer.parseInt(maxOrderNo)+1);
		}
		weixinNewsitem.setOrderNo(maxOrderNo);
		//update-end--Author:zhangweijian  Date: 20180724 for：获取素材最大序号
		weixinNewsitem.setCreateTime(new Date());
		weixinNewsitemService.doAdd(weixinNewsitem);
		j.setSuccess(true);
		j.setMsg("保存成功");
	} catch (Exception e) {
		log.error(e.getMessage());
		j.setSuccess(false);
		j.setMsg("保存失败");
	}
	return j;
}

/**
 * 跳转到编辑页面
 * @return
 */
@RequestMapping(value="toEdit",method = RequestMethod.GET)
public void toEdit(@RequestParam(required = true, value = "id" ) String id,HttpServletResponse response,HttpServletRequest request) throws Exception{
		 VelocityContext velocityContext = new VelocityContext();
		 WeixinNewsitem weixinNewsitem = weixinNewsitemService.queryById(id);
		 velocityContext.put("weixinNewsitem",weixinNewsitem);
		 String viewName = "weixin/back/weixinNewsitem-edit.vm";
		 ViewVelocity.view(request,response,viewName,velocityContext);
}

//update-begin--Author:zhangweijian  Date: 20180724 for：跳转预览页面
/**
 * 跳转到预览页面
 * @return
 */
@RequestMapping(value="goContent",method = RequestMethod.GET)
public void goContent(@RequestParam(required = true, value = "id" ) String id,HttpServletResponse response,HttpServletRequest request) throws Exception{
	VelocityContext velocityContext = new VelocityContext();
	WeixinNewsitem weixinNewsitem = weixinNewsitemService.queryById(id);
	velocityContext.put("weixinNewsitem",weixinNewsitem);
	String viewName = "weixin/back/weixinNewsitem-edit.vm";
	ViewVelocity.view(request,response,viewName,velocityContext);
}
//update-end--Author:zhangweijian  Date: 20180724 for：跳转预览页面

/**
 * 编辑
 * @return
 */
@RequestMapping(value = "/doEdit",method ={RequestMethod.GET, RequestMethod.POST})
@ResponseBody
public AjaxJson doEdit(@ModelAttribute WeixinNewsitem weixinNewsitem,HttpServletRequest request){
	AjaxJson j = new AjaxJson();
	try {
		weixinNewsitem.setUpdateTime(new Date());
		String updateBy = (String)request.getSession().getAttribute(Constants.SYSTEM_USERID);
		weixinNewsitem.setUpdateBy(updateBy);
		weixinNewsitemService.doEdit(weixinNewsitem);
		j.setMsg("编辑成功");
	} catch (Exception e) {
		log.error(e.getMessage());
		j.setSuccess(false);
		j.setMsg("编辑失败");
	}
	return j;
}


/**
 * 删除
 * @return
 */
@RequestMapping(value="doDelete",method = RequestMethod.GET)
@ResponseBody
public AjaxJson doDelete(@RequestParam(required = true, value = "id" ) String id){
		AjaxJson j = new AjaxJson();
		try {
			weixinNewsitemService.doDelete(id);
			j.setMsg("删除成功");
		} catch (Exception e) {
		    log.error(e.getMessage());
			j.setSuccess(false);
			j.setMsg("删除失败");
		}
		return j;
}

/**
 * @功能：图文素材编辑
 * @param newstemplateId
 * @param response
 * @param request
 * @throws Exception
 */
@RequestMapping(value="goMessage",method = RequestMethod.GET)
public void goMessage(@RequestParam(required = true, value = "newstemplateId" ) String newstemplateId
		,HttpServletResponse response,HttpServletRequest request) throws Exception{
	VelocityContext velocityContext = new VelocityContext();
	//获取当前模板的素材
	List<WeixinNewsitem> newsItems=weixinNewsitemService.queryByNewstemplateId(newstemplateId);
	String jwid =  request.getSession().getAttribute("jwid").toString();
	if(newsItems.size()>0){
		velocityContext.put("newsItem", newsItems.get(0));
		if(newsItems.size()>1){
			ArrayList list = new ArrayList(newsItems);
			list.remove(0);
			velocityContext.put("newsItems", list);
		}
	}
	velocityContext.put("newstemplateId", newstemplateId);
	velocityContext.put("jwid", jwid);
	String viewName = "weixin/back/weixinNewsitems.vm";
	ViewVelocity.view(request,response,viewName,velocityContext);
}

/**
 * 上传文件信息
 * @param request
 * @param response
 * @return
 */
@RequestMapping(params = "doUpload", method = RequestMethod.POST)
@ResponseBody
public  AjaxJson doUpload(MultipartHttpServletRequest request, HttpServletResponse response) {
	AjaxJson j = new AjaxJson();
	Map<String, Object> attributes = new HashMap<String, Object>();
	try {
		String basePath = request.getSession().getServletContext().getRealPath("/");
		//获取所有文件名称  
		Iterator<String> it = request.getFileNames();  
		while(it.hasNext()){  
		    //根据文件名称取文件  
		    MultipartFile multifile = request.getFile(it.next());  
		    String fileName = multifile.getOriginalFilename(); 
		    String filePath = "upload/files/"; 
		    File file = new File(basePath+filePath);
			if (!file.exists()) {
				file.mkdir();// 创建文件根目录
			}
			filePath = filePath+fileName;
		    String savePath = basePath+filePath;
		    File newFile = new File(savePath);  
		    //上传的文件写入到指定的文件中  
		    multifile.transferTo(newFile);  
		    attributes.put("url", filePath);
		    attributes.put("fileKey", fileName);
		}
		j.setMsg("文件上传成功");
		j.setAttributes(attributes);
	}  catch (Exception e) {
		e.printStackTrace();
		j.setSuccess(false);
		j.setMsg("文件上传失败");
	}  

	return j;
}

/**
 * 图文素材位置调整
 * @return
 */
@RequestMapping(value="/changeOrder",method = {RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public AjaxJson changeOrder(HttpServletRequest request,@RequestParam String id,@RequestParam String passiveNewsId
		,@RequestParam String state){
		AjaxJson j = new AjaxJson();
		try {
			weixinNewsitemService.changeOrder(id,passiveNewsId,state);
			j.setSuccess(true);
		} catch (Exception e) {
		    log.error(e.getMessage());
			j.setSuccess(false);
			j.setMsg("操作失败");
		}
		return j;
}

/**
 * 获取样式列表
 * @return
 */
@RequestMapping(value="/getMySheet",method = {RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public AjaxJson getMySheet(){
	AjaxJson j = new AjaxJson();
	try {
		WeixinSheetTypeEnum[] sheetTypeEnum=WeixinSheetTypeEnum.values();
		Map<String,String> sheetType=new HashMap<String, String>();
		for(int i=0;i<sheetTypeEnum.length;i++){
			sheetType.put(sheetTypeEnum[i].getCode(), sheetTypeEnum[i].getValue());
		}
		j.setObj(sheetType);
		j.setSuccess(true);
	} catch (Exception e) {
		log.error(e.getMessage());
		j.setSuccess(false);
		j.setMsg("操作失败");
	}
	return j;
}

/**
 * 获取单个样式库
 * @return
 */
@RequestMapping(value="/getTemplate",method = {RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public AjaxJson getTemplate(@RequestParam String type){
	AjaxJson j = new AjaxJson();
	try {
		//获取单个样式库
		List<WeixinTemplate> templates=weixinTemplateService.queryByType(type);
		j.setObj(templates);
		j.setSuccess(true);
	} catch (Exception e) {
		log.error(e.getMessage());
		j.setSuccess(false);
		j.setMsg("操作失败");
	}
	return j;
}

}

