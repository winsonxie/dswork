package dswork.flow.util;

import java.util.Map;

public class SvgUtil
{
	private static String quot = "\"";
	private static String nbsp = " ";
	private static String eq = "=";
	private static String lt = "<";
	private static String gt = ">";
	private static String ltf = "</";
	private static String fgt = "/>";
	
	/**
	 * 拼接属性
	 * @param sb StringBuilder
	 * @param name 属性名
	 * @param value 属性值
	 */
	private static void attr(StringBuilder sb, String name, Object value)
	{
		sb.append(nbsp).append(name).append(eq).append(quot).append(value).append(quot);
	}
	
	/**
	 * 画矩形
	 * @param x 左上角x坐标
	 * @param y 左上角y坐标
	 * @param width 宽
	 * @param height 高
	 * @param stroke 边框颜色
	 * @param strokeWidth 边框粗细
	 * @param fill 填充颜色
	 * @return svg
	 */
	public static String getRect(int x, int y, int width, int height, String stroke, int strokeWidth, String fill)
	{
		StringBuilder sb = new StringBuilder(128);
		sb.append(lt).append("rect");
		attr(sb, "x", x);
		attr(sb, "y", y);
		attr(sb, "width", width);
		attr(sb, "height", height);
		attr(sb, "stroke", stroke);
		attr(sb, "stroke-width", strokeWidth);
		attr(sb, "fill", fill);
		sb.append(fgt);
		return sb.toString();
	}
	
	/**
	 * 画多行文本
	 * @param text
	 * @param x 左上角x坐标
	 * @param y 左上角y坐标
	 * @param width 宽
	 * @param height 高
	 * @param fontSize 字体大小
	 * @return svg
	 */
	public static String getTextMult(String text, int x, int y, int width, int height, int fontSize)
	{
		fontSize = fontSize < 10 ? 10 : fontSize;
		double len =  Math.floor(width/fontSize);// 靠0取整
		int n = (int)Math.ceil(text.length()/len);// 靠无穷大取整
		String ss[] = new String[n];
		int l = (int)len;
		for(int i = 0; i < n - 1; i++)
		{
			ss[i] = text.substring(i * l, (i + 1) * l);
		}
		ss[n - 1] = text.substring((n - 1) * l);
		StringBuilder sb = new StringBuilder(128);
		if(ss.length > 0)
		{
			int _x = (width - ss[0].length() * fontSize) / 2;
			int _y = (height - ss.length * fontSize) / 2;
			x = _x > 0 ? x + _x : x;
			y = _y > 0 ? y + _y : y;
			sb.append(lt).append("text");
			attr(sb, "x", x);
			attr(sb, "y", y);
			attr(sb, "font-size", fontSize);
			sb.append(gt);
			for(int i = 0; i < ss.length; i++)
			{
				sb.append(lt).append("tspan");
				attr(sb, "text-anchor", "left");
				attr(sb, "x", x);
				attr(sb, "dy", fontSize);
				sb.append(gt).append(ss[i]).append(ltf).append("tspan").append(gt);
			}
			sb.append(ltf).append("text").append(gt);
		}
		return sb.toString();
	}

	/**
	 * 画单行文本
	 * @param text 文本
	 * @param x 左上角x坐标
	 * @param y 左上角y坐标
	 * @param fontSize 字体大小
	 * @return svg
	 */
	public static String getText(String text, int x, int y, int fontSize)
	{
		StringBuilder sb = new StringBuilder(64);
		sb.append(lt).append("text");
		attr(sb, "x", x);
		attr(sb, "y", y);
		attr(sb, "dy", fontSize);
		sb.append(gt).append(text).append(ltf).append("text").append(gt);
		return sb.toString();
	}
	
	/**
	 * 画圆
	 * @param cx 圆心x
	 * @param cy 圆心y
	 * @param r 半径
	 * @param stroke 周长颜色
	 * @param strokeWidth 周长粗细
	 * @param fill 填充颜色
	 * @return svg
	 */
	public static String getCircle(int cx, int cy, int r, String stroke, int strokeWidth, String fill)
	{
		StringBuilder sb = new StringBuilder(128);
		sb.append(lt).append("circle");
		attr(sb, "cx", cx);
		attr(sb, "cy", cy);
		attr(sb, "r", r);
		attr(sb, "stroke", stroke);
		attr(sb, "stroke-width", strokeWidth);
		attr(sb, "fill", fill);
		sb.append(fgt);
		return sb.toString();
	}
	
	

	/**
	 * 画点
	 * @param cx 圆心x
	 * @param cy 圆心y
	 * @param r 半径
	 * @param fill 填充颜色
	 * @param opacity 透明度
	 * @return svg
	 */
	public static String getPoint(int cx, int cy, int r, String fill, double opacity)
	{
		StringBuilder sb = new StringBuilder(128);
		sb.append(lt).append("circle");
		attr(sb, "cx", cx);
		attr(sb, "cy", cy);
		attr(sb, "r", r);
		attr(sb, "fill", fill);
		attr(sb, "opacity", opacity);
		sb.append(fgt);
		return sb.toString();
	}
	
	/**
	 * 画折线
	 * @param fill
	 * @param fillOpacity
	 * @param stroke
	 * @param strokeWidth
	 * @param points
	 * @return
	 */
	public static String getPolyline(String fill, double fillOpacity, String stroke, int strokeWidth, String points)
	{
		StringBuilder sb = new StringBuilder(128);
		sb.append(lt).append("polyline");
		attr(sb, "fill", fill);
		attr(sb, "fill-opacity", fillOpacity);
		attr(sb, "stroke", stroke);
		attr(sb, "stroke-width", strokeWidth);
		attr(sb, "points", points);
		sb.append(fgt);
		return sb.toString();
	}
	
	/**
	 * 画多边形
	 * @param fill
	 * @param points
	 * @return
	 */
	public static String getPolygon(String fill, String points)
	{
		StringBuilder sb = new StringBuilder(64);
		sb.append(lt).append("polygon");
		attr(sb, "fill", fill);
		attr(sb, "points", points);
		sb.append(fgt);
		return sb.toString();
	}
	
	/**
	 * 生成xml标签
	 * @param tagName 标签名
	 * @param attributes 属性集合
	 * @param nodeContent 标签值
	 * @return xml
	 */
	public static String getTag(String tagName, Map<String, String> attributes, String nodeContent)
	{
		StringBuilder sb = new StringBuilder(128);
		sb.append(lt).append(tagName);
		for(Map.Entry<String, String> entry : attributes.entrySet())
		{
			attr(sb, entry.getKey(), entry.getValue());
		}
		if(nodeContent == null)
		{
			sb.append(fgt);
		}
		else
		{
			sb.append(gt).append(nodeContent).append(ltf).append(tagName).append(gt);
		}
		return sb.toString();
	}
}
