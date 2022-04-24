package com.example.spring_proj_completo.service;

import com.example.spring_proj_completo.config.AnimeAssembler;
import com.example.spring_proj_completo.domain.Anime;
import com.example.spring_proj_completo.repository.AnimeRepository;
import com.example.spring_proj_completo.requests.AnimePostRequestBody;
import com.example.spring_proj_completo.requests.AnimePutRequestBody;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;
    private final AnimeAssembler animeAssembler;

    public List<Anime> listAll(){
        return animeRepository.findAll();
    }

    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }

    public Anime findByIdOrElseBadRequestException(Long id){
        return animeRepository.findById(id)
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "NOT FOUND"));
    }

    public Anime save(AnimePostRequestBody animePostRequestBody){
        Anime newAnime = animeAssembler.toAnimeSave(animePostRequestBody);
        return animeRepository.save(newAnime);
    }

    public void delete(Long id){

        animeRepository.delete(findByIdOrElseBadRequestException(id));
    }

    public Anime replace(AnimePutRequestBody animePutRequestBody){
        findByIdOrElseBadRequestException(animePutRequestBody.getId());
        Anime updateAnime = animeAssembler.toAnimeUpdate(animePutRequestBody);

        return animeRepository.save(updateAnime);
    }
}
