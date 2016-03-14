package FPTS.MarketSimulation;

/**
 * Created by traub_000 on 3/11/2016.
 */
public class BullMarketSimulation extends MarketSimulationAlgorithm {

    public BullMarketSimulation(double growthRate, int originalTimeSteps,
                                String timeInterval, double originalPortfolioValue) {
        super(growthRate, originalTimeSteps,
                timeInterval, originalPortfolioValue);
    }
}
