package dswork.ee;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.ContextConfig;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.descriptor.web.WebXml;
import org.apache.tomcat.Jar;
import org.apache.tomcat.util.scan.JarFactory;

public class EmbedContextConfig extends ContextConfig
{
	private static final Log log = LogFactory.getLog(EmbedContextConfig.class);

	@Override
	protected void processResourceJARs(Set<WebXml> fragments)
	{
		for(WebXml fragment : fragments)
		{
			URL url = fragment.getURL();
			try
			{
				String urlString = url.toString();
				if(isInsideNestedJar(urlString))
				{
					urlString = urlString.substring(0, urlString.length() - 2);
				}
				url = new URL(urlString);
				if("jar".equals(url.getProtocol()))
				{
					try (Jar jar = JarFactory.newInstance(url))
					{
						jar.nextEntry();
						String entryName = jar.getEntryName();
						while(entryName != null)
						{
							if(entryName.startsWith("META-INF/resources/"))
							{
								context.getResources().createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/", url, "/META-INF/resources");
								break;
							}
							jar.nextEntry();
							entryName = jar.getEntryName();
						}
					}
				}
				else if("file".equals(url.getProtocol()))
				{
					File file = new File(url.toURI());
					File resources = new File(file, "META-INF/resources/");
					if(resources.isDirectory())
					{
						context.getResources().createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/", resources.getAbsolutePath(), null, "/");
					}
				}
			}
			catch(IOException ioe)
			{
				log.error(sm.getString("contextConfig.resourceJarFail", url, context.getName()));
			}
			catch(URISyntaxException e)
			{
				log.error(sm.getString("contextConfig.resourceJarFail", url, context.getName()));
			}
		}
	}

	private static boolean isInsideNestedJar(String dir)
	{
		return dir.indexOf("!/") < dir.lastIndexOf("!/");
	}
}
