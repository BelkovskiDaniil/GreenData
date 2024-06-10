package com.example.testtask.service.impl;

import com.example.testtask.entity.ClientEntity;
import com.example.testtask.model.ClientModel;
import com.example.testtask.repository.ClientRepository;
import com.example.testtask.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Primary
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientEntity> getClientsFromDB() {
        return (List<ClientEntity>) clientRepository.findAll();
    }

    public void saveClient(ClientEntity client) {
        clientRepository.save(client);
    }

    @Override
    public ResponseEntity<?> getClients(String sort, String filter, String filterValue) {
        try {
            Stream<ClientModel> clientStream = getClientsFromDB()
                    .stream()
                    .map(ClientModel::toModel);

            if (filter != null && !filter.isEmpty() && filterValue != null) {
                clientStream = clientStream.filter(client -> {
                    switch (filter) {
                        case "name":
                            return filterValue.equals(client.getName());
                        case "shortName":
                            return filterValue.equals(client.getShortName());
                        case "address":
                            return filterValue.equals(client.getAddress());
                        case "form":
                            return filterValue.equals(client.getForm());
                        default:
                            return true;
                    }
                });
            }

            List<ClientModel> clients = clientStream.collect(Collectors.toList());

            if (sort != null && !sort.isEmpty()) {
                Comparator<ClientModel> comparator = getComparator(sort);
                if (comparator != null) {
                    clients.sort(comparator);
                }
            }

            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    String.format("Something went wrong \n %s", e.getMessage())
            );
        }
    }

    private Comparator<ClientModel> getComparator(String sort) {
        switch (sort) {
            case "name":
                return Comparator.comparing(ClientModel::getName);
            case "shortName":
                return Comparator.comparing(ClientModel::getShortName);
            case "address":
                return Comparator.comparing(ClientModel::getAddress);
            case "form":
                return Comparator.comparing(ClientModel::getForm);
            default:
                return null;
        }
    }


    @Override
    public ResponseEntity<?> createClient(ClientEntity clientEntity) {
        try {
            saveClient(clientEntity);
            return ResponseEntity.ok("Client created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    String.format("Something went wrong \n %s", e.getMessage())
            );
        }
    }

    @Override
    public ResponseEntity<String> updateClient(Long id, ClientEntity clientEntity) {
        try {
            return clientRepository.findById(id)
                    .map(existingClient -> {
                        existingClient.setName(clientEntity.getName());
                        existingClient.setShortName(clientEntity.getShortName());
                        existingClient.setAddress(clientEntity.getAddress());
                        existingClient.setForm(clientEntity.getForm());
                        saveClient(existingClient);
                        return ResponseEntity.ok("Client updated successfully");
                    })
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    String.format("Something went wrong \n %s", e.getMessage())
            );
        }
    }


    @Override
    public ResponseEntity<String> deleteClient(Long id) {
        return clientRepository.findById(id)
                .map(client -> {
                    clientRepository.delete(client);
                    return ResponseEntity.ok("Client deleted successfully");
                }).orElse(ResponseEntity.notFound().build());
    }
}
