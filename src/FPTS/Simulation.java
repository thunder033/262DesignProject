package FPTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by traub_000 on 3/11/2016.
 */
public class Simulation {

    private double currentPortfolioValue;

    private MarketSimulationAlgorithm currentAlgorithm;

    private Stack<MarketSimulationAlgorithm> algorithmStack;

    public Simulation(double portfolioValue){
        this.currentPortfolioValue = portfolioValue;
    }

    public void setAlgorithm(MarketSimulationAlgorithm currentAlgorithm){
        this.currentAlgorithm = currentAlgorithm;
    }

    public MarketSimulationAlgorithm getAlgorithm() {
        return currentAlgorithm;
    }




    public double addSimulation(){
        algorithmStack.push(currentAlgorithm);
        return currentAlgorithm.simulate(this.currentPortfolioValue);
    }

    public double getPortfolioValue() {
        return currentPortfolioValue;
    }

    public void setPortfolioValue(double portfolioValue) {
        currentPortfolioValue = portfolioValue;
    }
}
