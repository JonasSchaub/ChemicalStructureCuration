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

        DepictionGenerator depictionGenerator = new DepictionGenerator().withAtomColors().withSize(200,200);
        BufferedImage tmpImage = depictionGenerator.depict(atomContainer1).toImg();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(tmpImage, "png", outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        return base64Image;
    }


}
