package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest(classes = FilmorateApplication.class)
class FilmoRateApplicationTests {

	@Autowired
	private UserDbStorage userStorage;

	@Test
	public void testFindUserById() {
		// Предполагается, что в базе данных есть пользователь с ID = 1
		User user = userStorage.getUserById(1L);

		assertThat(user).isNotNull();
		assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
	}

	@Test
	public void testGetAllUsers() {
		Collection<User> users = userStorage.getAllUsers();

		assertThat(users).isNotNull();
	}

	@Test
	public void testFindCommonFriends() {
		Collection<User> commonFriends = userStorage.findCommonFriends(1L, 2L);

		assertThat(commonFriends).isNotNull();

		boolean hasFriendWithId = commonFriends.stream()
				.anyMatch(user -> {
					log.info("Общий друг-пользователь user с id: " + user.getId() + " -- " + user.getFriends());
					return user.getId() == 3L;
				});

		assertThat(hasFriendWithId).isTrue();
	}

	@Test
	public void testAddUser() {
		Collection<User> users = userStorage.getAllUsers();
		long sizeBeforeAdding = users.size();

		assertThat(users).isNotNull();

		User newUser = new User(sizeBeforeAdding + 1, "blabla@mail.ru", "Djogger", "Misha", LocalDate.parse("1982-05-21"));
		userStorage.addUser(newUser);

		Collection<User> updatedUsers = userStorage.getAllUsers();
		long sizeAfterAdding = updatedUsers.size();

		log.info("Количество пользователей перед добавлением одного: " + sizeBeforeAdding + ", после: " + sizeAfterAdding);
		log.info("Список перед добавлением: " + users + ", после добавления: " + updatedUsers);

		assertThat(updatedUsers).isNotNull();
		assertThat(sizeAfterAdding).isEqualTo(sizeBeforeAdding + 1);
	}

	@Test
	public void testUpdateUser() {
		Collection<User> users = userStorage.getAllUsers();
		long size = users.size();

		User newUser = new User(size, "blabla@mail.ru", "Mayger", "Tosha", LocalDate.parse("1982-05-21"));

		userStorage.updateUser(newUser);

		assertThat(userStorage.getUserById(newUser.getId())).hasFieldOrPropertyWithValue("login", "Mayger");
		assertThat(userStorage.getUserById(newUser.getId())).hasFieldOrPropertyWithValue("name", "Tosha");
	}

}