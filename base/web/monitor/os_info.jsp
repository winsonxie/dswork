<%@page language="java" pageEncoding="UTF-8"
import="com.sun.management.OperatingSystemMXBean,java.lang.management.ManagementFactory"
%><%
		OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		Runtime r = Runtime.getRuntime();
		StringBuilder sb = new StringBuilder();
		sb.append("{")
		.append("osname").append(":\"").append(osmb.getName()).append("\",")
		.append("osarch").append(":\"").append(osmb.getArch()).append("\",")
		.append("osversion").append(":\"").append(osmb.getVersion()).append("\",")
		.append("oscpu").append(":\"").append(osmb.getAvailableProcessors()).append("\",")

		.append("os_memory").append(":\"").append(osmb.getTotalPhysicalMemorySize() / 1024 / 1024).append("\",")
		.append("os_memory_free").append(":\"").append(osmb.getFreePhysicalMemorySize() / 1024 / 1024).append("\",")
		.append("os_swap").append(":\"").append(osmb.getTotalSwapSpaceSize() / 1024 / 1024).append("\",")
		.append("os_swap_free").append(":\"").append(osmb.getFreeSwapSpaceSize() / 1024 / 1024).append("\",")
		
		
		.append("run_memory_max").append(":\"").append(r.maxMemory() / 1024 / 1024).append("\",")
		.append("run_memory_total").append(":\"").append(r.totalMemory() / 1024 / 1024).append("\",")
		.append("run_memory_free").append(":\"").append(r.freeMemory() / 1024 / 1024).append("\",")
		.append("run_cpu").append(":\"").append(r.availableProcessors())
		.append("\"}");
%><%=sb.toString()%>