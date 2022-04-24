package com.example.spring_proj_completo.config;

import com.example.spring_proj_completo.domain.Anime;
import com.example.spring_proj_completo.requests.AnimePostRequestBody;
import com.example.spring_proj_completo.requests.AnimePutRequestBody;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AnimeAssembler {
    private ModelMapper modelMapper;


    public Anime toAnimeSave(AnimePostRequestBody animePostRequestBody){
        return modelMapper.map(animePostRequestBody, Anime.class);
    }

    public Anime toAnimeUpdate(AnimePutRequestBody animePutRequestBody){
        return modelMapper.map(animePutRequestBody, Anime.class);
    }
}
