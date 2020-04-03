SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS DS_BASE_UNIT;
CREATE TABLE DS_BASE_UNIT (
  ID                    BIGINT(16)    COMMENT '主键ID' NOT NULL,
  STATUS                INT(1)        COMMENT '状态(1启用,0禁用)' NOT NULL,
  NAME                  VARCHAR(30)   COMMENT '应用名称' NOT NULL,
  APPID                 VARCHAR(256)  COMMENT '应用ID' NOT NULL,
  APPSECRET             VARCHAR(2048) COMMENT '应用密钥' NOT NULL,
  RETURNURL             VARCHAR(1024) COMMENT '回调地址' NOT NULL,
  TYPE                  BIGINT(16)    COMMENT '标记，值相同的为同一个UNIT' NOT NULL,
  CREATETIME            VARCHAR(19)   COMMENT '创建时间' NOT NULL,
  LASTTIME              BIGINT(16)    COMMENT '最后更新时间' NOT NULL,
  MEMO                  VARCHAR(1024) COMMENT '备注说明' NOT NULL,
  PRIMARY KEY (ID
) COMMENT='授权应用的配置信息';

DROP TABLE IF EXISTS DS_BASE_BIND;
CREATE TABLE DS_BASE_BIND (
  ID                    BIGINT(16)    COMMENT '应用ID' NOT NULL,
  NAME                  VARCHAR(30)   COMMENT '应用名称' NOT NULL,
  STATUS                INT(1)        COMMENT '状态(1启用,0禁用)' NOT NULL,
  APPID                 VARCHAR(64)   COMMENT '第三方应用ID' NOT NULL,
  APPSECRET             VARCHAR(2048) COMMENT '第三方应用密钥' NOT NULL,
  APPKEYPRIVATE         VARCHAR(2048) COMMENT '第三方应用私钥' NOT NULL,
  APPKEYPUBLIC          VARCHAR(2048) COMMENT '第三方应用公钥' NOT NULL,
  APPRETURNURL          VARCHAR(256)  COMMENT '第三方应用回调地址' NOT NULL,
  APPTYPE               VARCHAR(32)   COMMENT '第三方应用类型' NOT NULL,
  MEMO                  VARCHAR(30)   COMMENT '备注' NOT NULL,
  CREATETIME            VARCHAR(19)   COMMENT '创建时间' NOT NULL,
  LASTTIME              BIGINT(16)    COMMENT '最后更新时间' NOT NULL,
  PRIMARY KEY (ID)
) COMMENT='第三方应用配置信息';

DROP TABLE IF EXISTS DS_BASE_LOGIN;
CREATE TABLE DS_BASE_LOGIN
(
  ID                   BIGINT(16)    COMMENT '主键ID' NOT NULL,
  APPID                VARCHAR(256)  COMMENT '登录的APPID' NOT NULL,
  LOGINTIME            VARCHAR(19)   COMMENT '登录时间' NOT NULL,
  LOGOUTTIME           VARCHAR(19)   COMMENT '登出时间',
  TIMEOUTTIME          VARCHAR(19)   COMMENT '超时时间',
  PWDTIME              VARCHAR(19)   COMMENT '密码修改时间,没修改则为空',
  TICKET               VARCHAR(128)  COMMENT '登录标识，格式：CODE或OPENID-ACCESS_TOKEN',
  IP                   VARCHAR(128)  COMMENT 'IP地址',
  USERID               BIGINT(16)    COMMENT '用户ID',
  BM                   VARCHAR(64)   COMMENT '操作人当前账号',
  NAME                 VARCHAR(30)   COMMENT '操作人名称',
  STATUS               INT           COMMENT '状态(0失败,1成功)' NOT NULL,
  PRIMARY KEY (ID)
) COMMENT '系统登录日志';

DROP TABLE IF EXISTS DS_BASE_USERTYPE;
CREATE TABLE DS_BASE_USERTYPE
(
  ID                   BIGINT(16)    COMMENT '主键' NOT NULL,
  NAME                 VARCHAR(300)  COMMENT '名称' NOT NULL,
  ALIAS                VARCHAR(300)  COMMENT '标识' NOT NULL,
  STATUS               INT(1)        COMMENT '状态(1)' NOT NULL,
  SEQ                  BIGINT(16)    COMMENT '排序' NOT NULL,
  MEMO                 VARCHAR(1000) COMMENT '扩展信息' NOT NULL,
  RESOURCES            VARCHAR(4000) COMMENT '资源集合' NOT NULL,
  PRIMARY KEY (ID)
) COMMENT '用户类型';

DROP TABLE IF EXISTS DS_BASE_USER;
CREATE TABLE DS_BASE_USER (
  ID                    BIGINT(16)    COMMENT '主键ID' NOT NULL,
  STATUS                INT(1)        COMMENT '状态(1启用,0禁用)' NOT NULL,
  SSQY                  VARCHAR(64)   COMMENT '最长12位的区域编码-区域名称',
  NAME                  VARCHAR(30)   COMMENT '姓名',
  MOBILE                VARCHAR(30)   COMMENT '手机',
  ACCOUNT               VARCHAR(64)   COMMENT '自定义账号' NOT NULL,
  PASSWORD              VARCHAR(64)   COMMENT '密码',
  WORKCARD              VARCHAR(64)   COMMENT '工作证号',
  SEX                   INT(1)        COMMENT '性别(0未知,1男,2女)',
  AVATAR                VARCHAR(300)  COMMENT '头像',
  IDCARD                VARCHAR(18)   COMMENT '身份证号',
  EMAIL                 VARCHAR(300)  COMMENT '电子邮箱',
  PHONE                 VARCHAR(30)   COMMENT '电话',
  ORGPID                BIGINT(16)    COMMENT '单位ID',
  ORGID                 BIGINT(16)    COMMENT '部门ID',
  TYPE                  VARCHAR(300)  COMMENT '类型',
  TYPENAME              VARCHAR(300)  COMMENT '类型名称',
  EXALIAS               VARCHAR(300)  COMMENT '类型扩展标识',
  EXNAME                VARCHAR(300)  COMMENT '类型扩展名称',
  CREATETIME            VARCHAR(19)   COMMENT '创建时间',
  LASTTIME              BIGINT(16)    COMMENT '最后更新时间',
  EXDATA                VARCHAR(64)   COMMENT '扩展字段，如需要放大数据，自动修改为TEXT或CLOB字段',
  PRIMARY KEY (ID)
) COMMENT='系统用户';

DROP TABLE IF EXISTS DS_BASE_USER_BM;
CREATE TABLE DS_BASE_USER_BM (
  BM                    VARCHAR(64)   COMMENT '账号唯一标识'  NOT NULL,
  USERID                BIGINT(16)    COMMENT 'USERID',
  TYPE                  INT(1)        COMMENT '类型(0账号,1手机号,2身份证)',
  PRIMARY KEY (BM)
) COMMENT='登录用户账号';

DROP TABLE IF EXISTS DS_BASE_USER_BIND;
CREATE TABLE DS_BASE_USER_BIND (
  ID                    BIGINT(16)    COMMENT '主键，登录时读到的用户只有id=id这个账号' NOT NULL,
  BINDID                BIGINT(16)    COMMENT '所属来源应用ID' NOT NULL,
  USERID                BIGINT(16)    COMMENT '主用户ID' NOT NULL,
  openid                VARCHAR(512)  COMMENT 'openid' NOT NULL,
  unionid               VARCHAR(512)  COMMENT 'unionid' NOT NULL,
  NAME                  VARCHAR(30)   COMMENT '昵称',
  SEX                   INT(1)        COMMENT '性别(0未知,1男,2女)',
  COUNTRY               VARCHAR(60)   COMMENT '国家',
  PROVINCE              VARCHAR(60)   COMMENT '省份',
  CITY                  VARCHAR(60)   COMMENT '城市',
  AVATAR                VARCHAR(300)  COMMENT '头像',
  CREATETIME            VARCHAR(19)   COMMENT '创建时间' NOT NULL,
  LASTTIME              BIGINT(16)    COMMENT '最后更新时间' NOT NULL,
  PRIMARY KEY (ID)
) COMMENT='第三方用户表';

DROP TABLE IF EXISTS DS_BASE_SYSTEM;
CREATE TABLE DS_BASE_SYSTEM
(
  ID                   BIGINT(16)    COMMENT '主键ID' NOT NULL,
  NAME                 VARCHAR(30)   COMMENT '名称' NOT NULL,
  ALIAS                VARCHAR(128)  COMMENT '系统别名' NOT NULL,
  PASSWORD             VARCHAR(2048) COMMENT '访问密码' NOT NULL,
  DOMAINURL            VARCHAR(30)   COMMENT '网络地址和端口' NOT NULL,
  ROOTURL              VARCHAR(30)   COMMENT '应用根路径' NOT NULL,
  MENUURL              VARCHAR(256)  COMMENT '菜单地址' NOT NULL,
  STATUS               INT(1)        COMMENT '状态(1启用,0禁用)' NOT NULL,
  SEQ                  BIGINT(16)    COMMENT '排序' NOT NULL,
  MEMO                 VARCHAR(300)  COMMENT '备注' NOT NULL,
  PRIMARY KEY (ID)
) COMMENT '应用系统';

DROP TABLE IF EXISTS DS_BASE_FUNC;
CREATE TABLE DS_BASE_FUNC
(
  ID                   BIGINT(16)    COMMENT '主键ID' NOT NULL,
  PID                  BIGINT(16)    COMMENT '上级ID(本表)',
  SYSTEMID             BIGINT(16)    COMMENT '所属系统ID' NOT NULL,
  NAME                 VARCHAR(30)   COMMENT '名称' NOT NULL,
  ALIAS                VARCHAR(30)   COMMENT '标识' NOT NULL,
  URI                  VARCHAR(256)  COMMENT '对应的URI' NOT NULL,
  IMG                  VARCHAR(128)  COMMENT '显示图标',
  STATUS               INT(1)        COMMENT '状态(0不是菜单,1菜单,不是菜单不能添加下级)' NOT NULL,
  SEQ                  BIGINT(16)    COMMENT '排序',
  RESOURCES            VARCHAR(4000) COMMENT '资源集合',
  MEMO                 VARCHAR(3000) COMMENT '扩展信息',
  PRIMARY KEY (ID),
  CONSTRAINT FK_DS_BASE_FUNC_SYSTEM FOREIGN KEY (SYSTEMID)
     REFERENCES DS_BASE_SYSTEM (ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_DS_BASE_FUNC FOREIGN KEY (PID)
     REFERENCES DS_BASE_FUNC (ID) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '功能';

DROP TABLE IF EXISTS DS_BASE_ROLE;
CREATE TABLE DS_BASE_ROLE
(
  ID                   BIGINT(16)    COMMENT '主键ID' NOT NULL,
  PID                  BIGINT(16)    COMMENT '上级ID(本表)',
  SYSTEMID             BIGINT(16)    COMMENT '所属系统ID' NOT NULL,
  NAME                 VARCHAR(30)   COMMENT '名称',
  SEQ                  BIGINT(16)    COMMENT '排序',
  MEMO                 VARCHAR(300)  COMMENT '备注',
  PRIMARY KEY (ID),
  CONSTRAINT FK_DS_BASE_ROLE_SYSTEM FOREIGN KEY (SYSTEMID)
     REFERENCES DS_BASE_SYSTEM (ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_DS_BASE_ROLE FOREIGN KEY (PID)
     REFERENCES DS_BASE_ROLE (ID) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '角色';

DROP TABLE IF EXISTS DS_BASE_ROLEFUNC;
CREATE TABLE DS_BASE_ROLEFUNC
(
  ID                   BIGINT(16)    COMMENT '主键ID' NOT NULL,
  SYSTEMID             BIGINT(16)    COMMENT '系统ID' NOT NULL,
  ROLEID               BIGINT(16)    COMMENT '角色ID' NOT NULL,
  FUNCID               BIGINT(16)    COMMENT '功能ID' NOT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_DS_BASE_ROLEFUNC_SYSTEM FOREIGN KEY (SYSTEMID)
     REFERENCES DS_BASE_SYSTEM (ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_DS_BASE_ROLEFUNC_FUNC FOREIGN KEY (FUNCID)
     REFERENCES DS_BASE_FUNC (ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_DS_BASE_ROLEFUNC_ROLE FOREIGN KEY (ROLEID)
     REFERENCES DS_BASE_ROLE (ID) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '角色功能关系';

DROP TABLE IF EXISTS DS_BASE_ORG;
CREATE TABLE DS_BASE_ORG
(
  ID                   BIGINT(16)    COMMENT '部门ID' NOT NULL,
  PID                  BIGINT(16)    COMMENT '上级ID(本表)',
  NAME                 VARCHAR(30)   COMMENT '名称' NOT NULL,
  STATUS               INT(1)        COMMENT '类型(2单位,1部门,0岗位)' NOT NULL,
  SEQ                  BIGINT(16)    COMMENT '排序' NOT NULL,
  DUTYSCOPE            VARCHAR(3000) COMMENT '职责范围',
  MEMO                 VARCHAR(3000) COMMENT '备注',
  PRIMARY KEY (ID),
  CONSTRAINT FK_DS_BASE_ORG FOREIGN KEY (PID)
     REFERENCES DS_BASE_ORG (ID) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '组织机构';

DROP TABLE IF EXISTS DS_BASE_ORGROLE;
CREATE TABLE DS_BASE_ORGROLE
(
  ID                   BIGINT(16)    COMMENT '主键ID' NOT NULL,
  ORGID                BIGINT(16)    COMMENT '岗位ID' NOT NULL,
  ROLEID               BIGINT(16)    COMMENT '角色ID',
  PRIMARY KEY (ID),
  CONSTRAINT FK_DS_BASE_ORGROLE_ORG FOREIGN KEY (ORGID)
     REFERENCES DS_BASE_ORG (ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_DS_BASE_ORGROLE_ROLE FOREIGN KEY (ROLEID)
     REFERENCES DS_BASE_ROLE (ID) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '组织机构角色关系';

DROP TABLE IF EXISTS DS_BASE_USERORG;
CREATE TABLE DS_BASE_USERORG
(
  ID                   BIGINT(16)    COMMENT '主键ID' NOT NULL,
  ORGID                BIGINT(16)    COMMENT '岗位ID' NOT NULL,
  USERID               BIGINT(16)    COMMENT '用户ID',
  PRIMARY KEY (ID),
  CONSTRAINT FK_DS_BASE_USERORG_ORG FOREIGN KEY (ORGID)
     REFERENCES DS_BASE_ORG (ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_DS_BASE_USERORG_USER FOREIGN KEY (USERID)
     REFERENCES DS_BASE_USER (ID) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '用户岗位关系';

DROP TABLE IF EXISTS DS_BASE_DICT;
CREATE TABLE DS_BASE_DICT  (
  ID                    BIGINT(16)    COMMENT '主键ID' NOT NULL,
  NAME                  VARCHAR(300)  COMMENT '引用名',
  LABEL                 VARCHAR(300)  COMMENT '名称',
  LEVEL                 INT(1)        COMMENT '层级(0为任意层,1层相当于列表,n=>n级属性)',
  RULE                  VARCHAR(300)  COMMENT '编码规则，逗号隔开每层位数(level大于0时必填，如SSXQ为2,2,2,3,3)',
  SEQ                   BIGINT(16)    COMMENT '排序',
  LASTTIME              BIGINT(16)    COMMENT '最后更新时间',
  PRIMARY KEY (ID)
) COMMENT = '字典分类';

DROP TABLE IF EXISTS DS_BASE_DICT_DATA;
CREATE TABLE DS_BASE_DICT_DATA  (
  NAME                  VARCHAR(180)  COMMENT '主键引用名' NOT NULL,
  ID                    VARCHAR(18)   COMMENT '主键标识' NOT NULL,
  PID                   VARCHAR(18)   COMMENT '上级标识(本表,所属标识)',
  LABEL                 VARCHAR(300)  COMMENT '名称',
  MARK                  VARCHAR(128)  COMMENT '标记',
  LEVEL                 INT(10)       COMMENT '层级' NOT NULL,
  STATUS                INT(1)        COMMENT '状态(1树叉,0树叶)' NOT NULL,
  SEQ                   BIGINT(16)    COMMENT '排序',
  MEMO                  VARCHAR(300)  COMMENT '备注',
  PRIMARY KEY (NAME, ID)
) COMMENT = '字典项';

