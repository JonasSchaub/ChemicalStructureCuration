package de.unijena.cheminf.reporter;

import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * class to depict the molecules that caused an error in the pipeline to then show them in the report
 * @throws //CDKExpetion
 * @returns imageString String to show the actual image in the markdown file
 */
public class ReportDepiction {

    public String getDepictionAsImage(IAtomContainer atomContainer1) throws CDKException, IOException {

        try {

            DepictionGenerator depictionGenerator = new DepictionGenerator().withAtomColors().withSize(400, 400);
            BufferedImage tmpImage = depictionGenerator.depict(atomContainer1).toImg();
            System.out.println(tmpImage);
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
}
