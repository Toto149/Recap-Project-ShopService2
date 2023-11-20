import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds) throws ProductDoesNotExistException {

        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (Optional.of(productToOrder).equals(Optional.empty())) {
                throw new ProductDoesNotExistException("Dieses Produkt konnte nicht gefunden werden");
            }
            products.add(productToOrder.get());
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products,OrderStatus.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> findOrdersWithSpecificOrderStatus(OrderStatus orderStatus){
        return this.orderRepo.getOrders()
                .stream()
                .filter(order -> order.orderStatus().equals(orderStatus))
                .collect(Collectors.toList());
    }

}
