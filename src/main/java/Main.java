import java.time.ZonedDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        OrderRepo oRepo = new OrderListRepo();
        ProductRepo pRepo = new ProductRepo();
        Product product1 = new Product("1","Apfel");
        Product product2 = new Product("2","Birne");
        Product product3 = new Product("3","Orange");
        Product product4 = new Product("4","Wassermelone");
        pRepo.addProduct(product1);
        pRepo.addProduct(product2);
        pRepo.addProduct(product3);
        pRepo.addProduct(product4);
        ShopService service = new ShopService(pRepo,oRepo);
        ZonedDateTime dateTime = ZonedDateTime.now();
        Order order1 = new Order("11", List.of(product1,product2),OrderStatus.PROCESSING,dateTime);
        Order order2 = new Order("12", List.of(product3,product4),OrderStatus.PROCESSING,dateTime);
        Order order3 = new Order("13", List.of(product1,product4),OrderStatus.PROCESSING,dateTime);

        try {
            service.addOrder(List.of("2","3"));
            service.addOrder(List.of("1","3"));
            service.addOrder(List.of("2","4"));
        } catch (ProductDoesNotExistException e) {
            throw new RuntimeException(e);
        }
        System.out.println(service.findOrdersWithSpecificOrderStatus(OrderStatus.PROCESSING));

    }
}
