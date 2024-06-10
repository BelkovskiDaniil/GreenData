package com.example.testtask.model;

import com.example.testtask.entity.BankEntity;
import lombok.Data;

@Data
public class BankModel {
    private String name;
    private long bankIdentificationCode;

    public static BankModel toModel(BankEntity entity) {
        BankModel bankModel = new BankModel();

        if (entity == null) {
            return bankModel;
        }

        bankModel.setName(entity.getName());
        bankModel.setBankIdentificationCode(entity.getBankIdentificationCode());

        return bankModel;
    }
}
