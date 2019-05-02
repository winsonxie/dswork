package dswork.ee;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.Jar;
import org.apache.tomcat.JarScanFilter;
import org.apache.tomcat.JarScanType;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.buf.UriUtil;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.util.scan.Constants;
import org.apache.tomcat.util.scan.JarFactory;
import org.apache.tomcat.util.scan.StandardJarScanFilter;

public class EmbededStandardJarScanner implements JarScanner
{
	private static final Log log = LogFactory.getLog(EmbededStandardJarScanner.class);
	private static final StringManager sm = StringManager.getManager(Constants.Package);
	private boolean scanClassPath = true;

	public EmbededStandardJarScanner()
	{
		StandardJarScanFilter f = new StandardJarScanFilter();
		f.setDefaultTldScan(false);
		f.setTldScan("jstl-1.2.jar");
		jarScanFilter = f;
	}
	public boolean isScanClassPath()
	{
		return scanClassPath;
	}

	public void setScanClassPath(boolean scanClassPath)
	{
		this.scanClassPath = scanClassPath;
	}
	private boolean scanAllFiles = true;

	public boolean isScanAllFiles()
	{
		return scanAllFiles;
	}

	public void setScanAllFiles(boolean scanAllFiles)
	{
		this.scanAllFiles = scanAllFiles;
	}
	private boolean scanAllDirectories = false;

	public boolean isScanAllDirectories()
	{
		return scanAllDirectories;
	}

	public void setScanAllDirectories(boolean scanAllDirectories)
	{
		this.scanAllDirectories = scanAllDirectories;
	}
	private boolean scanBootstrapClassPath = false;

	public boolean isScanBootstrapClassPath()
	{
		return scanBootstrapClassPath;
	}

	public void setScanBootstrapClassPath(boolean scanBootstrapClassPath)
	{
		this.scanBootstrapClassPath = scanBootstrapClassPath;
	}
	private JarScanFilter jarScanFilter;

	@Override
	public JarScanFilter getJarScanFilter()
	{
		return jarScanFilter;
	}

	@Override
	public void setJarScanFilter(JarScanFilter jarScanFilter)
	{
		this.jarScanFilter = jarScanFilter;
	}

	@Override
	public void scan(JarScanType scanType, ServletContext context, JarScannerCallback callback)
	{
		if(log.isTraceEnabled())
		{
			log.trace(sm.getString("jarScan.webinflibStart"));
		}
		Set<URL> processedURLs = new HashSet<>();
		// Scan WEB-INF/lib
		Set<String> dirList = context.getResourcePaths(Constants.WEB_INF_LIB);
		if(dirList != null)
		{
			Iterator<String> it = dirList.iterator();
			while(it.hasNext())
			{
				String path = it.next();
				if(path.endsWith(Constants.JAR_EXT) && getJarScanFilter().check(scanType, path.substring(path.lastIndexOf('/') + 1)))
				{
					if(log.isDebugEnabled())
					{
						log.debug(sm.getString("jarScan.webinflibJarScan", path));
					}
					URL url = null;
					try
					{
						url = context.getResource(path);
						processedURLs.add(url);
						process(scanType, callback, url, path, true, null);
					}
					catch(IOException e)
					{
						log.warn(sm.getString("jarScan.webinflibFail", url), e);
					}
				}
				else
				{
					if(log.isTraceEnabled())
					{
						log.trace(sm.getString("jarScan.webinflibJarNoScan", path));
					}
				}
			}
		}
		// Scan WEB-INF/classes
		try
		{
			URL webInfURL = context.getResource(Constants.WEB_INF_CLASSES);// "/WEB-INF/classes"
			if(webInfURL != null)
			{
				processedURLs.add(webInfURL);
				if(isScanAllDirectories())
				{
					URL url = context.getResource(Constants.WEB_INF_CLASSES + "/META-INF");
					if(url != null)
					{
						try
						{
							callback.scanWebInfClasses();
						}
						catch(IOException e)
						{
							log.warn(sm.getString("jarScan.webinfclassesFail"), e);
						}
					}
				}
			}
		}
		catch(MalformedURLException e)
		{
		}
		// Scan the classpath
		if(isScanClassPath())
		{
			if(log.isTraceEnabled())
			{
				log.trace(sm.getString("jarScan.classloaderStart"));
			}
			ClassLoader stopLoader = null;
			if(!isScanBootstrapClassPath())
			{
				stopLoader = ClassLoader.getSystemClassLoader().getParent();
			}
			ClassLoader classLoader = context.getClassLoader();
			// if(classLoader.getParent() != null)
			// {
			// stopLoader = classLoader.getParent().getParent();
			// }
			boolean isWebapp = true;
			while(classLoader != null && classLoader != stopLoader)
			{
				if(classLoader instanceof URLClassLoader)
				{
					URL[] urls = ((URLClassLoader) classLoader).getURLs();
					for(int i = 0; i < urls.length; i++)
					{
						if(processedURLs.contains(urls[i]))
						{
							// Skip this URL it has already been processed
							continue;
						}
						ClassPathEntry cpe = new ClassPathEntry(urls[i]);
						if((cpe.isJar() || scanType == JarScanType.PLUGGABILITY || isScanAllDirectories()) && getJarScanFilter().check(scanType, cpe.getName()))
						{
							if(log.isDebugEnabled())
							{
								log.debug(sm.getString("jarScan.classloaderJarScan", urls[i]));
							}
							try
							{
								process(scanType, callback, urls[i], null, isWebapp, null);
							}
							catch(IOException ioe)
							{
								log.warn(sm.getString("jarScan.classloaderFail", urls[i]), ioe);
							}
						}
						else
						{
							if(log.isTraceEnabled())
							{
								log.trace(sm.getString("jarScan.classloaderJarNoScan", urls[i]));
							}
						}
					}
				}
				classLoader = classLoader.getParent();
			}
		}
	}

