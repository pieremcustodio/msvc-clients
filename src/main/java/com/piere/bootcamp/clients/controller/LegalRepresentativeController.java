package com.piere.bootcamp.clients.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piere.bootcamp.clients.model.dto.LegalRepresentativeDto;
import com.piere.bootcamp.clients.service.LegalRepresentativeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/api/legalrepresentatives")
public class LegalRepresentativeController {

    @Autowired
    private LegalRepresentativeService legalRepresentativeService;
    
    
    /**
     * POST /api/legal-representantives : Create legal representative
     * Create a new legal representative
     *
     * @param legalRepresentativeDto  (required)
     * @return Legal representative created (status code 201)
     *         or Bad request (status code 400)
     *         or already exists (status code 409)
     */
    @ApiOperation(value = "Create legal representative", nickname = "createLegalRepresentative", notes = "Create a new legal representative",
            response = LegalRepresentativeDto.class, tags = { "legalRepresentatives", })
    @PostMapping(
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    Mono<ResponseEntity<LegalRepresentativeDto>> createLegalRepresentative(@ApiParam(value = "", required = true)  @Valid @RequestBody LegalRepresentativeDto legalRepresentativeDto) {
        return legalRepresentativeService.createLegalRepresentative(legalRepresentativeDto)
                .map(legalRepresentative -> ResponseEntity.created(URI.create("/api/legalrepresentatives/")).body(legalRepresentative));
    }

    /**
     * DELETE /api/legal-representantives : Legal representative deleted
     * Delete an existing legal representative
     *
     * @param legalRepresentativeDto  (required)
     * @return Legal representative deleted (status code 200)
     *         or Bad request (status code 400)
     */
    @ApiOperation(value = "Legal representative deleted", nickname = "deleteLegalRepresentative", notes = "Delete an existing legal representative",
            response = LegalRepresentativeDto.class, responseContainer = "List", tags = { "legalRepresentatives", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Legal representative deleted", response = LegalRepresentativeDto.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad request") })
    @DeleteMapping(
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    Mono<ResponseEntity<Void>> deleteLegalRepresentative(@ApiParam(value = "", required = true)  @Valid @RequestBody LegalRepresentativeDto legalRepresentativeDto) {
        return legalRepresentativeService.deleteLegalRepresentative(legalRepresentativeDto)
                .map(legalRepresentative -> ResponseEntity.ok().body(legalRepresentative));
    }

    /**
     * GET /api/legal-representantives : Get all legal representatives
     * Use to request all legal representatives
     *
     * @return A list of legal representatives (status code 200)
     */
    @ApiOperation(value = "Get all legal representatives", nickname = "findAllLegalRepresentatives", notes = "Use to request all legal representatives",
            response = LegalRepresentativeDto.class, responseContainer = "List", tags = { "legalRepresentatives", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "A list of legal representatives", response = LegalRepresentativeDto.class, responseContainer = "List") })
    @GetMapping(
        produces = { "application/json" }
    )
    Mono<ResponseEntity<Flux<LegalRepresentativeDto>>> findAllLegalRepresentatives() {
        return Mono.just(ResponseEntity.ok().body(legalRepresentativeService.findAllLegalRepresentatives()));
    }

    /**
     * PUT /api/legal-representantives : Update an existing legal representative
     * Update a legal representative
     *
     * @param legalRepresentativeDto  (required)
     * @return Legal representative updated (status code 200)
     *         or Bad request (status code 400)
     *         or Not found (status code 404)
     */
    @ApiOperation(value = "Update an existing legal representative", nickname = "updateLegalRepresentative", notes = "Update a legal representative",
            response = LegalRepresentativeDto.class, tags = { "legalRepresentatives", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Legal representative updated", response = LegalRepresentativeDto.class),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not found") })
    @PutMapping(
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    Mono<ResponseEntity<LegalRepresentativeDto>> updateLegalRepresentative(@ApiParam(value = "", required = true)  @Valid @RequestBody LegalRepresentativeDto legalRepresentativeDto) {
        return legalRepresentativeService.updateLegalRepresentative(legalRepresentativeDto)
                .map(legalRepresentative -> ResponseEntity.ok(legalRepresentative));
    }

    
}
