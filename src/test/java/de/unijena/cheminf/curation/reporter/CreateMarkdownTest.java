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

import de.unijena.cheminf.curation.TestUtils;
import de.unijena.cheminf.curation.enums.ErrorCodes;
import de.unijena.cheminf.curation.processingSteps.filters.MinAtomCountFilter;
import de.unijena.cheminf.curation.processingSteps.filters.MinBondCountFilter;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.exception.CDKException;

import java.io.File;
import java.io.IOException;

/**
 * test class for creating a markdown report
 *
 * @author Maximilian Schaten
 * @version 1.0.0.0
 */
class CreateMarkdownTest {

    /**
     *Test class for creating a markdown report.
     *
     * @throws IOException If an error occurs while accessing or writing the report file
     * @throws CDKException when parsing a SMILES fails
     */
    @Test
    public void CreateCurationPipelineReport() throws IOException, CDKException {
        ReportDataObject testReportDataObject = new ReportDataObject(TestUtils.parseSmilesString("CCC"),
                "Idetifier", "OptionalIdentifier", "1",
                MinBondCountFilter.class, ErrorCodes.CLONE_ERROR);
        ReportDataObject testReportDataObject2 = new ReportDataObject(TestUtils.parseSmilesString("CCCCCCCCCCCCCCCC"),
                "Idetifier2", "OptionalIdentifier2", "1",
                MinBondCountFilter.class, ErrorCodes.ATOM_CONTAINER_NULL_ERROR);
        ReportDataObject testReportDataObject3 = new ReportDataObject(TestUtils.parseSmilesString("CN1C=NC2=C1C(=O)N(C(=O)N2C)C"),
                "Idetifier3", "OptionalIdentifier3", "3",
                MinAtomCountFilter.class, ErrorCodes.ATOM_CONTAINER_NULL_ERROR);
        MarkDownReporter tmpMDReporter = new MarkDownReporter();
        new File("Processing_Reports").mkdir();
        tmpMDReporter.setFilePath("Processing_Reports\\");
        tmpMDReporter.appendReport(testReportDataObject);
        tmpMDReporter.appendReport(testReportDataObject2);
        tmpMDReporter.appendReport(testReportDataObject3);
        tmpMDReporter.report();
    }

}
