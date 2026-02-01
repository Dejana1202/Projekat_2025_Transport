package com.example.projekat.algorithms;

import com.example.projekat.controllers.CountryController;
import com.example.projekat.models.*;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

/**
 * Implementacija Yen algoritma za pronalazenje K najboljih ruta izmedju source i target cvorova, u ovom slucaju K=5.
 * Koristi prethodno implementiran Dijkstrin algoritam da dobije optimalan put, u zavisnosti od kriterijuma.
 * Nako toga generise alternativne (spur) rute tako sto iskljucuje grane i cvorove prethodno pronadjenih puteva.
 * @return Lista 5 najboljih puteva, gdje je svaki put predstavljen u obliku liste ruta izmedju ishodisnog i odredisnog cvora.
 */
public class Yen {
    /**
     * Prvu rutu dobijamo pozivom Dijkstra algoritma.
     * Koristimo PriorityQueue za prikupljanje liste ruta radi sortiranja potencijalnih alternativnih ruta.
     * 
     * @param graph mapa gradova u obliku grafa
     * @param source izvoriste
     * @param target odrediste
     * @param criteria kriterijum pretrage @see {@link Criteria}
     * @return lista 5 najboljih puteva
     */
    public static List<List<Route>> yen(Graph graph, Node source, Node target, Criteria criteria){
        final int bestRoutesNum = 5;

        List<Route> A0 = new ArrayList<>();
        A0 = CountryController.buildRoutesFromPath(graph, Dijkstra.dijkstra(graph, source, target, criteria), criteria);

        List<List<Route>> aList = new ArrayList<>();
        aList.add(A0);

        PriorityQueue<List<Route>> queue = new PriorityQueue<>(new RouteComparator(criteria));

        /**
         * Pronalazak k najboljih ruta, zapocinje kod prve i trazi se do najbolje prema Dijkstri,
         */
        for(int k = 1; k < bestRoutesNum; k++) {
            /**
             * Uzimanje alternativnih ruta, jednu po jednu za svaku zadnju najbolju rutu.
             * Dijkstra vraca jednu
             */
            for(int i = 0; i < aList.get(k-1).size() - 1; i++){

                // Add missing edges
                /**
                 * Dodavanje cvorova, koji nedostaju
                 */
                for(Edge e: graph.edges().toList()){
                    if(e.getAttribute("disabled") != null) {
                        e.removeAttribute("disabled");
                    }
//                    if(e.getAttribute("disabled") != null && (boolean) e.getAttribute("disabled")) {
//                        e.removeAttribute("disabled");
//                    }
                }

                for (int iterator = 0; iterator < graph.getNodeCount(); iterator++) {
                    Node n = graph.getNode(iterator);
                    if(n == null) continue;
                    n.setAttribute("distance", Double.POSITIVE_INFINITY);
                    n.setAttribute("previous", null);
                    if (n.hasAttribute("disabled")){
                        n.removeAttribute("disabled");
                    }
                }

                Route spurRoute = aList.get(k-1).get(i);
                List<Route> rootPath = aList.get(k - 1).subList(0, i);

                for(List<Route> p: aList){
                    if(i > p.size()) continue;
                    List<Route> subPath = p.subList(0, i);
                    /**
                     * Uklanjanje ivica iz originalne putanje
                     */
                    if(rootPath.equals(subPath)) {
                        int finalI = i;
                        List<Edge> edgesFromSource =  graph.getNode(p.get(i).getSource()).edges().toList();
                        for(Edge e : edgesFromSource){
                            if(e.getTargetNode().equals(getNodeFromString(graph, p.get(finalI).getDestination()))){
                                e.setAttribute("disabled", true);
                            }
                        }
                    }
                }

                for(Route routeNode: rootPath){
                    /**
                     * Uklanjanje cvorova iz grafa
                     */
                    if(routeNode.equals(spurRoute) == false){
                        graph.getNode(routeNode.getSource()).setAttribute("disabled", true);
                    }
                }

                /**
                 * Potraznja nove najbolje rute pomocu Dijkstra algoritma
                 */
                List<Route> spurPath = CountryController.buildRoutesFromPath(graph, Dijkstra.dijkstra(graph, getNodeFromString(graph, spurRoute.getSource()), target, criteria), criteria);

                /**
                 * Formiranje total path - liste svih ruta
                 */
                List<Route> totalPath = new ArrayList<>();
                totalPath.addAll(rootPath);
                totalPath.addAll(spurPath);


                /**
                 * Provjeravamo da li cijela ruta yaista ide od source-a do targeta
                 */
                if(totalPath.size() > 0
                        && queue.contains(totalPath) == false
                        && totalPath.get(0).getSource().equals(source.getId())
                        && totalPath.get(totalPath.size() - 1).getDestination().equals(target.getId())){
                    queue.add(totalPath);
                }

            }
            if(queue.isEmpty()){

                break;
            }
            aList.add(queue.poll());
        }

        /**
         * Resetovanje cvorova
         */
        for(Node n: graph.nodes().toList()){
            if(n.getAttribute("disabled") != null) {
                n.removeAttribute("disabled");
            }
        }
        /**
         * Resetovanje ivica na pocetku vrijednost
         */
        for(Edge e: graph.edges().toList()){
            if(e.getAttribute("disabled") != null) {
                e.removeAttribute("disabled");
            }
        }

        return aList;
    }

    private static Node getNodeFromString(Graph graph, String nodeName){
        return graph.getNode(nodeName);
    }
}
