/* Runner class to test the image resizing algorithm in SeamCarver.java */

import java.util.*;
import java.io.*;

public class Runner {

    public static void main(String[] args) throws IOException{
        Scanner input = new Scanner(System.in);

        // Prompt user for the name of the image to be resized
        System.out.print("Enter input image: ");
        String img = input.nextLine();

        // Create seamCarver object with image input as a new Picture object
        SeamCarver seamCarver = new SeamCarver(new Picture(img));

        // Prompt user for the number of vertical seams to be removed
        System.out.print("How many vertical seams to remove: ");
        int vertical = Integer.parseInt(input.nextLine());

        // Prompt user for the number of horizontal seams to be removed
        System.out.print("How many horizontal seams to remove: ");
        int horizontal = Integer.parseInt(input.nextLine());

        // Record start time of running the algorithm
        long startTime = System.nanoTime();
        
        // Remove vertical seams 
        for (int i = 0; i < vertical; i++) {
            seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        }
        
        // Remove horizontal seams
        for (int i = 0; i < horizontal; i++) {
            seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
        }

        // Record end time of running the algorithm
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;

        // Prompt user for name of resized image
        System.out.print("Enter output image: ");
        
        // Save resized image
        seamCarver.picture().save(input.nextLine());

        // Print out running time of algorithm
        System.out.print("Image saved in " + totalTime + "ms...");

        input.close();
    }
}
