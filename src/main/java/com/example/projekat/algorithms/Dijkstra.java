package com.example.projekat.algorithms;

import com.example.projekat.models.Criteria;
import com.example.projekat.models.Departure;
import com.example.projekat.models.Route;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Implementacija Dijkstra algoritma, koja radi nad GraphStream grafom.
 * Tezina svake grane se racuna dinamicki, u zavisnosti od proslijedjenog krijerijuma pretrage. @see {@link Criteria}.
 * Cvorovi i ivice koriste atribute, za cuvanje podataka za pretragu.
 */
public class Dijkstra {
    /**
     *
     * @param graph mapa gradova
     * @param source polazna stanica
     * @param target odredisna stanica
     * @param criteria kriterijum pretrage najbolje rute
     * @return Lista cvorova (Node) koji predstavljaju sve rute izmedju gradova, kroz koje se prolazi. Uz pomoc klase @see {@link Route} pamtimo osnovne informacije kao sto su cijena, vrijeme, gradovi kroz koje se prolazi,....
     * Lista predstavlja podatke o medjugradovima i rutama izmedju njih.
     */
    public static List<Node> dijkstra(Graph graph, Node source, Node target, Criteria criteria) {
//        // inicijalizacija atributa za sve čvorove
        /**
         * Inicijalizuju se atributi svih cvorova prolaskom kroz graf.
         * Svakom cvoru se postavlja atribut "distance" na "beskonacnost".
         * "previus" se postavlja na null, radi kasnije rekonstrukcije puta.
         * "currentTime" se uklanja zbor resetovanja prethodno sacuvanog vremena dolaska.
         */
        for (int i = 0; i < graph.getNodeCount(); i++) {
            Node n = graph.getNode(i);
            n.setAttribute("distance", Double.POSITIVE_INFINITY);
            n.setAttribute("previous", null);
            n.removeAttribute("currentTime");
        }

        /**
         * "distance" se na pocetku postavlja na nulu, jer je udaljenost cvora "do sebe" = 0.
         */
        LocalDateTime currentDateTime = LocalDateTime.now();
        source.setAttribute("distance", 0.0);
        source.setAttribute("currentTime", currentDateTime);


        /**
         * @param settled - cvorovi cija je udaljenost obradjena.
         * @param unsettled - cvorovi sa neobradjenom udaljenosti, na pocetku je to samo source cvor.
         */
        Set<Node> settled = new HashSet<>();
        Set<Node> unsettled = new HashSet<>();
        unsettled.add(source);

        /**
         * Obrada neobradjenih cvorova, dok god takvi postoje.
         * Petlju prekida dolazak do krajnjeg cvora tj. kada je current = target.
         */
        while (!unsettled.isEmpty()) {
            Node current = getLowestDistanceNode(unsettled);
            if (current == null) break;
            unsettled.remove(current);

            if (current.equals(target) || current.getId().equals(target.getId())) {
                break;
            }


            double minEdgeWeight = 999999999;
            // iteriraj kroz sve izlazne grane (usmjeren graf)
            /**
             * Obrada svake grane. Za svaki trenutni cvor se prolazi kroz njegove grane, a poslije obrade se taj cvor dodaje u set obradjenih (settled).
             * Ako grana nije usmjerena od tekuceg cvora prema drugom cvoru ili ako ima disabled atribut ili ako je target vec u obradjenom setu, preskace se.
             */
            for (Edge e : current.edges().toList()) {
                if(e.getSourceNode().equals(current) == false) {
                    continue;
                }
                if(e.getAttribute("disabled") != null && e.getAttribute("disabled").equals(true)) continue;

                Node neighbor = e.getTargetNode();

                if (settled.contains(neighbor)) continue;

                /**
                 * @param edgeWeight tezina grane, u zavisnosti od kriterijuma i trenutnog vremena.
                 * Atributi trenutnog cvora i njegovog susjeda .
                 * @param currentDistance
                 * @param neighborDistance
                 * @param newDistance suma trenutne udaljenosti do tekuceg cvora i tezine grane.
                 */
                double edgeWeight = getEdgeWeight(e, criteria, (LocalDateTime) current.getAttribute("currentTime"));

                Number currDistN = (Number) current.getAttribute("distance");
                double currentDistance = currDistN == null ? Double.POSITIVE_INFINITY : currDistN.doubleValue();

                Number neighDistN = (Number) neighbor.getAttribute("distance");
                double neighborDistance = neighDistN == null ? Double.POSITIVE_INFINITY : neighDistN.doubleValue();

                double newDistance = currentDistance + edgeWeight;

                /**
                 * Azuriranje susjednog cvora : distance postaje neighbor, previous postaje current
                 */
                if (newDistance < neighborDistance) {
                    neighbor.setAttribute("distance", newDistance);
                    neighbor.setAttribute("previous", current);
                    neighbor.setAttribute("currentTime", ((LocalDateTime)((LocalDateTime) current.getAttribute("currentTime")).plusMinutes((int)edgeWeight)));
                    unsettled.add(neighbor);

//                    if(Criteria.FASTEST.equals(criteria)) {
//                        // Track current time when the bus/train arrives
//                        currentDateTime = currentDateTime.plusMinutes((int)minEdgeWeight);
//                    }
//                    minEdgeWeight = edgeWeight;
                }
            }
//            if(Criteria.FASTEST.equals(criteria)) {
//                // Track current time when the bus/train arrives
//                currentDateTime = currentDateTime.plusMinutes((int)minEdgeWeight);
//            }
            settled.add(current);
        }

        return reconstructPath(target);
    }

