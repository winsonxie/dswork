package dswork.util;

import java.io.FileInputStream;
import java.io.InputStream;

import dswork.core.util.FileUtil;
import dswork.core.util.ImageUtil;

public class ImageTest
{
	public static void main(String[] args)
	{
		String format = "bmp";
		try
		{
			String sfile = "C:\\Users\\skey\\Desktop\\update\\z." + format;
			String ofile = "C:\\Users\\skey\\Desktop\\update\\x." + format;
			InputStream in = new FileInputStream(sfile);
			byte[] arr = ImageUtil.resize(in, 800, 2000, format);
			FileUtil.writeFile(ofile, FileUtil.getToInputStream(arr), true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
