package com.example.projekat.algorithms;

import com.example.projekat.models.Criteria;
import com.example.projekat.models.Route;

import java.util.Comparator;
import java.util.List;

public class RouteComparator implements Comparator<List<Route>> {
    private Criteria criteria;

    public  RouteComparator(Criteria criteria){
        this.criteria = criteria;
    }

    @Override
    public int compare(List<Route> routeList1, List<Route> routeList2) {
        switch (criteria) {
            case CHEAPEST -> {
                // Criteria == cheapest
                double totalPrice1 = 0;
                for (Route r : routeList1) {
                    totalPrice1 += r.getPrice();
                }
                double totalPrice2 = 0;
                for (Route r : routeList2) {
                    totalPrice2 += r.getPrice();
                }


                if (totalPrice1 > totalPrice2) {
                    return 1;
                } else if (totalPrice2 > totalPrice1) {
                    return -1;
                } else {
                    return 0;
                }
            }
            case LEAST_TRANSFERS -> {
                if(routeList1.size() > routeList2.size()){
                    return 1;
                }
                else if(routeList1.size() < routeList2.size()){
                    return -1;
                }else {
                    return  0;
                }
            }
            case FASTEST -> {
                // TODO: implement this
                return 1;
            }
            default -> {
                return 0;
            }
        }
    }
}
