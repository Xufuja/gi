package dev.xfj.controllers;

import java.util.Map;

public interface Ascendable {
    Map<String, Double> getAscensionStat();
    Map<Integer, Integer> getAscensionItems();
    Map<Integer, Integer> getAscensionItems(int startingAscension, int targetAscension);
    Integer getAscensionCost();
    Integer getAscensionCost(int startingAscension, int targetAscension);
    Map<String, Integer> getAllAscensionItems();
    Integer getAllAscensionCosts();
    Integer getMaxLevel();
}
