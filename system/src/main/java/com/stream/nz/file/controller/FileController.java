package com.stream.nz.file.controller;

import com.stream.nz.constant.ResultData;
import com.stream.nz.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author cheng hao
 * @Date 2024/5/13 15:12
 */
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/uploadFile", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public ResultData<String> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("fileName") String fileName,@RequestParam("dictionary") String dictionary) throws IOException {
        return new ResultData(ResultData.SUCCESS,"", fileService.uploadFile(file,fileName,dictionary));
    }
}
