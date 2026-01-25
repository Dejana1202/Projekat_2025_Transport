package com.example.projekat.algorithms;

import com.example.projekat.models.Route;

import java.util.Comparator;
import java.util.List;

public class RouteComparator implements Comparator<List<Route>> {
    @Override
    public int compare(List<Route> routeList1, List<Route> routeList2) {
        // Criteria == cheapest
        double totalPrice1 = 0;
        for (Route r: routeList1){
            totalPrice1 += r.getPrice();
        }
        double totalPrice2 = 0;
        for (Route r: routeList2){
            totalPrice2 += r.getPrice();
        }


        if(totalPrice1 > totalPrice2){
            return 1;
        }else if (totalPrice2 > totalPrice1){
            return -1;
        }
        else {
            return 0;
        }
    }
}
