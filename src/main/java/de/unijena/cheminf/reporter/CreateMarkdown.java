package de.unijena.cheminf.reporter;

import org.openscience.cdk.exception.CDKException;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.lang.StringBuilder;

/**
 * Class to create a markdown file as a report for the curation pipeline
 */
public class CreateMarkdown {

    /**
     * provides CreateMarkdown with needed variables
     */
    //private Report report;

    /**
     * provides Depiction as String to show it in the markdown file
     */
    //private ReportDepiction reportDepiction;
    private StringBuilder curationPipelineReport;
    private StringBuilder errors;

    /**
     * class to create a markdown file to report failures in the Curation Pipeline
     * provides molecule depiction, an identifier for the molecule, information about the type of error and the
     * location where each error occurred
     * @throws IOException
     * @throws CDKException
     */

    public void CreateCurationPipelineReport() throws IOException, CDKException {

        String header = "# Curation Pipeline Report\n";
        errors = new StringBuilder();
        int tmpNumberOfErrors = 3;
        String tmpErrorsAmount = String.valueOf(tmpNumberOfErrors);
        this.errors.append("\n|Number of Errors|"+ tmpErrorsAmount + "|\n");
        this.errors.append("|------|-------|\n\n");

        curationPipelineReport = new StringBuilder();
        this.curationPipelineReport.append("## Details\n");
        this.curationPipelineReport.append("|depiction|identifier|TypeOfError|ErrorLocation|\n");
        this.curationPipelineReport.append("| --- | -------- | -------- | -------- |\n");

        /* TODO create numberOfErrors count to make loop work and expand table in markdown file
         for (int i; i<= numberOfErrors; i++){
            String tmpDepictionString = reportDepiction.getDepictionAsImage(report.getAtomContainer(i));
            String tmpId = report.getIdentifier(i);
            String tmpTypeOfError = report.getErrorDescription(i);
            String tmpErrorLocation = report.getProcessingStepDescription(i);
            CurationPipelineReport.append(String.format("|![Depiction](%s)| %s | %s| %s|\n", i, tmpDepictionString, tmpId, tmpTypeOfError, tmpErrorLocation));
        }
        */

        try {
            File tmpFile = new File("C:\\Users\\maxim\\Documents\\ChemicalStructureCuration\\src\\main\\java\\de\\unijena\\cheminf\\reporter\\CurationPipelineReport.md");
            FileWriter tmpWriter = new FileWriter(tmpFile);
            tmpWriter.write(header);
            tmpWriter.write(String.valueOf(errors));
            tmpWriter.write(String.valueOf(curationPipelineReport));
            tmpWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error writing to file:" + e.getMessage());
        }catch (NullPointerException e){
            e.printStackTrace();
            throw new NullPointerException("amountOfErrors cannot be NULL" + e.getMessage());
        }
    }

}
