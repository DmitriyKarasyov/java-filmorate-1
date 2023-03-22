package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmoRateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmDbStorage;
	private final MpaDao mpaDao;
	private final GenreDao genreDao;

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Test
	@Order(1)
	public void testAddUser() {
		User testUser1 = User.builder()
				.id(1L)
				.name("user1Name")
				.login("user1Login")
				.email("user1@mail.com")
				.birthday(LocalDate.parse("1999-11-11", formatter))
				.build();

		User testUser2 = User.builder()
				.id(1L)
				.name("user2Name")
				.login("user2Login")
				.email("user2@mail.com")
				.birthday(LocalDate.parse("1999-11-12", formatter))
				.build();

		User user1 = userStorage.addUser(testUser1);
		User user2 = userStorage.addUser(testUser2);

		assertEquals(user1, testUser1);
		assertEquals(user2, testUser2);
	}

	@Test
	@Order(2)
	public void testUpdateUser() {
		User testUpdateUser = User.builder()
				.id(1L)
				.name("userUpdateName")
				.login("userUpdateLogin")
				.email("userUpdate@mail.com")
				.birthday(LocalDate.parse("1999-11-11", formatter))
				.build();

		userStorage.updateUser(testUpdateUser);

		User updateUser = userStorage.getUserById(testUpdateUser.getId());

		assertEquals(updateUser, testUpdateUser);
	}

	@Test
	@Order(3)
	public void testGetUserById() {
		User user = userStorage.getUserById(1L);

		assertNotNull(user);
		assertEquals(user.getId(), 1L);
		assertEquals("userUpdateName", user.getName());
	}

	@Test
	@Order(4)
	public void testGetAllUsers() {
		List<User> testUsers = userStorage.getAllUsers();

		assertEquals(2, testUsers.size());
		assertEquals("userUpdateName", testUsers.get(0).getName());
		assertEquals("user2Name", testUsers.get(1).getName());
	}

	@Test
	@Order(5)
	public void testAddFilm() {
		Film testFilm1 = Film.builder()
				.id(1L)
				.name("film1")
				.description("film1 description")
				.releaseDate(LocalDate.parse("1991-11-11"))
				.duration(90)
				.mpa(new Mpa(1, "G"))
				.genres(new TreeSet<>(List.of(new Genre(1, "Комедия"))))
				.likes(new HashSet<>())
				.build();

		Film testFilm2 = Film.builder()
				.id(1L)
				.name("film2")
				.description("film2 description")
				.releaseDate(LocalDate.parse("1991-11-12"))
				.duration(90)
				.mpa(new Mpa(1, "G"))
				.genres(new TreeSet<>(List.of(new Genre(2, "Драма"))))
				.likes(new HashSet<>())
				.build();

		Film film1 = filmDbStorage.addFilm(testFilm1);
		Film film2 = filmDbStorage.addFilm(testFilm2);

		assertEquals(testFilm1, film1);
		assertEquals(testFilm2, film2);

		assertEquals(testFilm1.getMpa().getName(), mpaDao.getMpaById(1).getName());
		assertEquals(testFilm2.getGenres().size(), 1);
		assertTrue(testFilm2.getGenres().contains(new Genre(2, "Драма")));
	}

	@Test
	@Order(6)
	public void testUpdateFilm() {
		Film testFilm = filmDbStorage.getFilmById(1L);
		testFilm.setLikes(new TreeSet<>(List.of(1L)));

		filmDbStorage.updateFilm(testFilm);

		Film film = filmDbStorage.getFilmById(1L);

		assertEquals(testFilm, film);
	}

	@Test
	@Order(7)
	public void testGetFilmById() {
		Film testFilm = filmDbStorage.getFilmById(1L);

		assertNotNull(testFilm);
		assertEquals(testFilm.getId(), 1L);
	}

	@Test
	@Order(8)
	public void testGetAllFilms() {
		List<Film> testFilms = filmDbStorage.getAllFilms();

		assertEquals(2, testFilms.size());
		assertEquals("film2", testFilms.get(1).getName());
		assertEquals(90, testFilms.get(0).getDuration());
	}

	@Test
	public void testGetMpaById() {
		Mpa mpa = mpaDao.getMpaById(1);

		assertNotNull(mpa);
		assertEquals(1, mpa.getId());
		assertEquals("G", mpa.getName());
	}

	@Test
	public void testGetAllMpa() {
		List<Mpa> mpaList = List.of(
				new Mpa(1, "G"),
				new Mpa(2, "PG"),
				new Mpa(3, "PG-13"),
				new Mpa(4, "R"),
				new Mpa(5, "NC-17"));

		List<Mpa> testMpaList = mpaDao.getAllMpa();

		assertEquals(testMpaList, mpaList);
	}

	@Test
	public void testGetGenreById() {
		Genre genre = new Genre(1, "Комедия");

		Genre testGenre = genreDao.getGenreById(1);

		assertEquals(testGenre, genre);
	}

	@Test
	public void testGetAllGenres() {
		List<Genre> genres = List.of(
				new Genre(1, "Комедия"),
				new Genre(2, "Драма"),
				new Genre(3, "Мультфильм"),
				new Genre(4, "Триллер"),
				new Genre(5, "Документальный"),
				new Genre(6, "Боевик"));

		List<Genre> testGenres = genreDao.getAllGenres();

		assertEquals(testGenres, genres);
	}
}
