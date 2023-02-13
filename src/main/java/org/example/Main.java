package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        List<String> requests = Arrays.asList(
                "1 172.253.115.138 50000",
                "2 172.253.115.139 50100",
                "3 172.253.115.138 50210",
                "4 172.253.115.139 50300",
                "5 172.253.115.138 51000",
                "6 172.253.115.139 60300"
        );
        int limit_per_second = 1;

        List<Integer> rejectedRequests = getRejectedRequests(requests, limit_per_second);
        System.out.println(rejectedRequests);
    }

    public static List<Integer> getRejectedRequests(List<String> requests, int limit_per_second) {
        Map<String, Queue<Long>> map = new HashMap<>();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            System.out.println(requests.get(i));
            String[] parts = requests.get(i).split(" ");
            int id = Integer.parseInt(parts[0]);
            String ip = parts[1];
            long time = Long.parseLong(parts[2]);
            if (!map.containsKey(ip)) {
                map.put(ip, new PriorityQueue<>());
            }
            Queue<Long> queue = map.get(ip);
            while (!queue.isEmpty() && time - queue.peek() >= 1000) {
                queue.poll();
            }
            if (queue.size() >= limit_per_second) {
                result.add(id);
                continue;
            }
            queue.offer(time);
        }
        return result;
    }
}