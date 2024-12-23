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

import com.piere.bootcamp.clients.model.dto.AuthorizedSignatoryDto;
import com.piere.bootcamp.clients.service.AuthorizedSignatoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/api/authorizedsignatories")
public class AuthorizedSignatoryController {

    @Autowired
    private AuthorizedSignatoryService authorizedSignatoryService;
    
    /**
     * POST /api/authorized-signatories : Create authorized signatory
     * Create a new authorized signatory
     *
     * @param authorizedSignatoryDto  (required)
     * @return Authorized signatory created (status code 201)
     *         or Bad request (status code 400)
     *         or already exists (status code 409)
     */
    @ApiOperation(value = "Create authorized signatory", nickname = "createAuthorizedSignatory", notes = "Create a new authorized signatory", response = AuthorizedSignatoryDto.class, tags={ "authorizedSignatories", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Authorized signatory created", response = AuthorizedSignatoryDto.class),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 409, message = "already exists") })
    @PostMapping(
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    Mono<ResponseEntity<AuthorizedSignatoryDto>> createAuthorizedSignatory(@ApiParam(value = "" ,required=true )  @Valid @RequestBody AuthorizedSignatoryDto authorizedSignatoryDto) {
        return authorizedSignatoryService.createAuthorizedSignatory(authorizedSignatoryDto)
                .map(authorizedSignatory -> ResponseEntity.created(URI.create("/api/authorizedsignatories")).body(authorizedSignatory));
    }


    /**
     * DELETE /api/authorized-signatories : Authorized signatory deleted
     * Delete an existing authorized signatory
     *
     * @param authorizedSignatoryDto  (required)
     * @return Authorized signatory deleted (status code 200)
     */
    @ApiOperation(value = "Authorized signatory deleted", nickname = "deleteAuthorizedSignatory", notes = "Delete an existing authorized signatory", response = AuthorizedSignatoryDto.class, responseContainer = "List", tags={ "authorizedSignatories", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Authorized signatory deleted", response = AuthorizedSignatoryDto.class, responseContainer = "List") })
    @DeleteMapping(
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    Mono<ResponseEntity<Void>> deleteAuthorizedSignatory(@ApiParam(value = "" ,required=true )  @Valid @RequestBody AuthorizedSignatoryDto authorizedSignatoryDto) {
        return authorizedSignatoryService.deleteAuthorizedSignatory(authorizedSignatoryDto)
                .map(authorizedSignatory -> ResponseEntity.ok().body(authorizedSignatory));
    }


    /**
     * GET /api/authorized-signatories : Get all authorized signatories
     * Use to request all authorized signatories
     *
     * @return A list of authorized signatories (status code 200)
     */
    @ApiOperation(value = "Get all authorized signatories", nickname = "findAllAuthorizedSignatories", notes = "Use to request all authorized signatories", response = AuthorizedSignatoryDto.class, responseContainer = "List", tags={ "authorizedSignatories", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "A list of authorized signatories", response = AuthorizedSignatoryDto.class, responseContainer = "List") })
    @GetMapping(
        produces = { "application/json" }
    )
    Mono<ResponseEntity<Flux<AuthorizedSignatoryDto>>> findAllAuthorizedSignatories() {
        return Mono.just(ResponseEntity.ok().body(authorizedSignatoryService.findAllAuthorizedSignatories()));
    }


    /**
     * PUT /api/authorized-signatories : Update an existing authorized signatory
     * Update an authorized signatory
     *
     * @param authorizedSignatoryDto  (required)
     * @return Authorized signatory updated (status code 200)
     *         or Bad request (status code 400)
     *         or Not found (status code 404)
     */
    @ApiOperation(value = "Update an existing authorized signatory", nickname = "updateAuthorizedSignatory", notes = "Update an authorized signatory", response = AuthorizedSignatoryDto.class, tags={ "authorizedSignatories", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Authorized signatory updated", response = AuthorizedSignatoryDto.class),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not found") })
    @PutMapping(
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    Mono<ResponseEntity<AuthorizedSignatoryDto>> updateAuthorizedSignatory(@ApiParam(value = "" ,required=true )  @Valid @RequestBody AuthorizedSignatoryDto authorizedSignatoryDto) {
        return authorizedSignatoryService.updateAuthorizedSignatory(authorizedSignatoryDto)
                .map(authorizedSignatory -> ResponseEntity.ok(authorizedSignatory));
    }

}
