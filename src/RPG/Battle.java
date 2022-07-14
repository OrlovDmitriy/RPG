package RPG;

import java.io.IOException;

public class Battle {
    //Метод, который вызывается при начале боя, сюда мы передаем ссылки на нашего героя и монстра, который встал у него на пути
    public void fight(Character hero, Character monster, FightCallback fightCallback) {
        //Ходы будут идти в отдельном потоке
        Runnable runnable = () -> {
            //Сюда будем записывать, какой сейчас ход по счету
            int turn = 1;
            //Когда бой будет закончен мы
            boolean isFightEnded = false;
            while (!isFightEnded) {
                System.out.println("---Ход: " + turn + "---");
                //Войны бьют по очереди, поэтому здесь мы описываем логику смены сторон
                if (turn++ % 2 != 0) {
                    try {
                        isFightEnded = makeHit(monster, hero, fightCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        isFightEnded = makeHit(hero, monster, fightCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    //Чтобы бой не проходил за секунду, сделаем имитацию работы, как если бы у нас была анимация
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //запускаем новый поток
        Thread thread = new Thread(runnable);
        thread.start();
    }

    //Метод для совершения удара
    private Boolean makeHit(Character defender, Character attacker, FightCallback fightCallback) throws IOException {
        //Получаем силу удара
        int hit = attacker.attack();
        //Отнимает кол-во урона из здоровья защищающегося
        int defenderHealth = defender.getHp() - hit;
        if (defenderHealth < 0) {
            defenderHealth = 0;
        }
        //Если атака прошла успешно выводим сообщение
        if (hit != 0) {
            System.out.println(String.format("%s нанес удар в %d единиц!", attacker.getName(), hit));
            System.out.println(String.format("У %s осталось %d единиц здоровья...", defender.getName(), defenderHealth));
        } else {
            //Если атакующий промахнулся (то есть урон не 0), выводим это сообщение
            System.out.println(String.format("%s промахнулся!", attacker.getName()));
        }
        if (defenderHealth == 0 && defender instanceof Player) {
            //Если здоровье меньше 0 и защищающийся был героем, то игра заканчивается
            System.out.println("Вы проиграли =(");
            //Вызываем коллбэк, что мы проиграли
            fightCallback.fightLost();
            return true;
        } else if (defenderHealth == 0) {

            //Если здоровья больше нет и защищающийся - это монстр, то мы забираем от монстра его опыт и золото
            System.out.println(String.format("Враг повержен! Вы получаете %d опыт и %d золота", defender.getExp(), defender.getGold()));
            attacker.setExp(attacker.getExp() + defender.getExp());
            attacker.setGold(attacker.getGold() + defender.getGold());
            if(attacker.getLvl() * 500 <= attacker.getExp()) {
                attacker.setLvl(attacker.getLvl() + 1);
                attacker.setExp(0);
                attacker.setStr(attacker.getStr() + 10);
                attacker.setDex(attacker.getDex() + 10);
            }

            //Вызываем коллбэк, что мы победили
            fightCallback.fightWin();
            return true;
        } else {
            //Если защищающийся повержен, то мы устанавливаем ему новый уровень здоровья
            defender.setHp(defenderHealth);
            return false;
        }
    }
}
