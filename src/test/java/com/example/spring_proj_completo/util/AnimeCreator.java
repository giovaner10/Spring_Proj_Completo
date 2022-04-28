package com.example.spring_proj_completo.util;

import com.example.spring_proj_completo.domain.Anime;
import com.example.spring_proj_completo.requests.AnimePostRequestBody;
import com.example.spring_proj_completo.requests.AnimePutRequestBody;

public  class  AnimeCreator {

    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("Novo anime")
                .build();
    }

    public static Anime createAnimeValid(){
        return Anime.builder()
                .name("Novo anime")
                .id(1L)
                .build();
    }

    public static   Anime createAnimeUpdated(){
        return Anime.builder()
                .name("Novo anime 2")
                .id(1L)
                .build();
    }

    public static AnimePostRequestBody createAnimePost(){
        return AnimePostRequestBody.builder()
                .name(createAnimeValid().getName())
                .build();
    }

    public static AnimePutRequestBody createAnimePut(){
        return AnimePutRequestBody.builder()
                .name("Novo anime 2")
                .id(createAnimeValid().getId())
                .build();
    }

}
