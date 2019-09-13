package com.izyver.presentation.alghoritm.utils;

import com.izyver.presentation.alghoritm.model.Image;
import com.izyver.presentation.alghoritm.model.SlideshowImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SlideshowCalculator {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) throw new IOException("You mast give two arguments int parser, " +
                "where 1 arguments - path to your output (result), second - input (source)");

        String pathToOutput = args[0];
        String pathToInput = args[1];

        SlideshowCalculator parser = new SlideshowCalculator();
        int score = parser.calculateScore(pathToOutput, pathToInput);
        System.out.println("Your score is " + score);
    }

    public int calculateScore(String pathToOutput, String pathToInput) throws IOException {
        File fileInput = new File(pathToInput);
        File fileOutput = new File(pathToOutput);

        try (
                FileReader inputReader = new FileReader(fileInput);
                FileReader outputReader = new FileReader(fileOutput);

                BufferedReader bufferedInputReader = new BufferedReader(inputReader);
                BufferedReader bufferedOutputReader = new BufferedReader(outputReader);
        ) {
            SlideshowParser parser = new SlideshowParser();
            Image[] inputImages = parser.getInputImages(bufferedInputReader);
            SlideshowImage[] outputImages = parser.getOutputImages(bufferedOutputReader);

            int score = 0;
            String[] previousTags = null;
            for (SlideshowImage outputImage : outputImages) {
                int[] imageIndexes = outputImage.indexes;
                Image[] imagesOnTheScreen = new Image[imageIndexes.length];
                for (int j = 0; j < imageIndexes.length; j++) {
                    imagesOnTheScreen[j] = inputImages[imageIndexes[j]];
                }
                String[] tags = splitTags(imagesOnTheScreen);
                if (previousTags != null){
                    score += scoreFromTags(previousTags, tags);
                }
                previousTags = tags;
            }
            return score;
        }
    }



    private String[] splitTags(Image[] images) {
        Set<String> tags = new HashSet<>();

        for (Image image : images) {
            tags.addAll(Arrays.asList(image.tags));
        }

        String[] strTags = new String[tags.size()];
        tags.toArray(strTags);
        return strTags;

    }

    private int scoreFromTags(String[] previousTags, String[] currentTags) {
        int previousUniqueTags = previousTags.length;
        int currentUniqueTags = currentTags.length;
        int commonTags = 0;

        for (String previousTag : previousTags) {
            for (String currentTag : currentTags) {
                if (currentTag.equalsIgnoreCase(previousTag)) {
                    if (previousUniqueTags == 0 || currentUniqueTags == 0)
                        return 0;
                    previousUniqueTags--;
                    currentUniqueTags--;
                    commonTags++;
                }
            }
        }

        return min(previousUniqueTags, currentUniqueTags, commonTags);
    }

    private int min(int var1, int var2, int var3) {
        int temp = Math.min(var1, var2);
        return Math.min(temp, var3);
    }
}
