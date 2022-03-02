package ${packageName};

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: actionParam.ftl
 *  
 */
public class ${className}
{

<#list varList as var>
    public ${var.type} ${var.name} = null;
</#list>

}
