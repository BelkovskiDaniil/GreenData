package com.example.testtask.controller;


import com.example.testtask.entity.ClientEntity;
import com.example.testtask.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<?> getClients(@RequestParam(name = "sort_by", required = false) String sort,
                                        @RequestParam(name = "filter_by", required = false) String filter,
                                        @RequestParam(name = "filter_value", required = false) String filterValue) {
        return clientService.getClients(sort, filter, filterValue);
    }

    @PostMapping
    public ResponseEntity<?> createClient(@Valid @RequestBody ClientEntity clientEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }
        return clientService.createClient(clientEntity);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable("id") Long id, @Valid @RequestBody ClientEntity clientEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }
        return clientService.updateClient(id, clientEntity);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable("id") Long id) {
        return clientService.deleteClient(id);
    }

}
