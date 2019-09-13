package com.izyver.presentation.alghoritm.exception;

public class OrientationNotFount extends  RuntimeException{

    public OrientationNotFount(int value) {
        super("Orientation value '" + value + "' not found!");
    }
}
