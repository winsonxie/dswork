/**
 * 功能:ExcelController测试类，请忽略
 */
package testwork.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.core.page.Page;
import dswork.core.util.IdUtil;
import dswork.mvc.BaseController;
import dswork.web.MyFile;
import testwork.model.Demo;
import testwork.model.view.ViewMsg;
import testwork.service.ManageDemoService;

@Controller
@RequestMapping("/manage/excel")
public class ManageExcelController extends BaseController
{
	@Autowired
	private ManageDemoService service;// 一个service对应一个控制器Controller
	
	@RequestMapping
	public String export()
	{
		Page<Demo> pageModel = service.queryPage(getPageRequest());
		put("list", pageModel.getResult());
		return "/manage/excel/export.jsp";
	}
	

	@RequestMapping
	public String import1()
	{
		return "/manage/excel/import1.jsp";
	}

	@RequestMapping
	public void import2()
	{
		List<Demo> list = new ArrayList<Demo>();
		List<ViewMsg> mlist = new ArrayList<ViewMsg>();
		try
		{
			MyFile[] files = req().getFileArray("excel");
			if(files.length > 0)
			{
				for(MyFile file : files)
				{
					String ext = file.getFileExt();
					InputStream is = null;
					Workbook wb = null;
					try
					{
						is = new ByteArrayInputStream(file.getFileData());
						if("xls".equals(ext))
						{
							wb = new HSSFWorkbook(is);
						}
						else if("xlsx".equals(ext))
						{
							wb = new XSSFWorkbook(is);
						}
						else
						{
							throw new Exception("文件类型错误，必须为xls或xlsx");
						}
						Sheet sheet = wb.getSheetAt(0);
						Row row = null;
						int rowSum = sheet.getLastRowNum();
						row = sheet.getRow(0);
						if(row == null)
						{
							throw new Exception("文件错误，请下载模板进行输入");
						}
						String t = getString(row.getCell(0));
						if(!t.equals("标题"))
						{
							mlist.add(new ViewMsg(1, "首列标题不为标题"));
							throw new Exception("文件错误，请下载模板进行输入");
						}
						for(int j = 1; j <= rowSum; j++)
						{
							row = sheet.getRow(j);
							if(row == null)
							{
								continue;
							}
							// 标题title, 内容content, 创建时间foundtime
							Demo o = new Demo();
							o.setId(IdUtil.genId());
							String title = getString(row.getCell(0));
							String content = getString(row.getCell(1));
							String foundtime = getString(row.getCell(2));
							o.setTitle(title);
							o.setContent(content);
							o.setFoundtime(foundtime);
							list.add(o);
						}
					}
					finally
					{
						if(is != null)
						{
							is.close();
						}
						if(wb != null)
						{
							wb.close();
						}
					}
				}
			}
		}
		catch(Exception e)
		{
		}
		if(mlist.size() > 0)
		{
			printJson(formatJson(0, toJson(mlist), "数据有误"));
		}
		else
		{
			if(list.size() > 0)
			{
				for(Demo d : list)
				{
					service.save(d);// test
					break;// 测试，只导入一行
				}
				printJson(formatJson(1, null, "测试，只导入一行"));
			}
			else
			{
				printJson(formatJson(0, null, "没有数据"));
			}
		}
		return;
	}
	static com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
	static com.google.gson.Gson gson = builder.create();

	// static com.alibaba.fastjson.serializer.SerializeConfig mapping = new
	// com.alibaba.fastjson.serializer.SerializeConfig();
	// static{mapping.put(java.util.Date.class, new
	// com.alibaba.fastjson.serializer.SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));}
	public static String toJson(Object object)
	{
		if(object != null)
		{
			return gson.toJson(object);
		}
		else
		{
			return "{}";
		}
		// return com.alibaba.fastjson.JSON.toJSONString(object, mapping);
	}

	public static String getString(Cell cell)
	{
		String s = "";
		if(cell != null)
		{
			s = cell.toString().replaceAll(" ", "").replaceAll("　", "").toUpperCase();
		}
		return s;
	}

	public static int getInt(Cell cell)
	{
		int s = 0;
		if(cell != null)
		{
			String ss = cell.toString().replaceAll(" ", "").replaceAll("　", "");
			try
			{
				int dotIndex = ss.indexOf(".");
				if(dotIndex != -1)
				{
					ss = ss.substring(0, dotIndex);
				}
				s = Integer.parseInt(ss);
				if(s < 0)
				{
					s = 0;
				}
			}
			catch(Exception e)
			{
				s = 0;
			}
		}
		return s;
	}

	public static float getFloat(Cell cell)
	{
		float s = 0f;
		if(cell != null)
		{
			String ss = cell.toString().replaceAll(" ", "").replaceAll("　", "");
			try
			{
				s = (float) Math.round(Float.parseFloat(ss) * 100) / 100;
				if(s < 0)
				{
					s = 0f;
				}
			}
			catch(Exception e)
			{
				s = 0f;
			}
		}
		return s;
	}
}
