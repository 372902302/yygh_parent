package com.chen.yygh.cmn.listener;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.chen.yygh.cmn.mapper.DictMapper;
import com.chen.yygh.model.cmn.Dict;
import com.chen.yygh.vo.cmn.DictEeVo;

public class DictListener extends AnalysisEventListener<DictEeVo> {
    private DictMapper mapper;
    public DictListener(){}
    public DictListener(DictMapper mapper){
        this.mapper = mapper;
    }
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = BeanUtil.copyProperties(dictEeVo, Dict.class);
        mapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
