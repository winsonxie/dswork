SET FOREIGN_KEY_CHECKS=0;

-- 为cms添加默认的meta信息
ALTER TABLE DS_CMS_SITE
ADD COLUMN METAKEYWORDS  varchar(300) NULL COMMENT 'meta关键词' AFTER ENABLELOG,
ADD COLUMN METADESCRIPTION  varchar(300) NULL COMMENT 'meta描述' AFTER METAKEYWORDS;

-- 更新栏目和内容的url
update DS_CMS_CATEGORY set URL=concat('/a/', ID, '/index.html') where SCOPE!=2;
update DS_CMS_PAGE set URL=concat('/a/', CATEGORYID, '/', ID, '.html') where SCOPE!=2;

-- 创建栏目编辑表
CREATE TABLE DS_CMS_CATEGORY_EDIT
(
   ID                   BIGINT NOT NULL COMMENT '主键',
   SITEID               BIGINT COMMENT '站点ID',
   STATUS               INT COMMENT '状态(-1删除,0新增,1更新)',
   MSG                  VARCHAR(300) COMMENT '审核意见',
   METAKEYWORDS         VARCHAR(300) COMMENT 'meta关键词',
   METADESCRIPTION      VARCHAR(300) COMMENT 'meta描述',
   SUMMARY              VARCHAR(300) COMMENT '摘要',
   RELEASETIME          VARCHAR(19) COMMENT '发布时间',
   RELEASESOURCE        VARCHAR(300) COMMENT '来源',
   RELEASEUSER          VARCHAR(300) COMMENT '作者',
   IMG                  VARCHAR(300) COMMENT '图片',
   URL                  VARCHAR(255) COMMENT '外链URL',
   EDITID               VARCHAR(64) COMMENT '编辑人员ID',
   EDITNAME             VARCHAR(30) COMMENT '编辑人员名称',
   EDITTIME             VARCHAR(19) COMMENT '编辑时间',
   AUDITSTATUS          INT COMMENT '状态(0草稿,1未审核,2不通过,4通过)',
   AUDITID              VARCHAR(64) COMMENT '审核人员ID',
   AUDITNAME            VARCHAR(30) COMMENT '审核人员名称',
   AUDITTIME            VARCHAR(19) COMMENT '审核时间',
   CONTENT              TEXT COMMENT '内容',
   PRIMARY KEY (ID),
   CONSTRAINT FK_DS_CMS_CATEGORY_EDIT_S FOREIGN KEY (SITEID)
      REFERENCES DS_CMS_SITE (ID) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_DS_CMS_CATEGORY_EDIT_C FOREIGN KEY (ID)
      REFERENCES DS_CMS_CATEGORY (ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '审核栏目';

-- 创建内容编辑表
CREATE TABLE DS_CMS_PAGE_EDIT
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
   SITEID               BIGINT COMMENT '站点ID',
   CATEGORYID           BIGINT COMMENT '栏目ID',
   STATUS               INT COMMENT '状态(-1删除,0新增,1更新)',
   MSG                  VARCHAR(300) COMMENT '审核意见',
   TITLE                VARCHAR(300) COMMENT '标题',
   SCOPE                INT COMMENT '类型(1单页,2外链)',
   URL                  VARCHAR(300) COMMENT '链接',
   METAKEYWORDS         VARCHAR(300) COMMENT 'meta关键词',
   METADESCRIPTION      VARCHAR(300) COMMENT 'meta描述',
   SUMMARY              VARCHAR(300) COMMENT '摘要',
   RELEASETIME          VARCHAR(19) COMMENT '发布时间',
   RELEASESOURCE        VARCHAR(300) COMMENT '信息来源',
   RELEASEUSER          VARCHAR(300) COMMENT '作者',
   IMG                  VARCHAR(300) COMMENT '图片',
   IMGTOP               INT COMMENT '焦点图(0否,1是)',
   PAGETOP              INT COMMENT '首页推荐(0否,1是)',
   EDITID               VARCHAR(64) COMMENT '编辑人员ID',
   EDITNAME             VARCHAR(30) COMMENT '编辑人员名称',
   EDITTIME             VARCHAR(19) COMMENT '编辑时间',
   AUDITSTATUS          INT COMMENT '状态(0草稿,1未审核,2不通过,4通过)',
   AUDITID              VARCHAR(64) COMMENT '审核人员ID',
   AUDITNAME            VARCHAR(30) COMMENT '审核人员名称',
   AUDITTIME            VARCHAR(19) COMMENT '审核时间',
   CONTENT              TEXT COMMENT '内容',
   PRIMARY KEY (ID),
   CONSTRAINT FK_DS_CMS_PAGE_EDIT_S FOREIGN KEY (SITEID)
      REFERENCES DS_CMS_SITE (ID) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_DS_CMS_PAGE_EDIT_C FOREIGN KEY (CATEGORYID)
      REFERENCES DS_CMS_CATEGORY (ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '审核内容';

-- 创建采编审发权限表
CREATE TABLE DS_CMS_PERMISSION
(
   ID                   BIGINT NOT NULL COMMENT '主键',
   SITEID               BIGINT COMMENT '站点ID',
   ACCOUNT              VARCHAR(300) COMMENT '用户账号',
   EDITOWN              VARCHAR(3000) COMMENT '可采编栏目（限个人）',
   EDITALL              VARCHAR(3000) COMMENT '可采编栏目',
   AUDIT                VARCHAR(3000) COMMENT '可审核栏目',
   PUBLISH              VARCHAR(3000) COMMENT '可发布栏目',
   PRIMARY KEY (ID),
   CONSTRAINT FK_DS_CMS_PERMISSION FOREIGN KEY (SITEID)
      REFERENCES DS_CMS_SITE (ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '网站权限';

-- 建立DS_CMS_USER视图（可选，用于授权时读用户）
CREATE VIEW DS_CMS_USER AS
SELECT
	ID,
	ACCOUNT,
	NAME,
	'adminadmin' AS OWN
FROM
	DS_COMMON_USER;

-- 将内容表里的数据拷贝至内容编辑表
insert into DS_CMS_PAGE_EDIT (ID,SITEID,CATEGORYID,STATUS,TITLE,SCOPE,URL,METAKEYWORDS,METADESCRIPTION,SUMMARY,RELEASETIME,RELEASESOURCE,RELEASEUSER,IMG,IMGTOP,PAGETOP,CONTENT)
select ID,SITEID,CATEGORYID,0 STATUS,TITLE,SCOPE,URL,METAKEYWORDS,METADESCRIPTION,SUMMARY,RELEASETIME,RELEASESOURCE,RELEASEUSER,IMG,IMGTOP,PAGETOP,CONTENT from DS_CMS_PAGE;

-- 修改了日志表的定义，需要重新设置动作
UPDATE ds_cms_log SET AUDITSTATUS=0 WHERE (EDITNAME!='' AND EDITNAME IS NOT NULL) AND STATUS='0';
UPDATE ds_cms_log SET AUDITSTATUS=1 WHERE (EDITNAME!='' AND EDITNAME IS NOT NULL) AND STATUS='1';
UPDATE ds_cms_log SET AUDITSTATUS=2 WHERE (EDITNAME!='' AND EDITNAME IS NOT NULL) AND STATUS='-1';
UPDATE ds_cms_log SET AUDITSTATUS=3 WHERE (EDITNAME!='' AND EDITNAME IS NOT NULL) AND STATUS='4';
UPDATE ds_cms_log SET AUDITSTATUS=4 WHERE (AUDITNAME!='' AND AUDITNAME IS NOT NULL) AND AUDITSTATUS='2';
UPDATE ds_cms_log SET AUDITSTATUS=5 WHERE (AUDITNAME!='' AND AUDITNAME IS NOT NULL) AND AUDITSTATUS='4';
