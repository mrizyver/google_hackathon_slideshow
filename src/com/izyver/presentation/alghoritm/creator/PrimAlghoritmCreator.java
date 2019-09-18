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

        TreeSet[] treeSet = createGraph(inputImages);

        List<SlideshowImage> slideshowImages = new ArrayList<>(treeSet.length);
        TreeSet set = treeSet[0];

        while (true){
            NeighbouringVertex neighbour = (NeighbouringVertex) set.pollFirst();
            if (neighbour == null){
                System.out.println();
                break;
            }
            slideshowImages.add(new SlideshowImage(new int[]{neighbour.vertex}, null));
            set = treeSet[neighbour.vertex];
        }


        return new SlideshowImage[0];
    }

    private TreeSet[] createGraph(Image[] images) {
        TreeSet[] graph = new TreeSet[images.length];
        SlideshowCalculator calculator = new SlideshowCalculator();
        HashMap<String, TreeSet<Integer>> mapByTags = createMapByTags(images);

        Set<Map.Entry<String, TreeSet<Integer>>> entries = mapByTags.entrySet();
        for (Map.Entry<String, TreeSet<Integer>> entry : entries) {
            mapMetric.start();
            TreeSet<Integer> indexes = entry.getValue();
            HashMap<Integer, Integer> buffer = new HashMap<>(indexes.size());
            for (int index : indexes) {
                metric.start();
                if (graph[index] == null) {
                    graph[index] = new TreeSet<>(this::compare);
                }
                for (int neighIndex : indexes) {
                    if (index == neighIndex) continue;
                    Integer weight = buffer.get(neighIndex);
                    if (weight == null){
                        weight = calculator.scoreFromTags(images[index].tags, images[neighIndex].tags);
                        buffer.put(index, weight);
                    }
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
