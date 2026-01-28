package com.example.projekat.algorithms;

import com.example.projekat.models.Criteria;
import com.example.projekat.models.Route;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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

                int totalTripDuration1 = calculateTotalTripDuration(routeList1);
                int totalTripDuration2 = calculateTotalTripDuration(routeList2);

                if(totalTripDuration1 > totalTripDuration2) {
                    return 1;
                } else if (totalTripDuration1 < totalTripDuration2) {
                     return -1;
                } else {
                    return 0;
                }
            }
            default -> {
                return 0;
            }
        }
    }

    public static int calculateTotalTripDuration(List<Route> routeList){
        int totalTripDuration = 0;
        int waitTimeInMinutes = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

        LocalDateTime currentTime = LocalDateTime.now();

        for(Route r : routeList) {
            LocalTime departureTime = LocalTime.parse(r.getDepartureTime(),formatter);
            int departureTimeInMinutes = departureTime.getHour() * 60 + departureTime.getMinute();

            int hour1 = currentTime.getHour();
            int minute1 = currentTime.getMinute();
            int currentTimeInMinutes = (hour1*60) + minute1;

            if (currentTimeInMinutes > departureTimeInMinutes){
// proso voz, sta ako je ponoc uskoro
                // currentTime = 23:50, departureTime = 00:15 ------- za 25 minuta
                // wait time = 15 - 23*60+50 + 1440
                // 15 - 1430 + 1440

                // 1440 minutes in a day
//                waitTimeInMinutes = currentTimeInMinutes - departureTimeInMinutes + 1440;
                waitTimeInMinutes = departureTimeInMinutes - currentTimeInMinutes + 1440;

            } else {
                // nije pros'o
                // calculate wait time
                waitTimeInMinutes = departureTimeInMinutes - currentTimeInMinutes;
            }
//            totalTripDuration = waitTimeInMinutes + r.getDurationMinutes() + r.getMinTransferTime();
            totalTripDuration += waitTimeInMinutes + r.getDurationMinutes() + r.getMinTransferTime();

//            currentTime = currentTime.plusMinutes(r.getDurationMinutes() + r.getMinTransferTime());
            currentTime = currentTime.plusMinutes(r.getDurationMinutes() + r.getMinTransferTime() + waitTimeInMinutes);

        }
    return  totalTripDuration;
    }
}
