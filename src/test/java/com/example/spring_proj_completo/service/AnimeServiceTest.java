package com.example.spring_proj_completo.service;

import com.example.spring_proj_completo.domain.Anime;
import com.example.spring_proj_completo.exception.BadRequestException;
import com.example.spring_proj_completo.repository.AnimeRepository;
import com.example.spring_proj_completo.util.AnimeCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {


    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createAnimeValid()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(animePage);


        List<Anime> animeList = new ArrayList<>(List.of(AnimeCreator.createAnimeValid()));

        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(animeList);

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(animeList);

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.ofNullable(AnimeCreator.createAnimeValid()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any()))
                .thenReturn(AnimeCreator.createAnimeValid());


        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));


    }

    @Test
    void listAllPage() {

        String expectedName = AnimeCreator.createAnimeValid().getName();

        Page<Anime> body = animeService.listAll(PageRequest.of(1, 2));

        assertThat(body).isNotEmpty();

        assertThat(body.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    void listAll() {

        String expectedName = AnimeCreator.createAnimeValid().getName();

        List<Anime> body = animeService.listAllNonPageable();

        assertThat(body).isNotEmpty();

        assertThat(body.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    void findByIdOrElseThrow() {

        Long expectedId = AnimeCreator.createAnimeValid().getId();

        Anime body = animeService.findByIdOrElseBadRequestException(2l);

        assertThat(body).isNotNull();

        assertThat(body.getId()).isEqualTo(expectedId);

    }


    @Test
    void findByName() {

        String expectedName = AnimeCreator.createAnimeValid().getName();

        List<Anime> body = animeService.findByName("nome");

        assertThat(body).isNotEmpty();

        assertThat(body.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    void findByNameEmpty() {

        BDDMockito.when(animeRepositoryMock.findByName("null"))
                .thenReturn(Collections.emptyList());


        List<Anime> body = animeService.findByName("null");

        assertThat(body).isNotNull().isEmpty();

    }


    @Test
    void delete() {

        assertThatCode(() -> animeService.delete(2l))
                .doesNotThrowAnyException();

    }


    @Test
    void findByIdTrhow() {
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> animeService.findByIdOrElseBadRequestException(2l))
                .isInstanceOf(BadRequestException.class);
    }


}