package com.izyver.presentation.alghoritm.model;

import java.util.TreeSet;

public class SlideshowImage {
    public final int[] indexes;
    public final TreeSet<String> tags;

    public SlideshowImage(int[] indexes, TreeSet<String> tags) {
        this.indexes = indexes;
        this.tags = tags;
    }
}
