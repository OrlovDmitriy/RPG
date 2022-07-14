package RPG;

import java.io.IOException;

public interface FightCallback {
    void fightWin() throws IOException;
    void fightLost();
}
