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

import de.unijena.cheminf.curation.enums.SortProperty;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.xmlcml.euclid.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to create a report as markdown file for the Curation Pipeline.
 *
 * @author Maximilian Schaten
 * @version 1.0.0.0
 */
public class MarkDownReporter implements IReporter {

    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(MarkDownReporter.class.getName());

    /**
     * Workaround: TODO remove after merge
     */
    public static final String REPORTS_FOLDER_PATH_STRING = "Processing_Reports" + File.separator;

    /**
     * ArrayList storing ReportDataObjects.
     */
    private final List<ReportDataObject> reportDataObjectList = new ArrayList<>();

    /**
     * String for file path.
     */
    private String filePathString;

    /**
     * Resource Bundle for String literals in the report.
     */
    private static final ResourceBundle reportStringLiterals = ResourceBundle.getBundle("ReportStringLiterals", Locale.getDefault());

    private static final ResourceBundle errorMessages = ResourceBundle.getBundle("Errors", Locale.getDefault());

    /**
     * Comparator for sorting errors
     */
    private Comparator<ReportDataObject> sortComparator;

    /**
     * Constructor for the given file path where the report file is created.
     *
     * @param aFilePathString file path where the report file is created.
     * @throws NullPointerException if aFilePathString is null.
     * @throws IllegalArgumentException if aFilePathString is empty or blank.
     */
    public MarkDownReporter(String aFilePathString, SortProperty aSortProperty) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aFilePathString, "aFilePathString (instance of String) is null.");
        if (aFilePathString.isBlank()){
            throw new IllegalArgumentException("aFilePathString (instance of String) is empty or blank.");
        }
        Path filePath = Paths.get(aFilePathString);
        if (Files.isDirectory(filePath)) {
            this.filePathString = aFilePathString;
        }
        this.sortComparator = aSortProperty.getComparator();
    }

    /**
     * Constructor workaround: TODO remove after merge
     */
    public MarkDownReporter() {
        this(MarkDownReporter.REPORTS_FOLDER_PATH_STRING, SortProperty.PROCESSING_STEP_ID);
    }

    /**
     * Initializes a new report.
     */
    @Override
    public void initializeNewReport() {
        this.clear();
    }

    /**
     * Appends the ReportDataObjects to the list which is displayed in the markdown report.
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
     * @throws IOException if an error occurs while accessing or writing the report file.
     */
    @Override
    public void report() throws CDKException, IOException {
        // sorting list of objects for ProcessingStep
        List<ReportDataObject> sortedReportDataObjects = reportDataObjectList.stream()
                .sorted(sortComparator).toList();
        
        StringBuilder markdownReport = new StringBuilder();
        markdownReport.append("# ").append(reportStringLiterals.getString("HEADER")).append("\n");
        markdownReport.append(reportStringLiterals.getString("TIMESTAMPOFREPORTGENERATION")).append(getCurrentTime()).append("\n");
        markdownReport.append(reportStringLiterals.getString("DATESTAMPOFREPORTGENERATION")).append(getCurrentDate()).append("\n\n");
        markdownReport.append(reportStringLiterals.getString("FILENAME"));//TODO
        markdownReport.append(reportStringLiterals.getString("NUMBEROFERRORS")).append(sortedReportDataObjects.size()).append("\n\n");
        markdownReport.append("## ").append(reportStringLiterals.getString("DETAILS")).append("\n\n");

        //TODO  not repeating the ProcessingStep for more than one object with the same ProcessingStep
        //TODO Bringing string literals into properties file

        int index = 1;
        for (ReportDataObject reportDataObject : this.reportDataObjectList) {
            markdownReport.append("**Error number:** ").append(index).append("\n\n");
            IAtomContainer atomContainer = reportDataObject.getAtomContainer();
            boolean tmpImageCouldBeGenerated;
            if (atomContainer != null && atomContainer.getAtomCount() > 0) {
                try {
                    String depictionBase64 = ReportDepictionUtils.getDepictionAsString(atomContainer);
                    markdownReport.append("**Molecule Depiction:**\n\n");
                    markdownReport.append("\n![Molecule Depiction](data:image/png;base64,").append(depictionBase64).append(")\n\n");
                    //
                    tmpImageCouldBeGenerated = true;
                } catch (Exception anException) {
                    MarkDownReporter.LOGGER.log(Level.WARNING, anException.toString(), anException);
                    tmpImageCouldBeGenerated = false;
                }
            } else {
                tmpImageCouldBeGenerated = false;
            }
            // image generation failed
            if (!tmpImageCouldBeGenerated) {
                String ErrorMessageBase64 = ReportDepictionUtils.getErrorMessageImage();
                markdownReport.append("\n![Error Message](data:image/png;base64,").append(ErrorMessageBase64).append(")\n\n");
            }
            markdownReport.append("**Processing Step ID:** ").append(reportDataObject.getProcessingStepIdentifier()).append("\n\n");
            markdownReport.append("**Processing Step Class:** ").append(reportDataObject.getSimpleNameOfClassOfProcessingStep()).append("\n\n");
            markdownReport.append("**Error Code:** ").append(reportDataObject.getErrorCode()).append("\n\n");
            markdownReport.append("**Error Message:** ").append(errorMessages.getString(reportDataObject.getErrorCode().toString())).append("\n\n");
            markdownReport.append("**Identifier:** ").append(reportDataObject.getIdentifier()).append("\n\n");
            if (reportDataObject.getExternalIdentifier() != null) {
                markdownReport.append("**External Identifier:** ").append(reportDataObject.getExternalIdentifier()).append("\n\n");
            }
            markdownReport.append("______");
            index++;
            markdownReport.append("\n\n");
        }

        try {
            String tmpFileName = MarkDownReporter.getFileName();
            File tmpFile = new File(this.filePathString + File.separator + tmpFileName);
            FileWriter tmpWriter = new FileWriter(tmpFile);
            tmpWriter.write(markdownReport.toString());
            tmpWriter.flush();
            tmpWriter.close();
        } catch (IOException anIOException) {
            anIOException.printStackTrace();
            throw new IOException("Error writing to file:" + anIOException.getMessage());
        } catch (NullPointerException aNullPointerException) {
            aNullPointerException.printStackTrace();
            throw new NullPointerException("NumberOfErrors cannot be null" + aNullPointerException.getMessage());
        }
        this.clear();
    }

    /**
     * method to clear the list of DataObjects.
     */
    @Override
    public void clear () {
        this.reportDataObjectList.clear();
    }

    /**
     * returns the filepath where the report markdown file is created.
     *
     * @return file path as String
     */
    public String getFilePathString() {
        return this.filePathString;
    }

    /**
     * sets the filepath where the report  markdown file will be stored.
     *
     * @param aFilePath The file path to set for creating the markdown file
     */
    public void setFilePathString(String aFilePath) {
        this.filePathString = aFilePath;
    }

    private static String getFileName(){
        return "Report_" + getCurrentTimeStampForFilename() + ".md";
    }

    /**
     * creates a String from timestamp to be used in filename.
     *
     * @return timestamp as legit String for filename
     */
    private static String getCurrentTimeStampForFilename(){
        LocalDateTime tmpNow = LocalDateTime.now();
        DateTimeFormatter tmpFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        return tmpNow.format(tmpFormatter);
    }
    private static String getCurrentTime(){
        LocalDateTime tmpTime = LocalDateTime.now();
        DateTimeFormatter tmpTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return tmpTime.format(tmpTimeFormatter);
    }
    private static String getCurrentDate(){
        LocalDateTime tmpDate = LocalDateTime.now();
        DateTimeFormatter tmpDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return tmpDate.format(tmpDateFormatter);
    }

}
