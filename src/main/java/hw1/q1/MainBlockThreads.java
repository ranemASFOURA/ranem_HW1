package hw1.q1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainBlockThreads {
    public static final String SOURCE_FILE = "src/main/resources/optimizing-for-latency-example_resources_many-flowers.jpg";
    public static final String DESTINATION_FILE = "./out/many-flowers-block.jpg";

    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        // Test different numbers of threads
        int[] threadCounts = {1, 2, 4, 8, 16}; // You can modify the thread numbers here

        for (int numberOfThreads : threadCounts) {
            // Measure the execution time for each number of threads
            long startTime = System.currentTimeMillis();
            recolorMultithreadedBlock(originalImage, resultImage, numberOfThreads);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("Threads: " + numberOfThreads + ", Time taken (Block): " + duration + " ms");

            // Save the image with the corresponding number of threads
            File outputFile = new File("./out/many-flowers-block-" + numberOfThreads + "-threads.jpg");
            ImageIO.write(resultImage, "jpg", outputFile);
        }
    }

    // Recolor image using multiple threads by dividing it into blocks
    public static void recolorMultithreadedBlock(BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int blockSizeX = width / (int) Math.sqrt(numberOfThreads);
        int blockSizeY = height / (int) Math.sqrt(numberOfThreads);

        for (int threadIndex = 0; threadIndex < numberOfThreads; threadIndex++) {
            final int threadMultiplier = threadIndex;
            Thread thread = new Thread(() -> {
                // Calculate block position for each thread
                int numBlocksPerRow = (int) Math.sqrt(numberOfThreads);
                int blockRow = threadMultiplier / numBlocksPerRow;
                int blockCol = threadMultiplier % numBlocksPerRow;

                int xOrigin = blockCol * blockSizeX;
                int yOrigin = blockRow * blockSizeY;

                // Adjust block size at edges of the image
                int currentBlockWidth = (xOrigin + blockSizeX > width) ? width - xOrigin : blockSizeX;
                int currentBlockHeight = (yOrigin + blockSizeY > height) ? height - yOrigin : blockSizeY;

                // Recolor block of the image
                recolorImage(originalImage, resultImage, xOrigin, yOrigin, currentBlockWidth, currentBlockHeight);
            });
            threads.add(thread);
        }

        // Start and join threads
        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Recolor pixels in the given section of the image
    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner, int width, int height) {
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }

    // Modify the pixel colors
    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);
        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        // Check if it's a shade of gray and apply recoloring
        if (isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(resultImage, x, y, newRGB);
    }

    // Set the RGB value of a pixel
    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    // Check if a pixel is a shade of gray
    public static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

    // Create an RGB value from separate red, green, and blue components
    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;
        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;
        rgb |= 0xFF000000;
        return rgb;
    }

    // Extract the red component from an RGB value
    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    // Extract the green component from an RGB value
    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    // Extract the blue component from an RGB value
    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
