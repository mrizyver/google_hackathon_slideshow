package com.izyver.presentation.alghoritm.utils;

import com.izyver.presentation.alghoritm.model.Image;
import com.izyver.presentation.alghoritm.model.Orientation;
import com.izyver.presentation.alghoritm.model.SlideshowImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class SlideshowParser {

    public SlideshowImage[] getOutputImages(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        int size = Integer.parseInt(line);
        SlideshowImage[] slideshowImages = new SlideshowImage[size];

        for (int i = 0; i < size; i++) {
            line = bufferedReader.readLine();

            String[] strImageIndexes = line.split(" ");
            int[] imageIndexes = new int[strImageIndexes.length];
            for (int j = 0; j < strImageIndexes.length; j++) {
                imageIndexes[j] = Integer.parseInt(strImageIndexes[j]);
            }
            slideshowImages[i] = new SlideshowImage(imageIndexes, null);
        }
        return slideshowImages;
    }

    public Image[] getInputImages(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        int size = Integer.parseInt(line);
        Image[] images = new Image[size];

        for (int i = 0; i < size; i++) {
            /* "H 9 tpvh8 t7fgq t6p61 t5x7p twzsw tkw11 tlhlt tlvcq tns01" */
            line = bufferedReader.readLine();
            int orientation = getOrientation(line.charAt(0));
            String[] words = line.split(" ");
            int numberOfTags = Integer.parseInt(words[1]);
            TreeSet<String> tags = new TreeSet<>();

            for (int j = 0; j < numberOfTags; j++) {
                tags.add(words[j + 2]);
            }

            Image image = new Image(i, orientation, tags);
            images[i] = image;
        }
        return images;
    }

    public int getOrientation(char orientationChar) {
        switch (orientationChar) {
            case 'V':
                return Orientation.ORIENTATION_VERTICAL;
            case 'H':
                return Orientation.ORIENTATION_HORIZONTAL;
            default:
                throw new IllegalArgumentException("Line must contains 'V' or 'H' character at 0 index");
        }
    }

    public TreeSet<String> splitTags(Image... images) {
        TreeSet<String> tags = new TreeSet<>();
        for (Image image : images) {
            tags.addAll(image.tags);
        }
        return tags;
    }
}
