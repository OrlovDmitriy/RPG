package RPG;

public abstract class Character implements doAttack {

    private String name;    //Имя

    private int hp;         //здоровье
    private int dex;        //ловкость
    private int str;        //сила

    private int exp;        //опыт
    private int lvl;        //lvl

    private int gold;       //золото

    public Character(String name, int hp, int dex, int str, int exp, int lvl, int gold) {
        this.name = name;
        this.hp = hp;
        this.dex = dex;
        this.str = str;
        this.exp = exp;
        this.lvl = lvl;
        this.gold = gold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDex() {
        return dex;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    @Override
    public int attack() {
        if (dex * 3 >= (int) (Math.random() * 100)) { //проверка на урон
            if (((double) dex/100) >= Math.random()) return str * 2; //проверка на критический урон
            else return str;
        } else {
            return 0;
        }
    }
}
