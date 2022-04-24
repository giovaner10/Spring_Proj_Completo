package com.example.spring_proj_completo.controller;

import com.example.spring_proj_completo.domain.Anime;
import com.example.spring_proj_completo.requests.AnimePostRequestBody;
import com.example.spring_proj_completo.requests.AnimePutRequestBody;
import com.example.spring_proj_completo.service.AnimeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animes")
@AllArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<List<Anime>> list(){
        return ResponseEntity.ok(animeService.listAll());
    }

    @GetMapping(path = "/findname", params = "name")
    public ResponseEntity<List<Anime>> findByName(String name){
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id){
        return ResponseEntity.ok(animeService.findByIdOrElseBadRequestException(id));
    }

    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody AnimePostRequestBody anime){
        return new ResponseEntity<>(animeService.save(anime), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        animeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Anime> save(@RequestBody AnimePutRequestBody anime , @PathVariable Long id){
        anime.setId(id);
        return new ResponseEntity<>(animeService.replace(anime), HttpStatus.CREATED);

    }


}
