package com.stream.nz.model.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author cheng hao
 * @Date 2023/12/22 18:52
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuzzySearchVinVo implements Serializable {

    private String vin;

    private String modelYear;

    private String carSeriesCode;

    private String engineNo;

    // registerDate
    private String purchaseDate;

    private String productionDate;
}
