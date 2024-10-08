import hw1.q1.MainBlock;
import junit.framework.TestCase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainBlockTest extends TestCase {

    public void testRecolorMultithreadedBlock() throws IOException {
        // Arrange
        String sourceFile = MainBlock.SOURCE_FILE;
        BufferedImage originalImage = ImageIO.read(new File(sourceFile));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        int numberOfThreads = 4;

        // Act
        MainBlock.recolorMultithreadedBlock(originalImage, resultImage, numberOfThreads);

        // Assert
        assertNotNull(resultImage);
        assertEquals(originalImage.getWidth(), resultImage.getWidth());
        assertEquals(originalImage.getHeight(), resultImage.getHeight());
    }


}
