package com.yun.market.service.foxxcode;

import com.yun.market.model.quotation.QuotationFinVolumeModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuotationFinVolumeService {
    void saveDataDo(List<QuotationFinVolumeModel> list);
}
