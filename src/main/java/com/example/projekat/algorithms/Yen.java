package com.example.projekat.algorithms;

import com.example.projekat.controllers.CountryController;
import com.example.projekat.models.*;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

public class Yen {
    public static List<List<Route>> yen(Graph graph, Node source, Node target, Criteria criteria){
        final int bestRoutesNum = 5;

        List<Route> A0 = new ArrayList<>();
        A0 = CountryController.buildRoutesFromPath(graph, Dijkstra.dijkstra(graph, source, target, criteria), criteria);

        List<List<Route>> aList = new ArrayList<>();
        aList.add(A0);

        PriorityQueue<List<Route>> queue = new PriorityQueue<>(new RouteComparator(criteria));

        // Find k best routes. K starts at 1 since the first route is provided by dijkstra
        for(int k = 1; k < bestRoutesNum; k++) {
            // Get spur nodes, one by one, for every "last best route" - initially the one returned by dijkstra
            for(int i = 0; i < aList.get(k-1).size() - 1; i++){

                // Add missing edges
                for(Edge e: graph.edges().toList()){
//                    if(e.getAttribute("disabled") != null && (boolean) e.getAttribute("disabled").equals(true)) {
//                        e.removeAttribute("disabled");
//                    }
                    if(e.getAttribute("disabled") != null && (boolean) e.getAttribute("disabled")) {
                        e.removeAttribute("disabled");
                    }
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
                    // Remove edges from original path
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
                    // remove nodes from graph and remember them
                    if(routeNode.equals(spurRoute) == false){
                        graph.getNode(routeNode.getSource()).setAttribute("disabled", true);

                    }
                }

                // Now get the new best route using Dijkstra
                List<Route> spurPath = CountryController.buildRoutesFromPath(graph, Dijkstra.dijkstra(graph, getNodeFromString(graph, spurRoute.getSource()), target, criteria), criteria);

                // Construct totalPath
                List<Route> totalPath = new ArrayList<>();
                totalPath.addAll(rootPath);
                totalPath.addAll(spurPath);

                // Ensure that dead-end is not added to queue, so ensure that the whole route goes from target to source
                if(totalPath.size() > 0
                        && queue.contains(totalPath) == false
                        && totalPath.get(0).getSource().equals(source.getId())
                        && totalPath.get(totalPath.size() - 1).getDestination().equals(target.getId())){
                    queue.add(totalPath);
                }

            }
            if(queue.isEmpty()){
                // Dead end
                break;
            }
            aList.add(queue.poll());
        }

        return aList;
    }

    private static Node getNodeFromString(Graph graph, String nodeName){
        return graph.getNode(nodeName);
    }
}
