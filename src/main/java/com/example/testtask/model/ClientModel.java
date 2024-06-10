package com.example.testtask.model;

import com.example.testtask.entity.ClientEntity;
import lombok.Data;

@Data
public class ClientModel {
    private String name;
    private String shortName;
    private String address;
    private String form;

    public static ClientModel toModel(ClientEntity entity) {
        ClientModel clientModel = new ClientModel();

        if (entity == null) {
            return clientModel;
        }

        clientModel.setName(entity.getName());
        clientModel.setShortName(entity.getShortName());
        clientModel.setAddress(entity.getAddress());
        clientModel.setForm(((Enum)entity.getForm()).name());

        return clientModel;
    }
}
