package FPTS.MarketSimulation;

/**
 * Created by Brian on 3/10/2016.
 */
public abstract class MarketSimulationAlgorithm {

    private double growthRate;

    private int originalTimeSteps;

    private int simulatedTimeSteps;

    private String timeInterval;

    private double ratePerInterval;

    private double originalPortfolioValue;

    public MarketSimulationAlgorithm(double growthRate, int originalTimeSteps,
                                     String timeInterval, double originalPortfolioValue){
        this.growthRate = growthRate;
        this.timeInterval = timeInterval;
        this.originalTimeSteps = originalTimeSteps;
        this.originalPortfolioValue = originalPortfolioValue;
        this.simulatedTimeSteps = 0;

        // This block determines the growth rate per time step
        // based on the time interval
        if(timeInterval.equals("year")){
            ratePerInterval = growthRate;
        }else if(timeInterval.equals("month")){
            ratePerInterval = growthRate / 12;
        }else if(timeInterval.equals("day")){
            ratePerInterval = growthRate / 365;
        }else{
            // Throw some error here
        }
    }

    public void setGrowthRate(double newRate){

        this.growthRate = newRate;
    }

    public double getGrowthRate(){

        return this.growthRate;
    }

    public int getOriginalTimeSteps() {
        return originalTimeSteps;
    }

    public void setOriginalTimeSteps(int originalTimeSteps) {
        this.originalTimeSteps = originalTimeSteps;
    }

    public int getSimulatedTimeSteps() {
        return simulatedTimeSteps;
    }

    public void setSimulatedTimeSteps(int simulatedTimeSteps) {
        this.simulatedTimeSteps = simulatedTimeSteps;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public double getOriginalPortfolioValue() {
        return originalPortfolioValue;
    }

    public double getRatePerInterval() {
        return ratePerInterval;
    }

    /*
             * Returns the new value of the portfolio
             * if there are still steps left to be run
             */
    public double step(){
        if(simulatedTimeSteps < originalTimeSteps) {
            simulatedTimeSteps++;
            System.err.println(getSimulatedTimeSteps());
            System.err.println(getOriginalPortfolioValue());
            System.err.println(getRatePerInterval());
        }
        return originalPortfolioValue * (1.0 + ratePerInterval * simulatedTimeSteps);
    }

    /*
     * Returns the value at the end of the simulation
     */
    public double simulate(){
        simulatedTimeSteps = originalTimeSteps;
        return originalPortfolioValue * (1.0 + ratePerInterval * simulatedTimeSteps);
    }
}
