package com.course.business.controller.admin;

import com.course.server.domain.Section;
import com.course.server.dto.SectionDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.dto.SectionPageDto;
import com.course.server.exception.ValidatorException;
import com.course.server.service.SectionService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/admin/section")
public class SectionController {
    private static final Logger LOG = LoggerFactory.getLogger(SectionController.class);
    public static final String BUSINESS_NAME = "小节";
    @Resource
    private SectionService sectionService;
    @PostMapping("/list")
    public ResponseDto section(@RequestBody SectionPageDto sectionPageDto) {
        LOG.info("pageDto: {}", sectionPageDto);
        ResponseDto responseDto = new ResponseDto();
        ValidatorUtil.require(sectionPageDto.getCourseId(), "课程ID");
        ValidatorUtil.require(sectionPageDto.getChapterId(), "章节ID");
        sectionService.list(sectionPageDto);
        responseDto.setContent(sectionPageDto);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto save(@RequestBody SectionDto sectionDto) {
        LOG.info("sectionDto: {}", sectionDto);
        // 保存校验
        ValidatorUtil.require(sectionDto.getTitle(), "标题");
        ValidatorUtil.length(sectionDto.getTitle(), "标题", 1, 50);
        ValidatorUtil.length(sectionDto.getVideo(), "视频", 1, 200);
        ResponseDto responseDto = new ResponseDto();
        sectionService.save(sectionDto);
        responseDto.setContent(sectionDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        LOG.info("id: {}", id);
        ResponseDto responseDto = new ResponseDto();
        sectionService.delete(id);
        return responseDto;
    }
}
