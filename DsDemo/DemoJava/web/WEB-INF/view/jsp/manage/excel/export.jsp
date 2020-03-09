<%@page language="java" pageEncoding="UTF-8" import="org.apache.poi.hssf.usermodel.*,org.apache.poi.ss.usermodel.*,org.apache.poi.xssf.usermodel.*,
java.io.ByteArrayInputStream,java.io.InputStream,java.io.OutputStream"%><%
java.util.List<testwork.model.Demo> list = (java.util.List<testwork.model.Demo>)request.getAttribute("list");
String filename = "导出";
response.setContentType("application/vnd.ms-excel");
response.addHeader("Content-Disposition", "attachment;filename=\"" + (new String(filename.getBytes("GBK"), "ISO-8859-1")) + ".xls\"");
OutputStream fos = response.getOutputStream();
HSSFWorkbook wb = new HSSFWorkbook();
HSSFSheet sheet = wb.createSheet();
HSSFFont fontTitle = wb.createFont();// 创建标题字体，设置其为黑色、粗体：
fontTitle.setBold(true);
HSSFCellStyle cellStyleTitle = wb.createCellStyle();// 创建标题格式
cellStyleTitle.setFont(fontTitle);
cellStyleTitle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
cellStyleTitle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);

int titleRow = -1;
wb.setSheetName(0, "sheet1");// 设置sheet名称
HSSFRow row = null;
HSSFCell cell;
int colNum = 0;// 设置excel的位置

titleRow++;
row = sheet.createRow(titleRow);
//new Region(开始行, 开始列, 结束行, 结束列);
//new CellRangeAddress(开始行, 结束行, 开始列, 结束列);
sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 2));
cell = row.createCell(colNum++, HSSFCell.CELL_TYPE_STRING);
cell.setCellStyle(cellStyleTitle);
cell.setCellValue("数据导出");

if(true)
{
	titleRow++;
	row = sheet.createRow(titleRow);
	sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 1, 2));
	colNum = 0;// 复位,设置excel的位置
	cell = row.createCell(colNum++, HSSFCell.CELL_TYPE_STRING);
	cell.setCellStyle(cellStyleTitle);
	cell.setCellValue("主要信息");
	
	cell = row.createCell(colNum++, HSSFCell.CELL_TYPE_STRING);
	cell.setCellStyle(cellStyleTitle);
	cell.setCellValue("其它信息");
	
	cell = row.createCell(colNum++, HSSFCell.CELL_TYPE_STRING);
	cell.setCellStyle(cellStyleTitle);
	cell.setCellValue("测试信息1");// 这个列被合并了，并不会显示
	
	cell = row.createCell(colNum++, HSSFCell.CELL_TYPE_STRING);
	cell.setCellStyle(cellStyleTitle);
	cell.setCellValue("测试信息2");
}

if(true)
{
	titleRow++;
	row = sheet.createRow(titleRow);
	colNum = 0;// 复位,设置excel的位置
	row.createCell(colNum++, HSSFCell.CELL_TYPE_STRING).setCellValue("标题");
	row.createCell(colNum++, HSSFCell.CELL_TYPE_STRING).setCellValue("内容");
	row.createCell(colNum++, HSSFCell.CELL_TYPE_STRING).setCellValue("创建时间");
}
for(int i = 0; i < list.size(); i++)
{
	colNum = 0;// 复位,设置excel的位置
	titleRow++;
	row = sheet.createRow(titleRow);
	testwork.model.Demo d = list.get(i);
	row.createCell(colNum++, HSSFCell.CELL_TYPE_STRING).setCellValue(d.getTitle());
	row.createCell(colNum++, HSSFCell.CELL_TYPE_STRING).setCellValue(d.getContent());
	row.createCell(colNum++, HSSFCell.CELL_TYPE_STRING).setCellValue(d.getFoundtime());
}

wb.write(fos);
wb.close();
fos.close();
%>