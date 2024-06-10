package com.example.testtask.model;

import com.example.testtask.entity.ClientEntity;
import com.example.testtask.entity.DepositEntity;
import lombok.Data;

import java.util.Date;

@Data
public class DepositModel {

    private ClientModel clientModel;
    private BankModel bankModel;

    private int percent;
    private Date dateOpen;
    private int monthPeriod;

    public static DepositModel toModel(DepositEntity entity) {
        DepositModel depositModel = new DepositModel();

        if (entity == null) {
            return depositModel;
        }

        depositModel.setBankModel(BankModel.toModel(entity.getBank()));
        depositModel.setClientModel(ClientModel.toModel(entity.getClient()));
        depositModel.setPercent(entity.getPercent());
        depositModel.setDateOpen(entity.getDateOpen());
        depositModel.setMonthPeriod(entity.getMonthPeriod());

        return depositModel;
    }
}
