package com.izyver.presentation.alghoritm;

import com.izyver.presentation.alghoritm.utils.SlideshowCalculator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            SlideshowCalculator.main(new String[]{
                    "/Users/izyver/Projects/PresentationsAlghoritm/qualification_round_2019.in/a_result.txt",
                    "/Users/izyver/Projects/PresentationsAlghoritm/qualification_round_2019.in/a_example.txt"});
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
