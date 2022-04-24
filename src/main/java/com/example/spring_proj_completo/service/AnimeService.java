package com.example.spring_proj_completo.service;

import com.example.spring_proj_completo.config.AnimeAssembler;
import com.example.spring_proj_completo.domain.Anime;
import com.example.spring_proj_completo.exception.BadRequestException;
import com.example.spring_proj_completo.repository.AnimeRepository;
import com.example.spring_proj_completo.requests.AnimePostRequestBody;
import com.example.spring_proj_completo.requests.AnimePutRequestBody;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;
    private final AnimeAssembler animeAssembler;

    public Page<Anime> listAll(Pageable pageable){
        return animeRepository.findAll(pageable);
    }

    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }

    public Anime findByIdOrElseBadRequestException(Long id){
        return animeRepository.findById(id)
                .orElseThrow(()->
                        new BadRequestException("NOT FOUND"));
    }

    @Transactional
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
