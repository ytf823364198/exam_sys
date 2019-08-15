package com.ziyue.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ziyue.dao.BaseUserDao;
import com.ziyue.dao.BaseUserMinorDao;


@Service
public class  ExampleTransaction{
	@Autowired
	private BaseUserDao baseUserDao;
	@Autowired
	private BaseUserMinorDao baseUserMinorDao;
	@Autowired
	@Qualifier("transactionManager")
	private PlatformTransactionManager transactionManager;
	@Autowired
	@Qualifier("minorTransactionManager")
	private PlatformTransactionManager minorTransactionManager;

	@Transactional
	public void testTx() {
		baseUserDao.modifyUser("管理员", "admin");
		//Integer.parseInt("xx");
	}
	
	
	@Transactional("minorTransactionManager")
	public void testTxMultiple() {
		String realname = "管理员3",id="admin";
		baseUserDao.modifyUser(realname, id);
		baseUserMinorDao.modifyUser(realname, id);
		Integer.parseInt("xx");
	}

	//编程式事务
	public void modifyUser() {
	    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
	    TransactionStatus minorStatus = minorTransactionManager.getTransaction(new DefaultTransactionDefinition());
	    try {
	    	String realname = "管理员99999",id="admin";
	    	baseUserDao.modifyUser(realname, id);
			baseUserMinorDao.modifyUser(realname, id);
			//Integer.parseInt("xx");
			minorTransactionManager.commit(minorStatus);
	        transactionManager.commit(status);
	    } catch (Exception e) {
	    	minorTransactionManager.rollback(minorStatus);
	        transactionManager.rollback(status);
	        e.printStackTrace();
	    }
	}
	
}
