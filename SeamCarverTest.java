import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SeamCarverTest {
    // TODO: Provide accuracy tests on the methods marked with TODO && provide your own images to SeamCarve! (original + seamcarved)
    @Test
    public void balloon() {
        Picture balloon = new Picture("hotAirBalloon.jpeg");
        SeamCarver sc = new SeamCarver(balloon);

        for (int i = 0; i < 200; i++) {
            sc.removeVerticalSeam(sc.findVerticalSeam());
        }

        for (int i = 0; i < 200; i++) {
            sc.removeHorizontalSeam(sc.findHorizontalSeam());
        }

        sc.picture().save("hotAirBalloon-cut.jpg");
        System.out.println("done");
    }

    @Test
    public void balloon2() {
        Picture balloon = new Picture("balloon.jpeg");
        SeamCarver sc = new SeamCarver(balloon);

        for (int i = 0; i < 500; i++) {
            sc.removeVerticalSeam(sc.findVerticalSeam());
        }

        for (int i = 0; i < 200; i++) {
            sc.removeHorizontalSeam(sc.findHorizontalSeam());
        }

        sc.picture().save("cut-balloon.jpg");
        System.out.println("done");
    }

    @Test
    public void pixel1Test() {
        Picture pixel = new Picture("pixels-2.png");
        SeamCarver sc = new SeamCarver(pixel);
        int[] seam = sc.findVerticalSeam();


        sc.removeVerticalSeam(sc.findVerticalSeam());
        sc.picture().save("cut-pixels-2.png");
        System.out.println("done");
    }

    @Test
    public void pixel2Test() {
        Picture pixel = new Picture("pixil-frame-0.png");
        SeamCarver sc = new SeamCarver(pixel);
        int[] seam = sc.findVerticalSeam();


        sc.removeVerticalSeam(sc.findVerticalSeam());
        sc.picture().save("cut-pixil-frame-0.png");
        System.out.println("done");
    }
}
