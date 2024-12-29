package com.piere.bootcamp.clients.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piere.bootcamp.clients.model.dto.ClientDto;
import com.piere.bootcamp.clients.service.ClientService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * POST /api/clients : Create client
     * Create a new client
     *
     * @param clientDto  (required)
     * @return Client created (status code 201)
     *         or Bad request (status code 400)
     *         or already exists (status code 409)
     */
    @ApiOperation(value = "Create client", nickname = "create", notes = "Create a new client", response = ClientDto.class, tags = { "clients", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Client created", response = ClientDto.class),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 409, message = "already exists") })
    @PostMapping(
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    Mono<ResponseEntity<ClientDto>> createClient(@ApiParam(value = "", required = true)  @Valid @RequestBody ClientDto clientDto) {
        return clientService.createClient(clientDto)
                .map(client -> ResponseEntity.created(URI.create("/api/clients/")).body(client));
    }


    /**
     * DELETE /api/clients : Client deleted
     * Delete an existing client
     *
     * @param clientDto  (required)
     * @return Client deleted (status code 200)
     *         or Bad request (status code 400)
     *         or Not found (status code 404)
     */
    @ApiOperation(value = "Client deleted", nickname = "delete", notes = "Delete an existing client", response = ClientDto.class, responseContainer = "List", tags = { "clients", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Client deleted", response = ClientDto.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not found") })
    @DeleteMapping(
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    Mono<ResponseEntity<Void>> deleteClient(@ApiParam(value = "", required = true)  @Valid @RequestBody ClientDto clientDto) {
        return clientService.deleteClient(clientDto)
                .map(client -> ResponseEntity.ok().build());
    }


    /**
     * GET /api/clients : Get all clients
     * Use to request all clients
     *
     * @return A list of clients (status code 200)
     */
    @ApiOperation(value = "Get all clients", nickname = "findAll", notes = "Use to request all clients", response = ClientDto.class, responseContainer = "List", tags = { "clients", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "A list of clients", response = ClientDto.class, responseContainer = "List") })
    @GetMapping(
        produces = { "application/json" }
    )
    Mono<ResponseEntity<Flux<ClientDto>>> findAllClients() {
        return Mono.just(ResponseEntity.ok(clientService.findAllClients()));
    }

    /**
     * POST /api/clients/findAllByIdList : Get clients by ID list
     * Use to request a list of clients by ID
     *
     * @param requestBody  (required)
     * @return A list of clients (status code 200)
     *         or Not found (status code 404)
     */
    @ApiOperation(value = "Get clients by ID list", nickname = "findAllByIdList", notes = "Use to request a list of clients by ID",
            response = ClientDto.class, responseContainer = "List", tags = { "clients", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "A list of clients", response = ClientDto.class, responseContainer = "List"),
        @ApiResponse(code = 404, message = "Not found") })
    @PostMapping(
        value = "/findAllByIdList",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    Mono<ResponseEntity<Flux<ClientDto>>> findAllByIdList(@ApiParam(value = "", required = true)  @Valid @RequestBody List<String> idList) {
        return Mono.just(ResponseEntity.ok(clientService.findAllByIdList(idList)));
    }

    /**
     * GET /api/clients/findByDocumentNumber/{documentNumber} : Get client by document number
     * Use to request a client by document number
     *
     * @param documentNumber Document number of client to return (required)
     * @return A client (status code 200)
     *         or Not found (status code 404)
     */
    @ApiOperation(value = "Get client by document number", nickname = "findByDocumentNumber", notes = "Use to request a client by document number", response = ClientDto.class, tags = { "clients", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "A client", response = ClientDto.class),
        @ApiResponse(code = 404, message = "Not found") })
    @GetMapping(
        value = "/findByDocumentNumber/{documentNumber}",
        produces = { "application/json" }
    )
    Mono<ResponseEntity<ClientDto>> findByDocumentNumber(@ApiParam(value = "Document number of client to return", required = true) @PathVariable("documentNumber") String documentNumber) {
        return clientService.findByDocumentNumber(documentNumber)
                .map(client -> ResponseEntity.ok(client));
    }

    /**
     * GET /api/clients/{id} : Get client by ID
     * Use to request a client by ID
     *
     * @param id ID of client to return (required)
     * @return A client (status code 200)
     *         or Not found (status code 404)
     */
    @ApiOperation(value = "Get client by ID", nickname = "findById", notes = "Use to request a client by ID", response = ClientDto.class, tags = { "clients", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "A client", response = ClientDto.class),
        @ApiResponse(code = 404, message = "Not found") })
    @GetMapping(
        value = "/{id}",
        produces = { "application/json" }
    )
    Mono<ResponseEntity<ClientDto>> findById(@ApiParam(value = "ID of client to return", required = true) @PathVariable("id") String id) {
        return clientService.findByDocumentNumber(id)
        .map(client -> ResponseEntity.ok(client));
    }

    /**
     * PUT /api/clients : Update an existing client
     * Update a client
     *
     * @param clientDto  (required)
     * @return Client updated (status code 200)
     *         or Bad request (status code 400)
     *         or Not found (status code 404)
     */
    @ApiOperation(value = "Update an existing client", nickname = "update", notes = "Update a client", response = ClientDto.class, tags = { "clients", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Client updated", response = ClientDto.class),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not found") })
    @PutMapping(
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    Mono<ResponseEntity<ClientDto>> updateClient(@ApiParam(value = "", required = true)  @Valid @RequestBody ClientDto clientDto) {
        return clientService.updateClient(clientDto)
                .map(client -> ResponseEntity.ok(client));
    }
}
