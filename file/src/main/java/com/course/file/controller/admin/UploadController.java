package com.course.file.controller.admin;

import com.course.server.domain.Test;
import com.course.server.dto.FileDto;
import com.course.server.dto.ResponseDto;
import com.course.server.enums.FileUseEnum;
import com.course.server.service.FileService;
import com.course.server.service.TestService;
import com.course.server.util.Base64ToMultipartFile;
import com.course.server.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RequestMapping("/admin")
@RestController
public class UploadController {
    private static final Logger LOG = LoggerFactory.getLogger(UploadController.class);

    public static final String BUSINESS_NAME = "文件上传";

    @Value("${file.domain}")
    private String FILE_DOMAIN;

    @Resource
    private FileService fileService;

    @Value("${file.path}")
    private String FILE_PATH;
    @RequestMapping("/upload")
    public ResponseDto upload(@RequestBody FileDto fileDto
    ) throws IOException {
//        LOG.info("文件上传开始：{}", shard);
//        LOG.info(shard.getOriginalFilename());
//        LOG.info(String.valueOf(shard.getSize()));
        String use = fileDto.getUse();
        String key = fileDto.getKey();
        String suffix = fileDto.getSuffix();
        String shardBase64 = fileDto.getShard();
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);
        // 保存文件到本地
        FileUseEnum useEnum = FileUseEnum.getByCode(use);
//        String key = UuidUtil.getShortUuid();
//        String fileName = shard.getOriginalFilename();
//        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // 如果文件夹不存在则创建
        String dir = useEnum.name().toLowerCase();
        File fullDir = new File(FILE_PATH + dir);
        if(!fullDir.exists()) {
            fullDir.mkdir();
        }

        String path = dir + File.separator + key + "." + suffix;
        String fullPath = FILE_PATH + path;
        File dest = new File(fullPath);
        shard.transferTo(dest);
        LOG.info(dest.getAbsolutePath());

        LOG.info("保存文件记录开始");
//        FileDto fileDto = new FileDto();
        fileDto.setPath(path);
//        fileDto.setName(name);
//        fileDto.setSize(size);
//        fileDto.setSuffix(suffix);
//        fileDto.setUse(use);
//        fileDto.setShardIndex(shardIndex);
//        fileDto.setShardSize(shardSize);
//        fileDto.setShardTotal(shardTotal);
//        fileDto.setKey(key);

        fileService.save(fileDto);

        ResponseDto<Object> objectResponseDto = new ResponseDto();
        fileDto.setPath(FILE_DOMAIN + path);
        objectResponseDto.setContent(fileDto);
        return objectResponseDto;
    }

    @GetMapping("/merge")
    public ResponseDto merge() throws Exception {
        File newFile = new File(FILE_PATH + "/course/test123.mp4");
        FileOutputStream outputStream = new FileOutputStream(newFile, true); // 文件追加写入
        FileInputStream fileInputStream = null;
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;
        try {
            // 读取第一个分片
            fileInputStream = new FileInputStream(new File(FILE_PATH + "/course/3wICkR4b.blob"));
            while ((len = fileInputStream.read(byt)) != -1) {
                outputStream.write(byt, 0, len);
            }

            // 读取第二个分片
            fileInputStream = new FileInputStream(new File(FILE_PATH + "/course/ms6kj7q4.blob"));
            while ((len = fileInputStream.read(byt)) != -1) {
                outputStream.write(byt, 0, len);
            }
        } catch(IOException e) {
            LOG.error("分片合并异常", e);
        } finally {
            try {
                if(fileInputStream != null) {
                    fileInputStream.close();
                }
                outputStream.close();
                LOG.info("IO流关闭");
            } catch (Exception e) {
                LOG.error("IO流关闭", e);
            }
        }
        ResponseDto responseDto = new ResponseDto();
        return responseDto;
    }
}
