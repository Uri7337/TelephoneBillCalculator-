package com.phonecompany.billing;

/**
 *
 * @author ourie
 */
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {
	
	private static class Call {
        String phoneNumber;
        LocalDateTime start;
        LocalDateTime end;

        Call(String phoneNumber, LocalDateTime start, LocalDateTime end) {
            this.phoneNumber = phoneNumber;
            this.start = start;
            this.end = end;
        }
    }

    @Override
    public BigDecimal calculate(String phoneLog) {
        Map<String, List<Call>> callsByNumber = new HashMap<>();
        String[] lines = phoneLog.split("\n");
		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        for (String line : lines) {
            String[] parts = line.split(",");
            String phoneNumber = parts[0];
            LocalDateTime start = LocalDateTime.parse(parts[1], formatter);
            LocalDateTime end = LocalDateTime.parse(parts[2], formatter);

            Call call = new Call(phoneNumber, start, end);
            callsByNumber.computeIfAbsent(phoneNumber, k -> new ArrayList<>()).add(call);
        }

        String mostFrequentNumber = findMostFrequentNumber(callsByNumber);
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Map.Entry<String, List<Call>> entry : callsByNumber.entrySet()) {
            if (!entry.getKey().equals(mostFrequentNumber)) {
                for (Call call : entry.getValue()) {
					
                    totalAmount = totalAmount.add(calculateCallCost(call));
                }
            }
        }

        return totalAmount;
    }

    private String findMostFrequentNumber(Map<String, List<Call>> callsByNumber) {
		return callsByNumber.entrySet().stream()
				.max(Comparator.comparingInt(e -> {
					int totalDuration = e.getValue().stream()
							.mapToInt(call -> (int) ChronoUnit.SECONDS.between(call.start, call.end))
							.sum();
					return totalDuration;
				}))
				.map(Map.Entry::getKey)
				.orElse("");
	}



    private BigDecimal calculateCallCost(Call call) {
        BigDecimal cost = BigDecimal.ZERO;
        LocalDateTime currentMinute = call.start;
        long totalMinutes = ChronoUnit.MINUTES.between(call.start, call.end) + 1;

        for (int minute = 0; minute < totalMinutes; minute++) {
            if (minute >= 5) {
                cost = cost.add(BigDecimal.valueOf(0.20));
            } else {
                if (currentMinute.getHour() >= 8 && currentMinute.getHour() < 16) {
                    cost = cost.add(BigDecimal.valueOf(1.00));
                } else {
                    cost = cost.add(BigDecimal.valueOf(0.50));
                }
            }
            currentMinute = currentMinute.plusMinutes(1);
        }

        return cost;
    }

    
}
