<%@page language="java" pageEncoding="UTF-8" import="org.apache.poi.ss.usermodel.*,
java.io.ByteArrayInputStream,java.io.InputStream,java.io.OutputStream"%><%
// org.apache.poi.hssf.usermodel.*,
// org.apache.poi.xssf.usermodel.*,
// org.apache.poi.ss.usermodel.*,
java.util.List<testwork.model.Demo> list = (java.util.List<testwork.model.Demo>)request.getAttribute("list");
String filename = "导出";
response.setContentType("application/vnd.ms-excel");
response.addHeader("Content-Disposition", "attachment;filename=\"" + (new String(filename.getBytes("GBK"), "ISO-8859-1")) + ".xlsx\"");
OutputStream fos = response.getOutputStream();
Workbook wb = new org.apache.poi.xssf.streaming.SXSSFWorkbook(500);// xlsx||org.apache.poi.hssf.usermodel.HSSFWorkbook();// xls
Sheet sheet = wb.createSheet();
Font fontTitle = wb.createFont();// 创建标题字体，设置其为黑色、粗体：
fontTitle.setBold(true);
CellStyle cellStyleTitle = wb.createCellStyle();// 创建标题格式
cellStyleTitle.setFont(fontTitle);
cellStyleTitle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
cellStyleTitle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);

int titleRow = -1;
wb.setSheetName(0, "sheet1");// 设置sheet名称
Row row = null;
Cell cell;
int colNum = 0;// 设置excel的位置

int fw = 256;
sheet.setColumnWidth(0, 20*fw);
sheet.setColumnWidth(1, 35*fw);
sheet.setColumnWidth(2, 19*fw);
sheet.setColumnWidth(3, 10*fw);

titleRow++;
row = sheet.createRow(titleRow);
//new Region(开始行, 开始列, 结束行, 结束列);
//new CellRangeAddress(开始行, 结束行, 开始列, 结束列);
sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 3));
cell = row.createCell(colNum++, Cell.CELL_TYPE_STRING);
cell.setCellStyle(cellStyleTitle);
cell.setCellValue("数据导出");

if(true)
{
	titleRow++;
	row = sheet.createRow(titleRow);
	sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 1, 2));
	colNum = 0;// 复位,设置excel的位置
	cell = row.createCell(colNum++, Cell.CELL_TYPE_STRING);
	cell.setCellStyle(cellStyleTitle);
	cell.setCellValue("主要信息");
	
	cell = row.createCell(colNum++, Cell.CELL_TYPE_STRING);
	cell.setCellStyle(cellStyleTitle);
	cell.setCellValue("其它信息");
	
	cell = row.createCell(colNum++, Cell.CELL_TYPE_STRING);
	cell.setCellStyle(cellStyleTitle);
	cell.setCellValue("测试信息1");// 这个列被合并了，并不会显示
	
	cell = row.createCell(colNum++, Cell.CELL_TYPE_STRING);
	cell.setCellStyle(cellStyleTitle);
	cell.setCellValue("扩展信息");
}

if(true)
{
	titleRow++;
	row = sheet.createRow(titleRow);
	colNum = 0;// 复位,设置excel的位置
	row.createCell(colNum++, Cell.CELL_TYPE_STRING).setCellValue("标题");
	row.createCell(colNum++, Cell.CELL_TYPE_STRING).setCellValue("内容");
	row.createCell(colNum++, Cell.CELL_TYPE_STRING).setCellValue("创建时间");
	row.createCell(colNum++, Cell.CELL_TYPE_STRING).setCellValue("序号");
}
for(int i = 0; i < list.size(); i++)
{
	colNum = 0;// 复位,设置excel的位置
	titleRow++;
	row = sheet.createRow(titleRow);
	testwork.model.Demo d = list.get(i);
	row.createCell(colNum++, Cell.CELL_TYPE_STRING).setCellValue(d.getTitle());
	row.createCell(colNum++, Cell.CELL_TYPE_STRING).setCellValue(d.getContent());
	row.createCell(colNum++, Cell.CELL_TYPE_STRING).setCellValue(d.getFoundtime());
	row.createCell(colNum++, Cell.CELL_TYPE_STRING).setCellValue(i+1);
}


if(true)
{
	Font ft = wb.createFont();// 创建标题字体，设置其为黑色、粗体：
	ft.setBold(true);
	CellStyle cst = wb.createCellStyle();// 创建标题格式
	cst.setFont(ft);
	
	titleRow++;
	row = sheet.createRow(titleRow);
	
	cell = row.createCell(2, Cell.CELL_TYPE_STRING);
	cell.setCellStyle(cst);
	cell.setCellValue("合计：");
	
	
	colNum = 3;// 下标，从0开始
	String colstr = "";
	colstr = org.apache.poi.hssf.util.CellReference.convertNumToColString(colNum);// D列
	
	cell = row.createCell(colNum, Cell.CELL_TYPE_STRING);
	cst = wb.createCellStyle();// 创建标题格式
	ft = wb.createFont();// 创建标题字体，设置其为黑色、粗体：
	ft.setBold(true);
	ft.setColor(org.apache.poi.ss.usermodel.IndexedColors.YELLOW.getIndex());// 字体颜色
	
	cst = wb.createCellStyle();
	cst.setFont(ft);
	
	// 填充色
	cst.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.BLUE.getIndex());
	cst.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	
	cst.setFillBackgroundColor(org.apache.poi.ss.usermodel.IndexedColors.BLACK.getIndex());
	
	cell.setCellStyle(cst);
	cell.setCellFormula("SUM(" + colstr + "4:" + colstr + titleRow + ")");// 公式中的行列从1开始
}

if(true)
{
	short j = 1;
	int i = 0;
	
	short b = org.apache.poi.ss.usermodel.IndexedColors.BLACK.getIndex();
	short w = org.apache.poi.ss.usermodel.IndexedColors.WHITE.getIndex();
	for(; i < 81; i++, j++)
	{
		short k = new Integer(i%8).shortValue();
		if(k == 0)
		{
			titleRow++;
			row = sheet.createRow(titleRow);
			colNum = 4;
		}
		cell = row.createCell(colNum++, Cell.CELL_TYPE_STRING);

		Font ft = wb.createFont();// 创建标题字体，设置其为黑色、粗体：
		ft.setBold(true);
		if(j == 2 || j == 4 || j == 8 || j == 10 || j == 12 || j == 16 || j == 17 || j == 18 || j == 19 || j == 20 || j == 21 || j == 23 || j == 25 || j == 28 || j == 30 || j == 32 || j == 36 || j == 37 || j == 38 || j == 39 || j == 48 || j == 56 || j == 58 || j == 59 || j == 60 || j == 61 || j == 62 || j == 63 || j == 64 || j == 66 || j == 68 || j == 71 || j == 72 || j == 74 || j == 76 || j == 77 || j == 79 || j == 81)
		{
			ft.setColor(w);// 字体颜色
		}
		else
		{
			ft.setColor(b);// 字体颜色
		}
		
		CellStyle cst = wb.createCellStyle();// 创建标题格式
		cst.setFont(ft);
		// 填充色
		cst.setFillForegroundColor(j);
		cst.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		// 边框
		cst.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
		cst.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
		cst.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
		cst.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
		
		cell.setCellStyle(cst);
		cell.setCellValue("S" + j);
	}
}

wb.write(fos);
wb.close();
fos.close();
%>