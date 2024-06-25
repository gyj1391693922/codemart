package com.codemart.codemart_back_java.service.impl;

import com.codemart.codemart_back_java.entity.FontCnt;
import com.codemart.codemart_back_java.mapper.FontMapper;
import com.codemart.codemart_back_java.service.FontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FontServiceImpl implements FontService {

    @Autowired
    FontMapper fontMapper;

    public FontCnt getUserCnt() {

        return fontMapper.getUserCnt();

    }

    public FontCnt getProjectCnt() {
        return fontMapper.getProjectCnt();
    }

    public FontCnt getMoneyCnt() {
            FontCnt moneyCnt = fontMapper.getMoneyCnt();
            if (moneyCnt == null) {
                moneyCnt = new FontCnt();
                moneyCnt.setCnt(0.0);
            }
            return moneyCnt;
    }
}
