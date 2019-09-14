package com.izyver.presentation.alghoritm.model;

import java.util.TreeSet;

public class Image {
    public final int index;
    public final int orientation;
    public final TreeSet<String> tags;

    public Image(int index, int orientation, TreeSet<String> tags) {
        this.index = index;
        this.orientation = orientation;
        this.tags = tags;
    }
}
