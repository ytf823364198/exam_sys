CREATE TABLE EX_LEAVE
(
  ID         VARCHAR(32) PRIMARY KEY ,
  DAY        INT(3),
  TELPHONE   VARCHAR(12),
  EMAIL      VARCHAR(64),
  AGE        INT(3),
  MONEY      FLOAT(12,2),
  ADDDATE    DATETIME,
  REMARK     VARCHAR(512),
  STATUS     VARCHAR(2),
  SEX        VARCHAR(2),
  APPUSERID  VARCHAR(32),
  PROCINSTID VARCHAR(32)
)
;

-- 必须添加好表注解
alter table EX_LEAVE comment  '请假申请';
-- 必须添加好字段的注解 
alter table `ziyue_assets`.`ex_leave` 
   change `ID` `ID` varchar(32) character set utf8 collate utf8_general_ci NOT NULL comment '主键', 
   change `DAY` `DAY` int(3) NULL  comment '请假天数', 
   change `TELPHONE` `TELPHONE` varchar(12) character set utf8 collate utf8_general_ci NULL  comment '电话', 
   change `EMAIL` `EMAIL` varchar(64) character set utf8 collate utf8_general_ci NULL  comment '邮件', 
   change `AGE` `AGE` int(3) NULL  comment '年龄', 
   change `MONEY` `MONEY` float(12,2) NULL  comment '扣除薪水', 
   change `ADDDATE` `ADDDATE` datetime NULL  comment '请假时间', 
   change `REMARK` `REMARK` varchar(512) character set utf8 collate utf8_general_ci NULL  comment '备注说明', 
   change `STATUS` `STATUS` varchar(2) character set utf8 collate utf8_general_ci NULL  comment '状态|0:未提交,1:审批中,-1:已否决,2:已审批,9:已作废', 
   change `SEX` `SEX` varchar(2) character set utf8 collate utf8_general_ci NULL  comment '性别|F:女的,M:男的', 
   change `APPUSERID` `APPUSERID` varchar(32) character set utf8 collate utf8_general_ci NULL  comment '申请人', 
   change `PROCINSTID` `PROCINSTID` varchar(32) character set utf8 collate utf8_general_ci NULL  comment '流程实例';