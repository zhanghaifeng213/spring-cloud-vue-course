package com.course.server.service;

import com.course.server.domain.File;
import com.course.server.domain.FileExample;
import com.course.server.dto.FileDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.FileMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Service
public class FileService {
    @Resource
    private FileMapper fileMapper;

    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        FileExample fileExample = new FileExample();
        List<File> files = fileMapper.selectByExample(fileExample);
        PageInfo<File> pageInfo = new PageInfo<>(files);
        pageDto.setTotal(pageInfo.getTotal());

        List<FileDto> fileDtos = new ArrayList<FileDto>();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            FileDto fileDto = new FileDto();
            BeanUtils.copyProperties(file, fileDto);
            fileDtos.add(fileDto);
        }
        pageDto.setList(fileDtos);
    }
    public void save(FileDto fileDto) {
        File file = CopyUtil.copy(fileDto, File.class);
        File fileDb = selectByKey(fileDto.getKey());
        if(fileDb == null) {
            this.insert(file);
        } else {
            fileDb.setShardIndex(fileDto.getShardIndex());
            this.update(fileDb);
        }
    }
    private void insert(File file) {
        Date now = new Date();
        file.setId(UuidUtil.getShortUuid());
        fileMapper.insert(file);
    }

    private void update(File file) {
        fileMapper.updateByPrimaryKey(file);
    }

    public void delete(String id) {
        fileMapper.deleteByPrimaryKey(id);
    }

    public File selectByKey(String key) {
        FileExample example = new FileExample();
        example.createCriteria().andKeyEqualTo(key);
        List<File> files = fileMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(files)) {
            return null;
        } else {
            return files.get(0);
        }
    }
}
