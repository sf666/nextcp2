package ${packageName};

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ${classSpecification.name}
{

<#list classSpecification.fieldSpecifications as field>
    public ${field.type} ${field.name};
</#list>

    public ${classSpecification.name}()
    {
    }

    public ${classSpecification.name}(<#list classSpecification.fieldSpecifications as field><#if classSpecification.fieldSpecifications?first != field>, </#if>${field.type} ${field.name}</#list>)
    {
        <#list classSpecification.fieldSpecifications as field>
        this.${field.name} = ${field.name};
        </#list>
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("${classSpecification.name} [");
        <#list classSpecification.fieldSpecifications as field>
        sb.append("${field.name}=").append(this.${field.name}).append(", ");
        </#list>
        sb.append("]");
        return sb.toString();
    }

}