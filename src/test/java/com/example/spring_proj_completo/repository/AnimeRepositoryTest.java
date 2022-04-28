package com.example.spring_proj_completo.repository;

import com.example.spring_proj_completo.domain.Anime;

import static com.example.spring_proj_completo.util.AnimeCreator.createAnimeToBeSaved;
import static org.assertj.core.api.Assertions.*;

import com.example.spring_proj_completo.exception.BadRequestException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@Log4j2
@DisplayName("AnimeRepositoryTest")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;


    @Test
    @DisplayName("save create anime in repository when sucessfull")
    void save_PersistAnime_whenSucessful(){
        Anime anime = createAnimeToBeSaved();
        Anime animeSaved = animeRepository.save(anime);
        assertThat(animeSaved.getId()).isNotNull();
        assertThat(animeSaved.getName()).isEqualTo(anime.getName());
    }


    @Test
    @DisplayName("update anime in repository when sucessfull")
    void update_PersistAnime_whenSucessful(){
        Anime anime = createAnimeToBeSaved();
        Anime animeSaved = animeRepository.save(anime);
        animeSaved.setName("Novo nome");
        Anime saveUpdate = animeRepository.save(animeSaved);

        assertThat(saveUpdate.getId()).isNotNull().isEqualTo(animeSaved.getId());
        assertThat(saveUpdate.getName()).isEqualTo(animeSaved.getName());
    }


    @Test
    @DisplayName("delete anime in repository when sucessfull")
    void delete(){
        Anime anime = createAnimeToBeSaved();
        Anime animeSaved = animeRepository.save(anime);

        assertThat(animeSaved).isNotNull();

        animeRepository.deleteById(animeSaved.getId());

        Optional<Anime> byId = animeRepository.findById(animeSaved.getId());

        assertThat(byId).isEmpty();

    }


    @Test
    @DisplayName("findByName list not empty anime in repository when sucessfull")
    void findByName(){
        Anime anime = createAnimeToBeSaved();
        Anime animeSaved = animeRepository.save(anime);


        List<Anime> byName = animeRepository.findByName(animeSaved.getName());


        assertThat(byName)
                .isNotEmpty()
                .contains(animeSaved);


    }


    @Test
    @DisplayName("findByName list empty anime in repository when sucessfull")
    void findByNameEmpty(){
        Anime anime = createAnimeToBeSaved();
        Anime animeSaved = animeRepository.save(anime);

        List<Anime> byName = animeRepository.findByName("new name");

        assertThat(byName).isEmpty();

    }


    @Test
    @DisplayName("thrhow ConstraintViolationException name in blanck")
    void saveAnimeInBlanck(){

        Anime anime = Anime.builder().build();


        assertThatThrownBy(()-> animeRepository.save(anime)).isInstanceOf(ConstraintViolationException.class);

    }

    @Test
    @DisplayName("thrhow ConstraintViolationException name in blanck")
    void deleteAnimeIdNotExists(){



        assertThatThrownBy(()-> animeRepository.deleteById(1L)).isInstanceOf(EmptyResultDataAccessException.class);

    }



}