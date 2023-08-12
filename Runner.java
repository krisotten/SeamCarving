import java.util.*;
import java.io.*;

public class Runner {

    public static void main(String[] args) throws IOException{
        // TODO: write the runner so that it follows the same format as the directions
        Scanner input = new Scanner(System.in);
        System.out.print("Enter input image: ");
        String img = input.nextLine();

        SeamCarver seamCarver = new SeamCarver(new Picture(img));

        System.out.print("How many vertical seams to remove: ");
        int vertical = Integer.parseInt(input.nextLine());
        System.out.print("How many horizontal seams to remove: ");
        int horizontal = Integer.parseInt(input.nextLine());

        long startTime = System.nanoTime();
        for (int i = 0; i < vertical; i++) {
            seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        }
        for (int i = 0; i < horizontal; i++) {
            seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
        }
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;

        System.out.print("Enter output image: ");
        seamCarver.picture().save(input.nextLine());

        System.out.print("Image saved in " + totalTime + "ms...");

        input.close();
    }
}
