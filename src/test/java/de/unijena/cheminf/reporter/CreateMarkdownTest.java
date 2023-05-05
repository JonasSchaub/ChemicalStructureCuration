package de.unijena.cheminf.reporter;

import org.junit.jupiter.api.Test;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.openscience.cdk.interfaces.IElement.C;


/**
 * test whether the report markdown file gets created
 */
class CreateMarkdownTest {

    /**
     * provides the depiction of the molecules in the pipeline
     */
    ReportDepiction reportDepiction;

    /**
     * test class which is provides with depiction and initial strings, which will later be provided by the Report class,
     * to see if the layout of the report works
     * @throws IOException
     * @throws CDKException
     */
    @Test
    public void CreateCurationPipelineReport() throws IOException, CDKException {

        //create test Atomcontainer to depict a test molecule
        IAtom atom = new Atom(C);
        IAtomContainer atomContainer = new AtomContainer();
        atomContainer.addAtom(atom);
        reportDepiction = new ReportDepiction();

        String header = "# Curation Pipeline Report\n";
        StringBuilder errors = new StringBuilder();
        int numberOfErrors = 1;
        String errorsNumber = String.valueOf(numberOfErrors);
        errors.append("\n|Number of Errors|"+ errorsNumber + "|\n");
        errors.append("|------|-------|\n\n");

        StringBuilder CurationPipelineReport = new StringBuilder();
        CurationPipelineReport.append("## Details\n");
        CurationPipelineReport.append("|depiction|identifier|TypeOfError|ErrorLocation|\n");
        CurationPipelineReport.append("| --- | -------- | -------- | -------- |\n");

        for (int i = 1; i <= numberOfErrors; i++){
            String tmpDepictionString = reportDepiction.getDepictionAsImage(atomContainer);
            String tmpId = "Identifier here";
            String tmpTypeOfError = "add type of error";
            String tmpErrorLocation = "add location of error";
            CurationPipelineReport.append(String.format("|![Depiction](%s%s)| %s|%s|%s|", "data:image/png;base64,", tmpDepictionString, tmpId, tmpTypeOfError, tmpErrorLocation));
            System.out.println("data:image/png;base64," + tmpDepictionString);
        }

        try {
            File tmpFile = new File("C:\\Users\\maxim\\Documents\\ChemicalStructureCuration\\src\\test\\java\\de\\unijena\\cheminf\\reporter\\CurationPipelineReport.md");//TODO das ist mein persönliches Verzeichnis, bin mir noch nicht sicher, wie ich das ändere
            FileWriter tmpWriter = new FileWriter(tmpFile);
            tmpWriter.write(header);
            tmpWriter.write(String.valueOf(errors));
            tmpWriter.write(String.valueOf(CurationPipelineReport));
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
