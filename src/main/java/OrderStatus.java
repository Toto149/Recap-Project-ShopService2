public enum OrderStatus {
    PROCESSING("PROCESSING"),
    IN_DELIVERY("IN_DELIVERY"),
    COMPLETED("COMPLETED");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
