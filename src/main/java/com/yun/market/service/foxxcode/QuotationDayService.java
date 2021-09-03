package com.yun.market.service.foxxcode;

import com.yun.market.model.quotation.QuotationDayModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuotationDayService {

    void saveQuotationDay(List<QuotationDayModel> list);

    void delQuotationDay(String foxxcode);
}
