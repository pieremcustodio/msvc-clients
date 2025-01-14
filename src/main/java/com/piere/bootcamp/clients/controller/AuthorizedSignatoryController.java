package com.piere.bootcamp.clients.controller;

import javax.validation.Valid;

import com.piere.bootcamp.clients.model.dto.BankResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.piere.bootcamp.clients.model.dto.AuthorizedSignatoryDto;
import com.piere.bootcamp.clients.service.AuthorizedSignatoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/api/authorizedsignatories")
public class AuthorizedSignatoryController {

    @Autowired
    private AuthorizedSignatoryService authorizedSignatoryService;

    /**
     * POST /api/authorized-signatories : Crear firmante autorizado
     * Crear un nuevo firmante autorizado
     *
     * @param authorizedSignatoryDto (required)
     * @return Firmante autorizado creado correctamente (status code 201)
     * or Solicitud mal formada (status code 400)
     * or Recurso ya existente (status code 409)
     */
    @ApiOperation(value = "Crear firmante autorizado", nickname = "createAuthorizedSignatory",
            notes = "Crear un nuevo firmante autorizado", response = BankResponse.class, tags = {"authorizedSignatories"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Firmante autorizado creado correctamente", response = BankResponse.class),
            @ApiResponse(code = 400, message = "Solicitud mal formada", response = BankResponse.class),
            @ApiResponse(code = 409, message = "Recurso ya existente", response = BankResponse.class)})
    @PostMapping(
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public Mono<ResponseEntity<BankResponse>> createAuthorizedSignatory(
            @ApiParam(value = "", required = true) @Valid @RequestBody AuthorizedSignatoryDto authorizedSignatoryDto) {
        return authorizedSignatoryService.createAuthorizedSignatory(authorizedSignatoryDto)
                .map(authorizedSignatory -> ResponseEntity.status(HttpStatus.CREATED).body(BankResponse.ok("Firmante autorizado creado correctamente", authorizedSignatory)));
    }

    /**
     * DELETE /api/authorized-signatories : Eliminar firmante autorizado
     * Eliminar un firmante autorizado existente
     *
     * @param id ID del firmante autorizado a eliminar (required)
     * @return Firmante autorizado eliminado correctamente (status code 200)
     * or Solicitud mal formada (status code 400)
     * or Recurso no encontrado (status code 404)
     */
    @ApiOperation(value = "Eliminar firmante autorizado", nickname = "deleteAuthorizedSignatory",
            notes = "Eliminar un firmante autorizado existente", response = BankResponse.class, tags = {"authorizedSignatories"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Firmante autorizado eliminado correctamente", response = BankResponse.class),
            @ApiResponse(code = 400, message = "Solicitud mal formada", response = BankResponse.class),
            @ApiResponse(code = 404, message = "Recurso no encontrado", response = BankResponse.class)})
    @DeleteMapping(
            value = "/{id}",
            produces = {"application/json"}
    )
    public Mono<ResponseEntity<BankResponse>> deleteAuthorizedSignatoryById(
            @ApiParam(value = "", required = true) @PathVariable("id") String id) {
        return authorizedSignatoryService.deleteAuthorizedSignatoryById(id)
                .then(Mono.just(ResponseEntity.ok(BankResponse.ok("Firmante autorizado eliminado correctamente", null))));
    }

    /**
     * GET /api/authorized-signatories : Buscar firmantes autorizados
     * Buscar todos los firmantes autorizados
     *
     * @return Lista de firmantes autorizados (status code 200)
     */
    @ApiOperation(value = "Buscar firmantes autorizados", nickname = "findAllAuthorizedSignatories",
            notes = "Buscar todos los firmantes autorizados", response = BankResponse.class, tags = {"authorizedSignatories"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de firmantes autorizados", response = BankResponse.class)})
    @GetMapping(
            produces = {"application/json"}
    )
    public Mono<ResponseEntity<BankResponse>> findAllAuthorizedSignatories() {
        return authorizedSignatoryService.findAllAuthorizedSignatories()
                .collectList()
                .map(authorizedSignatories -> ResponseEntity.ok(BankResponse.ok("Lista de firmantes autorizados", authorizedSignatories)));
    }

    /**
     * PUT /api/legal-representatives : Actualizar representante legal
     * Actualizar un representante legal existente
     *
     * @param authorizedSignatoryDto (required)
     * @return Representante legal actualizado correctamente (status code 200)
     * or Solicitud mal formada (status code 400)
     * or Recurso no encontrado (status code 404)
     */
    @ApiOperation(value = "Actualizar representante legal", nickname = "updateLegalRepresentative",
            notes = "Actualizar un representante legal existente", response = BankResponse.class, tags = {"legalRepresentatives"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Representante legal actualizado correctamente", response = BankResponse.class),
            @ApiResponse(code = 400, message = "Solicitud mal formada", response = BankResponse.class),
            @ApiResponse(code = 404, message = "Recurso no encontrado", response = BankResponse.class)})
    @PutMapping(
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public Mono<ResponseEntity<BankResponse>> updateAuthorizedSignatory(
            @ApiParam(value = "", required = true) @Valid @RequestBody AuthorizedSignatoryDto authorizedSignatoryDto) {
        return authorizedSignatoryService.updateAuthorizedSignatory(authorizedSignatoryDto)
                .map(authorizedSignatory -> ResponseEntity.ok(BankResponse.ok("Firmante autorizado actualizado correctamente", authorizedSignatory)));
    }

}
