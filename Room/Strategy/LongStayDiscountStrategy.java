package Room.Strategy;

public class LongStayDiscountStrategy implements PricingStrategy {
    @Override
    public double calculate(double basePrice, int nights) {
        double total = basePrice * nights;
        if (nights > 7) {
            return total * 0.8;
        }
        return total;
    }
}