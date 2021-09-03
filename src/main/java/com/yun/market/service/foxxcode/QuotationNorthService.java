package com.yun.market.service.foxxcode;

import com.yun.market.model.quotation.QuotationNorthModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuotationNorthService {
    void saveData(String sql,List<QuotationNorthModel> list);
}
