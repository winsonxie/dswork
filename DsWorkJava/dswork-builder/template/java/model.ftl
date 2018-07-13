/**
 * ${table.comment}Model
 */
package ${namespace}.model;
public class ${table.nameUpperCamel}
{
<#list columnList as c>
	// ${c.comment}
	<#if c.datatype == 'Long' || c.datatype == 'long'>
	private ${c.datatype} ${c.nameLowerCamel} = ${c.defaultvalue!'0'}L;
	<#elseif c.datatype == 'int'>
	private int ${c.nameLowerCamel} = ${c.defaultvalue!'0'};
	<#elseif c.datatype == 'float'>
	private float ${c.nameLowerCamel} = ${c.defaultvalue!'0.0'}F;
	<#elseif c.datatype == 'boolean'>
	private boolean ${c.nameLowerCamel} = false;
	<#elseif c.datatype == 'date'>
	private java.util.Date ${c.nameLowerCamel};
	<#elseif c.datatype == 'String'>
	private String ${c.nameLowerCamel} = "${c.defaultvalue!}";
	<#else>
	private ${c.datatype} ${c.nameLowerCamel};
	</#if>
</#list>
<#list columnList as c>

	public ${c.datatype} get${c.nameUpperCamel}()
	{
		return ${c.nameLowerCamel};
	}

	<#if c.datatype == 'date'>
	public void set${c.nameUpperCamel}(java.util.Date ${c.nameLowerCamel})
	{
		this.${c.nameLowerCamel} = ${c.nameLowerCamel};
	}
	<#else>
	public void set${c.nameUpperCamel}(${c.datatype} ${c.nameLowerCamel})
	{
		this.${c.nameLowerCamel} = ${c.nameLowerCamel};
	}
	</#if>
</#list>
}