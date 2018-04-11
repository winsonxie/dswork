INSERT INTO DS_EP_ENTERPRISE (ID, NAME, SSXQ, QYBM, STATUS, TYPE) VALUES (1, '屌爆天逗比技术特攻队', '', '100000', 1, '有限责任公司');
INSERT INTO DS_EP_USER (ID, QYBM, ACCOUNT, PASSWORD, NAME, IDCARD, STATUS, USERTYPE, EMAIL, MOBILE, PHONE, WORKCARD, CAKEY, CREATETIME, SSDW, SSBM, FAX) VALUES (1, '100000', '100000', '670B14728AD9902AECBA32E22FA4F6BD', '屌爆天逗比技术特攻队', '', 1, 1, 'paseweb@163.com', '13800138000', '', '', '', '', '', '', '');
INSERT INTO DS_PERSON_USER (ID, IDCARD, ACCOUNT, PASSWORD, NAME, STATUS, EMAIL, MOBILE, PHONE, CAKEY, CREATETIME, USERTYPE) VALUES (1, '440111180012310004', '440111180012310004', 'E3BA7E699F89B594FA58268130725BE2', '屌爆天逗比技术特攻队', 1, '', '', NULL, NULL, NULL, '1');
INSERT INTO GX_USER (ID, ALIAS, NAME, PASSWORD, TYPE, STATE, MEMO, VJSON) VALUES (17, '100000', '屌爆天逗比技术特攻队', '', '1', 1, '', '');


INSERT INTO DS_BBS_SITE (ID, OWN, NAME, FOLDER, URL, IMG, VIEWSITE) VALUES (1, 'ep100000', '操作系统', '', '', '', '');
INSERT INTO DS_BBS_SITE (ID, OWN, NAME, FOLDER, URL, IMG, VIEWSITE) VALUES (2, 'ep100000', '测试BBS', '', '', '', '');
INSERT INTO DS_BBS_SITE (ID, OWN, NAME, FOLDER, URL, IMG, VIEWSITE) VALUES (3, 'adminadmin', '默认', NULL, NULL, NULL, NULL);


INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (1, NULL, 0, 'Windows', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (2, NULL, 0, 'Mac', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (3, NULL, 0, 'Linux', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (4, NULL, 0, 'Android', '移动操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (5, NULL, 0, 'iOS', '移动操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (6, 1, 0, 'WindowsXP', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (7, 1, 0, 'WindowsVista', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (8, 1, 0, 'Windows7', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (9, 1, 0, 'Windows8', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (10, 1, 0, 'Windows10', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (11, 6, 0, 'WindowsXPSP1', '无', 1, '1', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (12, 6, 0, 'WindowsXPSP1', '无', 1, '2', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (13, 6, 0, 'WindowsXPSP3', '无', 1, '3', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (14, NULL, 1, 'Windows', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (15, NULL, 1, 'Mac', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (16, NULL, 1, 'Linux', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (17, 14, 1, 'WindowsXP', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (18, 14, 1, 'Windows7', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (19, 14, 1, 'Windows10', 'PC操作系统', 1, '9999', '');
INSERT INTO DS_BBS_FORUM (ID, PID, SITEID, NAME, SUMMARY, STATUS, SEQ, VIEWSITE) VALUES (20, 17, 1, 'WindowsXPSP3', '无', 1, '9999', '');


INSERT INTO DS_BBS_PAGE (ID, SITEID, FORUMID, USERID, TITLE, URL, SUMMARY, ISESSENCE, ISTOP, METAKEYWORDS, METADESCRIPTION, RELEASEUSER, RELEASETIME, OVERTIME, LASTUSER, LASTTIME, NUMPV, NUMHT, CONTENT) VALUES (1, 0, 10, '', 'Windows10主题', '', 'Windows10主题。。。', 1, 1, '', '', 'admin', '2015-04-08 12:01:01', '', 'admin', '2015-04-08 12:01:01', 0, 0, 'Windows10主题');
INSERT INTO DS_BBS_PAGE (ID, SITEID, FORUMID, USERID, TITLE, URL, SUMMARY, ISESSENCE, ISTOP, METAKEYWORDS, METADESCRIPTION, RELEASEUSER, RELEASETIME, OVERTIME, LASTUSER, LASTTIME, NUMPV, NUMHT, CONTENT) VALUES (2, 0, 10, '', 'Windows10主题2', '', 'Windows10主题。。。', 1, 0, '', '', 'admin', '2015-04-08 12:01:01', '', 'admin', '2015-04-08 12:01:01', 0, 0, 'Windows10主题2');
