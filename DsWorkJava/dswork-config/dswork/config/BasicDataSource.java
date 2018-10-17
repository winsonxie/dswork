package dswork.config;

public class BasicDataSource extends org.apache.commons.dbcp.BasicDataSource
{
	private String filters;

	public String getFilters()
	{
		return filters;
	}

	public void setFilters(String filters)
	{
		this.filters = filters;
	}
}
