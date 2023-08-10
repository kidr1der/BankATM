import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MenuOperations implements MenuOperationsInterface {
    ControllerDatabase controller;
    Map<String, String[]> userDatabase;
    Card card;
    String userCardNumber;


    public MenuOperations(String userCardNumber, ControllerDatabase controller) {
        this.controller = controller;
        this.userCardNumber = userCardNumber;
        this.userDatabase = controller.getClientDatabase();
        this.card = new Card(Double.parseDouble(userDatabase.get(userCardNumber)[1]));

    }

    @Override
    public void depositMoney(double depositMoney) {
        if (depositMoney > 1000000) {
            System.out.println("Сумма пополнения не должна превышать 1 000 000");
        } else {
            card.setBalance(card.getBalance() + depositMoney);
            System.out.println("Сумма " + depositMoney + " успешно внесена на счет");
            viewBalance();

            userDatabase.get(userCardNumber)[1] = String.valueOf(card.getBalance());
            controller.writeToDatabase(userDatabase);
        }

    }

    @Override
    public void withdrawMoney(double withdrawMoney) {
        System.out.println(withdrawMoney);
        if (withdrawMoney % 100 == 0) {
            if (withdrawMoney <= card.getBalance()) {
                System.out.println("Заберите сумму в размере " + withdrawMoney);
                card.setBalance(card.getBalance() - withdrawMoney);
                viewBalance();

                userDatabase.get(userCardNumber)[1] = String.valueOf(card.getBalance());
                controller.writeToDatabase(userDatabase);
            } else {
                System.out.println("Недостаточно средств на счете!");
            }
        } else {
            System.out.println("Пожалуйста, введите сумму, кратную 100");
        }

    }

    @Override
    public void viewBalance() {
        System.out.println("Баланс на вашем счете: " + card.getBalance());

    }
}
