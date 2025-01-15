package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldAddUser() throws Exception {
		User user = User.builder()
				.name("Nick Name")
				.email("mail@mail.ru")
				.login("dolore")
				.birthday(LocalDate.of(1946, 8, 20))
				.build();

		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Nick Name"));
	}

	@Test
	void shouldAddUserWithEmptyName() throws Exception {
		User user = User.builder()
				.email("something@mail.ru")
				.login("s1mple")
				.birthday(LocalDate.of(1946, 8, 20))
				.build();

		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("s1mple"));
	}

	@Test
	void shouldNotAddUserWithBadEmail() throws Exception {
		User user = User.builder()
				.name("Sasha")
				.email("bad-email")
				.login("s1mple")
				.birthday(LocalDate.of(1946, 8, 20))
				.build();

		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
	}

	@Test
	void shouldNotAddUserWithEmptyData() throws Exception {
		User user = User.builder().build();

		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
	}

	@Test
	void shouldAddFilm() throws Exception {
		Film film = Film.builder()
				.name("War Of the worlds")
				.description("Cool film")
				.duration(120)
				.releaseDate(LocalDate.of(1946, 8, 20))
				.build();

		mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(film)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("War Of the worlds"));
	}

}