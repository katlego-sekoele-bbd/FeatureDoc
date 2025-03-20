package com.featuredoc.repository;

import jakarta.activation.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeatureStatusRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private FeatureStatusRepository FeatureStatusRepository;

    // should realistically only have to test any custom queries created in the repository

}