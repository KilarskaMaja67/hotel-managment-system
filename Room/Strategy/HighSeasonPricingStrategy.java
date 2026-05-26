package Room.Strategy;

public class HighSeasonPricingStrategy implements PricingStrategy {
    @Override
    public double calculate(double basePrice, int nights) {
        return (basePrice * 1.5) * nights;
    }
}