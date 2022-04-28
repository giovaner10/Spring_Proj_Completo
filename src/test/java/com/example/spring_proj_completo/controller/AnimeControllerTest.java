package com.example.spring_proj_completo.controller;

import com.example.spring_proj_completo.domain.Anime;
import com.example.spring_proj_completo.requests.AnimePostRequestBody;
import com.example.spring_proj_completo.requests.AnimePutRequestBody;
import com.example.spring_proj_completo.service.AnimeService;
import com.example.spring_proj_completo.util.AnimeCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {


    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createAnimeValid()));
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);


        List<Anime> animeList = new ArrayList<>(List.of(AnimeCreator.createAnimeValid()));

        BDDMockito.when(animeServiceMock.listAllNonPageable())
                .thenReturn(animeList);

        BDDMockito.when(animeServiceMock.findByName(null))
                .thenReturn(animeList);

        BDDMockito.when(animeServiceMock.findByIdOrElseBadRequestException(ArgumentMatchers.any()))
                .thenReturn(AnimeCreator.createAnimeValid());

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createAnimeValid());

        BDDMockito.when(animeServiceMock.replace(ArgumentMatchers.any(AnimePutRequestBody.class)))
                .thenReturn(AnimeCreator.createAnimeValid());

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());



    }

    @Test
    void listAllPage() {

        String expectedName = AnimeCreator.createAnimeValid().getName();

        Page<Anime> body = animeController.listPageable(null).getBody();

        assertThat(body).isNotEmpty();

        assertThat(body.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    void listAll() {

        String expectedName = AnimeCreator.createAnimeValid().getName();

        List<Anime> body = animeController.list().getBody();

        assertThat(body).isNotEmpty();

        assertThat(body.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    void findByIdOrElseThrow() {

        Long expectedId = AnimeCreator.createAnimeValid().getId();

        Anime body = animeController.findById(null).getBody();

        assertThat(body).isNotNull();

        assertThat(body.getId()).isEqualTo(expectedId );

    }


    @Test
    void findByName() {

        String expectedName = AnimeCreator.createAnimeValid().getName();

        List<Anime> body = animeController.findByName(null).getBody();

        assertThat(body).isNotEmpty();

        assertThat(body.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    void findByNameEmpty() {

        BDDMockito.when(animeServiceMock.findByName(null))
                .thenReturn(Collections.emptyList());


        List<Anime> body = animeController.findByName(null).getBody();

        assertThat(body).isNotNull().isEmpty();

    }

    @Test
    void save() {

        Long expectedId = AnimeCreator.createAnimeValid().getId();

        Anime body = animeController.save(AnimeCreator.createAnimePost()).getBody();

        assertThat(body.getId()).isNotNull().isEqualTo(expectedId );


    }

    @Test
    void replace() {

        Long expectedId = AnimeCreator.createAnimeValid().getId();

        Anime body = animeController.update(AnimeCreator.createAnimePut(),null).getBody();

        assertThat(body.getId()).isNotNull().isEqualTo(expectedId );


    }

    @Test
    void delete() {


        ResponseEntity<Void> delete = animeController.delete(2l);

        assertThatCode(()-> animeController.delete(2l))
                .doesNotThrowAnyException();

        assertThat(delete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

}