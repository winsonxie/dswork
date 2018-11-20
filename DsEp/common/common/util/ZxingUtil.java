package common.util;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public final class ZxingUtil
{
	private ZxingUtil()
	{
	}
	
	public static ByteArrayOutputStream generate(String content, int width, int height, InputStream logoInputStream)
	{
		return generate(content, width, height, logoInputStream, LOGO.DEFAULT);
	}

	public static ByteArrayOutputStream generate(String content, int width, int height, InputStream logoInputStream, LOGO logoLevel)
	{
		try
		{
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			gen(content, width, height, outStream, logoInputStream, logoLevel);
			return outStream;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public static void generate(String content, int width, int height, InputStream logoInputStream, OutputStream outputStream) throws Exception
	{
		gen(content, width, height, outputStream, logoInputStream, LOGO.DEFAULT);
	}

	public static void generate(String content, int width, int height, InputStream logoInputStream, LOGO logoLevel, OutputStream outputStream) throws Exception
	{
		gen(content, width, height, outputStream, logoInputStream, logoLevel);
	}
	
	/**
	 * 失败返回null
	 * @param content 需要转化的值
	 * @param width 二维码宽度
	 * @param height 二维码高度
	 * @param ext 图片格式(PNG,JPG)
	 * @param logoInputStream logo图片二进制流
	 * @param logoLevel(LOW,DEFAULT,HIGH)
	 * @return
	 */
	private static void gen(String content, int width, int height, OutputStream outputStream, InputStream logoInputStream, LOGO logoLevel) throws Exception
	{
		if(logoLevel == null)
		{
			logoLevel = LOGO.DEFAULT;
		}
		if(width <= 0)
		{
			width = 256;
		}
		if(height <= 0)
		{
			height = 256;
		}
		int logoWidth = width*24/100;
		int logoHeight = height*24/100;
		switch(logoLevel)
		{
			// case DEFAULT:break;
			case DEFAULT:// 0.25 + 0.03125
				break;
			case LOW:// 0.125 + 0.03125
				logoWidth = width*18/100;
				logoHeight = height*18/100;
				break;
			case HIGH:// 0.3 + 0.025
				logoWidth = width*3/10;
				logoHeight = height*3/10;
				break;
		}
		int paddingWidth = width*3/100;
		int paddingHeight = height*3/100;
		BufferedImage image = genImageForQR(content, width, height, logoLevel);// 生成二维码
		if(logoInputStream != null)
		{
			// 为logo增加一个白边画布
			int w = logoWidth + paddingWidth;
			int h = logoHeight + paddingHeight;
			Image draw = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics g = draw.getGraphics();// g.setColor(new java.awt.Color(255, 255, 255));
			g.fillRect(0, 0, w, h);
			
			// 在画布上画上logo
			Image logo = ImageIO.read(logoInputStream);
			// g.drawImage(logo, (paddingWidth / 2), (paddingHeight / 2), logoWidth, logoHeight, null);// 拉伸放入

			logo = resize(logo, logoWidth, logoHeight);
			g.drawImage(logo, 
				(paddingWidth / 2 + (logoWidth - logo.getWidth(null)) / 2),
				(paddingHeight / 2 + (logoHeight - logo.getHeight(null)) / 2),
				logo.getWidth(null), 
				logo.getHeight(null), 
				null);// 缩放放入，按比例放入logo
			
			g.dispose();// 结束logo画布
			
			// 在二维码上贴上logo画布
			g = image.createGraphics();
			g.drawImage(draw, ((width - logoWidth) / 2), ((height - logoHeight) / 2), logoWidth, logoHeight, null);// 放入logo
			g.dispose();
		}
		
		// 二维码转化为byte数组
		ImageIO.write(image, PNG, outputStream);
	}



	/**
	 * logo+logo白边
	 * LOW ~15%(12.5%+2.5%)
	 * DEFAULT ~25%(20%+5%)
	 * HIGH ~30%(25%+5%)
	 * @author skey
	 *
	 */
	public static enum LOGO
	{
		DEFAULT,
		LOW,
		HIGH
	}
	
	private static final String PNG = "png";
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	private static BufferedImage genImageForQR(String content, int width, int height, LOGO logoLevel) throws Exception
	{
		Map<com.google.zxing.EncodeHintType, Object> map = new HashMap<com.google.zxing.EncodeHintType, Object>();
		map.put(com.google.zxing.EncodeHintType.MARGIN, 1);// 二维码白边最小
		if(logoLevel != null)
		{
			switch(logoLevel)
			{
				case DEFAULT:
					map.put(com.google.zxing.EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
					break;
				case LOW:
					map.put(com.google.zxing.EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
					break;
				case HIGH:
					map.put(com.google.zxing.EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
					break;
			}
		}
		// else
		// {
		// 	map.put(com.google.zxing.EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// }
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		BitMatrix matrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, map);
		BufferedImage image = new BufferedImage(matrix.getWidth(), matrix.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
	
	private static Image resize(Image img, int width, int height)
	{
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
				img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			}
		}
		return img;
	}
}
