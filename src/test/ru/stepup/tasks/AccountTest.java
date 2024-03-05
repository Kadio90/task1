package ru.stepup.tasks;

import org.junit.jupiter.api.*;

import java.util.EmptyStackException;

public class AccountTest {
    static Account account;
    @BeforeEach
    void beforeEach() {
        account = new Account("AccountName");
        System.out.println("Создан новый счет: " + account);
    }

    @AfterEach
    void afterEach(){
        account = null;
    }

    @Test
    @DisplayName("Создание объекта Account возможно только с указанием имени владельца счета")
    public void createAccountByName ()
    {
        Assertions.assertThrows(IllegalArgumentException.class, ()->new Account(null));
    }

    @Test
    @DisplayName("Имя не может быть null (выдаем ошибку с неверным аргументом)")
    public void setAccountNullName()
    {
        Assertions.assertThrows(IllegalArgumentException.class, ()->account.setName(null));
    }

    @Test
    @DisplayName("Имя не может быть пустым (выдаем ошибку с неверным аргументом)")
    public void setAccountEmptyName()
    {
        Assertions.assertThrows(IllegalArgumentException.class, ()->account.setName(""));
    }

    @Test
    @DisplayName("Метод Set/Get успешно меняет и возвращает новое Имя счета")
    public void setAccountName()
    {
        account.setName("NewName");
        System.out.println("Счет: " + account);
        Assertions.assertEquals("NewName", account.getName());
    }

    @Test
    @DisplayName("Количество валюты не может быть отрицательным")
    public void setAccountNegativeAmount()
    {
        Assertions.assertThrows(IllegalArgumentException.class, ()->account.putAmounts(CurrencyGenerator.randomCurrencyType(), (long) -1));
    }

    @Test
    @DisplayName("Методы Put/Get успешно добавляют/получают валюту")
    public void putAccountNewCurrency()
    {
        CurrencyType currencyType = CurrencyGenerator.randomCurrencyType();
        Long number = (long) (Math.random() * 100);
        System.out.println("Добавляем Валюту: " + currencyType + " в количестве " + number);
        account.putAmounts(currencyType, number);
        System.out.println("Счет: " + account);
        Assertions.assertNotNull(account.getAmounts());
        Assertions.assertEquals(number, account.getAmounts().get(currencyType));
    }

    @Test
    @DisplayName("Методы Put/Get успешно изменяют существующую/получают валюту")
    public void putAccountExistsCurrency()
    {
        CurrencyType currencyType = CurrencyGenerator.randomCurrencyType();
        Long number = (long) (Math.random() * 100);
        System.out.println("Добавляем Валюту: " + currencyType + " в количестве " + number);
        account.putAmounts(currencyType, number);
        System.out.println("Счет: " + account);
        Long numberNew = (long) (Math.random() * 100);
        System.out.println("Заменяем значение Валюты: " + currencyType + " на " + numberNew);
        account.putAmounts(currencyType, numberNew);
        System.out.println("Счет: " + account);
        Assertions.assertNotNull(account.getAmounts());
        Assertions.assertEquals(numberNew, account.getAmounts().get(currencyType));
    }

    @Test
    @DisplayName("При отсутствии изменений в экземпляра откат должен кидать исключение!")
    public void undoAccountNoChanges()
    {
        Assertions.assertThrows(EmptyStackException.class, ()->account.undo());
    }

    @Test
    @DisplayName("При наличии изменений операция undoEmpty должна возвращать True!")
    public void undoAccountIsEmpty()
    {
        account.setName("NewName");
        Assertions.assertTrue(()->account.undoEmpty());
    }

    @Test
    @DisplayName("При отсутствии изменений операция undoEmpty должна возвращать False!")
    public void undoAccountIsNoEmpty()
    {
        Assertions.assertFalse(()->account.undoEmpty());
    }

    @Test
    @DisplayName("Откат изменений должен возвращать объект в исходное состояние!")
    public void undoAccount()
    {
        String oldName = account.getName();
        CurrencyType currencyType = CurrencyGenerator.randomCurrencyType();
        account.setName("NewName");
        System.out.println("Счет после смены наименования: " + account);
        Long amount = (long) (Math.random() * 100);
        account.putAmounts(currencyType, amount);
        System.out.println("Счет после добавления валюты: " + account);
        account.putAmounts(currencyType, (long) (Math.random() * 100));
        System.out.println("Счет после изменения суммы валюты: " + account);
        account.undo();
        System.out.println("Счет после отката изменения суммы: " + account);
        Assertions.assertEquals(amount, account.getAmounts().get(currencyType));
        account.undo();
        System.out.println("Счет после отката добавления валюты: " + account);
        Assertions.assertTrue(account.getAmounts().isEmpty());
        account.undo();
        System.out.println("Счет после отката изменения имени: " + account);
        Assertions.assertEquals(oldName, account.getName());
    }

    @Test
    @DisplayName("Метод Set/Get успешно меняет и возвращает новое значение типа счета")
    public void setAccountAccountType()
    {
        account.setAccountType(AccountType.Premium);
        System.out.println("Счет после изменения типа счета: " + account);
        Assertions.assertEquals(AccountType.Premium, account.getAccountType());
    }

    @Test
    @DisplayName("Откат изменений должен учитывать новые добавленные поля AccountType!")
    public void undoAccountByAccountType()
    {
        account.setAccountType(AccountType.Premium);
        System.out.println("Счет после изменения типа счета: " + account);
        account.undo();
        System.out.println("Счет после отката изменения имени: " + account);
        Assertions.assertEquals(AccountType.Standard, account.getAccountType());
    }
}
