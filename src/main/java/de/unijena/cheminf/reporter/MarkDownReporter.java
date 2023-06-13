package de.unijena.cheminf.reporter;

import org.openscience.cdk.exception.CDKException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * reporter to create the actual report and markdown file for the Curation Pipeline
 */

public class MarkDownReporter implements IReporter {

    private List<ReportDataObject> reportDataObjectList;

    @Override
    public void initializeNewReport(String aFileDestination) throws IOException {

        //needs a getter to later use it 'report'

    }

    @Override
    public void appendReport(ReportDataObject aReportDataObject) {
        this.reportDataObjectList.add(aReportDataObject);

    }

    @Override
    public void report() throws CDKException, IOException {

        //TODO if (reportDataObjectList.isEmpty(){String errorsNumber = "no error occurred";}
        //or maybe just throw an error message that no report could be created and not creating a markdown file at all

        String header = "# Curation Pipeline Report\n";
        StringBuilder errors = new StringBuilder();
        int numberOfErrors = this.reportDataObjectList.size();
        String errorsNumber = String.valueOf(numberOfErrors);
        errors.append("\n|Number of Errors|" + errorsNumber + "|\n");
        errors.append("|------|-------|\n\n");

        StringBuilder CurationPipelineReport = new StringBuilder();
        CurationPipelineReport.append("## Details\n");
        CurationPipelineReport.append("|depiction|identifier|TypeOfError|ErrorLocation|\n");
        CurationPipelineReport.append("| --- | -------- | -------- | -------- |\n");


        for (ReportDataObject tmpReportDataObject : this.reportDataObjectList) {

            ReportDepiction aReportDepiction = new ReportDepiction();
            String tmpDepictionString = aReportDepiction.getDepictionAsString(tmpReportDataObject.getAtomContainer());
            String tmpIdentifier = String.valueOf(tmpReportDataObject.getIdentifier());
            String tmpErrorDescription = tmpReportDataObject.getErrorDescription();
            String tmpProcessingStepDescription = tmpReportDataObject.getProcessingStepDescription();
            CurationPipelineReport.append(String.format("|![Depiction](%s%s)| %s|%s|%s|", "data:image/png;base64,",
                    tmpDepictionString, tmpIdentifier, tmpErrorDescription, tmpProcessingStepDescription));

        }
        try {
            File tmpFile = new File("\\CurationPipelineReport.md");
            FileWriter tmpWriter = new FileWriter(tmpFile);
            tmpWriter.write(header);
            tmpWriter.write(String.valueOf(errors));
            tmpWriter.write(String.valueOf(CurationPipelineReport));
            tmpWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error writing to file:" + e.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new NullPointerException("amountOfErrors cannot be NULL" + e.getMessage());
        }
    }


    @Override
    public void clear () {
    //either set all the Strings in the table to empty or delete whole markdown file?
    }

}
