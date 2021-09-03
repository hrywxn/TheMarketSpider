package com.yun.market.service.foxxcode;

import com.yun.market.model.FoxxcodeMode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FoxxcodeService {

    void saveFoxxcode(String sql,List<FoxxcodeMode> list);

    List<FoxxcodeMode> getFoxxcodeList();

    void updateFoxxcodeStatus(String foxxcode);
}
