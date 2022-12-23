package de.unijena.cheminf.curation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ChemicalStructureCurationPipelineTest {

    public static IAtomContainerSet importedAtomContainerSet;

    ChemicalStructureCurationPipelineTest() {
    }

    @BeforeAll
    public static void importAtomContainerSet() {
        try {
            SmilesParser tmpSmilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
            ChemicalStructureCurationPipelineTest.importedAtomContainerSet = new AtomContainerSet();
            String[] tmpFilePaths = new String[]{
                    "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUTfirstSMILES.smi",    //with \t and 1
                    "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB_first200kSMILES.txt",    //with \t and 1
                    "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB.smi",    //with \t and 1
                    "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB_canonical_2022_12_16.smi",   //with " " and 0
                    "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB_first_400k_absoluteSMILES_2022_12_16.smi",   //with " " and 0
                    "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB_last_500k_absoluteSMILES_2022_12_16.smi",    //with " " and 0
            };
            File tmpSmilesFile = new File(tmpFilePaths[0]);
            FileReader tmpFileReader = new FileReader(tmpSmilesFile);
            BufferedReader tmpBufferedReader = new BufferedReader(tmpFileReader);
            String tmpLine;
            String tmpSmilesString;
            int tmpUnparseableSmilesCount = 0;
            while ((tmpLine = tmpBufferedReader.readLine()) != null) {
                tmpSmilesString = tmpLine.split("\t", 2)[1];
                //tmpSmilesString = tmpLine.split(" ", 2)[0];
                try {
                    IAtomContainer tmpAtomContainer = tmpSmilesParser.parseSmiles(tmpSmilesString);
                    ChemicalStructureCurationPipelineTest.importedAtomContainerSet.addAtomContainer(tmpAtomContainer);
                } catch (InvalidSmilesException e) {
                    System.out.println(tmpLine);
                    tmpUnparseableSmilesCount++;
                }
            }
            System.out.println("tmpUnparseableSmilesCount = " + tmpUnparseableSmilesCount);
            tmpFileReader.close();
            tmpBufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    private void checkForBondTypesTest() {
        ChemicalStructureCurationUtils.hasBondTypes(ChemicalStructureCurationPipelineTest.importedAtomContainerSet);
        Assertions.assertEquals(true, true);
    }

}
