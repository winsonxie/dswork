package dswork.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.cms.model.DsCmsSite;
import dswork.cms.model.DsCmsSpecial;
import dswork.cms.service.DsCmsSpecialService;

@Scope("prototype")
@Controller
@RequestMapping("/cms/special")
public class DsCmsSpecialController extends DsCmsBaseController
{
	@Autowired
	private DsCmsSpecialService service;

	@RequestMapping("/getSpecial")
	public String getSpecial()
	{
		try
		{
			Long id = req.getLong("siteid"), siteid = -1L;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("own", getOwn());
			List<DsCmsSite> siteList = service.querySiteList(map);
			if(siteList != null && siteList.size() > 0)
			{
				put("siteList", siteList);
				if(id >= 0)
				{
					for(DsCmsSite m : siteList)
					{
						if(m.getId() == id)
						{
							siteid = m.getId();
							put("site", m);
							break;
						}
					}
				}
				if(siteid == -1)
				{
					siteid = siteList.get(0).getId();
					put("site", siteList.get(0));
				}
			}
			put("list", service.querySpecialListBySiteid(siteid));
			put("siteid", siteid);
			return "/cms/special/getSpecial";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/updSpecial")
	public void updSpecial(List<DsCmsSpecial> list)
	{
		try
		{
			service.saveUpdateBatch(list);
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}
}
