package com.chen.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.yygh.model.cmn.Dict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 */
@Service
public interface DictService extends IService<Dict> {

    List<Dict> findChildData(Long id);

    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);
}
