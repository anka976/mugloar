package mugloar.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by anna.kulikova on 20/09/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DragonFight {
    private Dragon dragon;

    public Dragon getDragon() {
        return dragon;
    }

    public void setDragon(Dragon dragon) {
        this.dragon = dragon;
    }

    @Override
    public String toString() {
        return "Fight{" +
                "dragon=" + dragon +
                '}';
    }
}
