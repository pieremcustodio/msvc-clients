package com.piere.bootcamp.clients.controller;


import javax.validation.Valid;

import com.piere.bootcamp.clients.model.dto.BankResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.piere.bootcamp.clients.model.dto.LegalRepresentativeDto;
import com.piere.bootcamp.clients.service.LegalRepresentativeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/api/legalrepresentatives")
public class LegalRepresentativeController {

    @Autowired
    private LegalRepresentativeService legalRepresentativeService;


    /**
     * POST /api/legal-representatives : Crear representante legal
     * Crear un nuevo representante legal
     *
     * @param legalRepresentativeDto  (required)
     * @return Representante legal creado correctamente (status code 201)
     *         or Solicitud mal formada (status code 400)
     *         or Recurso ya existente (status code 409)
     */
    @ApiOperation(value = "Crear representante legal", nickname = "createLegalRepresentative",
            notes = "Crear un nuevo representante legal", response = BankResponse.class, tags = { "legalRepresentatives", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Representante legal creado correctamente", response = BankResponse.class),
            @ApiResponse(code = 400, message = "Solicitud mal formada", response = BankResponse.class),
            @ApiResponse(code = 409, message = "Recurso ya existente", response = BankResponse.class) })
    @PostMapping(
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    public Mono<ResponseEntity<BankResponse>> createLegalRepresentative(@ApiParam(value = "", required = true)  @Valid @RequestBody LegalRepresentativeDto legalRepresentativeDto) {
        return legalRepresentativeService.createLegalRepresentative(legalRepresentativeDto)
                .map(legalRepresentative -> ResponseEntity.status(HttpStatus.CREATED).body(BankResponse.ok("Representante legal creado correctamente", legalRepresentative)));
    }

    /**
     * DELETE /api/legal-representatives : Eliminar representante legal
     * Eliminar un representante legal existente
     *
     * @param id ID del representante legal a eliminar (required)
     * @return Representante legal eliminado correctamente (status code 200)
     *         or Solicitud mal formada (status code 400)
     *         or Recurso no encontrado (status code 404)
     */
    @ApiOperation(value = "Eliminar representante legal", nickname = "deleteLegalRepresentative",
            notes = "Eliminar un representante legal existente", response = BankResponse.class, tags = { "legalRepresentatives", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Representante legal eliminado correctamente", response = BankResponse.class),
            @ApiResponse(code = 400, message = "Solicitud mal formada", response = BankResponse.class),
            @ApiResponse(code = 404, message = "Recurso no encontrado", response = BankResponse.class) })
    @DeleteMapping(
            value = "/{id}",
            produces = { "application/json" }
    )
    public Mono<ResponseEntity<BankResponse>> deleteLegalRepresentative(@PathVariable("id") String id) {
        return legalRepresentativeService.deleteLegalRepresentativeById(id)
                .then(Mono.just(ResponseEntity.ok(
                        BankResponse.ok("Representante legal eliminado correctamente", null)
                )));
    }

    /**
     * GET /api/legal-representatives : Buscar representantes legales
     * Buscar todos los representantes legales
     *
     * @return Lista de representantes legales (status code 200)
     */
    @ApiOperation(value = "Buscar representantes legales", nickname = "findAllLegalRepresentatives",
            notes = "Buscar todos los representantes legales", response = BankResponse.class, tags = { "legalRepresentatives", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de representantes legales", response = BankResponse.class) })
    @GetMapping(
            produces = { "application/json" }
    )
    public Mono<ResponseEntity<BankResponse>> findAllLegalRepresentatives() {
        return legalRepresentativeService.findAllLegalRepresentatives()
                .collectList()
                .map(legalRepresentatives -> ResponseEntity.ok(BankResponse.ok("Lista de representantes legales", legalRepresentatives)));
    }

    /**
     * PUT /api/legal-representatives : Actualizar representante legal
     * Actualizar un representante legal existente
     *
     * @param legalRepresentativeDto  (required)
     * @return Representante legal actualizado correctamente (status code 200)
     *         or Solicitud mal formada (status code 400)
     *         or Recurso no encontrado (status code 404)
     */
    @ApiOperation(value = "Actualizar representante legal", nickname = "updateLegalRepresentative",
            notes = "Actualizar un representante legal existente", response = BankResponse.class, tags = { "legalRepresentatives", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Representante legal actualizado correctamente", response = BankResponse.class),
            @ApiResponse(code = 400, message = "Solicitud mal formada", response = BankResponse.class),
            @ApiResponse(code = 404, message = "Recurso no encontrado", response = BankResponse.class) })
    @PutMapping(
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    public Mono<ResponseEntity<BankResponse>> updateLegalRepresentative(@ApiParam(value = "", required = true)  @Valid @RequestBody LegalRepresentativeDto legalRepresentativeDto) {
        return legalRepresentativeService.updateLegalRepresentative(legalRepresentativeDto)
                .map(legalRepresentative -> ResponseEntity.ok(BankResponse.ok("Representante legal actualizado correctamente", legalRepresentative)));
    }

    
}
