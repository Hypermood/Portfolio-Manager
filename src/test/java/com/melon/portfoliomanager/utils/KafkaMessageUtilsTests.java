package com.melon.portfoliomanager.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.melon.portfoliomanager.models.PortfolioItem;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.PortfolioItemRepository;
import com.melon.portfoliomanager.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;

public class KafkaMessageUtilsTests {
    @Mock
    private PortfolioItemRepository portfolioItemRepository;

    @Mock
    private UserRepository userRepository;

    private final SerializationUtils serializationUtils = new SerializationUtils();

    private KafkaMessageUtils kafkaMessageUtils;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        kafkaMessageUtils = new KafkaMessageUtils(serializationUtils, portfolioItemRepository, userRepository);
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testPrepareStockPriceChangesMessageWillSucceed() throws JsonProcessingException {
        List<PortfolioItem> portfolioItems = buildPortfolioItems();
        List<User> users = buildUsers();
        doReturn(portfolioItems).when(portfolioItemRepository).findByCompanyNameIn(anyList());
        doReturn(users).when(userRepository).findByIdIn(anyList());
        Map<String, Double> companyStockPrices = Map.of("IBM", 1.23, "FB", 2.34);

        String actualMessage = kafkaMessageUtils.prepareStockPriceChangesMessage(companyStockPrices);

        String expectedMessageFirstFormat = "{\"firstUser@somewhere.com\":{\"IBM\":1.23,\"FB\":2.34}," +
                "\"secondUser@somewhere.com\":{\"IBM\":1.23}}";
        String expectedMessageSecondFormat = "{\"secondUser@somewhere.com\":{\"IBM\":1.23}," +
                "\"firstUser@somewhere.com\":{\"IBM\":1.23,\"FB\":2.34}}";
        assertTrue(expectedMessageFirstFormat.equals(actualMessage)
                || expectedMessageSecondFormat.equals(actualMessage));
    }

    private List<PortfolioItem> buildPortfolioItems() {
        PortfolioItem firstPortfolioItem = new PortfolioItem();
        firstPortfolioItem.setUserId(1L);
        firstPortfolioItem.setCompanyName("IBM");

        PortfolioItem secondPortfolioItem = new PortfolioItem();
        secondPortfolioItem.setUserId(1L);
        secondPortfolioItem.setCompanyName("FB");

        PortfolioItem thirdPortfolioItem = new PortfolioItem();
        thirdPortfolioItem.setUserId(2L);
        thirdPortfolioItem.setCompanyName("IBM");

        return List.of(firstPortfolioItem, secondPortfolioItem, thirdPortfolioItem);
    }

    private List<User> buildUsers() {
        User firstUser = new User();
        firstUser.setId(1L);
        firstUser.setEmail("firstUser@somewhere.com");

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setEmail("secondUser@somewhere.com");

        return List.of(firstUser, secondUser);
    }
}
