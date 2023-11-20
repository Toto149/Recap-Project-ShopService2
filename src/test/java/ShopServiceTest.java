import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");
        ZonedDateTime dateTime = ZonedDateTime.of(2023,10,10,10,10,10, 0,ZoneId.of("GMT"));

        //WHEN
        Order actual = null;
        try {
            actual = shopService.addOrder(productsIds);
        } catch(ProductDoesNotExistException e){
            System.out.println(e.getMessage());
            fail();
        }
        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")),OrderStatus.PROCESSING,dateTime);
        assert actual != null;
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        //WHEN
        Order actual = null;
        try{
            actual = shopService.addOrder(productsIds);
        } catch (ProductDoesNotExistException e){
            System.out.println(e.getMessage());
        }

        //THEN
        assertNull(actual);
    }
}
