import hw1.q1.MainHorizontal;
import junit.framework.TestCase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainHorizontalTest extends TestCase {

    public void testRecolorMultithreadedHorizontal() throws IOException {
        // Arrange
        String sourceFile = MainHorizontal.SOURCE_FILE;
        BufferedImage originalImage = ImageIO.read(new File(sourceFile));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        int numberOfThreads = 4;

        // Act
        MainHorizontal.recolorMultithreadedHorizontal(originalImage, resultImage, numberOfThreads);

        // Assert
        // Check if resultImage is not null
        assertNotNull(resultImage);

        // check if the dimensions are the same as the original image
        assertEquals(originalImage.getWidth(), resultImage.getWidth());
        assertEquals(originalImage.getHeight(), resultImage.getHeight());

    }

}
