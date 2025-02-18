@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockUpdatedEvent {
    private Long productId;
    private Integer newStock;
    private String status;
} 