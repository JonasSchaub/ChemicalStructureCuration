package de.unijena.cheminf.reporter;

import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.GeometryUtil;
import org.openscience.cdk.interfaces.IAtomContainer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

/**
 * class to depict the molecules that caused an error in the pipeline to then show them in the report
 * @throws //CDKExpetion
 * @returns imageString String to show the actual image in the markdown file
 */
public class ReportDepiction {

    DepictionGenerator depictionGenerator;

    public String getDepictionAsString(IAtomContainer atomContainer1) throws IOException {

        try {

            int numAtoms = atomContainer1.getAtomCount();
            //double[] dimension = GeometryUtil.get2DDimension(atomContainer1);
            //double width = dimension[0];
            //double height = dimension[1];
            int width = numAtoms * 30;
            int height = numAtoms * 30;

            this.depictionGenerator = new DepictionGenerator().withAtomColors().withSize(width, height)
                    .withFillToFit();
            BufferedImage tmpImage = depictionGenerator.depict(atomContainer1).toImg();
            ByteArrayOutputStream tmpOutputStream = new ByteArrayOutputStream();
            ImageIO.write(tmpImage, "png", tmpOutputStream);
            byte[] imageBytes = tmpOutputStream.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            return base64Image;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error writing to file:" + e.getMessage());
        } catch (CDKException e) {
            e.printStackTrace();
            throw new IOException("Error writing to file:" + e.getMessage());
        }
    }

    /**
     * method to return the depcited Atomcontainer as an Image to safe it as a png file and safe it local to add it
     * to the markdown file if base64 String does not show the actual picture but only the String/Link
     * @param atomContainer the Atomcontainer to depict
     * @return depiction as an Imagefile
     * @throws IOException
     * @throws CDKException
     */
    /**
     * Generate a depiction of the given AtomContainer and return it as a PNG image byte array.
     *
     * @param atomContainer the AtomContainer to depict
     * @return byte array representing the PNG image
     * @throws IOException if there is an error writing the image
     * @throws CDKException if there is an error in the CDK library
     */
    public byte[] getDepictionAsPNG(IAtomContainer atomContainer) throws IOException, CDKException {
        int width = 200;
        int height = 200;

        DepictionGenerator depictionGenerator = new DepictionGenerator().withAtomColors().withSize(width, height)
                .withFillToFit();
        BufferedImage tmpImage = depictionGenerator.depict(atomContainer).toImg();

        ByteArrayOutputStream tmpOutputStream = new ByteArrayOutputStream();
        ImageIO.write(tmpImage, "png", tmpOutputStream);
        return tmpOutputStream.toByteArray();
    }

    /**
     * Save the depiction of the given AtomContainer as a PNG image at the specified file path.
     *
     * @param atomContainer the AtomContainer to depict
     * @param filePath      the file path to save the image
     * @throws IOException if there is an error writing the image
     * @throws CDKException if there is an error in the CDK library
     */
    public void saveDepictionAsPNG(IAtomContainer atomContainer, String filePath) throws IOException, CDKException {
        byte[] imageBytes = getDepictionAsPNG(atomContainer);
        Path imagePath = Path.of(filePath);
        Files.write(imagePath, imageBytes, StandardOpenOption.CREATE);
    }
}

