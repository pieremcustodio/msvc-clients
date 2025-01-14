package com.piere.bootcamp.clients.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.piere.bootcamp.clients.model.dto.BankResponse;
import com.piere.bootcamp.clients.model.dto.ClientDto;
import com.piere.bootcamp.clients.service.ClientService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/api/clients")
@Slf4j
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * POST /api/clients : Crear un nuevo cliente
     * Crear cliente
     *
     * @param clientDto  (required)
     * @return Cliente creado correctamente (status code 201)
     *         or Solicitud mal formada (status code 400)
     *         or Recurso ya existente (status code 409)
     */
    @ApiOperation(value = "Crear un nuevo cliente", nickname = "createClient",
            notes = "Crear cliente", response = BankResponse.class, tags = { "clients" })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Cliente creado correctamente", response = BankResponse.class),
            @ApiResponse(code = 400, message = "Solicitud mal formada", response = BankResponse.class),
            @ApiResponse(code = 409, message = "Recurso ya existente", response = BankResponse.class) })
    @PostMapping(
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    public Mono<ResponseEntity<BankResponse>> createClient(@ApiParam(value = "", required = true) @Valid @RequestBody ClientDto clientDto) {
        return clientService.createClient(clientDto)
                .map(client -> ResponseEntity.status(HttpStatus.CREATED).body(BankResponse.ok("Cliente creado correctamente", client)))
                .doOnError(error -> log.error(error.getMessage()));
    }


    /**
     * DELETE /api/clients : Eliminar cliente
     * Eliminar un cliente existente
     *
     * @param id ID del cliente (required)
     * @return Cliente eliminado correctamente (status code 200)
     *         or Solicitud mal formada (status code 400)
     *         or Recurso no encontrado (status code 404)
     */
    @ApiOperation(value = "Eliminar cliente", nickname = "deleteClient",
            notes = "Eliminar un cliente existente", response = BankResponse.class, tags = { "clients" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente eliminado correctamente", response = BankResponse.class),
            @ApiResponse(code = 400, message = "Solicitud mal formada", response = BankResponse.class),
            @ApiResponse(code = 404, message = "Recurso no encontrado", response = BankResponse.class) })
    @DeleteMapping(
            value = "/{id}",
            produces = { "application/json" }
    )
    public Mono<ResponseEntity<BankResponse>> deleteClient(@ApiParam(value = "ID del cliente", required = true) @PathVariable("id") String id) {
        return clientService.deleteClientById(id)
                .then(Mono.just(ResponseEntity.ok(BankResponse.ok("Cliente eliminado correctamente", null))));
    }


    /**
     * GET /api/clients : Buscar todos los clientes
     * Buscar todos los clientes
     *
     * @return Listado de clientes (status code 200)
     */
    @ApiOperation(value = "Buscar todos los clientes", nickname = "findAllClients",
            notes = "Buscar todos los clientes", response = BankResponse.class, responseContainer = "List", tags = { "clients" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Listado de clientes", response = BankResponse.class, responseContainer = "List") })
    @GetMapping(
            produces = { "application/json" }
    )
    public Mono<ResponseEntity<BankResponse>> findAllClients() {
        return clientService.findAllClients()
                .collectList()
                .map(clientList -> ResponseEntity.ok(BankResponse.ok("Listado de clientes", clientList)));
    }

    /**
     * POST /api/clients/findAllByIdList : Busqueda de clientes por lista de IDs
     * Busqueda de clientes por lista de IDs
     *
     * @param idList  (required)
     * @return A list of clients (status code 200)
     *         or Recurso no encontrado (status code 404)
     */
    @ApiOperation(value = "Busqueda de clientes por lista de IDs", nickname = "findAllByIdList",
            notes = "Busqueda de clientes por lista de IDs", response = BankResponse.class, responseContainer = "List", tags = { "clients" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A list of clients", response = BankResponse.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Recurso no encontrado", response = BankResponse.class) })
    @PostMapping(
            value = "/findAllByIdList",
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    public Mono<ResponseEntity<BankResponse>> findAllByIdList(@ApiParam(value = "", required = true) @Valid @RequestBody List<String> idList) {
        return clientService.findAllByIdList(idList)
                .collectList()
                .map(clientList -> ResponseEntity.ok(BankResponse.ok("Listado de clientes por lista de IDs", clientList)));
    }

    /**
     * GET /api/clients/findByDocumentNumber/{documentNumber} : Búsqueda de cliente por número de documento
     * Búsqueda de cliente por número de documento
     *
     * @param documentNumber Número de documento del cliente a buscar (required)
     * @return Obtener cliente por número de documento (status code 200)
     *         or Recurso no encontrado (status code 404)
     */
    @ApiOperation(value = "Búsqueda de cliente por número de documento", nickname = "findByDocumentNumber",
            notes = "Búsqueda de cliente por número de documento", response = BankResponse.class, tags = { "clients" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Obtener cliente por número de documento", response = BankResponse.class),
            @ApiResponse(code = 404, message = "Recurso no encontrado", response = BankResponse.class) })
    @GetMapping(
            value = "/findByDocumentNumber/{documentNumber}",
            produces = { "application/json" }
    )
    public Mono<ResponseEntity<BankResponse>> findByDocumentNumber(@ApiParam(value = "Número de documento del cliente a buscar", required = true)
                                                                   @PathVariable("documentNumber") String documentNumber) {
        return clientService.findByDocumentNumber(documentNumber)
                .map(client -> ResponseEntity.ok(BankResponse.ok("Obtener cliente por número de documento", client)));
    }

    /**
     * GET /api/clients/{id} : Buscar cliente por ID
     * Buscar cliente por ID
     *
     * @param id ID del cliente a retornar (required)
     * @return Obtener cliente por ID (status code 200)
     *         or Recurso no encontrado (status code 404)
     */
    @ApiOperation(value = "Buscar cliente por ID", nickname = "findById",
            notes = "Buscar cliente por ID", response = BankResponse.class, tags = { "clients" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Obtener cliente por ID", response = BankResponse.class),
            @ApiResponse(code = 404, message = "Recurso no encontrado", response = BankResponse.class) })
    @GetMapping(
            value = "/{id}",
            produces = { "application/json" }
    )
    public Mono<ResponseEntity<BankResponse>> findById(@ApiParam(value = "ID of client to return", required = true) @PathVariable("id") String id) {
        return clientService.findById(id)
                .map(client -> ResponseEntity.ok(BankResponse.ok("Obtener cliente por ID", client)));
    }

    /**
     * PUT /api/clients : Actualizar un cliente existente
     * Actualizar un cliente
     *
     * @param clientDto  (required)
     * @return Cliente actualizado correctamente (status code 200)
     *         or Solicitud mal formada (status code 400)
     *         or Recurso no encontrado (status code 404)
     */
    @ApiOperation(value = "Actualizar un cliente existente", nickname = "updateClient",
            notes = "Actualizar un cliente", response = BankResponse.class, tags = { "clients" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente actualizado correctamente", response = BankResponse.class),
            @ApiResponse(code = 400, message = "Solicitud mal formada", response = BankResponse.class),
            @ApiResponse(code = 404, message = "Recurso no encontrado", response = BankResponse.class) })
    @PutMapping(
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    public Mono<ResponseEntity<BankResponse>> updateClient(@ApiParam(value = "", required = true) @Valid @RequestBody ClientDto clientDto) {
        return clientService.updateClient(clientDto)
                .map(client -> ResponseEntity.ok(BankResponse.ok("Cliente actualizado correctamente", client)));
    }
}
