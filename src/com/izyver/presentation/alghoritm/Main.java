package com.izyver.presentation.alghoritm;

import com.izyver.presentation.alghoritm.creator.FirstCreator;
import com.izyver.presentation.alghoritm.creator.SecondCreator;
import com.izyver.presentation.alghoritm.creator.SlideshowGenerator;
import com.izyver.presentation.alghoritm.utils.SlideshowCalculator;

import java.io.IOException;
import java.util.Date;

public class Main {
    static String moments = "/Users/izyver/Projects/PresentationsAlghoritm/qualification_round_2019.in/c_memorable_moments.txt";
    static String selfie = "/Users/izyver/Projects/PresentationsAlghoritm/qualification_round_2019.in/e_shiny_selfies.txt";
    static String pet = "/Users/izyver/Projects/PresentationsAlghoritm/qualification_round_2019.in/d_pet_pictures.txt";

    public static void main(String[] args) {
        SlideshowGenerator generator = new SlideshowGenerator();
        generator.setCreator(new SecondCreator());
        generator.createSlideshow(pet);

    }
}
