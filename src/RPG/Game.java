package RPG;

import java.util.Objects;
import java.util.Scanner;

public class Game {
    //инвентарь


    //Класс для чтения введенных строк из консоли
    private static Scanner sc;
    //Игрок должен хранится на протяжении всей игры
    private static Character player = null;
    //Класс для битвы можно не создавать каждый раз, а переиспользовать
    private static Battle battle = null;
    //кол-во зелий
    private static int countPotion = 0;
    public static boolean weapon = false;
    public static int lvlWeapon = 1;


    public static void main(String[] args) {


        //Инициализируем scanner
        sc = new Scanner(System.in);
        //Инциализируем класс для боя
        battle = new Battle();
        //Первое, что нужно сделать при запуске игры, это создать персонажа, поэтому мы предлагаем ввести его имя
        System.out.println("Введите имя персонажа: ");
        //Далее ждем ввод от пользователя
        //Если это первый запуск, то мы должны создать игрока, именем будет служить первая веденная строка из консоли
        String string = sc.nextLine();
        if (player == null) {
            player = new Player(string, 100, 20, 35, 0, 5, 1000);
            System.out.printf("Игрок %s появился в мире!%n", player.getName());
            //Метод для вызова меню
            printNavigation();
        }

        while (true) {
            String menu1 = sc.nextLine();
            switch (menu1) {
                case "1" -> {
                    goToTrader();
                    printNavigation();
                }
                case "2" -> {
                    commitFight();
                    printNavigation();
                }
                case "3" -> System.exit(1);
                case "да" -> commitFight();
                case "нет" -> printNavigation();
                case "исцелиться" -> usePotion();
            }
        }
    }

    private static void printNavigation() {
        System.out.println("Куда вы хотите пойти?");
        System.out.println("1. К Торговцу");
        System.out.println("2. В темный лес");
        System.out.println("3. Выход");
    }

    private static Character createMonster() {
        //Рандомайзер
        int random = (int) (Math.random() * 10);
        //С вероятностью 50% создается скелет или гоблин
        if (random % 2 == 0) return new Goblin("Гоблин", 100, 10, 10, 101, 0, 28);
        else return new Skeleton("Скелет", 100, 10, 10, 102, 0, 34);
    }

    private static void commitFight() {
        battle.fight(player, createMonster(), new FightCallback() {
            @Override
            public void fightWin() {
                System.out.printf("""
                        %s победил! Теперь у вас:
                        %d опыта
                        %d золота
                        %d единиц здоровья
                        %d уровень(До следующего уровня %d опыта)%n""", player.getName(), player.getExp(), player.getGold(), player.getHp(), player.getLvl(), player.getLvl() * 500 - player.getExp());
                System.out.println("Желаете продолжить поход?(да/нет/исцелиться)");

            }

            @Override
            public void fightLost() {
                System.out.println("К сожалению вы пали в бою, в этом мире игра для вас закончена");
                System.exit(1);
            }
        });
    }

    private static void goToTrader() {
        System.out.printf("оружие: %b || уровень оружие: %d || золота :%d || зелья :%d\n", weapon, lvlWeapon, player.getGold(), countPotion);
        System.out.println("Что желаете купить?");
        System.out.println("1. Oружие");
        System.out.println("2. Зелье");
        System.out.println("3. Выход");

        String menuTrade = sc.nextLine();

        while (!Objects.equals(menuTrade, "3")) {

            switch (menuTrade) {

                case "1" -> {
                    if (weapon) {
                        System.out.printf("Вы желаете улучшить оружие за %d золота?(да/нет)\n", getPrice());
                        switch (sc.nextLine()) {
                            case "да" -> upgradeWeapon();
                            case "нет" -> menuTrade = "3";
                            //goToTrader();

                        }
                    } else {
                        System.out.printf("Чтобы приобрести оружие нужно 50 золота(да/нет), у вас %d золота\n", player.getGold());
                        switch (sc.nextLine()) {
                            case "да" -> buyWeapon();
                            case "нет" -> menuTrade = "3";
                            //goToTrader();
                        }
                    }
                }
                case "2" -> {
                    if (player.getGold() >= 50) {
                        player.setGold(player.getGold() - 50);
                        countPotion++;
                        System.out.printf("У вас %d зелий, золота %d\n", countPotion, player.getGold());
                        System.out.println("Продолжить покупку?(купить/нет)");

                        if ("нет".equals(sc.nextLine())) {
                            menuTrade = "3";
                        }
                    } else {
                        System.out.println("недостаточно золота, сходите в лес");
                        menuTrade = "3";
                    }
                }
            }
        }
    }

    private static void usePotion() {

        if (countPotion > 0) {
            countPotion--;
            player.setHp(100);
            System.out.printf("Вы вылечились, у вас %d хп и %d золота\n", player.getHp(), player.getGold());
            printNavigation();
        } else {
            System.out.println("У вас нет зелья");
            printNavigation();
        }

    }

    private static void buyWeapon() {
        if (player.getGold() >= 50) {
            player.setStr(player.getStr() + lvlWeapon * 5);
            player.setGold(player.getGold() - 50);
            weapon = true;
        } else {
            System.out.println("У вас недостаточно золота");
        }
    }


    private static int getPrice() {
        return lvlWeapon * 100 + 50;
    }

    private static void upgradeWeapon() {
        int price = getPrice();

        if (player.getGold() >= price && player.getLvl() > lvlWeapon) {
            lvlWeapon++;
            player.setGold(player.getGold() - price);
            player.setStr(player.getStr() + lvlWeapon * 5);
            System.out.println(lvlWeapon + " - уровень оружия");
            System.out.println(player.getGold() + " - золота");
        } else {
            System.out.printf("До следующего апгрейда нехватает %d золота\n", price - player.getGold());
            System.out.println("Недостаточно золота или ваш уровень ниже уровня оружия");
        }
    }

}







