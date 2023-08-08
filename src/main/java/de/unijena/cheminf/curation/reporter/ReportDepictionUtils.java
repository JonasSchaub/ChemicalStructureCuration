/*
 * MIT License
 *
 * Copyright (c) 2023 Samuel Behr, Felix Baensch, Jonas Schaub, Christoph Steinbeck, and Achim Zielesny
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.unijena.cheminf.curation.reporter;

import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Utility class to create a depiction of the molecules that caused an error in the pipeline as a base64 string
 * to then show them in the markdown report.
 *
 * @author Maximilian Schaten
 * @version 1.0.0.0
 */
public class ReportDepictionUtils {

    /**
     * Creates a base64 String from a depiction of the molecule in the AtomContainer.
     *
     * @param anAtomContainer from ReportDataObject to depict for the report
     * @return base64String of the depicted AtomContainer
     * @throws IOException If an error occurs while creating the depiction as base64 String
     */
    public static String getDepictionAsString(IAtomContainer anAtomContainer) throws IOException {
        try {
            int tmpNumAtoms = anAtomContainer.getAtomCount();
            int tmpWidth = tmpNumAtoms * 50; //
            int tmpHeight = tmpNumAtoms * 30;
            DepictionGenerator tmpDepictionGenerator = new DepictionGenerator().withAtomColors().withSize(tmpWidth, tmpHeight)
                    .withFillToFit();
            BufferedImage tmpImage = tmpDepictionGenerator.depict(anAtomContainer).toImg();
            ByteArrayOutputStream tmpOutputStream = new ByteArrayOutputStream();
            ImageIO.write(tmpImage, "png", tmpOutputStream);
            byte[] tmpImageBytesArray = tmpOutputStream.toByteArray();
            String tmpBase64ImageString = Base64.getEncoder().encodeToString(tmpImageBytesArray);
            return tmpBase64ImageString;
        } catch (CDKException aCDKException) {
            aCDKException.printStackTrace();
            throw new IOException("Error creating String: " + aCDKException.getMessage());
        }
    }

}
