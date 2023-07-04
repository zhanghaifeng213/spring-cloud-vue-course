package com.course.server.service;

import com.course.server.domain.Chapter;
import com.course.server.domain.ChapterExample;
import com.course.server.dto.ChapterDto;
import com.course.server.mapper.ChapterMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChapterService {
    @Resource
    private ChapterMapper chapterMapper;

    public List<ChapterDto> list() {
        PageHelper.startPage(1, 1);
        ChapterExample chapterExample = new ChapterExample();
        List<Chapter> chapters = chapterMapper.selectByExample(chapterExample);
        List<ChapterDto> chapterDtos = new ArrayList<ChapterDto>();
        for (int i = 0; i < chapters.size(); i++) {
            Chapter chapter = chapters.get(i);
            ChapterDto chapterDto = new ChapterDto();
            BeanUtils.copyProperties(chapter, chapterDto);
            chapterDtos.add(chapterDto);
        }
        return chapterDtos;
    }
}
