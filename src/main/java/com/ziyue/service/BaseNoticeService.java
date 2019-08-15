package com.ziyue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ziyue.dao.BaseClobDao;
import com.ziyue.dao.BaseNoticeDao;
import com.ziyue.entity.BaseClob;
import com.ziyue.entity.BaseNotice;
import com.ziyue.util.DateUtil;
import com.ziyue.util.PageModel;
import com.ziyue.util.StringUtil;
/***
 * 操作通知服务层
 * @author 胡永强
 * @since 2011/07/21
 * @version 1.0.1
 */
@Service
public class BaseNoticeService {
	@Autowired
	private BaseNoticeDao baseNoticeDao;
	@Autowired
	private BaseClobDao baseClobDao;
	
	/***
	 * 添加通知
	 * @param notice 通知类
	 */
	public void addNotice(BaseNotice notice){
		notice.setIswarn("n");
		if(StringUtil.notEmpty(notice.getWarndate())){
			if( notice.getWarndate().compareTo(DateUtil.shortDate()) >= 0 ){
				notice.setIswarn("y");
			}
		}
		String noticeid = baseNoticeDao.addNotice(notice);
		baseClobDao.addClob(new BaseClob(noticeid,"BaseNotice.content",notice.getContent()));
	}
	
	/***
	 * 修改通知
	 * @param notice 通知类
	 */
	public void modifyNotice(BaseNotice notice){
		notice.setIswarn("n");
		if(StringUtil.notEmpty(notice.getWarndate())){
			if( notice.getWarndate().compareTo(DateUtil.shortDate()) >= 0 ){
				notice.setIswarn("y");
			}
		}
		baseNoticeDao.modifyNotice(notice);
		baseClobDao.delClobByObjectIdAttrKey(notice.getId(), "BaseNotice.content");
		baseClobDao.addClob(new BaseClob(notice.getId(),"BaseNotice.content",notice.getContent()));
	}
	
	/***
	 * 删除通知
	 * @param id
	 */
	public void delNoticeById(String id){
		baseNoticeDao.delNoticeById(id);
		baseClobDao.delClobByObjectId(id);
	}
	
	/***
	 * 根据Id查找通知
	 * @param id 通知Id
	 * @return  notice 通知实体
	 */
	public BaseNotice findNoticeById(String id){
	   BaseNotice notice =  baseNoticeDao.findNoticeById(id);
	   notice.setContent(baseClobDao.findClobByObjectIdAttrKey(id,"BaseNotice.content" ));
	   return notice;
	} 
	
	/**
	 * 分页条件查询
	 * @param  pageModel 分页封装类
	 * @return PageModel 分页封装类
	 */
	public PageModel findBaseSplitPage(PageModel pageModel){
		return baseNoticeDao.findBaseSplitPage(pageModel);
	}
	
	/**
	 * 查最近number条消息
	 * @return
	 */
	public List<BaseNotice> findNoticeLimit(int number){
		return baseNoticeDao.findNoticeLimit(number);
	}

	public void modifyNoticeWarn(){
		List<BaseNotice> notices = baseNoticeDao.findNoticeWarn("y");
		if(null != notices){
			for(BaseNotice notice : notices ){
				if( notice.getWarndate().compareTo(DateUtil.shortDate()) < 0 ){
					baseNoticeDao.modifyNoticeWarn("n", notice.getId());
				}
			}
		}
	}
}
