package com.course.server.service;

import com.course.server.domain.${Domain};
import com.course.server.domain.${Domain}Example;
import com.course.server.dto.${Domain}Dto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.${Domain}Mapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
<#list typeSet as type>
    <#if type == 'Date'>
import java.util.Date;
    </#if>
</#list>

@Service
public class ${Domain}Service {
    @Resource
    private ${Domain}Mapper ${domain}Mapper;

    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        <#list fieldList as field>
            <#if field.nameHump=='sort'>
        ${domain}Example.setOrderByClause("sort asc");
            </#if>
        </#list>
        List<${Domain}> ${domain}s = ${domain}Mapper.selectByExample(${domain}Example);
        PageInfo<${Domain}> pageInfo = new PageInfo<>(${domain}s);
        pageDto.setTotal(pageInfo.getTotal());

        List<${Domain}Dto> ${domain}Dtos = new ArrayList<${Domain}Dto>();
        for (int i = 0; i < ${domain}s.size(); i++) {
            ${Domain} ${domain} = ${domain}s.get(i);
            ${Domain}Dto ${domain}Dto = new ${Domain}Dto();
            BeanUtils.copyProperties(${domain}, ${domain}Dto);
            ${domain}Dtos.add(${domain}Dto);
        }
        pageDto.setList(${domain}Dtos);
    }
    public void save(${Domain}Dto ${domain}Dto) {
        ${Domain} ${domain} = CopyUtil.copy(${domain}Dto, ${Domain}.class);
        if(StringUtils.isEmpty(${domain}Dto.getId())) {
            this.insert(${domain});
        } else {
            this.update(${domain});
        }
    }
    private void insert(${Domain} ${domain}) {
        Date now = new Date();
        <#list fieldList as field>
            <#if field.nameHump=="createAt">
        ${domain}.setCreatedAt(now);
            </#if>
            <#if field.nameHump=="updateAt">
        ${domain}.setUpdateAt(now);
            </#if>
        </#list>
        ${domain}.setId(UuidUtil.getShortUuid());
        ${domain}Mapper.insert(${domain});
    }

    private void update(${Domain} ${domain}) {
        <#list fieldList as field>
            <#if field.nameHump=="updateAt">
        ${domain}.setUpdateAt(new Date());
            </#if>
        </#list>
        ${domain}Mapper.updateByPrimaryKey(${domain});
    }

    public void delete(String id) {
        ${domain}Mapper.deleteByPrimaryKey(id);
    }
}
