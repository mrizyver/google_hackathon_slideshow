package com.izyver.presentation.alghoritm.model;

public class Image {
    public final int index;
    public final int orientation;
    public final String[] tags;

    public Image(int index, int orientation, String... tags) {
        this.index = index;
        this.orientation = orientation;
        this.tags = tags;
    }
}
