package com.ust.project.service;

import com.ust.project.dto.RatingDto;
import com.ust.project.model.Music;
import com.ust.project.model.Rating;
import com.ust.project.repository.MusicRepository;
import com.ust.project.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userServiceUnderTest;

    @BeforeEach
    void setUp() {
        userServiceUnderTest = new UserService();
        userServiceUnderTest.musicRepository = mock(MusicRepository.class);
        userServiceUnderTest.ratingRepository = mock(RatingRepository.class);
    }

    @Test
    void testFetchAllMusics() {
        // Setup
        final List<Music> expectedResult = List.of(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0));

        // Configure MusicRepository.findAll(...).
        final List<Music> music = List.of(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0));
        when(userServiceUnderTest.musicRepository.findAll()).thenReturn(music);

        // Run the test
        final List<Music> result = userServiceUnderTest.fetchAllMusics();

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFetchAllMusics_MusicRepositoryReturnsNoItems() {
        // Setup
        when(userServiceUnderTest.musicRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<Music> result = userServiceUnderTest.fetchAllMusics();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testFetchMusicByName() {
        // Setup
        final Music expectedResult = new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate",
                "songLanguage", "duration", "country", 0.0);

        // Configure MusicRepository.findByMusicName(...).
        final Music music = new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage",
                "duration", "country", 0.0);
        when(userServiceUnderTest.musicRepository.findByMusicName("name")).thenReturn(music);

        // Run the test
        final Music result = userServiceUnderTest.fetchMusicByName("name");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFetchMusicByDate() {
        // Setup
        final List<Music> expectedResult = List.of(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0));

        // Configure MusicRepository.findAllBySongReleaseDate(...).
        final List<Music> music = List.of(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0));
        when(userServiceUnderTest.musicRepository.findAllBySongReleaseDate("date")).thenReturn(music);

        // Run the test
        final List<Music> result = userServiceUnderTest.fetchMusicByDate("date");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFetchMusicByDate_MusicRepositoryReturnsNoItems() {
        // Setup
        when(userServiceUnderTest.musicRepository.findAllBySongReleaseDate("date")).thenReturn(Collections.emptyList());

        // Run the test
        final List<Music> result = userServiceUnderTest.fetchMusicByDate("date");

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testAddMusicRating() {
        // Setup
        final RatingDto ratingDto = new RatingDto(0.0);

        // Configure MusicRepository.findById(...).
        final Optional<Music> music = Optional.of(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0));
        when(userServiceUnderTest.musicRepository.findById(0L)).thenReturn(music);

        // Configure RatingRepository.findByUserId(...).
        final Optional<List<Rating>> ratings = Optional.of(List.of(new Rating(0L, 0L, 0L, 0.0)));
        when(userServiceUnderTest.ratingRepository.findByUserId(0L)).thenReturn(ratings);

        when(userServiceUnderTest.ratingRepository.save(new Rating(0L, 0L, 0L, 0.0)))
                .thenReturn(new Rating(0L, 0L, 0L, 0.0));
        when(userServiceUnderTest.ratingRepository.findAllByMusicId(0L))
                .thenReturn(List.of(new Rating(0L, 0L, 0L, 0.0)));

        // Configure MusicRepository.save(...).
        final Music music1 = new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage",
                "duration", "country", 0.0);
        when(userServiceUnderTest.musicRepository.save(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0))).thenReturn(music1);

        // Run the test
        final boolean result = userServiceUnderTest.addMusicRating(ratingDto, 0L, 0L);

        // Verify the results
        assertThat(result).isTrue();
        verify(userServiceUnderTest.ratingRepository).save(new Rating(0L, 0L, 0L, 0.0));
        verify(userServiceUnderTest.musicRepository).save(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0));
    }

    @Test
    void testAddMusicRating_MusicRepositoryFindByIdReturnsAbsent() {
        // Setup
        final RatingDto ratingDto = new RatingDto(0.0);
        when(userServiceUnderTest.musicRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        final boolean result = userServiceUnderTest.addMusicRating(ratingDto, 0L, 0L);

        // Verify the results
        assertThat(result).isFalse();
    }

    @Test
    void testAddMusicRating_RatingRepositoryFindByUserIdReturnsAbsent() {
        // Setup
        final RatingDto ratingDto = new RatingDto(0.0);

        // Configure MusicRepository.findById(...).
        final Optional<Music> music = Optional.of(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0));
        when(userServiceUnderTest.musicRepository.findById(0L)).thenReturn(music);

        when(userServiceUnderTest.ratingRepository.findByUserId(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.addMusicRating(ratingDto, 0L, 0L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testAddMusicRating_RatingRepositoryFindByUserIdReturnsNoItems() {
        // Setup
        final RatingDto ratingDto = new RatingDto(0.0);

        // Configure MusicRepository.findById(...).
        final Optional<Music> music = Optional.of(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0));
        when(userServiceUnderTest.musicRepository.findById(0L)).thenReturn(music);

        when(userServiceUnderTest.ratingRepository.findByUserId(0L)).thenReturn(Optional.of(Collections.emptyList()));
        when(userServiceUnderTest.ratingRepository.save(new Rating(0L, 0L, 0L, 0.0)))
                .thenReturn(new Rating(0L, 0L, 0L, 0.0));
        when(userServiceUnderTest.ratingRepository.findAllByMusicId(0L))
                .thenReturn(List.of(new Rating(0L, 0L, 0L, 0.0)));

        // Configure MusicRepository.save(...).
        final Music music1 = new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage",
                "duration", "country", 0.0);
        when(userServiceUnderTest.musicRepository.save(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0))).thenReturn(music1);

        // Run the test
        final boolean result = userServiceUnderTest.addMusicRating(ratingDto, 0L, 0L);

        // Verify the results
        assertThat(result).isTrue();
        verify(userServiceUnderTest.ratingRepository).save(new Rating(0L, 0L, 0L, 0.0));
        verify(userServiceUnderTest.musicRepository).save(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0));
    }

    @Test
    void testAddMusicRating_RatingRepositoryFindAllByMusicIdReturnsNoItems() {
        // Setup
        final RatingDto ratingDto = new RatingDto(0.0);

        // Configure MusicRepository.findById(...).
        final Optional<Music> music = Optional.of(
                new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage", "duration",
                        "country", 0.0));
        when(userServiceUnderTest.musicRepository.findById(0L)).thenReturn(music);

        // Configure RatingRepository.findByUserId(...).
        final Optional<List<Rating>> ratings = Optional.of(List.of(new Rating(0L, 0L, 0L, 0.0)));
        when(userServiceUnderTest.ratingRepository.findByUserId(0L)).thenReturn(ratings);

        when(userServiceUnderTest.ratingRepository.save(any(Rating.class)))
                .thenReturn(new Rating(0L, 0L, 0L, 0.0));
        when(userServiceUnderTest.ratingRepository.findAllByMusicId(0L)).thenReturn(Collections.emptyList());

        // Configure MusicRepository.save(...).
        final Music music1 = new Music(0L, "musicName", "artistName", "musicGenre", "songReleaseDate", "songLanguage",
                "duration", "country", 0.0);
        when(userServiceUnderTest.musicRepository.save(any(Music.class))).thenReturn(music1);

        // Run the test
        final boolean result = userServiceUnderTest.addMusicRating(ratingDto, 0L, 0L);

        // Verify the results
        assertThat(result).isTrue();
        verify(userServiceUnderTest.ratingRepository).save(any(Rating.class));
        verify(userServiceUnderTest.musicRepository).save(any(Music.class));
    }

}
