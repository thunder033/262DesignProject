package FPTS;

/**
 * Created by traub_000 on 3/11/2016.
 */
public class BearMarketSimulation extends MarketSimulationAlgorithm {

    public BearMarketSimulation(double growthRate, int originalTimeSteps,
                                String timeInterval, double originalPortfolioValue){
        super(growthRate, originalTimeSteps,
                timeInterval, originalPortfolioValue);
    }

    public double step(){
        if(getSimulatedTimeSteps() < getOriginalTimeSteps()) {
            setSimulatedTimeSteps(getSimulatedTimeSteps() + 1);
        }
        return getOriginalPortfolioValue() * (1 - getRatePerInterval() * getSimulatedTimeSteps());
    }

    public double simulate(double portfolioValue){
        setSimulatedTimeSteps(getOriginalTimeSteps());
        return getOriginalPortfolioValue() * (1 - getRatePerInterval() * getSimulatedTimeSteps());
    }
}
