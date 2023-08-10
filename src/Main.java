
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args)  {
        ControllerDatabase controller = new ControllerDatabase("database.txt");
        Map<String, String[]> userDatabase = controller.getClientDatabase();

        while (true) {
            String enterUserCardNumber = authorization(userDatabase);
            if (enterUserCardNumber != null) {
                menu(enterUserCardNumber, controller);
            }
        }
    }


    public static String authorization(Map<String, String[]> userDatabase) {
        Pattern pattern = Pattern.compile("^\\d{4}-\\d{4}-\\d{4}-\\d{4}$");
        Instant instant = Instant.now();
        long currentTime = instant.toEpochMilli();
        Blocked blocked = new Blocked("blockedcard.txt");
        Scanner input = new Scanner(System.in);
        boolean flag = true;
        String enterUserCardNumber;
        while (flag) {
            System.out.println("Введите номер карты в формате ХХХХ-ХХХХ-ХХХХ-ХХХХ");
            enterUserCardNumber = input.nextLine();
            Matcher matcher = pattern.matcher(enterUserCardNumber);
            if (!matcher.find()){
                System.out.println("Неверный формат карты\n");
                continue;
            }
            if (userDatabase.get(enterUserCardNumber) == null) {
                System.out.println("Введенный номер карты не найден. Повторите попытку\n");
                continue;
            }
            if (blocked.checkBlocked(currentTime, enterUserCardNumber)) {
                System.out.println("""
                        Извините. Данная карта заблокирована :(
                        Попробуй ввести другую карту.
                        """);
                break;

            }
            for (int i = 1; i <= 3; i++) {
                System.out.println("Введите PIN-код");
                String enterUserPin = input.nextLine();
                if (userDatabase.get(enterUserCardNumber)[0].equals(enterUserPin)) {
                    return enterUserCardNumber;
                }
                if (i == 3) {
                    blocked.addBlocked(currentTime, enterUserCardNumber);
                    System.out.println("Неверный PIN-код. Ваша карта заблокирована на 24ч");
                    break;
                }
                System.out.println("Неверный PIN-код. У вас осталось " + (3 - i) + " попытки(а)\n");
                flag = false;

            }

        }
        return null;
    }


    public static void menu(String enterUserCardNumber, ControllerDatabase controller) {
        Scanner input = new Scanner(System.in);

        String[] menu = {
                "1 - Узнать баланс",
                "2 - Снять наличные",
                "3 - Внести наличный",
                "4 - Завершить обслуживание"
        };

        MenuOperationsInterface menuOperations  = new MenuOperations(enterUserCardNumber, controller);

        while (true) {
            System.out.println("\nВыберите действие:");
            for (String line : menu) {
                System.out.println(line);
            }
            String choice = input.nextLine();
            switch (choice) {
                case "1":
                    menuOperations.viewBalance();
                    break;
                case "2":
                    System.out.println("Введите сумму для снятия:");
                    try {
                        menuOperations.withdrawMoney(input.nextDouble());
                    } catch (InputMismatchException e){
                        System.out.println("Некорректный ввод");
                    }
                    break;
                case "3":
                    System.out.println("Введите сумму для пополнения:");
                    try {
                        menuOperations.depositMoney(input.nextDouble());
                    } catch (InputMismatchException e){
                        System.out.println("Некорректный ввод");
                    }
                    break;
                case "4":
                    System.out.println("Спасибо, что выбрали Банк N. Приходите еще.");
                    System.exit(0);
                default:
                    System.out.println("Пожалуйста, выберите один из предложенных вариантов");
                    break;
            }
        }
    }


}
