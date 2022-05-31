package homework3;

import lombok.Data;

import java.util.List;

@Data
public class ConvertResponse {
    private double sourceAmount;
    private String sourceUnit;
    private double targetAmount;
    private String targetUnit;
    private String answer;
}
