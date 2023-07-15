package com.course.server.service;

import com.course.server.domain.CourseContentFile;
import com.course.server.domain.CourseContentFileExample;
import com.course.server.dto.CourseContentFileDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.CourseContentFileMapper;
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

@Service
public class CourseContentFileService {
    @Resource
    private CourseContentFileMapper courseContentFileMapper;

//    public void list(PageDto pageDto) {
//        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
//        CourseContentFileExample courseContentFileExample = new CourseContentFileExample();
//        List<CourseContentFile> courseContentFiles = courseContentFileMapper.selectByExample(courseContentFileExample);
//        PageInfo<CourseContentFile> pageInfo = new PageInfo<>(courseContentFiles);
//        pageDto.setTotal(pageInfo.getTotal());
//
//        List<CourseContentFileDto> courseContentFileDtos = new ArrayList<CourseContentFileDto>();
//        for (int i = 0; i < courseContentFiles.size(); i++) {
//            CourseContentFile courseContentFile = courseContentFiles.get(i);
//            CourseContentFileDto courseContentFileDto = new CourseContentFileDto();
//            BeanUtils.copyProperties(courseContentFile, courseContentFileDto);
//            courseContentFileDtos.add(courseContentFileDto);
//        }
//        pageDto.setList(courseContentFileDtos);
//    }
    public List<CourseContentFileDto> list(String courseId) {
        CourseContentFileExample example = new CourseContentFileExample();
        CourseContentFileExample.Criteria criteria = example.createCriteria();
        criteria.andCourseIdEqualTo(courseId);
        List<CourseContentFile> fileList = courseContentFileMapper.selectByExample(example);
        return CopyUtil.copyList(fileList, CourseContentFileDto.class);
    }
    public void save(CourseContentFileDto courseContentFileDto) {
        CourseContentFile courseContentFile = CopyUtil.copy(courseContentFileDto, CourseContentFile.class);
        if(StringUtils.isEmpty(courseContentFileDto.getId())) {
            this.insert(courseContentFile);
        } else {
            this.update(courseContentFile);
        }
    }
    private void insert(CourseContentFile courseContentFile) {
        courseContentFile.setId(UuidUtil.getShortUuid());
        courseContentFileMapper.insert(courseContentFile);
    }

    private void update(CourseContentFile courseContentFile) {
        courseContentFileMapper.updateByPrimaryKey(courseContentFile);
    }

    public void delete(String id) {
        courseContentFileMapper.deleteByPrimaryKey(id);
    }
}
