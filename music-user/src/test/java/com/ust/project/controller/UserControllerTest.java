package com.ust.project.controller;

import com.ust.project.dto.RatingDto;
import com.ust.project.model.Music;
import com.ust.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    @Test
    void testGetAllMusics() throws Exception {
        // Setup
        // Configure UserService.fetchAllMusics(...).
        final List<Music> music = List.of(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0));
        when(mockUserService.fetchAllMusics()).thenReturn(music);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/1.0/users/viewAllMusics")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        //   assertThat(response.getContentAsString()).isEqualTo(asJsonString(music));
    }


    @Test
    void testGetAllMusics_UserServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockUserService.fetchAllMusics()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/1.0/users/viewAllMusics")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testGetMusicByName() throws Exception {
        // Setup
        // Configure UserService.fetchMusicByName(...).
        final Music music = new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage",
                "duration", "country", 0.0);
        when(mockUserService.fetchMusicByName("name")).thenReturn(music);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/1.0/users/search/music/name/{name}", "name")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        //  assertThat(response.getContentAsString()).isEqualTo(asJsonString(music));
    }

    @Test
    void testGetMusicByDate() throws Exception {
        // Setup
        // Configure UserService.fetchMusicByDate(...).
        final List<Music> music = List.of(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0));
        when(mockUserService.fetchMusicByDate("date")).thenReturn(music);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/1.0/users/search/music/date/{date}", "date")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        //  assertThat(response.getContentAsString()).isEqualTo(asJsonString(music));
    }

    @Test
    void testGetMusicByDate_UserServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockUserService.fetchMusicByDate("date")).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/1.0/users/search/music/date/{date}", "date")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testAddRatingMusic() throws Exception {
        // Setup
        when(mockUserService.addMusicRating(any(RatingDto.class), anyLong(), anyLong())).thenReturn(true);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        post("/api/1.0/users/add/rating/music/{musicid}/{userid}", 0, 0)
                                .content("{\"rating\": 0.0}")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
      //  assertThat(response.getContentAsString()).isEqualTo("true");
    }
}
