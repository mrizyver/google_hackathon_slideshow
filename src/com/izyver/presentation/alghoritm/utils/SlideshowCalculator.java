package com.izyver.presentation.alghoritm.utils;

import com.izyver.presentation.alghoritm.model.Image;
import com.izyver.presentation.alghoritm.model.SlideshowImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

public class SlideshowCalculator {

    private SlideshowParser parser = new SlideshowParser();

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
            Image[] inputImages = parser.getInputImages(bufferedInputReader);
            SlideshowImage[] outputImages = parser.getOutputImages(bufferedOutputReader);

            return calculateScoreFromImages(inputImages, outputImages);
        }
    }

    public int calculateScoreFromImages(Image[] inputImages, SlideshowImage[] outputImages) {
        int score = 0;
        TreeSet<String> previousTags = null;
        for (SlideshowImage outputImage : outputImages) {
            int[] imageIndexes = outputImage.indexes;
            Image[] imagesOnTheScreen = new Image[imageIndexes.length];
            for (int j = 0; j < imageIndexes.length; j++) {
                int imageIndex = imageIndexes[j];
                imagesOnTheScreen[j] = inputImages[imageIndex];
            }
            TreeSet<String> tags = parser.splitTags(imagesOnTheScreen);
            if (previousTags != null) {
                score += scoreFromTags(previousTags, tags);
            }
            previousTags = tags;
        }
        return score;
    }


    public int scoreFromTags(TreeSet<String> previousTags, TreeSet<String> currentTags) {
        int previousUniqueTags = previousTags.size();
        int currentUniqueTags = currentTags.size();
        int commonTags = 0;

        if (previousUniqueTags < currentUniqueTags) {
            for (String previousTag : previousTags) {
                if (currentTags.contains(previousTag)) {
                    previousUniqueTags--;
                    currentUniqueTags--;
                    commonTags++;
                }
            }
        } else {
            for (String currentTag : currentTags) {
                if (previousTags.contains(currentTag)) {
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
