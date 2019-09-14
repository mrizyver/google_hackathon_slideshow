package com.izyver.presentation.alghoritm;

import com.izyver.presentation.alghoritm.creator.FirstCreator;
import com.izyver.presentation.alghoritm.creator.SlideshowGenerator;
import com.izyver.presentation.alghoritm.utils.SlideshowCalculator;

import java.io.IOException;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        SlideshowGenerator generator = new SlideshowGenerator();
        generator.setCreator(new FirstCreator());
        generator.createSlideshow("/Users/izyver/Projects/PresentationsAlghoritm/qualification_round_2019.in/c_memorable_moments.txt");

    }
}
