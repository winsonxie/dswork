<%@page language="java" pageEncoding="UTF-8" import="
dswork.web.MyRequest,
dswork.spring.BeanFactory,
dswork.cms.model.DsCmsPage,
dswork.cms.model.DsCmsPageEdit,
dswork.cms.dao.DsCmsPageDao,
dswork.cms.dao.DsCmsPageEditDao,
java.util.Map,
java.util.HashMap,
common.json.GsonUtil
"%><%
Map<String, String> map = new HashMap<String, String>();
map.put("msg", "");

try
{
	MyRequest req = new MyRequest(request);
	String action = req.getString("action");
	String pushkey = req.getString("pushkey");
	if(pushkey.length() == 0)
	{
		map.put("msg", "pushkey不能为空");
		out.print(GsonUtil.toJson(map));
		return;
	}

	DsCmsPageDao pageDao = (DsCmsPageDao)BeanFactory.getBean("dsCmsPageDao");
	DsCmsPageEditDao pageEditDao = (DsCmsPageEditDao)BeanFactory.getBean("dsCmsPageEditDao");

	if("insert".equals(action))
	{
		if(pageEditDao.getByPushkey(pushkey) != null)
		{
			map.put("msg", "pushkey已存在，不能重复插入");
			out.print(GsonUtil.toJson(map));
			return;
		}

		DsCmsPageEdit po = new DsCmsPageEdit();
		req.getFillObject(po);// 自动装载对象
		po.setAuditstatus(4);// pageEdit设置为已通过状态
		po.setPushkey(pushkey);
		pageEditDao.save(po);
		po.setUrl("/a/" + po.getCategoryid() + "/" + po.getId() + ".html");// 拼接URl
		pageEditDao.updateUrl(po.getId(), po.getUrl());

		DsCmsPage p = new DsCmsPage();
		p.setSiteid(po.getSiteid());
		p.setCategoryid(po.getCategoryid());
		p.setTitle(po.getTitle());
		p.setMetakeywords(po.getMetakeywords());
		p.setMetadescription(po.getMetadescription());
		p.setSummary(po.getSummary());
		p.setContent(po.getContent());
		p.setReleasetime(po.getReleasetime());
		p.setReleasesource(po.getReleasesource());
		p.setReleaseuser(po.getReleaseuser());
		p.setImg(po.getImg());
		p.setImgtop(po.getImgtop());
		p.setPagetop(po.getPagetop());
		p.setScope(po.getScope());
		p.setStatus(0);// page设置为新建未发布状态
		p.setUrl(po.getUrl());
		p.setJsondata(po.getJsondata());

		pageDao.save(p);
		out.print(GsonUtil.toJson(map));
		return;
	}
	if("update".equals(action))
	{
		boolean save = false;
		DsCmsPageEdit po = (DsCmsPageEdit)pageEditDao.getByPushkey(pushkey);
		if(po == null)
		{
			po = new DsCmsPageEdit();
			req.getFillObject(po);// 自动装载对象
			po.setUrl("/a/" + po.getCategoryid() + "/" + po.getId() + ".html");// 拼接URl
			po.setPushkey(pushkey);
			save = true;
		}
		else
		{
			po.setScope(req.getInt("scope"));
			po.setTitle(req.getString("title"));
			po.setSummary(req.getString("summary"));
			po.setMetakeywords(req.getString("metakeywords"));
			po.setMetadescription(req.getString("metadescription"));
			po.setReleasesource(req.getString("releasesource"));
			po.setReleaseuser(req.getString("releaseuser"));
			po.setReleasetime(req.getString("releasetime"));
			po.setContent(req.getString("content"));
			po.setImg(req.getString("img"));
			po.setImgtop(req.getInt("imgtop"));
			po.setPagetop(req.getInt("pagetop"));
			po.setJsondata(req.getString("jsondata"));
			if(po.getScope() != 2)
			{
				po.setUrl("/a/" + po.getCategoryid() + "/" + po.getId() + ".html");
			}
			else
			{
				po.setUrl(req.getString("url"));
			}
		}
		po.setAuditstatus(4);
		if(save)
		{
			po.setStatus(0);
			pageEditDao.save(po);
		}
		else
		{
			po.setStatus(1);
			pageEditDao.update(po);
		}

		save = false;
		DsCmsPage p = (DsCmsPage)pageDao.get(po.getId());
		if(p == null)
		{
			p = new DsCmsPage();
			p.setId(po.getId());
			p.setSiteid(po.getSiteid());
			p.setCategoryid(po.getCategoryid());
			save = true;
		}
		p.setStatus(po.getStatus());
		p.setTitle(po.getTitle());
		p.setMetakeywords(po.getMetakeywords());
		p.setMetadescription(po.getMetadescription());
		p.setSummary(po.getSummary());
		p.setContent(po.getContent());
		p.setReleasetime(po.getReleasetime());
		p.setReleasesource(po.getReleasesource());
		p.setReleaseuser(po.getReleaseuser());
		p.setImg(po.getImg());
		p.setImgtop(po.getImgtop());
		p.setPagetop(po.getPagetop());
		p.setScope(po.getScope());
		p.setUrl(po.getUrl());
		p.setJsondata(po.getJsondata());
		if(save)
		{
			p.setStatus(0);// page设置为新建未发布状态
			pageDao.save(p);
		}
		else
		{
			p.setStatus(1);
			pageDao.update(p);
		}
		out.print(GsonUtil.toJson(map));
		return;
	}
	if("delete".equals(action))
	{
		DsCmsPageEdit po = pageEditDao.getByPushkey(pushkey);
		if(po == null)
		{
			out.print(GsonUtil.toJson(map));
			return;
		}
		pageDao.delete(po.getId());
		pageEditDao.delete(po.getId());
		out.print(GsonUtil.toJson(map));
		return;
	}
	map.put("msg", "未定义action");
}
catch(Exception e)
{
	map.put("msg", e.getMessage());
}
out.print(GsonUtil.toJson(map));
%>