package dswork.core.util;

import java.io.*;
import java.util.Locale;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class ImageUtil
{

	/**
	 * 等比例图片压缩处理
	 * @param filePath 文件路径
	 * @param width 缩放目标最大宽，为0则不缩放
	 * @param height 缩放目标最大高，为0则不缩放
	 * @return byte[]
	 */
	public static byte[] resize(String filePath, int width, int height)
	{
		try
		{
			return resize(new FileInputStream(new File(filePath)), width, height);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * 等比例图片压缩处理
	 * @param inStream 图片流
	 * @param width 缩放目标最大宽，为0则不缩放
	 * @param height 缩放目标最大高，为0则不缩放
	 * @return byte[]
	 */
	public static byte[] resize(InputStream inStream, int width, int height)
	{
		return resize(inStream, width, height, "jpg");
	}
	
	/**
	 * 等比例图片压缩处理
	 * @param inStream 图片流
	 * @param width 缩放目标最大宽，为0则不缩放
	 * @param height 缩放目标最大高，为0则不缩放
	 * @param format png,jpg,gif,bmp,wbmp
	 * @return byte[]
	 */
	public static byte[] resize(InputStream inStream, int width, int height, String format)
	{
		try
		{
			format = String.valueOf(format).toLowerCase(Locale.ENGLISH);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			Image img = ImageIO.read(inStream);// 构造Image对象
			int w = img.getWidth(null);// 源图宽
			int h = img.getHeight(null);// 源图高
			if(width > 0 && height > 0)
			{
				if(w > width || h > height)
				{
					if((w * 1f / h) >= (width * 1f / height))
					{
						height = h * width / w;// 缩放高
					}
					else
					{
						width = w * height / h;// 缩放宽
					}
				}
				else
				{
					// 不需要压缩
					width = w;
					height = h;
				}
			}
			else
			{
				// 不压缩
				width = w;
				height = h;
			}
			// img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);// 直接压缩至指定比例
			// BufferedImage.TYPE_INT_RGB
			// BufferedImage.SCALE_SMOOTH 的
			
			BufferedImage image;
			if("png".equals(format) || "gif".equals(format))
			{
				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);// 保留透明度
			}
			else
			{
				image = new BufferedImage(width, height, BufferedImage.SCALE_SMOOTH);
			}
			image.getGraphics().drawImage(img, 0, 0, width, height, null); // 绘制缩小后的图
			
			ImageIO.write(image, format, outStream);
			// 可以正常实现bmp、png、gif转jpg
			// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outStream);
			// encoder.encode(image);
			// JPEGCodec.createJPEGEncoder(outStream).encode(image);// JPEG编码
			
			// com.sun.imageio.plugins.jpeg.JPEGImageWriter imageWriter  =  (com.sun.imageio.plugins.jpeg.JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpg").next();
			// javax.imageio.stream.ImageOutputStream ios  =  ImageIO.createImageOutputStream(outStream);
			// imageWriter.setOutput(ios);
			// javax.imageio.metadata.IIOMetadata imageMetaData  =  imageWriter.getDefaultImageMetadata(new javax.imageio.ImageTypeSpecifier(image), null);
			// imageWriter.write(imageMetaData, new javax.imageio.IIOImage(image, null, null), null);
			// ios.close();
			// imageWriter.dispose();
			return outStream.toByteArray();
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
//	/**
//	 * 等比例图片压缩处理
//	 * @param inStream 图片流
//	 * @param width 缩放目标最大宽，为0则不缩放
//	 * @param height 缩放目标最大高，为0则不缩放
//	 * @return byte[]
//	 */
//	@SuppressWarnings("restriction")
//	public static byte[] formatToBMP(InputStream inStream, int width, int height)
//	{
//		try
//		{
//			Image img = ImageIO.read(inStream);// 构造Image对象
//			int w = img.getWidth(null);// 源图宽
//			int h = img.getHeight(null);// 源图高
//			if(width > 0 && height > 0)
//			{
//				if(w > width || h > height)
//				{
//					if((w * 1f / h) >= (width * 1f / height))
//					{
//						height = h * width / w;// 缩放高
//					}
//					else
//					{
//						width = w * height / h;// 缩放宽
//					}
//				}
//				else
//				{
//					// 不需要压缩
//					width = w;
//					height = h;
//				}
//			}
//			else
//			{
//				// 不压缩
//				width = w;
//				height = h;
//			}
//			// img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);// 直接压缩至指定比例
//			// BufferedImage.TYPE_INT_RGB
//			// BufferedImage.SCALE_SMOOTH 的缩略算法生成缩略图片的平滑度的优先级比速度高 生成的图片质量比较好,但速度慢
//			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//			image.getGraphics().drawImage(img, 0, 0, width, height, null); // 绘制缩小后的图
//			
//			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//			com.sun.imageio.plugins.bmp.BMPImageWriter imageWriter  =  (com.sun.imageio.plugins.bmp.BMPImageWriter) ImageIO.getImageWritersBySuffix("bmp").next();
//			javax.imageio.stream.ImageOutputStream ios  =  ImageIO.createImageOutputStream(outStream);
//			imageWriter.setOutput(ios);
//			javax.imageio.metadata.IIOMetadata imageMetaData  =  imageWriter.getDefaultImageMetadata(new javax.imageio.ImageTypeSpecifier(image), null);
//			imageWriter.write(imageMetaData, new javax.imageio.IIOImage(image, null, null), null);
//			ios.close();
//			imageWriter.dispose();
//			return outStream.toByteArray();
//			
//			// img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);// 直接压缩至指定比例
//			// BufferedImage.TYPE_INT_RGBBufferedImage.TYPE_INT_RGB：8 位 RGB 颜色分量，不带alpha通道。
//			// BufferedImage.TYPE_INT_ARGB：8 位 RGBA 颜色分量，带alpha通道。
//			// BufferedImage.TYPE_INT_ARGB_PRE：8 位 RGBA 颜色分量，已预乘以 alpha。
//			// BufferedImage.TYPE_INT_BGR：8 位 RGB 颜色分量Windows 或 Solaris 风格的图像，不带alpha通道。
//			// BufferedImage.TYPE_3BYTE_BGR：8位GBA颜色分量，用3字节存储Blue、Green和Red三种颜色，不存在alpha。
//			// BufferedImage.TYPE_4BYTE_ABGR：8位RGBA颜色分量，用3字节存储Blue、Green和Red三种颜色以及1字节alpha。
//			// BufferedImage.TYPE_4BYTE_ABGR_PRE：具有用3字节存储的Blue、Green和Red三种颜色以及1字节alpha。
//			// BufferedImage.TYPE_USHORT_565_RGB：具有5-6-5RGB颜色分量（5位Red、6位Green、5位Blue）的图像，不带alpha。// ?16位bmp
//			// BufferedImage.TYPE_USHORT_555_RGB：具有5-5-5RGB颜色分量（5位Red、5位Green、5位Blue）的图像，不带alpha。
//			// BufferedImage.TYPE_BYTE_GRAY：表示无符号byte灰度级图像（无索引）。
//			// BufferedImage.TYPE_USHORT_GRAY：表示一个无符号short 灰度级图像（无索引）。
//			// BufferedImage.TYPE_BYTE_BINARY：表示一个不透明的以字节打包的 1、2 或 4 位图像。
//			// BufferedImage.TYPE_BYTE_INDEXED：表示带索引的字节图像。
//			//BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
//			//image.getGraphics().drawImage(img, 0, 0, width, height, null); // 绘制缩小后的图
//			// 可以正常实现bmp、png、gif转jpg
//			// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outStream);
//			// encoder.encode(image);
//			//JPEGCodec.createJPEGEncoder(outStream).encode(image);// JPEG编码
//			
//			// com.sun.imageio.plugins.jpeg.JPEGImageWriter imageWriter  =  (com.sun.imageio.plugins.jpeg.JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpg").next();
//			// javax.imageio.stream.ImageOutputStream ios  =  ImageIO.createImageOutputStream(outStream);
//			// imageWriter.setOutput(ios);
//			// javax.imageio.metadata.IIOMetadata imageMetaData  =  imageWriter.getDefaultImageMetadata(new javax.imageio.ImageTypeSpecifier(image), null);
//			// imageWriter.write(imageMetaData, new javax.imageio.IIOImage(image, null, null), null);
//			// ios.close();
//			// imageWriter.dispose();
//			//return outStream.toByteArray();
//		}
//		catch(Exception e)
//		{
//		}
//		return null;
//	}
}
