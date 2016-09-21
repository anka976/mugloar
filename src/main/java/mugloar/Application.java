package mugloar;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import mugloar.entities.Battle;
import mugloar.entities.Dragon;
import mugloar.entities.Fight;
import mugloar.entities.Report;
import mugloar.entities.Response;


@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static final Logger logFile = LoggerFactory.getLogger("STATS");

    private static final String DRAGONSOFMUGLOAR_COM_API_GAME = "http://www.dragonsofmugloar.com/api/game";
    private static final String DRAGONSOFMUGLOAR_COM_WEATHER_API_REPORT = "http://www.dragonsofmugloar.com/weather/api/report/";
    private static final String DRAGONSOFMUGLOAR_COM_API_GAME_SOLUTION = "http://www.dragonsofmugloar"
            + ".com/api/game/{gameId}/solution";

    private static final String STORM_REPORT = "SRO";
    private static final String UMBRELLA_REPORT = "HVA";
    private static final String BALANCE_REPORT = "T E";
//    private static final String NORM_REPORT = "NMR";

    private static final String KEY_CLAW = "CLAW";
    private static final String KEY_FIRE = "FIRE";
    private static final String KEY_SCALE = "SCALE";
    private static final String KEY_WING = "WING";

    private static final String RESP_VICTORY = "Victory";

    public static void main(String args[]) {
        int numberOfBattles = 0;
        String[] newArgs = {"5"};

        if (args.length > 0) {
            try {
                numberOfBattles = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }
        if (numberOfBattles <= 0) {
            System.out.println("Please, enter the number of desired battles (default 5):");
            try {
                Scanner scanIn = new Scanner(System.in);
                String inputString = scanIn.nextLine();
                scanIn.close();
                numberOfBattles = Integer.parseInt(inputString);
            } catch (Exception ignored) {
            }
        }
        if (numberOfBattles > 0) {
            newArgs[0] = "" + numberOfBattles;
        }

        SpringApplication.run(Application.class, newArgs);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        InetSocketAddress address = new InetSocketAddress("proxy", 80);
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
//        factory.setProxy(proxy);
        return builder
//                .requestFactory(factory)
                .build();
    }

    @Bean
    public Unmarshaller unmarshaller() throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Report.class);
        return jc.createUnmarshaller();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate, Unmarshaller unmarshaller) throws Exception {
        return args -> {
            log.info(Arrays.toString(args));
            int numberOfBattles = Integer.parseInt(args[0]);
            int victories = 0;
            int loses = 0;
            String info;

            logFile.info("----------------------------- BATTLE TIME! --------------------------------------");

            for (int i = 0; i < numberOfBattles; i++) {
                info = "Starting the battle " + (i + 1) + " of " + numberOfBattles;
                log.info(info);
                logFile.info(info);

                Battle battle = restTemplate.getForObject(DRAGONSOFMUGLOAR_COM_API_GAME, Battle.class);
                info = "Battle claimed with: " + battle.toString();
                log.info(info);
                logFile.info(info);
                String forecast = getForecast(restTemplate, unmarshaller, battle.getGameId());
                if (fight(restTemplate, battle, forecast)) {
                    victories++;
                } else {
                    loses++;
                }
            }
            info = "============ Final stats for " + numberOfBattles + " battles: " + victories + " won, " + loses + " lost.";
            log.info(info);
            logFile.info(info);
        };
    }

    private String getForecast(RestTemplate restTemplate, Unmarshaller unmarshaller, Long gameId) {
        StreamSource source = restTemplate.getForObject(DRAGONSOFMUGLOAR_COM_WEATHER_API_REPORT + gameId, StreamSource.class);
        try {
            JAXBElement<Report> weather = unmarshaller.unmarshal(source, Report.class);
            log.info("Weather was: " + weather.getValue().toString());
            logFile.info("Weather was: " + weather.getValue().getCode());
            return weather.getValue().getCode();
        } catch (JAXBException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    private boolean fight(RestTemplate restTemplate, Battle battle, String weather) {
        Fight fight = new Fight();
        Dragon dragon = new Dragon();

        switch (weather) {
        case UMBRELLA_REPORT: {
            dragon.setClawSharpness(10); //armor
            dragon.setFireBreath(0); //endurance
            dragon.setScaleThickness(5); //attack
            dragon.setWingStrength(5); //agility
            break;
        }
        case BALANCE_REPORT: {
            dragon.setClawSharpness(5); //armor
            dragon.setFireBreath(5); //endurance
            dragon.setScaleThickness(5); //attack
            dragon.setWingStrength(5); //agility
            fight.setDragon(dragon);
            break;
        }
        case STORM_REPORT: {
            dragon = null;
            break;
        }
        default: {
            Map<String, Integer> solution = new TreeMap<>();
            solution.put(KEY_CLAW, battle.getKnight().getArmor());
            solution.put(KEY_FIRE, battle.getKnight().getEndurance());
            solution.put(KEY_SCALE, battle.getKnight().getAttack());
            solution.put(KEY_WING, battle.getKnight().getAgility());

            Counter counter = new Counter();

            solution.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEachOrdered(stringIntegerEntry -> stringIntegerEntry.setValue(counter.getValue(stringIntegerEntry
                            .getValue())));

            dragon.setClawSharpness(solution.get(KEY_CLAW)); //armor
            dragon.setFireBreath(solution.get(KEY_FIRE)); //endurance
            dragon.setScaleThickness(solution.get(KEY_SCALE)); //attack
            dragon.setWingStrength(solution.get(KEY_WING)); //agility
            break;
        }
        }

        fight.setDragon(dragon);

        log.info(fight.toString());
        logFile.info(fight.toString());
        HttpEntity<Fight> entity = new HttpEntity<>(fight);
        ResponseEntity<Response> response = restTemplate.exchange(DRAGONSOFMUGLOAR_COM_API_GAME_SOLUTION,
                HttpMethod.PUT,
                entity,
                Response.class,
                battle.getGameId());
        log.info(response.getBody().getStatus() + ": " + response.getBody().getMessage());
        logFile.info(response.getBody().getStatus());
        return RESP_VICTORY.equals(response.getBody().getStatus());
    }

    private class Counter {
        private final int finalSum = 20;
        private final int superPower = 8;
        private final int epicWin = 2;
        private final int win = 1;
        private final Set<Integer> epics = new HashSet<>(Arrays.asList(6, 7, superPower));

        int index;
        int sum;
        boolean epic;
        boolean superpower;

        int getValue(int init) {
            index++;
            if (init == 0) {
                return 0;
            }

            switch (index) {
            case 1:
                epic = epics.contains(init);
                superpower = init > superPower;
                sum = epic ? init + epicWin : init + win;
                return sum;
            case 2:
                int a = superpower ? init - win : epic ? init : init + win;
                a = finalSum - a < sum ? finalSum - sum : a;
                sum += a;
                return a;
            case 3:
                int b = init > win ? init - win : 0;
                b = finalSum - b < sum ? finalSum - sum : b;
                sum += b;
                return b;
            case 4:
                return finalSum - sum;
            }

            return 0;
        }
    }
}
