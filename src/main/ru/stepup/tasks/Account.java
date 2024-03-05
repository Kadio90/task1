package ru.stepup.tasks;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

public class Account {
    @Getter
    private String name;   // Имя владельца
    @Getter
    private AccountType accountType; // Тип счета
    private HashMap<CurrencyType, Long> amounts; // Количество валют
    private Stack<Command> commands; // Очередь для перечня команд

    private Account(){}; // Запрещаем конструктор без параметров
    public Account(String name) {
        setName(name);
        setAccountType(AccountType.Standard);
        this.amounts = new HashMap<>(); // Инициализируем коллекцию для хранения Валюты/Количества
        this.commands = new Stack<>();  // Инициализируем стек
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Имя не может быть пустым!");
        if (this.name != null) {
            String oldName = this.name;
            this.commands.push(()->{this.name = oldName;}); // Сохраняем операцию изменения имени в стек
        }
        this.name = name;
    }

    public HashMap<CurrencyType, Long> getAmounts() {
        return new HashMap<>(amounts);
    }

    public void putAmounts(CurrencyType currencyType, Long amount) {
        if (amount == null || amount < 0) throw new IllegalArgumentException("Количество валюты не может быть отрицательным!");
        if (this.amounts.containsKey(currencyType)) {
            Long oldAmount = this.amounts.get(currencyType);
            this.commands.push(() -> {this.amounts.put(currencyType, oldAmount);}); // Сохраняем старое значение в стеке команд
        } else {
            this.commands.push(() -> {this.amounts.remove(currencyType);}); // Удаляем старое значение так как его не было ранее
        }
        this.amounts.put(currencyType, amount);
    }

    @Override
    public String toString() {
        return "\"" + this.name + "\" тип акаунта \"" + this.accountType + "\" список валют " + this.amounts.toString();
    }

    public void undo(){
        this.commands.pop().execute();
    }

    public boolean undoEmpty(){
       return !this.commands.isEmpty();
    }

    public void setAccountType(AccountType accountType) {
        if (accountType == null) throw new IllegalArgumentException("Тип счета не может быть пустым!");
        if (this.accountType != null) {
            AccountType oldAccountType = this.accountType;
            this.commands.push(()->{this.accountType = oldAccountType;}); // Сохраняем операцию изменения типа счета в стек
        }
        this.accountType = accountType;
    }

    public Loadable save(){
        return new Snapshot();
    }
    // Создание вложенного класса для сохранения счета
    private class Snapshot implements Loadable {
        private String name;   // Имя владельца
        private AccountType accountType; // Тип счета
        private HashMap<CurrencyType, Long> amounts; // Количество валют
        private Stack<Command> commands; // Очередь для перечня команд

        public Snapshot(){
            this.name = Account.this.name;
            this.accountType = Account.this.accountType;
            this.amounts = new HashMap<>(Account.this.amounts);
            this.commands = (Stack<Command>)Account.this.commands.clone();
        }
        @Override
        public void load() {
            Account.this.name = this.name;
            Account.this.accountType = this.accountType;
            Account.this.amounts = new HashMap<>(this.amounts);
            Account.this.commands = (Stack<Command>)this.commands.clone();
        }
    }
}
