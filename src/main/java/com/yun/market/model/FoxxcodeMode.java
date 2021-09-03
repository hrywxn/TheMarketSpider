package com.yun.market.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "t_market_foxxcode")
public class FoxxcodeMode {
    public String foxxcode;
    public String name;
    public int status;

    public String getFoxxcode() {
        return foxxcode;
    }

    public void setFoxxcode(String foxxcode) {
        this.foxxcode = foxxcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