    /**
     * Rekonstrukcija puteva pomocu previous atributa svih cvorova
     * @return lista cvorova od izvora do cilja (ako postoji put)
     */
    private static List<Node> reconstructPath(Node target) {
        LinkedList<Node> path = new LinkedList<>();

        Object prev = target.getAttribute("previous");
        if (prev == null) {
            return path; // nema puta
        }

        Node step = target;
        path.addFirst(step);
        while ((prev = step.getAttribute("previous")) != null) {
            step = (Node) prev;
            path.addFirst(step);
        }
        return path;
    }

    /**
     * Bira sljedeci cvor u setu cvorova na osnovu najmanje vrijednosti "distance" atributa
     * @param nodes neobradjeni cvorovi
     * @return cvor, sa najmanjim atributom "distance" unutar skupa neobradjenih cvorova
     */
    private static Node getLowestDistanceNode(Set<Node> nodes) {
        Node lowest = null;
        double min = Double.POSITIVE_INFINITY;
        for (Node n : nodes) {
            if(n.getAttribute("disabled") != null && (boolean) n.getAttribute("disabled") == true) continue;

            Number d = (Number) n.getAttribute("distance");
            double dist = d == null ? Double.POSITIVE_INFINITY : d.doubleValue();
            if (dist < min) {
                min = dist;
                lowest = n;
            }
        }
        return lowest;
    }

    /**
     * Metoda za racunanje tezine grane e, na osnovu kriterijuma. Dijkstra ovu tezinu koristi dalje za svoj algoritam.
     * @param e grana, na kojoj se racuna tezina
     * @param criteria kriterijum @see {@link Criteria}
     * @param startTime pocetno vrijeme
     * @return double vrijednost tezine grane za Dijkstru, na osnovu kriterijuma
     */
    private static double getEdgeWeight(Edge e, Criteria criteria, LocalDateTime startTime) {
        switch (criteria) {
            case CHEAPEST -> {
                Number num = (Number) e.getAttribute("minPrice");
                return num == null ? Double.POSITIVE_INFINITY : num.doubleValue();
            }
            case FASTEST ->  {
                // getNextDeparture in minutes
//                LocalDateTime currentTime = LocalDateTime.now();
                int hour = startTime.getHour();
                int minute = startTime.getMinute();
                int currentTimeInMinutes = (hour*60) + minute;

                System.out.println("startTime TIME "+startTime);
                System.out.println(hour+":"+minute);

// todo: set infinity
                double minimumDuration = 999999999;

                List<Departure> depList =  (List<Departure>) e.getAttribute("departures");
                for(Departure d: depList) {
                    int waitTimeInMinutes = 0;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
                    LocalTime departureTime = LocalTime.parse(d.getDepartureTime(),formatter);
                    int departureTimeInMinutes = departureTime.getHour() * 60 + departureTime.getMinute();
                    if (currentTimeInMinutes > departureTimeInMinutes){
// proso voz, sta ako je ponoc uskoro
                        // currentTime = 23:50, departureTime = 00:15 ------- za 25 minuta
                        // wait time = 15 - 23*60+50 + 1440
                        // 15 - 1430 + 1440

                        //waitTimeInMinutes = currentTimeInMinutes - departureTimeInMinutes + 1440;
                        // and in  RouteComparator class
                        waitTimeInMinutes = departureTimeInMinutes - currentTimeInMinutes + 1440;
                    } else {
                        // nije pros'o
                        // calculate wait time
                        waitTimeInMinutes = departureTimeInMinutes - currentTimeInMinutes;
                    }

                    double currentDepartureDuration = d.getDuration() + d.getMinTransferTime() + waitTimeInMinutes;
                    // find departure that has the best weight, set it to smallestWeight

                    if( currentDepartureDuration < minimumDuration) {
                        minimumDuration = currentDepartureDuration;
//                         e.setAttribute("chosenDepartureTime", d.getDepartureTime());
                    }
                }
                return minimumDuration;
            }
            case LEAST_TRANSFERS -> {
                // TODO: incorrect
                //Number num = (Number) e.getAttribute("departuresCount");
                return 1;
            }
            default -> {
                Number num = (Number) e.getAttribute("minDuration");
                return num == null ? Double.POSITIVE_INFINITY : num.doubleValue();
            }
        }
    }


}
