package com.jeecg.p3.weixin.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.jeecgframework.p3.core.utils.common.PageList;
import org.jeecgframework.p3.core.utils.common.PageQuery;
import org.jeecgframework.p3.core.utils.common.Pagenation;
import com.jeecg.p3.weixin.service.WeixinMenuService;
import com.jeecg.p3.weixin.entity.WeixinMenu;
import com.jeecg.p3.weixin.dao.WeixinMenuDao;

/**
 * 描述：</b>微信菜单表<br>
 * @author：weijian.zhang
 * @since：2018年07月12日 13时58分38秒 星期四 
 * @version:1.0
 */
@Service("weixinMenuService")
public class WeixinMenuServiceImpl implements WeixinMenuService {
	@Resource
	private WeixinMenuDao weixinMenuDao;

	@Override
	public void doAdd(WeixinMenu weixinMenu) {
		//判断父级
		weixinMenuDao.add(weixinMenu);
	}

	@Override
	public void doEdit(WeixinMenu weixinMenu) {
		weixinMenuDao.update(weixinMenu);
	}

	@Override
	public void doDelete(String id) {
		weixinMenuDao.delete(id);
	}

	@Override
	public WeixinMenu queryById(String id) {
		WeixinMenu weixinMenu  = weixinMenuDao.get(id);
		return weixinMenu;
	}

	@Override
	public PageList<WeixinMenu> queryPageList(
		PageQuery<WeixinMenu> pageQuery) {
		PageList<WeixinMenu> result = new PageList<WeixinMenu>();
		Integer itemCount = weixinMenuDao.count(pageQuery);
		List<WeixinMenu> list = weixinMenuDao.queryPageList(pageQuery,itemCount);
		Pagenation pagenation = new Pagenation(pageQuery.getPageNo(), itemCount, pageQuery.getPageSize());
		result.setPagenation(pagenation);
		result.setValues(list);
		return result;
	}

	//根据orders获取父级id
	@Override
	public String getFatherIdByorders(String orders) {
		return weixinMenuDao.getFatherIdByorders(orders);
	}

	//根据orders查询菜单信息
	@Override
	public WeixinMenu queryByOrders(String orders) {
		return weixinMenuDao.queryByOrders(orders);
	}

	//根据fatherId查询其子级菜单
	@Override
	public int getSonMenuByFatherId(String fatherId) {
		return weixinMenuDao.getSonMenuByFatherId(fatherId);
	}

	//update-begin--Author:zhangweijian  Date: 20180723 for：获取一级菜单
	//获取一级菜单
	@Override
	public List<WeixinMenu> queryMenusByJwid(WeixinMenu query) {
		return weixinMenuDao.queryMenusByJwid(query);
	}
	//update-end--Author:zhangweijian  Date: 20180723 for：获取一级菜单

	@Override
	public List<WeixinMenu> queryMenuByKeyAndJwid(String key, String jwid) {
		return weixinMenuDao.queryMenuByKeyAndJwid(key, jwid);
	}
	
}
