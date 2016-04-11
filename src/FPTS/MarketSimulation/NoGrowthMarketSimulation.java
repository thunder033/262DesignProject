package FPTS.MarketSimulation;

/**
 * Created by traub_000 on 3/11/2016.
 * A no-growth algorithm doesn't change the portfolio's value
 */
public class NoGrowthMarketSimulation extends MarketSimulationAlgorithm{

    public NoGrowthMarketSimulation(double growthRate, int originalTimeSteps,
                                    String timeInterval, double originalPortfolioValue) {
        super(growthRate, originalTimeSteps,
        timeInterval, originalPortfolioValue);
    }

    public double simulate(){
        return getOriginalPortfolioValue();
    }

    public double step(){
        return getOriginalPortfolioValue();
    }
}
