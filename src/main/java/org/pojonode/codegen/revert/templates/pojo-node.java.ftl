<#assign pojoClassSimpleName = entity.pojoClassName?substring(entity.pojoClassName?last_index_of(".") + 1)/>
<#assign thisPackage = entity.pojoClassName?substring(0, entity.pojoClassName?last_index_of("."))/>
package ${thisPackage};

import org.pojonode.api.annotations.*;
import org.pojonode.api.model.*;
import java.util.LinkedList;
import java.util.List;

<#assign handledImports = ""/>
<#if entity.parentPojoClassName??>
    <#assign parentPojoClassSimpleName = entity.parentPojoClassName?substring(entity.parentPojoClassName?last_index_of(".") + 1)/>
    <#assign parentPojoClassPackage = entity.parentPojoClassName?substring(0, entity.parentPojoClassName?last_index_of("."))/>
    <#if parentPojoClassPackage != thisPackage && handledImports?index_of(entity.parentPojoClassName) < 0>
    import ${entity.parentPojoClassName};
    </#if>
</#if>
<#-- Handle properties imports -->
<#list entity.properties?sort_by("pojoProperty") as prop>
    <#if !prop.isPrimitive()>
        <#assign targetPackage = prop.targetClassName?substring(0, prop.targetClassName?last_index_of("."))/>
        <#if targetPackage != thisPackage && handledImports?index_of(prop.targetClassName) < 0 && !targetPackage?starts_with("java.lang")>
            <#assign handledImports = handledImports + "," + prop.targetClassName/>
        import ${prop.targetClassName};
        </#if>
    </#if>
</#list>
<#-- Handle assocs imports -->
<#list entity.associations?sort_by("pojoProperty") as assoc>
    <#assign targetPackage = assoc.targetClassName?substring(0, assoc.targetClassName?last_index_of("."))/>
    <#if targetPackage != thisPackage && handledImports?index_of(assoc.targetClassName) < 0 && !targetPackage?starts_with("java.lang")>
        <#assign handledImports = handledImports + "," + assoc.targetClassName/>
    import ${assoc.targetClassName};
    </#if>
</#list>

/**
* Autogenerated by Pojonode, ${nowDatetime}
*/
@${annotation}(name = "${entity.type.prefixString}"<#if entity.parentType??>, parent = "${entity.parentType.prefixString}"</#if>)
public class ${pojoClassSimpleName}<#if entity.parentPojoClassName??> extends ${parentPojoClassSimpleName}</#if> {

<#list entity.properties?sort_by("pojoProperty") as prop>
    <#if prop.isPrimitive()>
        <#assign targetClassSimpleName = prop.targetClassName/>
        <#else>
            <#assign targetClassSimpleName = prop.targetClassName?substring(prop.targetClassName?last_index_of(".") + 1)/>
    </#if>
@Property(name = "${prop.qname.prefixString}"<#if title??>, title = "${prop.title}"</#if><#if prop.mandatory>, mandatory = true</#if><#if !prop.visible>, visible = false</#if>)
private ${targetClassSimpleName} ${prop.pojoProperty};

</#list>

<#list entity.associations?sort_by("pojoProperty") as assoc>
    <#assign targetClassSimpleName = assoc.targetClassName?substring(assoc.targetClassName?last_index_of(".") + 1)/>
    <#if assoc.annotationClass.simpleName == "OneToOne" || assoc.annotationClass.simpleName == "ManyToOne">
    @${assoc.annotationClass.simpleName}(target = ${targetClassSimpleName}.class, association = "${assoc.qname.prefixString}"<#if assoc.title?? >, title = "${assoc.title}"</#if>, childAssociation = <#if assoc.childAssociation>true<#else>false</#if>)
    private ${targetClassSimpleName} ${assoc.pojoProperty};
        <#else>
        @${assoc.annotationClass.simpleName}(target = ${targetClassSimpleName}.class, association = "${assoc.qname.prefixString}"<#if assoc.title?? >, title = "${assoc.title}"</#if>, childAssociation = <#if assoc.childAssociation>true<#else>false</#if>)
        private List<${targetClassSimpleName}> ${assoc.pojoProperty}s = new LinkedList<${targetClassSimpleName}>();
    </#if>
</#list>

<#-- Getters and setters for Properties -->
<#list entity.properties?sort_by("pojoProperty") as prop>
    <#assign targetClassSimpleName = prop.targetClassName?substring(prop.targetClassName?last_index_of(".") + 1)/>
    <#assign pojoPropMethodBase = prop.pojoProperty?substring(0, 1)?upper_case + prop.pojoProperty?substring(1)/>
public ${targetClassSimpleName} get${pojoPropMethodBase}() {
return ${prop.pojoProperty};
}

public void set${pojoPropMethodBase}(${targetClassSimpleName} ${prop.pojoProperty}) {
this.${prop.pojoProperty} = ${prop.pojoProperty};
}

</#list>

<#-- Methods for associations  -->
<#list entity.associations?sort_by("pojoProperty") as assoc>
    <#assign targetClassSimpleName = assoc.targetClassName?substring(assoc.targetClassName?last_index_of(".") + 1)/>
    <#assign pojoPropMethodBase = assoc.pojoProperty?substring(0, 1)?upper_case + assoc.pojoProperty?substring(1)/>
    <#if assoc.annotationClass.simpleName == "OneToOne" || assoc.annotationClass.simpleName == "ManyToOne">
    public ${targetClassSimpleName} get${pojoPropMethodBase}() {
    return ${assoc.pojoProperty};
    }

    @AssocCreate(property = "${assoc.qname.prefixString}")
    public void set${pojoPropMethodBase}(${targetClassSimpleName} ${assoc.pojoProperty}) {
    }

    @AssocRemove(property = "${assoc.qname.prefixString}")
    public void remove${pojoPropMethodBase}(${targetClassSimpleName} ${assoc.pojoProperty}) {
    }
        <#else>
        public List<${targetClassSimpleName}> get${pojoPropMethodBase}() {
        return ${assoc.pojoProperty}s;
        }

        public void set${pojoPropMethodBase}s(List<${targetClassSimpleName}> ${assoc.pojoProperty}s) {
        this.${assoc.pojoProperty}s = ${assoc.pojoProperty}s;
        }

        @AssocCreate(property = "${assoc.qname.prefixString}")
        public void add${pojoPropMethodBase}(${targetClassSimpleName} ${assoc.pojoProperty}) {
        }

        @AssocRemove(property = "${assoc.qname.prefixString}")
        public void remove${pojoPropMethodBase}(${targetClassSimpleName} ${assoc.pojoProperty}) {
        }
    </#if>
</#list>
}