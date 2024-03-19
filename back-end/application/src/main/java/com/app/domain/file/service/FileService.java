package com.app.domain.file.service;

import cn.hutool.core.io.FileMagicNumber;
import cn.hutool.core.lang.Assert;
import com.app.domain.base.AbstractService;
import com.app.domain.file.entity.FileEntity;
import com.app.domain.file.mapper.FileMapper;
import com.app.toolkit.file.FileUtils;
import com.sdk.exception.GlobalException;
import com.sdk.util.asserts.AssertUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author xxl
 * @since 2024/3/13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileService extends AbstractService<FileMapper, FileEntity> {

    @Value("${file.save.path}")
    private String savePath;

    private static final String FILE_REQUEST_PATH = "/file/";

    private final HttpServletResponse response;

    private final HttpServletRequest request;

    public String saveFile(MultipartFile file) {
        try {
            FileEntity entity = new FileEntity();
            String downloadId = FileUtils.upload(file.getInputStream(), savePath);
            entity.setId(downloadId);
            entity.setUrl(getUrl(request) + downloadId);
            this.save(entity);
            return entity.getUrl();
        } catch (IOException e) {
            throw new GlobalException("文件保存失败: " + e.getMessage());
        }
    }

    public void dowload(String id)  {
        FileEntity entity = this.getById(id);
        AssertUtils.notNull(entity,"文件不存在");
        FileUtils.webDownload(entity.getPath(),response,FileUtils.getFileSuffix(entity.getPath()));
    }

    private void check(String fileSavePath, FileMagicNumber...type)  {
        boolean match = Arrays.stream(type).anyMatch(t -> t.getExtension().equalsIgnoreCase(FileUtils.getFileSuffix(fileSavePath)));
        Assert.isTrue(match,"不支持的文件格式 = "+ FileUtils.getFileSuffix(fileSavePath));
    }

    private String getUrl(HttpServletRequest request) {
        // 获取协议（如http或https）
        String scheme = request.getScheme();
        // 获取服务器名称（如localhost或具体域名）
        String serverName = request.getServerName();
        // 获取服务器端口号
        int serverPort = request.getServerPort();
        // 获取请求URI（如/endpoint）
        String requestUri = request.getRequestURI();
        // 组合成完整的请求URL
        return scheme + "://" + serverName + ":" + serverPort + FILE_REQUEST_PATH;
    }
}


