package mugloar.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * {
 "gameId":483159,
 "knight": {
 "name": "Sir. Russell Jones of Alberta",
 "attack": 2,
 "armor": 7,
 "agility": 3,
 "endurance": 8
 }
 }.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Battle {
    private Long gameId;
    private Knight knight;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Knight getKnight() {
        return knight;
    }

    public void setKnight(Knight knight) {
        this.knight = knight;
    }

    @Override
    public String toString() {
        return "Battle{" + "gameId=" + gameId + ", knight=" + knight + '}';
    }

    public static class Knight {
        private String name;
        private Integer attack;
        private Integer armor;
        private Integer agility;
        private Integer endurance;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAttack() {
            return attack;
        }

        public void setAttack(Integer attack) {
            this.attack = attack;
        }

        public Integer getArmor() {
            return armor;
        }

        public void setArmor(Integer armor) {
            this.armor = armor;
        }

        public Integer getAgility() {
            return agility;
        }

        public void setAgility(Integer agility) {
            this.agility = agility;
        }

        public Integer getEndurance() {
            return endurance;
        }

        public void setEndurance(Integer endurance) {
            this.endurance = endurance;
        }

        @Override
        public String toString() {
            return "Knight{" + "name='" + name + '\'' + ", attack=" + attack + ", armor=" + armor + ", agility=" + agility +
                    ", endurance=" + endurance + '}';
        }
    }
}
