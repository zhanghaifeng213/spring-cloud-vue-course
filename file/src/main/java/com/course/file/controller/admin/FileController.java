package com.course.file.controller.admin;

import com.course.server.domain.File;
import com.course.server.dto.FileDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.exception.ValidatorException;
import com.course.server.service.FileService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/admin/file")
public class FileController {
    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);
    public static final String BUSINESS_NAME = "文件";
    @Resource
    private FileService fileService;
    @PostMapping("/list")
    public ResponseDto file(@RequestBody PageDto pageDto) {
        LOG.info("pageDto: {}", pageDto);
        ResponseDto responseDto = new ResponseDto();
        fileService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto save(@RequestBody FileDto fileDto) {
        LOG.info("fileDto: {}", fileDto);
        // 保存校验
        ValidatorUtil.require(fileDto.getPath(), "相对路径");
        ValidatorUtil.length(fileDto.getPath(), "相对路径", 1, 100);
        ValidatorUtil.length(fileDto.getName(), "文件名", 1, 100);
        ValidatorUtil.length(fileDto.getSuffix(), "后缀", 1, 10);
        ResponseDto responseDto = new ResponseDto();
        fileService.save(fileDto);
        responseDto.setContent(fileDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        LOG.info("id: {}", id);
        ResponseDto responseDto = new ResponseDto();
        fileService.delete(id);
        return responseDto;
    }
}
