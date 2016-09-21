package mugloar.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by anna.kulikova on 20/09/16.
 * {
 "dragon": {
 "scaleThickness": 10,
 "clawSharpness": 5,
 "wingStrength": 4,
 "fireBreath": 1
 }
 }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fight {
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
