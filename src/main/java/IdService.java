import java.util.UUID;

public class IdService {

    public UUID generateId(){
        return new UUID(48,16);
    }
}
