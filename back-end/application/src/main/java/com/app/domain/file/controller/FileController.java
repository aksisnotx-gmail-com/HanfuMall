package com.app.domain.file.controller;

import com.app.controller.Controller;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 *
 * @author xxl
 * @since 2024/3/13
 */
@RestController
@RequestMapping("/file")
@Validated
@Tag(name = "文件接口")
public class FileController extends Controller {


    @PostMapping("upload")
    public RespEntity<String> upload(@RequestBody MultipartFile file) {
        return RespEntity.success(fileService.saveFile(file));
    }

    @GetMapping("download/{fileId}")
    public  void download(@PathVariable("fileId") String fileId) {
        fileService.dowload(fileId);
    }
}
