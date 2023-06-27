package de.unijena.cheminf.curation.reporter;

import de.unijena.cheminf.curation.reporter.ReportDepiction;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * test whether the report markdown file gets created
 */
class CreateMarkdownTest {


    /**
     * provides the depiction of the molecules in the pipeline
     */
    private ReportDepiction reportDepiction;

    /**
     * test class which is provides with depiction and initial strings, which will later be provided by the Report class,
     * to see if the layout of the report works
     *
     * @throws IOException
     * @throws CDKException
     */
    @Test
    @Disabled
    public void CreateCurationPipelineReport() throws IOException, CDKException {

        //create test Atomcontainer to depict a test molecule
        IChemObjectBuilder bldr = SilentChemObjectBuilder.getInstance();
        SmilesParser smipar = new SmilesParser(bldr);
        IAtomContainer atomContainer1 = smipar.parseSmiles("c1(O)ccccc1C(O)=O");
        reportDepiction = new ReportDepiction();

        String header = "# Curation Pipeline Report\n";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        StringBuilder errors = new StringBuilder();
        int numberOfErrors = 1;
        String errorsNumber = String.valueOf(numberOfErrors);
        errors.append("\n|Number of Errors|" + errorsNumber + "|\n");
        errors.append("|------|-------|\n\n");
        errors.append("<style>\n" +
                "  table {\n" +
                "    page-break-inside: avoid;\n" +
                "  }\n" +
                "</style>\n");

        StringBuilder CurationPipelineReport = new StringBuilder();
        CurationPipelineReport.append("## Details\n");

        if (numberOfErrors == 0) {
            CurationPipelineReport.append("### No errors occured.");
        } else {
            for (int i = 1; i <= numberOfErrors; i++) {
                String tmpDepictionString = reportDepiction.getDepictionAsString(atomContainer1);
                String tmpId = "Identifier here";
                String tmpTypeOfError = "add type of error";
                String tmpErrorLocation = "add location of error";
                CurationPipelineReport.append("<table>\n\n");
                CurationPipelineReport.append("\n |Identifier:  " + tmpId + "|\n" +
                        "|------------------|\n");
                CurationPipelineReport.append("![Bild](data:image/png;base64," + tmpDepictionString + ")|\n");
                CurationPipelineReport.append("|"+ tmpErrorLocation + "|");
                CurationPipelineReport.append("</table>\n");
            }
        }
            try {
                File tmpFile = new File("C:\\Users\\maxim\\Documents\\ChemicalStructureCuration\\src\\test\\java\\de\\unijena\\cheminf\\reporter\\CurationPipelineReport.md");//TODO das ist mein persönliches Verzeichnis, bin mir noch nicht sicher, wie ich das ändere
                FileWriter tmpWriter = new FileWriter(tmpFile);
                tmpWriter.write(header);
                tmpWriter.write(">created: " + timestamp + " \n");
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
    }



