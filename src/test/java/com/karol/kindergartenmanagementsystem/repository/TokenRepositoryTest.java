package com.karol.kindergartenmanagementsystem.repository;

import com.karol.kindergartenmanagementsystem.model.Role;
import com.karol.kindergartenmanagementsystem.model.Token;
import com.karol.kindergartenmanagementsystem.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TokenRepositoryTest {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    public void setupUser() {
        user = User.builder()
                .firstName("test")
                .lastName("test")
                .email("email@gmail.com")
                .password("password")
                .role(Role.PARENT)
                .build();
        user = userRepository.save(user);
    }

    @Test
    public void TokenRepository_Save_ReturnsSavedToken() {
        Token token = Token.builder()
                .token("djndssfij")
                .loggedOut(false)
                .user(user)
                .build();

        Token savedToken = tokenRepository.save(token);

        Assertions.assertNotNull(savedToken);
        Assertions.assertTrue(savedToken.getId() > 0);
    }

    @Test
    public void TokenRepository_FindAll_ReturnsCorrectAmountOfUsers() {
        Token token1 = Token.builder()
                .token("djndssfij")
                .loggedOut(false)
                .user(user)
                .build();

        Token token2 = Token.builder()
                .token("djndssfij")
                .loggedOut(false)
                .user(user)
                .build();

        tokenRepository.save(token1);
        tokenRepository.save(token2);

        List<Token> savedTokens = tokenRepository.findAll();

        Assertions.assertNotNull(savedTokens);
        Assertions.assertEquals(2, savedTokens.size());
    }

    @Test
    public void TokenRepository_FindById_ReturnsToken() {
        Token token = Token.builder()
                .token("djndssfij")
                .loggedOut(false)
                .user(user)
                .build();

        tokenRepository.save(token);
        Token retrievedToken = tokenRepository.findById(token.getId())
                .orElseThrow(() -> new AssertionError("Token not found"));

        Assertions.assertNotNull(retrievedToken);
    }

    @Test
    public void TokenRepository_FindAllTokensByUser_ReturnsTokenByUser() {
        Token token1 = Token.builder()
                .token("djndssfij")
                .loggedOut(false)
                .user(user)
                .build();

        Token token2 = Token.builder()
                .token("djndssfij")
                .loggedOut(false)
                .user(user)
                .build();

        tokenRepository.save(token1);
        tokenRepository.save(token2);

        List<Token> tokensByUser = tokenRepository.findAllTokensByUser(user.getId());

        Assertions.assertNotNull(tokensByUser);
        Assertions.assertEquals(2, tokensByUser.size());
    }

    @Test
    public void TokenRepository_FindByToken_ReturnsToken() {
        Token token = Token.builder()
                .token("djndssfij")
                .loggedOut(false)
                .user(user)
                .build();

        tokenRepository.save(token);

        Token retrievedToken = tokenRepository.findByToken(token.getToken())
                .orElseThrow(() -> new AssertionError("Token not found"));

        Assertions.assertNotNull(retrievedToken);
    }

    @Test
    public void TokenRepository_Update_ReturnsToken() {
        Token token = Token.builder()
                .token("djndssfij")
                .loggedOut(false)
                .user(user)
                .build();

        tokenRepository.save(token);

        Token retrievedToken = tokenRepository.findById(token.getId())
                .orElseThrow(() -> new AssertionError("Token not found"));
        retrievedToken.setToken("newToken");

        Token updatedToken = tokenRepository.save(retrievedToken);

        Assertions.assertNotNull(updatedToken);
        Assertions.assertNotNull(updatedToken.getToken());
    }

    @Test
    public void TokenRepository_Delete_DeletesToken() {
        Token token = Token.builder()
                .token("djndssfij")
                .loggedOut(false)
                .user(user)
                .build();

        tokenRepository.save(token);
        tokenRepository.deleteById(token.getId());
        Optional<Token> removalResult = tokenRepository.findById(token.getId());

        Assertions.assertTrue(removalResult.isEmpty());
    }
}