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
        TagTable tagTable = new TagTable();
        for (Image inputImage : inputImages) {
            tagTable.put(inputImage);
        }

        List<SlideshowImage> slideshowImages = new ArrayList<>();

        SlideshowImage previousImage = createSlideshowImage(inputImages, tagTable, inputImages[0]);
        slideshowImages.add(previousImage);

        while (!tagTable.isEmpty()) {
            long d = System.currentTimeMillis();
            Image image = getNextImage(tagTable, previousImage.tags, inputImages);
            System.out.println(System.currentTimeMillis() - d);

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
            TreeSet<String> splitTags = parser.splitTags(image, secondImage);
            tagTable.remove(secondImage);
            tagTable.remove(image);
            return new SlideshowImage(indexes, splitTags);
        }

        if (image.orientation == Orientation.ORIENTATION_HORIZONTAL) {
            int[] indexes = {image.index};
            TreeSet<String> tags = image.tags;
            tagTable.remove(image);
            return new SlideshowImage(indexes, tags);
        }

        return new SlideshowImage(new int[0], new TreeSet<>());
    }


    @Nullable
    private Image getNextImage(TagTable tagTable, TreeSet<String> tags, Image[] inputImages) {
        int[] maxScore = new int[]{-1, -1};

        for (String tag : tags) {
            TreeSet<Integer> horizontalSet = tagTable.findHorizontal(tag);
            if (horizontalSet != null)
                checkMaxScore(tags, inputImages, maxScore, horizontalSet);

            TreeSet<Integer> verticalSet = tagTable.findVertical(tag);
            if (verticalSet != null)
                checkMaxScore(tags, inputImages, maxScore, verticalSet);
        }
        if (maxScore[1] == -1) {
            int randomIndex = tagTable.getRandomIndex();
            if (randomIndex == -1) return new Image(-1, 0, new TreeSet<>());
            return inputImages[randomIndex];
        }
        return inputImages[maxScore[1]];
    }

    @Nullable
    private Image getVerticalImage(TagTable tagTable, TreeSet<String> tags, Image[] inputImages) {
        int[] maxScore = new int[]{-1, -1};
        for (String tag : tags) {
            TreeSet<Integer> verticalSet = tagTable.findVertical(tag);
            checkMaxScore(tags, inputImages, maxScore, verticalSet);
        }
        return inputImages[maxScore[1]];
    }

    private void checkMaxScore(TreeSet<String> tags, Image[] inputImages, int[] maxScore, TreeSet<Integer> set) {
        for (int foundImageIndex : set) {
            int score = calculator.scoreFromTags(inputImages[foundImageIndex].tags, tags);
            if (score > maxScore[0]) {
                maxScore[0] = score;
                maxScore[1] = foundImageIndex;
            }
        }
    }

    class TagTable {
        private final HashMap<String, TreeSet<Integer>> vertical = new HashMap<>();
        private final HashMap<String, TreeSet<Integer>> horizontal = new HashMap<>();

        TreeSet<Integer> findVertical(String tag) {
            return vertical.get(tag);
        }

        TreeSet<Integer> findHorizontal(String tag) {
            return horizontal.get(tag);
        }

        int getRandomIndex() {
            if (!vertical.isEmpty()) {
                Collection<TreeSet<Integer>> values = vertical.values();
                for (TreeSet<Integer> value : values) {
                    if (value.isEmpty()) continue;
                    return value.first();
                }
            } else if (!horizontal.isEmpty()) {
                Collection<TreeSet<Integer>> values = horizontal.values();
                for (TreeSet<Integer> value : values) {
                    if (value.isEmpty()) continue;
                    return value.first();
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

        private void putInMap(HashMap<String, TreeSet<Integer>> map, Image inputImage) {
            TreeSet<String> tags = inputImage.tags;
            for (String tag : tags) {

                TreeSet<Integer> set = map.get(tag);
                int imageIndex = inputImage.index;
                if (set == null) {
                    TreeSet<Integer> value = new TreeSet<>();
                    value.add(imageIndex);
                    map.put(tag, value);
                } else {
                    set.add(imageIndex);
                }
            }
        }

        private void removeFromMap(HashMap<String, TreeSet<Integer>> map, Image image) {
            for (String tag : image.tags) {
                TreeSet<Integer> set = map.get(tag);
                if (set == null) continue;
                set.remove(image.index);
                if (set.isEmpty()) {
                    map.remove(tag);
                }
            }
        }
    }
}
