package com.mikemolenda.guestbook.repository;

import com.mikemolenda.guestbook.domain.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UserRepository subject;

    private User createEntity(int ordering) {
        User entity = new User();
        entity.setFirstName("FIRSTNAME" + ordering);
        entity.setLastName("LASTNAME" + ordering);
        entity.setType("TYPE" + ordering);
        entity.setEmail("EMAIL" + ordering);
        entity.setPassword("PASSWORD" + ordering);
        return entity;
    }

    @Test
    public void should_get_user_by_id() {
        User expected = new User();
        expected.setFirstName("FIRSTNAME1");
        expected.setLastName("LASTNAME1");
        expected.setType("TYPE1");
        expected.setEmail("EMAIL1");
        expected.setPassword("PASSWORD1");
        subject.save(createEntity(1));

        User result = subject.getOne(1L);

        assertThat(result.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(result.getLastName()).isEqualTo(expected.getLastName());
    }

    @Test
    public void should_get_all_users() {
        subject.save(createEntity(1));
        subject.save(createEntity(2));
        subject.save(createEntity(3));
        subject.save(createEntity(4));

        List<User> result = subject.findAll();

        assertThat(result).hasSize(4);
    }

}