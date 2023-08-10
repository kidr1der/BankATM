public class Card {
    private double balance;
    private double depositMoney;
    private double withdrawMoney;

    public Card(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(double depositMoney) {
        this.depositMoney = depositMoney;
    }

    public double getWithdrawMoney() {
        return withdrawMoney;
    }

    public void setWithdrawMoney(double withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }
}
