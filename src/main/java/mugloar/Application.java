package mugloar;

import java.util.Arrays;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import mugloar.entities.Battle;
import mugloar.entities.Report;


@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        int numberOfBattles = 0;
        String[] newArgs = {"5"};

        if (args.length > 0) {
            try {
                numberOfBattles = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }
        if (numberOfBattles == 0) {
            System.out.println("Please, enter the number of desired battles (default 5):");
            try {
                Scanner scanIn = new Scanner(System.in);
                String inputString = scanIn.nextLine();
                scanIn.close();
                numberOfBattles = Integer.parseInt(inputString);

            } catch (Exception ignored) {
            }
        }
        if (numberOfBattles != 0) {
            newArgs[0] = "" + numberOfBattles;
        }

        SpringApplication.run(Application.class, newArgs);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            log.info(Arrays.toString(args));
            int numberOfBattles = Integer.parseInt(args[0]);
            JAXBContext jc = JAXBContext.newInstance(Report.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();

            for (int i = 0; i < numberOfBattles; i++) {
                log.info("Running the battle " + i + " of " + numberOfBattles);

                Battle battle = restTemplate.getForObject("http://www.dragonsofmugloar.com/api/game", Battle.class);
                StreamSource source = restTemplate.getForObject("http://www.dragonsofmugloar.com/weather/api/report/" +
                                battle.getGameId(),
                        StreamSource.class);
                JAXBElement<Report> weather = unmarshaller.unmarshal(source,Report.class);
                String forecast = weather.getValue().toString();



                log.info("Battle claimed with: " + battle.toString());
                log.info("Weather was: " + forecast);
            }
        };
    }
}
