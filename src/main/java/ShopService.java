import lombok.AllArgsConstructor;
import lombok.With;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
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
        ZonedDateTime dateTime = ZonedDateTime.of(2023,10,10,
                                                10,10,10, 0,
                                                ZoneId.of("GMT"));
        Order newOrder = new Order(UUID.randomUUID().toString(), products,OrderStatus.PROCESSING, dateTime);

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> findOrdersWithSpecificOrderStatus(OrderStatus orderStatus){
        return this.orderRepo.getOrders()
                .stream()
                .filter(order -> order.orderStatus().equals(orderStatus))
                .collect(Collectors.toList());
    }
    public void updateOrder(String orderId, OrderStatus newStatus){
        this.orderRepo.getOrderById(orderId).withOrderStatus(newStatus);

    }

    public Map<OrderStatus,Order> getOldestOrderPerStatus(){
        Map<OrderStatus,Order> orderStatusOldestOrderMap = new HashMap<>();

        List<Order> orders = this.orderRepo.getOrders();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        Order orderProcessing = new Order("0",List.of(),OrderStatus.PROCESSING,zonedDateTime);
        Order orderInDelivery = new Order("1",List.of(),OrderStatus.IN_DELIVERY,zonedDateTime);
        Order orderCompleted = new Order("2",List.of(),OrderStatus.COMPELTED,zonedDateTime);

        for(Order order : orders){
            if(order.orderStatus().equals(OrderStatus.PROCESSING)){
               if(order.orderTimestamp().isBefore(orderProcessing.orderTimestamp())){
                   orderProcessing = order;
               }

            }
            if(order.orderStatus().equals(OrderStatus.IN_DELIVERY)){
                if(order.orderTimestamp().isBefore(orderInDelivery.orderTimestamp())){
                    orderInDelivery = order;
                }

            }
            if(order.orderStatus().equals(OrderStatus.COMPELTED)){
                if(order.orderTimestamp().isBefore(orderCompleted.orderTimestamp())){
                    orderCompleted = order;
                }

            }

        }
        orderStatusOldestOrderMap.put(OrderStatus.PROCESSING,orderProcessing);
        orderStatusOldestOrderMap.put(OrderStatus.IN_DELIVERY,orderInDelivery);
        orderStatusOldestOrderMap.put(OrderStatus.COMPELTED,orderCompleted);

        return orderStatusOldestOrderMap;

    }



}
