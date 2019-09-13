package com.izyver.presentation.alghoritm.creator;

import com.izyver.presentation.alghoritm.model.Image;
import com.izyver.presentation.alghoritm.model.SlideshowImage;

public interface Creator {
    SlideshowImage[] create(Image[] inputImages);
}
