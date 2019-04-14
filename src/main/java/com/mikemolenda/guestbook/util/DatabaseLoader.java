package com.mikemolenda.guestbook.util;

import com.mikemolenda.guestbook.domain.entity.User;
import com.mikemolenda.guestbook.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Load default data into database for given profile(s)
 */
@Slf4j
@Component
@Profile("local")
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public DatabaseLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info("Add default Users to database");
        try {
            this.userRepository.save(new User("FNAME1", "LNAME1", "TYPE1", "EMAIL1", "PASSWD1"));
            this.userRepository.save(new User("FNAME2", "LNAME2", "TYPE2", "EMAIL2", "PASSWD2"));
            this.userRepository.save(new User("FNAME3", "LNAME3", "TYPE3", "EMAIL3", "PASSWD3"));
            log.info("Add default Users success");
        } catch (Exception e) {
            log.error("Failed to add default Users");
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

}