package ru.stepup.tasks;
/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args ) {
        Account acount = new Account("Зюркалов Антон Александрович");
        acount.setAccountType(AccountType.Premium);
        acount.putAmounts(CurrencyType.RUB, (long)100000);
        System.out.println(acount);

        Loadable firstSave = acount.save();
        acount.putAmounts(CurrencyType.USD, (long)1000);
        System.out.println(acount);

        firstSave.load();
        System.out.println(acount);
        acount.undo();
        System.out.println(acount);
    }
}
