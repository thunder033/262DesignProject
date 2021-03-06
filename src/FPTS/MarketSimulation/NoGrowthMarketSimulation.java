package FPTS.MarketSimulation;

/**
 * Created by traub_000 on 3/11/2016.
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
