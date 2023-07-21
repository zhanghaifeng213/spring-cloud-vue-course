package com.course.server.service;

import com.course.server.domain.Resource;
import com.course.server.domain.ResourceExample;
import com.course.server.dto.ResourceDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.ResourceMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceService {
    @javax.annotation.Resource
    private ResourceMapper resourceMapper;

    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        ResourceExample resourceExample = new ResourceExample();
        List<Resource> resources = resourceMapper.selectByExample(resourceExample);
        PageInfo<Resource> pageInfo = new PageInfo<>(resources);
        pageDto.setTotal(pageInfo.getTotal());

        List<ResourceDto> resourceDtos = new ArrayList<ResourceDto>();
        for (int i = 0; i < resources.size(); i++) {
            Resource resource = resources.get(i);
            ResourceDto resourceDto = new ResourceDto();
            BeanUtils.copyProperties(resource, resourceDto);
            resourceDtos.add(resourceDto);
        }
        pageDto.setList(resourceDtos);
    }
    public void save(ResourceDto resourceDto) {
        Resource resource = CopyUtil.copy(resourceDto, Resource.class);
        if(StringUtils.isEmpty(resourceDto.getId())) {
            this.insert(resource);
        } else {
            this.update(resource);
        }
    }
    private void insert(Resource resource) {
        resource.setId(UuidUtil.getShortUuid());
        resourceMapper.insert(resource);
    }

    private void update(Resource resource) {
        resourceMapper.updateByPrimaryKey(resource);
    }

    public void delete(String id) {
        resourceMapper.deleteByPrimaryKey(id);
    }
}
