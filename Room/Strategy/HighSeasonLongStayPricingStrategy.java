package Room.Strategy;

public class HighSeasonLongStayPricingStrategy implements PricingStrategy {
    @Override
    public double calculate(double basePrice, int nights) {
        double seasonalPrice = basePrice * 1.5;
        double total = seasonalPrice * nights;
        return total * 0.90;
    }
}