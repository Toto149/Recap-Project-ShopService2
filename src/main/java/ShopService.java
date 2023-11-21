import lombok.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@AllArgsConstructor
public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();
    private IdService idService = new IdService();

    public Order addOrder(List<String> productIds) throws ProductDoesNotExistException {

        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (Optional.of(productToOrder).equals(Optional.empty())) {
                throw new ProductDoesNotExistException("Dieses Produkt konnte nicht gefunden werden");
            }
            products.add(productToOrder.get());
            productToOrder = Optional.ofNullable(productToOrder.get().withQuantity(productToOrder.get().quantity() - 1));

        }
        ZonedDateTime dateTime = ZonedDateTime.of(2023,10,10,
                                                10,10,10, 0,
                                                ZoneId.of("GMT"));
        Order newOrder = new Order(this.idService.generateId().toString(), products,OrderStatus.PROCESSING, dateTime);

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
        Order orderCompleted = new Order("2",List.of(),OrderStatus.COMPLETED,zonedDateTime);

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
            if(order.orderStatus().equals(OrderStatus.COMPLETED)){
                if(order.orderTimestamp().isBefore(orderCompleted.orderTimestamp())){
                    orderCompleted = order;
                }

            }

        }
        orderStatusOldestOrderMap.put(OrderStatus.PROCESSING,orderProcessing);
        orderStatusOldestOrderMap.put(OrderStatus.IN_DELIVERY,orderInDelivery);
        orderStatusOldestOrderMap.put(OrderStatus.COMPLETED,orderCompleted);

        return orderStatusOldestOrderMap;

    }
    public void transactionMethodCalls(List<String> transactionCalls){
        if(transactionCalls.get(0).equals("addOrder")){
            List<Product> products = new ArrayList<>();
            for(int i = 0; i<transactionCalls.size()-2;i++){
                Product altProduct = new Product("","",0);
                products.add(new Product(transactionCalls.get(i+2),
                        this.productRepo.getProductById(transactionCalls.get(i+2)).orElse(altProduct).name(),
                        this.productRepo.getProductById(transactionCalls.get(i+2)).orElse(altProduct).quantity()-1));
            }
            this.orderRepo.addOrder(new Order(transactionCalls.get(1),products,OrderStatus.PROCESSING,ZonedDateTime.now()));
        }
        if(transactionCalls.get(0).equals("setStatus")){
            this.orderRepo.getOrderById(transactionCalls.get(1)).withOrderStatus(OrderStatus.valueOf(transactionCalls.get(2)));
        }
        if(transactionCalls.get(0).equals("printOrders")){
            System.out.println(this.orderRepo.getOrders());
        }
    }



}
