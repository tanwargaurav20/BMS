package com.sapient.bms;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MovieBookingSystem {
    
    private final int maxSeats;
    private final Map<Integer, Seat> seatMap;
    private final ScheduledExecutorService executorService;
    private final Lock reservationLock;

    public MovieBookingSystem(int maxSeats) {
        this.maxSeats = maxSeats;
        this.seatMap = new ConcurrentHashMap<>();
        this.executorService = Executors.newScheduledThreadPool(10);
        this.reservationLock = new ReentrantLock();
        
        for (int i = 1; i <= maxSeats; i++) {
            seatMap.put(i, new Seat());
        }
    }

    public List<Integer> getAvailableSeats() {
        List<Integer> availableSeats = new ArrayList<>();
        for (Map.Entry<Integer, Seat> entry : seatMap.entrySet()) {
            if (entry.getValue().isAvailable()) {
                availableSeats.add(entry.getKey());
            }
        }
        return availableSeats;
    }

    public boolean reserveSeat(int seatNumber, String customerName, int timeoutMinutes) {
        reservationLock.lock();
        try {
            Seat seat = seatMap.get(seatNumber);
            if (seat == null || !seat.reserve(customerName)) {
                return false;
            }
            
            ScheduledFuture<?> timeoutTask = executorService.schedule(() -> {
                seat.cancelReservation(customerName);
            }, timeoutMinutes, TimeUnit.MINUTES);
            seat.setReservationTimeout(timeoutTask);
            return true;
        } finally {
            reservationLock.unlock();
        }
    }

    public boolean confirmReservation(int seatNumber, String customerName) {
        reservationLock.lock();
        try {
            Seat seat = seatMap.get(seatNumber);
            if (seat == null || !seat.confirmReservation(customerName)) {
                return false;
            }
            
            seat.cancelReservationTimeout();
            return true;
        } finally {
            reservationLock.unlock();
        }
    }

    public boolean cancelReservation(int seatNumber, String customerName) {
        reservationLock.lock();
        try {
            Seat seat = seatMap.get(seatNumber);
            if (seat == null || !seat.cancelReservation(customerName)) {
                return false;
            }
            
            seat.cancelReservationTimeout();
            return true;
        } finally {
            reservationLock.unlock();
        }
    }
    
    private static class Seat {
        private String customerName;
        private boolean available;
        private ScheduledFuture<?> reservationTimeout;

        public Seat() {
            this.customerName = null;
            this.available = true;
            this.reservationTimeout = null;
        }

        public synchronized boolean isAvailable() {
            return available;
        }

        public synchronized boolean reserve(String customerName) {
            if (!available) {
                return false;
            }
            this.customerName = customerName;
            this.available = false;
            return true;
        }

        public synchronized boolean confirmReservation(String customerName) {
            if (!customerName.equals(this.customerName)) {
                return false;
            }
            this.customerName = null;
            this.available = true;
            return true;
        }

        public synchronized boolean cancelReservation(String customerName) {
            if (!customerName.equals(this.customerName)) {
                return false;
            }
            this.customerName = null;
            this.available = true;
            return true;
        }

        public synchronized void setReservationTimeout(ScheduledFuture<?> timeoutTask) {
            this.reservationTimeout = timeoutTask;
        }

        public synchronized void cancelReservationTimeout() {
            if (reservationTimeout != null) {
                reservationTimeout.cancel(false);
                reservationTimeout = null;
            }
        }
    }
}