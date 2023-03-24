package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oUserRepository implements UserRepository {
    private final Sql2o sql2o;
    private static final Logger LOG = LoggerFactory.getLogger(Sql2oUserRepository.class);

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        Optional<User> userOptional = Optional.empty();
        try (var connection = sql2o.open()) {
            var sql = """
                      INSERT INTO users(name, email, password)
                      VALUES (:name, :email, :password)
                      """;
            var query = connection.createQuery(sql, true).bind(user);
            int generatedId = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedId);
            userOptional = Optional.of(user);
        } catch (Sql2oException e) {
            LOG.error("Пользователь с такой почтой уже существует", e);
        }
        return userOptional;
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(
                    "SELECT * FROM users WHERE email = :email AND password = :password")
                    .addParameter("email", email)
                    .addParameter("password", password);
            var user = query.executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(
                    "DELETE FROM users WHERE id = :id")
                    .addParameter("id", id);
            var affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }

    @Override
    public Collection<User> findAll() {
        try (var connection = sql2o.open()) {
            return connection.createQuery("SELECT * FROM users").executeAndFetch(User.class);
        }
    }
}
