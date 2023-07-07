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

import de.unijena.cheminf.curation.message.ErrorCodeMessage;
import org.openscience.cdk.exception.CDKException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * class to create a report as markdown file for the Curation Pipeline.
 *
 * @author Maximilian Schaten
 * @version 1.0.0.0
 */

public class MarkDownReporter implements IReporter {

    private List<ReportDataObject> reportDataObjectList = new ArrayList<>();
    private String filePath = "Processing_Reports\\";

    @Override
    public void initializeNewReport(String aFileDestination) {
    }

    /**
     * appends the ReportDataObjects to the list which is displayed in the markdown report.
     *
     * @param aReportDataObject the report to append
     * @throws NullPointerException if the given aReportDataObject is null
     */
    @Override
    public void appendReport(ReportDataObject aReportDataObject) throws NullPointerException {
        Objects.requireNonNull(aReportDataObject, "aReportDataObject (instance of ReportDataObject) is null.");
        this.reportDataObjectList.add(aReportDataObject);
    }

    /**
     * Content to fill the report, a header with timestamp, a table with the number of errors
     * and a table with processing step followed by tables for every molecule that caused an error in that processing step.
     *
     * @throws CDKException when parsing a SMILES fails.
     * @throws IOException If an error occurs while accessing or writing the report file.
     */
    @Override
    public void report() throws CDKException, IOException {
        //header
        StringBuilder header = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss\n\n");
        String headerString = "# Curation Pipeline Report\n";
        String timestamp = now.format(formatter);
        header.append(headerString);
        header.append(timestamp);
        //table for number of errors
        StringBuilder errors = new StringBuilder();
        int numberOfErrors = this.reportDataObjectList.size();
        String errorsNumber = String.valueOf(numberOfErrors);
        errors.append("\n|Number of Errors|" + errorsNumber + "|\n");
        errors.append("|------|-------|\n\n");
        //style tag to define tables for a page break if needed
        StringBuilder style = new StringBuilder();
        style.append("<style>\n" +
                "  table {\n" +
                "    page-break-inside: avoid;\n" +
                "  }\n" +
                "</style>\n\n");
        //Details
        StringBuilder CurationPipelineReport = new StringBuilder();
        CurationPipelineReport.append("## Molecules that caused an error, sorted by the ProcessingStep the error occured in:\n");
        String tmpPreviousProcessingStepIdentifier;
        String tmpCurrentProcessingStepIdentifier = null;
        //in case no error occured
        if (reportDataObjectList.isEmpty()) {
            CurationPipelineReport.append("| No error occurred |\n");
            CurationPipelineReport.append("|:------:|\n\n");
        } else {
            for (ReportDataObject tmpReportDataObject : reportDataObjectList) {
                tmpPreviousProcessingStepIdentifier = tmpCurrentProcessingStepIdentifier;
                tmpCurrentProcessingStepIdentifier = tmpReportDataObject.getProcessingStepID();
                CurationPipelineReport.append("<table>\n\n");
                if (!tmpCurrentProcessingStepIdentifier.equals(tmpPreviousProcessingStepIdentifier)) {
                    CurationPipelineReport.append("<div style=\"page-break-before:always\"></div>\n\n");
                    CurationPipelineReport.append("| ").append(tmpReportDataObject.getClassOfProcessingStep()).append(" |\n");
                    CurationPipelineReport.append("|:------:|\n");
                    CurationPipelineReport.append("|ProcessingStepIdentifier: " + tmpReportDataObject.getProcessingStepID() + "|\n\n");
                    CurationPipelineReport.append("</table>\n\n");
                }
                CurationPipelineReport.append("<table>\n\n");
                CurationPipelineReport.append("| Identifier: ").append(tmpReportDataObject.getIdentifier()).append("|\n");
                CurationPipelineReport.append("|:-------:|\n");
                CurationPipelineReport.append("|" + "![Depiction](data:image/png;base64,")
                        .append(ReportDepictionUtils.getDepictionAsString(tmpReportDataObject.getAtomContainer()))
                        .append(")|\n");
                CurationPipelineReport.append("|Optional Identifier: ").append(tmpReportDataObject.getOptionalIdentifier())
                        .append("|\n");
                CurationPipelineReport.append("|ErrorCode: ").append(tmpReportDataObject.getErrorCode()).append(" \n").append(ErrorCodeMessage.getErrorMessage(tmpReportDataObject.getErrorCode())).append("|\n\n");
                CurationPipelineReport.append("</table>\n\n");
            }
        }
        try {
            String fileName = "Report_" + getTimeStampAsFilename() + ".md";
            File tmpFile = new File(this.filePath + "\\" + fileName);
            FileWriter tmpWriter = new FileWriter(tmpFile);
            tmpWriter.write(String.valueOf(header));
            tmpWriter.write(String.valueOf(errors));
            tmpWriter.write(String.valueOf(style));
            tmpWriter.write(String.valueOf(CurationPipelineReport));
            tmpWriter.flush();
            tmpWriter.close();
        } catch (IOException anIOException) {
            anIOException.printStackTrace();
            throw new IOException("Error writing to file:" + anIOException.getMessage());
        } catch (NullPointerException aNullPointerException) {
            aNullPointerException.printStackTrace();
            throw new NullPointerException("amountOfErrors cannot be NULL" + aNullPointerException.getMessage());
        }
    }

    /**
     * method to clear the list of DataObjects.
     */
    @Override
    public void clear () {
    reportDataObjectList.clear();
    }

    /**
     * returns the filepath where the report markdown file is created.
     *
     * @return file path as String
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * sets the filepath where the report  markdown file will be stored.
     *
     * @param aFilePath The file path to set for creating the markdown file
     */
    public void setFilePath(String aFilePath) {
        this.filePath = aFilePath;
    }

    /**
     * creates a String from timestamp to be used in filename.
     *
     * @return timestamp as legit String for filename
     */
    public String getTimeStampAsFilename(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        String timeStampForFilename = now.format(formatter);
        return timeStampForFilename;
    }
    
}
