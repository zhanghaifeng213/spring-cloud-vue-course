package com.course.system.controller.admin;

import com.course.server.domain.RoleResource;
import com.course.server.dto.RoleResourceDto;
import com.course.server.dto.PageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.exception.ValidatorException;
import com.course.server.service.RoleResourceService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/admin/roleResource")
public class RoleResourceController {
    private static final Logger LOG = LoggerFactory.getLogger(RoleResourceController.class);
    public static final String BUSINESS_NAME = "角色资源关联";
    @Resource
    private RoleResourceService roleResourceService;
    @PostMapping("/list")
    public ResponseDto roleResource(@RequestBody PageDto pageDto) {
        LOG.info("pageDto: {}", pageDto);
        ResponseDto responseDto = new ResponseDto();
        roleResourceService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto save(@RequestBody RoleResourceDto roleResourceDto) {
        LOG.info("roleResourceDto: {}", roleResourceDto);
        // 保存校验
        ValidatorUtil.require(roleResourceDto.getRoleId(), "角色｜id");
        ValidatorUtil.require(roleResourceDto.getResourceId(), "资源｜id");
        ResponseDto responseDto = new ResponseDto();
        roleResourceService.save(roleResourceDto);
        responseDto.setContent(roleResourceDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        LOG.info("id: {}", id);
        ResponseDto responseDto = new ResponseDto();
        roleResourceService.delete(id);
        return responseDto;
    }
}
