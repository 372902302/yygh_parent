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

    /**
     * 根据上级编码与值获取数据字典名称
     * @param parentDictCode
     * @param value
     * @return
     */
    String getNameByParentDictCodeAndValue(String parentDictCode, String value);

    List<Dict> findChildrenByDictcode(String dictCode);
}
