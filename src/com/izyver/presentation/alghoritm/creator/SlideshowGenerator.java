package com.izyver.presentation.alghoritm.creator;

import com.izyver.presentation.alghoritm.model.Image;
import com.izyver.presentation.alghoritm.model.SlideshowImage;
import com.izyver.presentation.alghoritm.utils.SlideshowCalculator;
import com.izyver.presentation.alghoritm.utils.SlideshowParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SlideshowGenerator {

    private Creator creator;

    public String createSlideshow(String pathToInput) {
        SlideshowParser parser = new SlideshowParser();
        File inputFile = new File(pathToInput);
        Image[] inputImages;
        try (
                FileReader reader = new FileReader(inputFile);
                BufferedReader bufferedReader = new BufferedReader(reader);
        ) {
            inputImages = parser.getInputImages(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
            inputImages = new Image[0];
        }

        SlideshowImage[] slideshowImages = creator.create(inputImages);

        SlideshowCalculator calculator = new SlideshowCalculator();
        int score = calculator.calculateScoreFromImages(inputImages, slideshowImages);

        return "";

    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

}

