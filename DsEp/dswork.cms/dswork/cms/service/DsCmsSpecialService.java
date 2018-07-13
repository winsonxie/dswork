package dswork.cms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.cms.dao.DsCmsSiteDao;
import dswork.cms.dao.DsCmsSpecialDao;
import dswork.cms.model.DsCmsSite;
import dswork.cms.model.DsCmsSpecial;

@Service
@SuppressWarnings("all")
public class DsCmsSpecialService
{
	@Autowired
	private DsCmsSpecialDao dao;
	@Autowired
	private DsCmsSiteDao siteDao;

	public List<DsCmsSpecial> querySpecialListBySiteid(Long siteid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteid", siteid);
		return dao.queryList(map);
	}
	
	public void saveUpdateBatch(List<DsCmsSpecial> list)
	{
		for(DsCmsSpecial po : list)
		{
			if(dao.get(po.getId()) == null)
			{
				dao.save(po);
			}
			else
			{
				dao.update(po);
			}
		}
	}

	public List<DsCmsSite> querySiteList(Map<String, Object> map)
	{
		return dao.queryList(map);
	}
}
