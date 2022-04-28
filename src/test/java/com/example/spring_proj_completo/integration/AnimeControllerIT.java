package com.example.spring_proj_completo.integration;

import com.example.spring_proj_completo.domain.Anime;
import com.example.spring_proj_completo.domain.User;
import com.example.spring_proj_completo.repository.AnimeRepository;
import com.example.spring_proj_completo.repository.UserRepository;
import com.example.spring_proj_completo.requests.AnimePostRequestBody;
import com.example.spring_proj_completo.requests.AnimePutRequestBody;
import com.example.spring_proj_completo.util.AnimeCreator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimeControllerIT {

    @Autowired
    @Qualifier(value = "testRestTemplateRolerUserCreator")
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private  UserRepository userRepository;
    @TestConfiguration
    @Lazy
    static class config{
        @Bean(name = "testRestTemplateRolerUserCreator")
        public  TestRestTemplate testRestTemplateRolerUserCreator(@Value("${local.server.port}") int  port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("giovane", "giovane");
            return new TestRestTemplate(restTemplateBuilder);

        }


    }

    @BeforeEach
    void setUp() {

        User user = User.builder()
                .name("giovane")
                .password("{bcrypt}$2a$10$JNA./P4Lo6JLa8JJjEfDauIWyLoLsGCVnCGSXwxw1Ij0owkU3N.aC")
                .authorities("ROLE_ADMIN")
                .id(1L)
                .username("giovane")
                .build();
        userRepository.save(user);


    }



    @Test
    void listAll() {



        Anime animeValid = AnimeCreator.createAnimeValid();
        animeRepository.save(animeValid);

        String expectedName = animeValid.getName();

        List<Anime> body = testRestTemplate
                .exchange("/animes/list", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                        }).getBody();


                        assertThat(body).isNotEmpty();

        assertThat(body.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    void findBydId() {


        Anime animeValid = AnimeCreator.createAnimeValid();
        animeRepository.save(animeValid);

        Long expectedId = animeValid.getId();

        Anime body = testRestTemplate.getForEntity("/animes/{id}", Anime.class, expectedId).getBody();

        assertThat(body).isNotNull();

        assertThat(body.getId()).isEqualTo(expectedId );

    }


    @Test
    void findByName() {


        Anime animeValid = AnimeCreator.createAnimeValid();
        animeRepository.save(animeValid);

        String expectedName = animeValid.getName();

        String url = String.format("/animes/findname?name=%s", expectedName);

        List<Anime> body = testRestTemplate
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();


        assertThat(body).isNotEmpty();

        assertThat(body.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    void findByNameEmptyList() {


        String url = String.format("/animes/findname?name=%s", "");

        List<Anime> body = testRestTemplate
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();


        assertThat(body).isEmpty();


    }

    @Test
    void save() {


        AnimePostRequestBody animePost = AnimeCreator.createAnimePost();
        String expectedName = animePost.getName();

        String url = String.format("/animes/admin");

        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity(url, animePost, Anime.class);


        assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(animeResponseEntity.getBody().getId()).isNotNull();

        assertThat(animeResponseEntity.getBody().getName()).isEqualTo(expectedName);


    }


    @Test
    void update() {

        Anime animeValid = AnimeCreator.createAnimeValid();

        animeRepository.save(animeValid);
        Long expectedId = animeValid.getId();
        String expectedName = animeValid.getName();


        AnimePutRequestBody animePut = AnimeCreator.createAnimePut();


        String url = String.format("/animes/admin/%s",expectedId);

        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(animePut), Anime.class);


        assertThat(animeResponseEntity.getBody().getId()).isEqualTo(expectedId);
        assertThat(animeResponseEntity.getBody().getName()).isEqualTo(animePut.getName());



    }


    @Test
    void delelete() {

        Anime animeValid = AnimeCreator.createAnimeValid();
        animeRepository.save(animeValid);
        Long expectedId = animeValid.getId();


        String url = String.format("/animes/admin/%s",expectedId);

        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Anime.class);



        assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);



    }

}
