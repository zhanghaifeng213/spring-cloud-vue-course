package com.course.server.service;

import com.alibaba.fastjson.JSON;
import com.course.server.domain.Resource;
import com.course.server.domain.ResourceExample;
import com.course.server.dto.ResourceDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.ResourceMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ResourceService {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceService.class);

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
//        resource.setId(UuidUtil.getShortUuid());
        resourceMapper.insert(resource);
    }

    private void update(Resource resource) {
        resourceMapper.updateByPrimaryKey(resource);
    }

    public void delete(String id) {
        resourceMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void saveJson(String json) {
        List<ResourceDto> jsonList = JSON.parseArray(json, ResourceDto.class);
        List<ResourceDto> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(jsonList)) {
            for(ResourceDto d: jsonList) {
                d.setParent("");
                add(list, d);
            }
        }
        LOG.info("共{}条", list.size());
        resourceMapper.deleteByExample(null);
        for(int i = 0; i < list.size(); i++) {
            this.insert(CopyUtil.copy(list.get(i), Resource.class));
        }
    }

    public void add(List<ResourceDto> list, ResourceDto dto) {
        list.add(dto);
        if(!CollectionUtils.isEmpty(dto.getChildren())) {
            for (ResourceDto d: dto.getChildren()) {
                d.setParent(dto.getId());
                add(list, d);
            }
        }
    }

    /**
     * 按约定将列表转成树
     * 要求：ID要正序排列
     * @return
     */
    public List<ResourceDto> loadTree() {
        ResourceExample example = new ResourceExample();
        example.setOrderByClause("id asc");
        List<Resource> resourceList = resourceMapper.selectByExample(example);
        List<ResourceDto> resourceDtoList = CopyUtil.copyList(resourceList, ResourceDto.class);
        for (int i = resourceList.size() - 1; i >= 0 ; i--) {
            // 当前要移动的记录
            ResourceDto child = resourceDtoList.get(i);
            // 如果当前节点没有父节点，则不用往下了
            if(StringUtils.isEmpty(child.getParent())){
                continue;
            }
            // 查找父节点
            for (int j = i - 1; j >= 0 ; j--) {
                ResourceDto parent = resourceDtoList.get(j);
                if(child.getParent().equals(parent.getId())) {
                    if(CollectionUtils.isEmpty(parent.getChildren())) {
                        parent.setChildren(new ArrayList<>());
                    }
                    // 添加到最前面，否则会变成倒序，因为循环是从后往前循环的
                    parent.getChildren().add(0, child);
                    // 子节点找到父节点后，删除列表中的子节点
                    resourceDtoList.remove(child);
                }
            }
        }
        return resourceDtoList;
    }
}
