package org.project.repository;

import org.junit.jupiter.api.Test;
import org.project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindUserByUsername() {
        User user = new User("testuser", "password123");
        userRepository.save(user);

        User found = userRepository.findByUsername("testuser");
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("testuser");
    }
}
