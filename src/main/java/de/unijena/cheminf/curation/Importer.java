package de.unijena.cheminf.curation;

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
import java.util.LinkedList;

public class Importer {

    private final String[] filePaths;

    private final IAtomContainerSet importedAtomContainerSet;

    private final LinkedList<String> importedIdentifiersList;

    public Importer(int anIndexOfPath) throws IllegalArgumentException {
        this.filePaths = new String[]{
                "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUTfirstSMILES.smi",    //with \t and 1
                "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB_first200kSMILES.txt",    //with \t and 1
                "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB.smi",    //with \t and 1
                "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB_canonical_2022_12_16.smi",   //with " " and 0
                "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB_first_400k_absoluteSMILES_2022_12_16.smi",   //with " " and 0
                "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB_last_500k_absoluteSMILES_2022_12_16.smi",    //with " " and 0
        };
        this.importedAtomContainerSet = new AtomContainerSet();
        this.importedIdentifiersList = new LinkedList<>();
        //
        this.importDataSet(anIndexOfPath);
    }

    private void importDataSet(int anIndexOfPath) throws IllegalArgumentException {
        if (anIndexOfPath >= this.filePaths.length)
            throw new IllegalArgumentException();
        try {
            SmilesParser tmpSmilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
            File tmpSmilesFile = new File(this.filePaths[anIndexOfPath]);
            FileReader tmpFileReader = new FileReader(tmpSmilesFile);
            BufferedReader tmpBufferedReader = new BufferedReader(tmpFileReader);
            String[] tmpSplittedLine;
            String tmpLine;
            String tmpSmilesString;
            String tmpIdentifierString;
            int tmpUnparseableSmilesCount = 0;
            int tmpCounter = 0;
            while ((tmpLine = tmpBufferedReader.readLine()) != null) {
                if ((tmpCounter % 20000) == 0) {
                    System.out.println(tmpCounter);
                }
                if (anIndexOfPath <= 2) {
                    tmpSplittedLine = tmpLine.split("\t", 2);
                    tmpSmilesString = tmpSplittedLine[1];
                    tmpIdentifierString = tmpSplittedLine[0];
                } else {
                    tmpSplittedLine = tmpLine.split(" ", 2);
                    tmpSmilesString = tmpSplittedLine[0];
                    tmpIdentifierString = tmpSplittedLine[1];
                }
                try {
                    IAtomContainer tmpAtomContainer = tmpSmilesParser.parseSmiles(tmpSmilesString);
                    this.importedAtomContainerSet.addAtomContainer(tmpAtomContainer);
                    this.importedIdentifiersList.add(tmpIdentifierString);
                } catch (InvalidSmilesException e) {
                    System.out.println(tmpLine);
                    tmpUnparseableSmilesCount++;
                }
                tmpCounter++;
            }
            System.out.println("tmpUnparseableSmilesCount = " + tmpUnparseableSmilesCount);
            tmpFileReader.close();
            tmpBufferedReader.close();
        } catch (IOException e) {   //TODO
            throw new RuntimeException(e);
        }
    }

    public IAtomContainerSet getImportedAtomContainerSet() {
        return this.importedAtomContainerSet;
    }

    public LinkedList<String> getImportedIdentifiersList() {
        return this.importedIdentifiersList;
    }

}
