package com.izyver.presentation.alghoritm.creator;

import com.izyver.presentation.alghoritm.exception.OrientationNotFount;
import com.izyver.presentation.alghoritm.model.Image;
import com.izyver.presentation.alghoritm.model.Orientation;
import com.izyver.presentation.alghoritm.model.SlideshowImage;
import com.izyver.presentation.alghoritm.utils.SlideshowCalculator;
import com.izyver.presentation.alghoritm.utils.SlideshowParser;
import com.sun.istack.internal.Nullable;

import java.util.*;

public class FirstCreator implements Creator {

    private SlideshowCalculator calculator = new SlideshowCalculator();
    private SlideshowParser parser = new SlideshowParser();

    @Override
    public SlideshowImage[] create(final Image[] inputImages) {
        TagTable tagTable = prepareImages(inputImages);
        for (Image inputImage : inputImages) {
            tagTable.put(inputImage);
        }

        List<SlideshowImage> slideshowImages = new ArrayList<>();

        SlideshowImage previousImage = createSlideshowImage(inputImages, tagTable, inputImages[0]);
        slideshowImages.add(previousImage);

        while (!tagTable.isEmpty()) {
            Image image = getNextImage(tagTable, previousImage.tags, inputImages);
            SlideshowImage slideshowImage = createSlideshowImage(inputImages, tagTable, image);

            previousImage = slideshowImage;
            slideshowImages.add(slideshowImage);
        }

        SlideshowImage[] result = new SlideshowImage[slideshowImages.size()];
        slideshowImages.toArray(result);
        return result;
    }

    private SlideshowImage createSlideshowImage(Image[] inputImages, TagTable tagTable, Image image) {
        if (image.orientation == Orientation.ORIENTATION_VERTICAL) {
            Image secondImage = getVerticalImage(tagTable, image.tags, inputImages);

            int[] indexes = new int[]{image.index, secondImage.index};
            String[] splitTags = parser.splitTags(image, secondImage);
            tagTable.remove(secondImage);
            tagTable.remove(image);
            return new SlideshowImage(indexes, splitTags);
        }

        if (image.orientation == Orientation.ORIENTATION_HORIZONTAL) {
            int[] indexes = {image.index};
            String[] tags = image.tags;
            tagTable.remove(image);
            return new SlideshowImage(indexes, tags);
        }

        return new SlideshowImage(new int[0], new String[0]);
    }

    private TagTable prepareImages(Image[] inputImages) {
        TagTable tagTable = new TagTable();

        return tagTable;
    }

    @Nullable
    private Image getNextImage(TagTable tagTable, String[] tags, Image[] inputImages) {
        int[] maxScore = new int[]{-1, -1};

        for (String tag : tags) {
            List<Integer> horizontalList = tagTable.findHorizontal(tag);
            if (horizontalList != null)
                checkMaxScore(tags, inputImages, maxScore, horizontalList);

            List<Integer> verticalList = tagTable.findVertical(tag);
            if (verticalList != null)
                checkMaxScore(tags, inputImages, maxScore, verticalList);
        }
        if (maxScore[1] == -1) {
            int randomIndex = tagTable.getRandomIndex();
            if (randomIndex == -1) return new Image(-1, 0);
            return inputImages[randomIndex];
        }
        return inputImages[maxScore[1]];
    }

    @Nullable
    private Image getVerticalImage(TagTable tagTable, String[] tags, Image[] inputImages) {
        int[] maxScore = new int[]{-1, -1};
        for (String tag : tags) {
            List<Integer> verticalList = tagTable.findVertical(tag);
            checkMaxScore(tags, inputImages, maxScore, verticalList);
        }
        return inputImages[maxScore[1]];
    }

    private void checkMaxScore(String[] tags, Image[] inputImages, int[] maxScore, List<Integer> list) {
        for (int foundImageIndex : list) {
            int score = calculator.scoreFromTags(inputImages[foundImageIndex].tags, tags);
            if (score > maxScore[0]) {
                maxScore[0] = score;
                maxScore[1] = foundImageIndex;
            }
        }
    }

    class TagTable {
        private final HashMap<String, List<Integer>> vertical = new HashMap<>();
        private final HashMap<String, List<Integer>> horizontal = new HashMap<>();

        List<Integer> findVertical(String tag) {
            return vertical.get(tag);
        }

        List<Integer> findHorizontal(String tag) {
            return horizontal.get(tag);
        }

        int getRandomIndex() {
            if (!vertical.isEmpty()) {
                Collection<List<Integer>> values = vertical.values();
                for (List<Integer> value : values) {
                    if (value.isEmpty()) continue;
                    return value.get(0);
                }
            } else if (!horizontal.isEmpty()) {
                Collection<List<Integer>> values = horizontal.values();
                for (List<Integer> value : values) {
                    if (value.isEmpty()) continue;
                    return value.get(0);
                }
            }
            return -1;
        }

        void put(Image image) {
            switch (image.orientation) {
                case Orientation.ORIENTATION_HORIZONTAL:
                    putInMap(horizontal, image);
                    return;
                case Orientation.ORIENTATION_VERTICAL:
                    putInMap(vertical, image);
                    return;
                default:
                    throw new OrientationNotFount(image.orientation);
            }
        }

        void remove(Image image) {
            switch (image.orientation) {
                case Orientation.ORIENTATION_HORIZONTAL:
                    removeFromMap(horizontal, image);
                    return;
                case Orientation.ORIENTATION_VERTICAL:
                    removeFromMap(vertical, image);
                    return;
                default:
                    throw new OrientationNotFount(image.orientation);
            }
        }

        boolean isEmpty() {
            return vertical.isEmpty() && horizontal.isEmpty();
        }

        private void putInMap(HashMap<String, List<Integer>> map, Image inputImage) {
            String[] tags = inputImage.tags;
            for (String tag : tags) {

                List<Integer> list = map.get(tag);
                int imageIndex = inputImage.index;
                if (list == null) {
                    LinkedList<Integer> value = new LinkedList<>();
                    value.add(imageIndex);
                    map.put(tag, value);
                } else {
                    list.add(imageIndex);
                }
            }
        }

        private void removeFromMap(HashMap<String, List<Integer>> map, Image image) {
            for (String tag : image.tags) {
                List<Integer> list = map.get(tag);
                if (list == null) continue;
                list.remove(new Integer(image.index));
                if (list.isEmpty()) {
                    map.remove(tag);
                }
            }
        }
    }
}
