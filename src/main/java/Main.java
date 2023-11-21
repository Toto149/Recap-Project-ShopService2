import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        OrderRepo oRepo = new OrderListRepo();
        ProductRepo pRepo = new ProductRepo();
        IdService idService = new IdService();
        Product product1 = new Product("1","Apfel",1);
        Product product2 = new Product("2","Birne",2);
        Product product3 = new Product("3","Orange",2);
        Product product4 = new Product("4","Wassermelone",3);
        Product product5 = new Product(idService.generateId().toString(), "Banane",5);
        pRepo.addProduct(product1);
        pRepo.addProduct(product2);
        pRepo.addProduct(product3);
        pRepo.addProduct(product4);
        pRepo.addProduct(product5);
        ShopService service = new ShopService(pRepo,oRepo,idService);
        ZonedDateTime dateTime = ZonedDateTime.now();
        System.out.println(pRepo);
        try{
            List<String> lines = Files.readAllLines(Path.of("C:\\Users\\Thorsten Thomann\\IdeaProjects\\" +
                    "Recap-Project-ShopService\\src\\main\\java\\transactions.txt"));
            System.out.println(lines);
            for(String line : lines) {
                String[] line1 = line.split(" ");
                List<String> call = Arrays.stream(line1).toList();
                service.transactionMethodCalls(call);
            }
            System.out.println(service.getOldestOrderPerStatus());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }


    }


}