	private void process(JarScanType scanType, JarScannerCallback callback, URL url, String webappPath, boolean isWebapp, Deque<URL> classPathUrlsToProcess) throws IOException
	{
		if(log.isTraceEnabled())
		{
			log.trace(sm.getString("jarScan.jarUrlStart", url));
		}
		String urlStr = url.toString();
		System.err.println("===scan:" + urlStr);// 为了变红而已
		if("jar".equals(url.getProtocol()) || url.getPath().endsWith(Constants.JAR_EXT))
		{
			try (Jar jar = JarFactory.newInstance(url))
			{
				callback.scan(jar, webappPath, isWebapp);
			}
		}
		else if("file".equals(url.getProtocol()))
		{
			File f;
			try
			{
				f = new File(url.toURI());
				if(f.isFile() && isScanAllFiles())
				{
					URL jarURL = UriUtil.buildJarUrl(f);
					try (Jar jar = JarFactory.newInstance(jarURL))
					{
						callback.scan(jar, webappPath, isWebapp);
					}
				}
				else if(f.isDirectory())
				{
					if(scanType == JarScanType.PLUGGABILITY)
					{
						callback.scan(f, webappPath, isWebapp);
					}
					else
					{
						File metainf = new File(f.getAbsoluteFile() + File.separator + "META-INF");
						if(metainf.isDirectory())
						{
							callback.scan(f, webappPath, isWebapp);
						}
					}
				}
			}
			catch(Throwable t)
			{
				ExceptionUtils.handleThrowable(t);
				IOException ioe = new IOException();
				ioe.initCause(t);
				throw ioe;
			}
		}
	}

	private static class ClassPathEntry
	{
		private final boolean jar;
		private final String name;

		public ClassPathEntry(URL url)
		{
			String path = url.getPath();
			int end = path.indexOf(Constants.JAR_EXT);
			if(end != -1)
			{
				jar = true;
				int start = path.lastIndexOf('/', end);
				name = path.substring(start + 1, end + 4);
			}
			else
			{
				jar = false;
				if(path.endsWith("/"))
				{
					path = path.substring(0, path.length() - 1);
				}
				int start = path.lastIndexOf('/');
				name = path.substring(start + 1);
			}
		}

		public boolean isJar()
		{
			return jar;
		}

		public String getName()
		{
			return name;
		}
	}
}
