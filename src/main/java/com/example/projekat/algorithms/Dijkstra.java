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

public class Dijkstra {

    public static List<Node> dijkstra(Graph graph, Node source, Node target, Criteria criteria) {
//        // inicijalizacija atributa za sve čvorove
//        for (Node n : graph.getNodeSet()) {
//            n.setAttribute("distance", Double.POSITIVE_INFINITY);
//            n.setAttribute("previous", null);
//        }

        source.setAttribute("distance", 0.0);

        Set<Node> settled = new HashSet<>();
        Set<Node> unsettled = new HashSet<>();
        unsettled.add(source);

        while (!unsettled.isEmpty()) {
            Node current = getLowestDistanceNode(unsettled);
            if (current == null) break;
            unsettled.remove(current);

            if (current.equals(target) || current.getId().equals(target.getId())) {
                break;
            }

            // iteriraj kroz sve izlazne grane (usmjeren graf)
            for (Edge e : current.edges().toList()) {
                Node neighbor = e.getTargetNode();

                // Why?
                if (settled.contains(neighbor)) continue;

                double edgeWeight = getEdgeWeight(e, criteria);

                Number currDistN = (Number) current.getAttribute("distance");
                double currentDistance = currDistN == null ? Double.POSITIVE_INFINITY : currDistN.doubleValue();

                Number neighDistN = (Number) neighbor.getAttribute("distance");
                double neighborDistance = neighDistN == null ? Double.POSITIVE_INFINITY : neighDistN.doubleValue();

                double newDistance = currentDistance + edgeWeight;

                if (newDistance < neighborDistance) {
                    neighbor.setAttribute("distance", newDistance);
                    neighbor.setAttribute("previous", current);
                    unsettled.add(neighbor);
                }
            }

            settled.add(current);
        }

        return reconstructPath(target);
    }

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

    private static Node getLowestDistanceNode(Set<Node> nodes) {
        Node lowest = null;
        double min = Double.POSITIVE_INFINITY;
        for (Node n : nodes) {
            Number d = (Number) n.getAttribute("distance");
            double dist = d == null ? Double.POSITIVE_INFINITY : d.doubleValue();
            if (dist < min) {
                min = dist;
                lowest = n;
            }
        }
        return lowest;
    }

    private static double getEdgeWeight(Edge e, Criteria criteria) {
        switch (criteria) {
            case CHEAPEST -> {
                Number num = (Number) e.getAttribute("minPrice");
                return num == null ? Double.POSITIVE_INFINITY : num.doubleValue();
            }
            case FASTEST ->  {
                // getNextDeparture in minutes
                LocalDateTime currentTime = LocalDateTime.now();
                int hour = currentTime.getHour();
                int minute = currentTime.getMinute();
                int currentTimeInMinutes = (hour*60) + minute;

                System.out.println("CURRENT TIME "+currentTime);
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

                        waitTimeInMinutes = currentTimeInMinutes - departureTimeInMinutes + 1440;
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
                return  minimumDuration;
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
