package com.course.server.service;

import com.course.server.domain.RoleUser;
import com.course.server.domain.RoleUserExample;
import com.course.server.dto.RoleUserDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.RoleUserMapper;
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
public class RoleUserService {
    @Resource
    private RoleUserMapper roleUserMapper;

    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        RoleUserExample roleUserExample = new RoleUserExample();
        List<RoleUser> roleUsers = roleUserMapper.selectByExample(roleUserExample);
        PageInfo<RoleUser> pageInfo = new PageInfo<>(roleUsers);
        pageDto.setTotal(pageInfo.getTotal());

        List<RoleUserDto> roleUserDtos = new ArrayList<RoleUserDto>();
        for (int i = 0; i < roleUsers.size(); i++) {
            RoleUser roleUser = roleUsers.get(i);
            RoleUserDto roleUserDto = new RoleUserDto();
            BeanUtils.copyProperties(roleUser, roleUserDto);
            roleUserDtos.add(roleUserDto);
        }
        pageDto.setList(roleUserDtos);
    }
    public void save(RoleUserDto roleUserDto) {
        RoleUser roleUser = CopyUtil.copy(roleUserDto, RoleUser.class);
        if(StringUtils.isEmpty(roleUserDto.getId())) {
            this.insert(roleUser);
        } else {
            this.update(roleUser);
        }
    }
    private void insert(RoleUser roleUser) {
        roleUser.setId(UuidUtil.getShortUuid());
        roleUserMapper.insert(roleUser);
    }

    private void update(RoleUser roleUser) {
        roleUserMapper.updateByPrimaryKey(roleUser);
    }

    public void delete(String id) {
        roleUserMapper.deleteByPrimaryKey(id);
    }
}
