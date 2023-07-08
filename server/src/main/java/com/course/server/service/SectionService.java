package com.course.server.service;

import com.course.server.domain.Section;
import com.course.server.domain.SectionExample;
import com.course.server.dto.SectionDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.SectionPageDto;
import com.course.server.mapper.SectionMapper;
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
import java.util.Date;

@Service
public class SectionService {
    @Resource
    private SectionMapper sectionMapper;
    @Resource
    private CourseService courseService;

    public void list(SectionPageDto sectionPageDto) {
        PageHelper.startPage(sectionPageDto.getPage(), sectionPageDto.getSize());
        SectionExample sectionExample = new SectionExample();
        SectionExample.Criteria criteria = sectionExample.createCriteria();
        if(!StringUtils.isEmpty(sectionPageDto.getCourseId())) {
            criteria.andCourseIdEqualTo(sectionPageDto.getCourseId());
        }
        if(!StringUtils.isEmpty(sectionPageDto.getChapterId())) {
            criteria.andCourseIdEqualTo(sectionPageDto.getChapterId());
        }
        sectionExample.setOrderByClause("sort asc");
        List<Section> sections = sectionMapper.selectByExample(sectionExample);
        PageInfo<Section> pageInfo = new PageInfo<>(sections);
        sectionPageDto.setTotal(pageInfo.getTotal());

        List<SectionDto> sectionDtos = new ArrayList<SectionDto>();
        for (int i = 0; i < sections.size(); i++) {
            Section section = sections.get(i);
            SectionDto sectionDto = new SectionDto();
            BeanUtils.copyProperties(section, sectionDto);
            sectionDtos.add(sectionDto);
        }
        sectionPageDto.setList(sectionDtos);
    }
    public void save(SectionDto sectionDto) {
        Section section = CopyUtil.copy(sectionDto, Section.class);
        if(StringUtils.isEmpty(sectionDto.getId())) {
            this.insert(section);
        } else {
            this.update(section);
        }
        courseService.updateTime(sectionDto.getCourseId());
    }
    private void insert(Section section) {
        Date now = new Date();
        section.setUpdateAt(now);
        section.setId(UuidUtil.getShortUuid());
        sectionMapper.insert(section);
    }

    private void update(Section section) {
        section.setUpdateAt(new Date());
        sectionMapper.updateByPrimaryKey(section);
    }

    public void delete(String id) {
        sectionMapper.deleteByPrimaryKey(id);
    }
}
