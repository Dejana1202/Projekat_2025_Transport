package com.example.projekat.generator;

import com.example.projekat.models.Departure;
import com.example.projekat.models.Station;
import com.example.projekat.models.TransportData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Klasa za generisanje matrice grada dimenzija nxm i postavljanje u JSON fajl.
 */
public class TransportDataGenerator {

    int n;
    int m;
    private static final int DEPARTURES_PER_STATION = 5;
    private static final Random random = new Random();
    public TransportDataGenerator()
    {

    }

    /**
     * Konstruktor, koji generise gradove i polaske izmedju njih u obliku matrice dimenzija nxm.
     * Generisane podatke smjesta u JSON fajl.
     */
    public TransportDataGenerator(int n, int m)
    {
        this.n = n;
        this.m = m;
        TransportData data = generateData();
        saveToJson(data, "transport_data.json");
        System.out.println("Generisano.");
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    private TransportData generateData() {
        TransportData data = new TransportData();
        data.setCountryMap(generateCountryMap());
        data.setStations(generateStations());
        data.setDepartures(generateDepartures(data.getStations()));
        return data;
    }

    /**
     * generisanje gradova u formatu (G_X_Y)
     * @return mapa gradova
     */
    private String[][] generateCountryMap() {
        String[][] countryMap = new String[n][m];
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                countryMap[x][y] = "G_" + x + "_" + y;
            }
        }
        return countryMap;
    }

    /**
     * generisanje autobuskih i zeljeznickih stanica
     * @return lista stanica
     */
    private List<Station> generateStations() {
        List<Station> stations = new ArrayList<>();
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                Station station = new Station();
                station.setCity("G_" + x + "_" + y);
                station.setBusStation("A_" + x + "_" + y);
                station.setTrainStation("Z_" + x + "_" + y);
                stations.add(station);
            }
        }
        return stations;
    }

    /**
     * generisanje polazaka iz jedne stanice
     * @param stations
     * @return lista polazaka
     */
    private List<Departure> generateDepartures(List<Station> stations) {
        List<Departure> departures = new ArrayList<>();

        for (Station station : stations) {
            int x = Integer.parseInt(station.getCity().split("_")[1]);
            int y = Integer.parseInt(station.getCity().split("_")[2]);

            /**
             * generisanje polazaka autobusa
             */
            for (int i = 0; i < DEPARTURES_PER_STATION; i++) {
                departures.add(generateDeparture("autobus", station.getBusStation(), x, y));
            }
            /**
            *  generisanje polazaka vozova
            */

            for (int i = 0; i < DEPARTURES_PER_STATION; i++) {
                departures.add(generateDeparture("voz", station.getTrainStation(), x, y));
            }
        }
        return departures;
    }

    /**
     * Generisanje jednog polaska izmedju dvije stanice tj. izmedju dva grada
     * @param type autobus ili voz
     * @param from grad, iz kog se polazi
     * @param x
     * @param y
     * Odrediste zapisujemo u obliku G_X_Y, da bismo mogli provjeriti da li su dva grada susjedi. Na taj nacin formiramo odrediste za dati polazak.
     * @return
     */
    private Departure generateDeparture(String type, String from, int x, int y) {
        Departure departure = new Departure();
        departure.setType(type);
        departure.setFrom(from);

        /**
         *   generisanje susjeda
         */

        List<String> neighbors = getNeighbors(x, y);
        departure.setTo(neighbors.isEmpty() ? from : neighbors.get(random.nextInt(neighbors.size())));

        /**
         * generisanje vremena polaska
         */
        int hour = random.nextInt(24);
        int minute = random.nextInt(4) * 15; // 0, 15, 30, 45
        departure.setDepartureTime(String.format("%02d:%02d", hour, minute));

        /**
         * geneirsanje cijene
         */
        departure.setDuration(30 + random.nextInt(151));
        departure.setPrice(100 + random.nextInt(901));

        /**
         * generisanje vremena transfera
         */
        departure.setMinTransferTime(5 + random.nextInt(26));

        return departure;
    }

    /**
     *  pronalazak susjednih gradova za grad G_X_Y
     * @param x
     * @param y
     * @return svi susjedi
     */
    private List<String> getNeighbors(int x, int y) {
        List<String> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
                neighbors.add("G_" + nx + "_" + ny);
            }
        }
        return neighbors;
    }

    /**
     * cuvanje podataka u JSON fajl
     * @param data @see TransportData
     * @param filename JSON file name
     */
    private void saveToJson(TransportData data, String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            StringBuilder json = new StringBuilder();
            json.append("{\n");

            String[][] map = data.getCountryMap();
            /**
             * formiran se mapa drzave
             */
            json.append("  \"countryMap\": [\n");
            for (int i = 0; i < n; i++) {
                json.append("    [");
                for (int j = 0; j < m; j++) {
                    json.append("\"").append(map[i][j]).append("\"");
                    if (j < m - 1) json.append(", ");
                }
                json.append("]");
                if (i < n - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ],\n");

            /**
             * stanice
             */
            json.append("  \"stations\": [\n");
            for (int i = 0; i < data.getStations().size(); i++) {
                Station s = data.getStations().get(i);
                json.append("    {\"city\": \"").append(s.getCity())
                        .append("\", \"busStation\": \"").append(s.getBusStation())
                        .append("\", \"trainStation\": \"").append(s.getTrainStation())
                        .append("\"}");
                if (i < data.getStations().size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ],\n");

            /**
             * vremena polazaka
             */
            json.append("  \"departures\": [\n");
            for (int i = 0; i < data.getDepartures().size(); i++) {
                Departure d = data.getDepartures().get(i);
                json.append("    {\"type\": \"").append(d.getType())
                        .append("\", \"from\": \"").append(d.getFrom())
                        .append("\", \"to\": \"").append(d.getTo())
                        .append("\", \"departureTime\": \"").append(d.getDepartureTime())
                        .append("\", \"duration\": ").append(d.getDuration())
                        .append(", \"price\": ").append(d.getPrice())
                        .append(", \"minTransferTime\": ").append(d.getMinTransferTime())
                        .append("}");
                if (i < data.getDepartures().size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ]\n");

            json.append("}");
            file.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}