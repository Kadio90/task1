package ru.stepup.tasks;

import org.junit.jupiter.api.*;

public class SnapshotTest {
    static Account account;
    Loadable saveAccount;
    @BeforeEach
    void beforeEach() {
        account = new Account("AccountName");
        account.setAccountType(AccountType.Premium);
        account.putAmounts(CurrencyType.RUB, (long) (Math.random() * 100));
        account.putAmounts(CurrencyType.USD, (long) (Math.random() * 100));
        account.putAmounts(CurrencyType.EUR, (long) (Math.random() * 100));
        System.out.println("Создан новый счет: " + account);
        saveAccount = account.save();
    }

    @AfterEach
    void afterEach(){
        account = null;
        saveAccount = null;
    }
    @Test
    @DisplayName("Изменения оригинального объекта Account не оказывают влияния на Сохранение!")
    public void createLoadAccountSnapshot()
    {
        String oldName = account.getName();
        account.setName("NewName");
        System.out.println("Счет после изменения наименования: " + account);
        saveAccount.load();
        System.out.println("Счет после загрузки сохранения: " + account);
        Assertions.assertEquals(oldName, account.getName());
    }

    @Test
    @DisplayName("Объектов Сохранений может быть сколько угодно для каждого из Account!")
    public void createManuAccountSnapshot()
    {
        account.setName("FirstAccount");
        Loadable saveAcountSetName = account.save();
        System.out.println("Создано сохранение " + saveAcountSetName + " для счета: " + account);
        account.setAccountType(AccountType.Premium);
        Loadable saveAcountSetAccountType = account.save();
        System.out.println("Создано сохранение " + saveAcountSetAccountType + " для счета: " + account);
        account.putAmounts(CurrencyType.RUB, (long)1000);
        Loadable saveAcountPutEmount = account.save();
        System.out.println("Создано сохранение " + saveAcountPutEmount + " для счета: " + account);
        Assertions.assertNotEquals(saveAcountSetName, saveAcountSetAccountType);
        Assertions.assertNotEquals(saveAcountSetName, saveAcountPutEmount);
        Assertions.assertNotEquals(saveAcountSetAccountType, saveAcountPutEmount);
    }

    @Test
    @DisplayName("Любое Сохранение может быть использовано для приведения соответствующего ему объекта Account в состояние соответствующее моменту создания сохранения!")
    public void creatLoadSnapshotManuAccount()
    {
        String accountNewName = "NewName";
        Account acountNew = new Account(accountNewName);
        acountNew.setAccountType(AccountType.Premium);
        acountNew.putAmounts(CurrencyGenerator.randomCurrencyType(), (long)(Math.random()*100));
        System.out.println("Второй счет после изменения наименования: " + acountNew);
        Loadable saveNewAcount = acountNew.save();

        String accountOldName = account.getName();
        account.setName("FirstAccount");
        acountNew.setName("LastAccount");
        System.out.println("Первый счет после изменения наименования: " + account);
        System.out.println("Второй счет после изменения наименования: " + acountNew);
        saveNewAcount.load();
        saveAccount.load();
        System.out.println("Первый cчет после загрузки сохранения: " + account);
        System.out.println("Второй cчет после загрузки сохранения: " + acountNew);
        Assertions.assertEquals(accountOldName, account.getName());
        Assertions.assertEquals(accountNewName, acountNew.getName());
    }

    @Test
    @DisplayName("Информация для отмены действий так же восстанавливается на момент сохранения!")
    public void LoadSnapshotAccountUndo()
    {
        account.setName("NewName");
        saveAccount.load();
        System.out.println("Счет после загрузки сохранения: " + account);
        account.undo();
        System.out.println("Счет после отката: " + account);
        account.undo();
        System.out.println("Счет после отката: " + account);
        account.undo();
        System.out.println("Счет после отката: " + account);
        Assertions.assertTrue(account.getAmounts().isEmpty());
        Assertions.assertNotEquals("NewName", account.getName());
    }

}
