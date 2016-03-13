package FPTS;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * Created by traub_000 on 3/11/2016.
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
        double newVal = getCurrentAlgorithm().simulate();
        setPortfolioValue(newVal);
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
