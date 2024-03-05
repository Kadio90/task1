package ru.stepup.tasks;

import java.util.Random;

public class CurrencyGenerator {
    private static final Random random = new Random();

    public static CurrencyType randomCurrencyType(){
        CurrencyType[] currencyTypes = CurrencyType.values();
        return currencyTypes[random.nextInt(currencyTypes.length)];
    }
}
