package com.course.system.controller.admin;

import com.course.server.domain.Resource;
import com.course.server.dto.ResourceDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.exception.ValidatorException;
import com.course.server.service.ResourceService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/resource")
public class ResourceController {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceController.class);
    public static final String BUSINESS_NAME = "资源";
    @javax.annotation.Resource
    private ResourceService resourceService;
    @PostMapping("/list")
    public ResponseDto resource(@RequestBody PageDto pageDto) {
        LOG.info("pageDto: {}", pageDto);
        ResponseDto responseDto = new ResponseDto();
        resourceService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto save(@RequestBody ResourceDto resourceDto) {
        LOG.info("resourceDto: {}", resourceDto);
        // 保存校验
        ValidatorUtil.require(resourceDto.getName(), "名称｜菜单或按钮");
        ValidatorUtil.length(resourceDto.getName(), "名称｜菜单或按钮", 1, 100);
        ValidatorUtil.length(resourceDto.getPage(), "页面路由", 1, 50);
        ValidatorUtil.length(resourceDto.getRequest(), "请求｜接口", 1, 200);
        ResponseDto responseDto = new ResponseDto();
        resourceService.save(resourceDto);
        responseDto.setContent(resourceDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        LOG.info("id: {}", id);
        ResponseDto responseDto = new ResponseDto();
        resourceService.delete(id);
        return responseDto;
    }
}
