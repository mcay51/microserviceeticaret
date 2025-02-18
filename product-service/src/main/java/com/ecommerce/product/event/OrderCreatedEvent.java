@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private String status;
} 