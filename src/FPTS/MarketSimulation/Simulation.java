package FPTS.MarketSimulation;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created by traub_000 on 3/11/2016.
 * Holds information about the current simulation session.
 * Includes methods for keeping track of which algorithms have been used,
 * simulating the portfolio, and reverting the portfolio.
 */
public class Simulation {

    private double currentPortfolioValue;

    private MarketSimulationAlgorithm currentAlgorithm;

    private Stack<MarketSimulationAlgorithm> algorithmStack = new Stack<>();

    public Simulation(double portfolioValue){
        this.currentPortfolioValue = portfolioValue;
    }

    public void setBearAlgorithm(double growthRate, int originalTimeSteps,
                                 String timeInterval, double originalPortfolioValue){
        this.currentAlgorithm = new BearMarketSimulation(growthRate, originalTimeSteps,
                timeInterval, originalPortfolioValue);
        this.algorithmStack.push(getCurrentAlgorithm());
    }

    public void setBullAlgorithm(double growthRate, int originalTimeSteps,
                                 String timeInterval, double originalPortfolioValue){
        this.currentAlgorithm = new BullMarketSimulation(growthRate, originalTimeSteps,
                timeInterval, originalPortfolioValue);
        this.algorithmStack.push(getCurrentAlgorithm());
    }

    public void setNoGrowthAlgorithm (double growthRate, int originalTimeSteps,
                                 String timeInterval, double originalPortfolioValue){
        this.currentAlgorithm = new NoGrowthMarketSimulation(growthRate, originalTimeSteps,
                timeInterval, originalPortfolioValue);
        this.algorithmStack.push(getCurrentAlgorithm());
    }

    public MarketSimulationAlgorithm getCurrentAlgorithm() {
        return currentAlgorithm;
    }

    public double simulate(){
        double newVal;
        try {
            newVal = getCurrentAlgorithm().simulate();
            if(newVal < 0){
                newVal = 0;
            }
            setPortfolioValue(newVal);
        }catch (NullPointerException e){
            newVal = getPortfolioValue();
        }
        return newVal;
    }

    public double step(){
        double newVal;
        try {
            newVal = getCurrentAlgorithm().step();
            if(newVal < 0){
                newVal = 0;
            }
            setPortfolioValue(newVal);
        }catch (NullPointerException e){
            newVal = getPortfolioValue();
        }
        return newVal;
    }

    /*
     * Set the value to what it was before the last simulation was run
     */
    public double revertBackOneSimulation(){
        currentPortfolioValue = algorithmStack.pop().getOriginalPortfolioValue();
        try {
            currentAlgorithm = algorithmStack.peek();
        }catch (EmptyStackException e){
            currentAlgorithm = null;
        }
        return currentPortfolioValue;
    }

    public double revertToActualValue(){
        currentPortfolioValue = algorithmStack.firstElement().getOriginalPortfolioValue();
        algorithmStack.removeAllElements();
        currentAlgorithm = null;
        return currentPortfolioValue;
    }


    // This was an original method that I replaced with set___Algorithm
//    public double addSimulation(){
//        algorithmStack.push(currentAlgorithm);
//        return currentAlgorithm.simulate(this.currentPortfolioValue);
//    }

    public double getPortfolioValue() {
        return currentPortfolioValue;
    }

    public void setPortfolioValue(double portfolioValue) {
        currentPortfolioValue = portfolioValue;
    }
}
