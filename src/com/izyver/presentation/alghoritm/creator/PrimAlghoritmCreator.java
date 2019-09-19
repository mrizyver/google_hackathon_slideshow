package com.izyver.presentation.alghoritm.creator;

import com.izyver.presentation.alghoritm.model.Image;
import com.izyver.presentation.alghoritm.model.SlideshowImage;
import com.izyver.presentation.alghoritm.utils.Metric;
import com.izyver.presentation.alghoritm.utils.SlideshowCalculator;

import java.util.*;

public class PrimAlghoritmCreator implements Creator {

    private static Metric metric = new Metric("graph_iter", 100);
    private static Metric mapMetric = new Metric("map_loop_iter");


    @Override
    public SlideshowImage[] create(Image[] inputImages) {
        TreeSet<Integer> used = new TreeSet<>();
        PriorityQueue[] graph = createGraph(inputImages);
        List<SlideshowImage> slideshowImages = new ArrayList<>(graph.length);
        PriorityQueue<NeighbouringVertex> queue = graph[0];
        graph_label:
        while (true) {
            NeighbouringVertex neighbour = queue.poll();
            if (neighbour == null) {
                for (PriorityQueue priorityQueue : graph) {
                    if (priorityQueue != null && !priorityQueue.isEmpty()) {
                        queue =  priorityQueue;
                        continue graph_label;
                    }
                }
                break;
            }

            if (used.contains(neighbour.vertex)) continue;
            used.add(neighbour.vertex);

            slideshowImages.add(new SlideshowImage(new int[]{neighbour.vertex}, null));
            queue = graph[neighbour.vertex];
        }

        SlideshowImage[] result = new SlideshowImage[slideshowImages.size()];
        slideshowImages.toArray(result);
        return result;
    }

    private PriorityQueue[] createGraph(Image[] images) {
        PriorityQueue[] graph = new PriorityQueue[images.length];
        SlideshowCalculator calculator = new SlideshowCalculator();
        HashMap<String, TreeSet<Integer>> mapByTags = createMapByTags(images);

        Set<Map.Entry<String, TreeSet<Integer>>> entries = mapByTags.entrySet();
        for (Map.Entry<String, TreeSet<Integer>> entry : entries) {
            mapMetric.start();
            TreeSet<Integer> indexes = entry.getValue();
            for (int index : indexes) {
                metric.start();
                if (graph[index] == null) {
                    graph[index] = new PriorityQueue<>(this::compare);
                }
                for (int neighIndex : indexes) {
                    if (index == neighIndex) continue;
                    int weight = calculator.scoreFromTags(images[index].tags, images[neighIndex].tags);
                    graph[index].add(new NeighbouringVertex(neighIndex, weight));
                }
                metric.end();
            }
            metric.reset();
            mapMetric.end();
        }
//        for (int i = 0; i < images.length; i++) {
//            metric.start();
//            TreeSet<String> tags = images[i].tags;
//            TreeSet<NeighbouringVertex> neighbours = new TreeSet<>(this::compare);
//            for (String tag : tags) {
//                TreeSet<Integer> integers = mapByTags.get(tag);
//                for (Integer integer : integers) {
//                    if (integer == i) continue;
//                    int weight = calculator.scoreFromTags(images[i].tags, images[integer].tags);
//                    neighbours.add(new NeighbouringVertex(integer, weight));
//                }
//            }
//            graph[i] = neighbours;
//            metric.end();
//        }
        return graph;
    }

    private void fillGraph(Image[] images, TreeSet[] graph) {
        SlideshowCalculator calculator = new SlideshowCalculator();
        for (int i = 0; i < images.length; i++) {
            TreeSet<NeighbouringVertex> neighbouringVertices = new TreeSet<>(this::compare);
            for (int j = 0; j < images.length; j++) {
                if (i == j) continue;
                int score = calculator.scoreFromTags(images[i].tags, images[j].tags);
                neighbouringVertices.add(new NeighbouringVertex(images[j].index, score));
            }
            if (i % 100 == 0)
                System.out.println("created graph " + i);
            graph[i] = neighbouringVertices;
        }
    }

    private HashMap<String, TreeSet<Integer>> createMapByTags(Image[] images) {
        HashMap<String, TreeSet<Integer>> map = new HashMap<>(500);

        for (Image image : images) {
            for (String tag : image.tags) {
                TreeSet<Integer> integers = map.get(tag);
                if (integers == null) {
                    TreeSet<Integer> newSet = new TreeSet<>();
                    newSet.add(image.index);
                    map.put(tag, newSet);
                } else {
                    integers.add(image.index);
                }
            }
        }
        return map;
    }

    private int compare(NeighbouringVertex o1, NeighbouringVertex o2) {
        return o2.weight - o1.weight;
    }

    static class NeighbouringVertex {
        int vertex;
        int weight;

        NeighbouringVertex(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }
    }
}
