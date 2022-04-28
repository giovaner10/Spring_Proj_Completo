package com.example.spring_proj_completo.controller;

import com.example.spring_proj_completo.domain.Anime;
import com.example.spring_proj_completo.requests.AnimePostRequestBody;
import com.example.spring_proj_completo.requests.AnimePutRequestBody;
import com.example.spring_proj_completo.service.AnimeService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/animes")
@AllArgsConstructor
@Log4j2
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<Page<Anime>> listPageable(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(animeService.listAll(pageable));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Anime>> list(){
        return ResponseEntity.ok(animeService.listAllNonPageable());
    }


    @GetMapping(path = "/findname")
    public ResponseEntity<List<Anime>> findByName(@RequestParam("name") String name){
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id){
        return ResponseEntity.ok(animeService.findByIdOrElseBadRequestException(id));
    }

    @GetMapping(path = "admin/{id}")
    public ResponseEntity<Anime> findByIdAuth(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails ){
        log.info(userDetails);
        return ResponseEntity.ok(Anime.builder().name(userDetails.getUsername()).build());
    }

    @PostMapping("/admin")
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody anime){
        return new ResponseEntity<>(animeService.save(anime), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/admin/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        animeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/admin/{id}")
    public ResponseEntity<Anime> update(@RequestBody AnimePutRequestBody anime , @PathVariable Long id){
        anime.setId(id);
        return new ResponseEntity<>(animeService.replace(anime), HttpStatus.CREATED);

    }


}
