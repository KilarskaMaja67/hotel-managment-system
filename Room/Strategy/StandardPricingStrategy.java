package Room.Strategy;

public class StandardPricingStrategy implements PricingStrategy {
    @Override
    public double calculate(double basePrice, int nights) {
        return basePrice * nights;
    }
}
