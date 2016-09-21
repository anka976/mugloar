package mugloar.entities;

/**
 * Created by anna.kulikova on 21.09.2016.
 */
public class Dragon {
    private int scaleThickness;
    private int clawSharpness;
    private int wingStrength;
    private int fireBreath;

    public int getClawSharpness() {
        return clawSharpness;
    }

    public void setClawSharpness(int clawSharpness) {
        this.clawSharpness = clawSharpness;
    }

    public int getFireBreath() {
        return fireBreath;
    }

    public void setFireBreath(int fireBreath) {
        this.fireBreath = fireBreath;
    }

    public int getScaleThickness() {
        return scaleThickness;
    }

    public void setScaleThickness(int scaleThickness) {
        this.scaleThickness = scaleThickness;
    }

    public int getWingStrength() {
        return wingStrength;
    }

    public void setWingStrength(int wingStrength) {
        this.wingStrength = wingStrength;
    }

    @Override
    public String toString() {
        return "Dragon{" +
                "clawSharpness=" + clawSharpness +
                ", scaleThickness=" + scaleThickness +
                ", wingStrength=" + wingStrength +
                ", fireBreath=" + fireBreath +
                '}';
    }
}
