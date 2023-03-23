package com.lhc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/14 16:23
 * @ClassName:
 * @Description:
 */
@Controller
public class FileUpAndDownController {

    @RequestMapping("/test/down")
    public ResponseEntity<byte[]> testResponseEntity(HttpSession session) throws IOException {

        //获取ServletContext对象
        ServletContext servletContext = session.getServletContext();

        //获取服务器中文件的真实路径
        //1）使用servletContext.getRealPath("文件在webapp下的路径")
        //String realPath = servletContext.getRealPath("img/1.png");
        //realPath => E:\lhcSpring\springmvc09_file\target\springmvc09_file-1.0-SNAPSHOT\img\1.png
        //2）如果我们不知道路径之间的分隔符用什么符号 ，就使用File.separator获取分隔符，再拼接
        String realPath = servletContext.getRealPath("img");
        realPath = realPath + File.separator + "1.png";
        //realPath => E:\lhcSpring\springmvc09_file\target\springmvc09_file-1.0-SNAPSHOT\img\1.png

        //创建输入流
        InputStream is = new FileInputStream(realPath);
        //创建字节数组， is.available()表示获取当前字节输入流所对应的文件所有的字节数 即1.pmg文件的字节数
        byte[] bytes = new byte[is.available()];
        //将流读到字节数组中
        is.read(bytes);

        //创建HttpHeaders对象设置响应头信息
        MultiValueMap<String, String> headers = new HttpHeaders();
        //设置要下载方式以及下载文件的名字 (前面都是固定写法，我们只能修改one.png 即文件下载下来的文件名)
        headers.add("Content-Disposition", "attachment;filename=one.png");
        //设置响应状态码 （枚举OK代表200）
        HttpStatus statusCode = HttpStatus.OK;

        //创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode);
        //关闭输入流
        is.close();
        return responseEntity;

    }

    @RequestMapping("/test/up")
    public String testUp(MultipartFile photo, HttpSession session) throws IOException {

        //获取上传文件的文件名
        String filename = photo.getOriginalFilename();
        //处理文件重名问题(这样会导致相同文件名的内容被覆盖)
        //1）获取上传文件的后缀名 （保证文件重命名后 后缀名不改变）
        String hzName = filename.substring(filename.lastIndexOf("."));
        //2）使用UUID 将文件重命名
        filename = UUID.randomUUID().toString() + hzName;

        //获取ServletContext对象
        ServletContext servletContext = session.getServletContext();
        //获取当前工程下photo目录 的真实路径
        String photoPath = servletContext.getRealPath("photo");
        System.out.println(photoPath);

        //创建photoPath所对应的file对象
        File file = new File(photoPath);
        //判断file所对应目录是否存在,不存在就创建出来
        if (!file.exists()){
            file.mkdir();
        }
        //文件上传后 在服务器的全路径
        String finalPath = photoPath + File.separator + filename;

        //上传文件
        photo.transferTo(new File(finalPath));
        return "success";
    }
}
