package com.ziyue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ziyue.dao.BaseLogDao;
import com.ziyue.entity.BaseLog;
import com.ziyue.util.DateUtil;
import com.ziyue.util.PageModel;

@Service
public class BaseLogService {
	@Autowired
	private BaseLogDao baseLogDao;
	
	@Async
	public void addLog(BaseLog blog) {
		if(null == blog.getUserid()){
			blog.setUserid("Unknown");
		}
		if(null == blog.getRealname()){
			blog.setRealname(blog.getUserid());
		}
		baseLogDao.addLog(blog);
	}

	public void delLogOverDue() {
		String time = DateUtil.fullTime(DateUtil.beforeDay(30));
		baseLogDao.delLog(time);
	}

	public PageModel findBaseSplitPage(PageModel pageModel) {
		return baseLogDao.findBaseSplitPage(pageModel);
	}

	public void delUserLogOverDue() {
		String time = DateUtil.fullTime(DateUtil.beforeDay(30));
		baseLogDao.delUserLog(time);
	}

}
