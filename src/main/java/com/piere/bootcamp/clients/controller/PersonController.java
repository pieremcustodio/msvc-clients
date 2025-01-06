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

import com.piere.bootcamp.clients.model.dto.PersonDto;
import com.piere.bootcamp.clients.service.PersonService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/api/persons")
public class PersonController {
    
    @Autowired
    private PersonService personService;

    /**
     * POST /api/persons : Create person
     * Create a new person
     *
     * @param personDto  (required)
     * @return Person created (status code 201)
     *         or Bad request (status code 400)
     *         or already exists (status code 409)
     */
    @ApiOperation(value = "Create person", nickname = "createPerson", notes = "Create a new person", response = PersonDto.class, tags = { "persons", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Person created", response = PersonDto.class),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 409, message = "already exists") })
    @PostMapping(
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    Mono<ResponseEntity<PersonDto>> createPerson(@ApiParam(value = "", required = true)  @Valid @RequestBody PersonDto personDto) {
        return personService.createPerson(personDto)
                .map(person -> ResponseEntity.created(URI.create("/api/persons/")).body(person));
    }

    /**
     * DELETE /api/persons : Person deleted
     * Delete an existing person
     *
     * @param personDto  (required)
     * @return Person deleted (status code 200)
     *         or Bad request (status code 400)
     *         or Not found (status code 404)
     */
    @ApiOperation(value = "Person deleted", nickname = "deletePerson", notes = "Delete an existing person", response = PersonDto.class, responseContainer = "List", tags = { "persons", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Person deleted", response = PersonDto.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not found") })
    @DeleteMapping(
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    public Mono<ResponseEntity<Void>> deletePerson(@ApiParam(value = "", required = true)  @Valid @RequestBody PersonDto personDto) {
        return personService.deletePerson(personDto)
                .map(person -> ResponseEntity.ok().body(person));
    }

    /**
     * GET /api/persons : Get all persons
     * Use to request all persons
     *
     * @return A list of persons (status code 200)
     */
    @ApiOperation(value = "Get all persons", nickname = "findAllPersons", notes = "Use to request all persons", response = PersonDto.class, responseContainer = "List", tags = { "persons", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "A list of persons", response = PersonDto.class, responseContainer = "List") })
    @GetMapping(
        produces = { "application/json" }
    )
    public Mono<ResponseEntity<Flux<PersonDto>>> findAllPersons() {
        return Mono.just(ResponseEntity.ok().body(personService.findAllPersons()));
    }

    /**
     * PUT /api/persons : Update an existing person
     * Update a person
     *
     * @param personDto  (required)
     * @return Person updated (status code 200)
     *         or Bad request (status code 400)
     *         or Not found (status code 404)
     */
    @ApiOperation(value = "Update an existing person", nickname = "updatePerson", notes = "Update a person", response = PersonDto.class, tags = { "persons", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Person updated", response = PersonDto.class),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not found") })
    @PutMapping(
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    public Mono<ResponseEntity<PersonDto>> updatePerson(@ApiParam(value = "", required = true)  @Valid @RequestBody PersonDto personDto) {
        return personService.updatePerson(personDto)
                .map(person -> ResponseEntity.ok(person));
    }
}
