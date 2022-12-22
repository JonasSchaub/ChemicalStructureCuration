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

    private static final String FILE_DIRECTORY_PATH = "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\";

    private static final String[] FILE_NAMES_STRING_ARRAY = new String[]{
            "COCONUTfirstSMILES.smi",                               //with \t and 1
            "COCONUT_DB_first200kSMILES.txt",                       //with \t and 1
            "COCONUT_DB.smi",                                       //with \t and 1
            "COCONUT_DB_canonical_2022_12_16.smi",                  //with " " and 0
            "COCONUT_DB_first_400k_absoluteSMILES_2022_12_16.smi",  //with " " and 0
            "COCONUT_DB_last_500k_absoluteSMILES_2022_12_16.smi",   //with " " and 0
    };

    /**
     * Buffer size (64 kByte = 65536, 256 kByte = 262144, 512 kByte = 524288, 1 MByte = 1048576 Byte)
     */
    private static final int BUFFER_SIZE = 65536;

    private static final String[] separatorsStringArray = {"\t", " "};

    private final BufferedReader bufferedReader;

    private final int separatorIndex;

    private final int smilesStringIndex;

    private final LinkedList<String> unparseableLinesList;

    /**
     * Constructor. TODO
     *
     * @param anIndexOfPath
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public Importer(int anIndexOfPath) throws IllegalArgumentException, IOException {
        if (anIndexOfPath >= Importer.FILE_NAMES_STRING_ARRAY.length) {
            throw new IllegalArgumentException("Given index of path does not fit to the number of deposited paths");
        }
        String tmpFilePath = Importer.FILE_DIRECTORY_PATH + FILE_NAMES_STRING_ARRAY[anIndexOfPath];
        File tmpSmilesFile = new File(tmpFilePath);
        if (anIndexOfPath <= 2) {
            this.separatorIndex = 0;
            this.smilesStringIndex = 1;
        } else {
            this.separatorIndex = 1;
            this.smilesStringIndex = 0;
        }
        this.unparseableLinesList = new LinkedList<>();
        try {
            this.bufferedReader = new BufferedReader(new FileReader(tmpSmilesFile), Importer.BUFFER_SIZE);
            this.skipLine(1);
            this.bufferedReader.mark(Importer.BUFFER_SIZE);
            //
        } catch (IOException anIOException) {
            //TODO
            throw anIOException;
        }
    }

    public IAtomContainerSet importHoleDataSet() throws IllegalArgumentException {
        try {
            this.resetBufferedReader();
        } catch (IOException anIOException) {
            //TODO
            return null;
        }
        IAtomContainerSet tmpAtomContainerSet = new AtomContainerSet();
        try {
            SmilesParser tmpSmilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
            IAtomContainer tmpAtomContainer;
            int tmpCounter = 0;
            while (true) {
                if ((tmpAtomContainer = this.readNextLine(tmpSmilesParser)) == null) {
                    break;
                } else {
                    tmpAtomContainerSet.addAtomContainer(tmpAtomContainer);
                    tmpCounter++;
                }
                if ((tmpCounter % 20000) == 0) {
                    System.out.println(tmpCounter);
                }
            }
        } catch (IOException e) {   //TODO
            throw new RuntimeException(e);
        }
        return tmpAtomContainerSet;
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public IAtomContainer readNextLine() throws IOException {
        IAtomContainer tmpAtomContainer;
        try {
            SmilesParser tmpSmilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
            tmpAtomContainer = this.readNextLine(tmpSmilesParser);
        } catch (IOException e) {   //TODO
            throw new RuntimeException(e);
        }
        return tmpAtomContainer;
    }

    /**
     *
     * @param aNumberOfLines
     * @throws IOException
     */
    public void skipLine(int aNumberOfLines) throws IOException {
        for (int i = 0; i < aNumberOfLines; i++) {
            this.bufferedReader.readLine();
        }
    }

    /**
     *
     * @throws IOException
     */
    public void resetBufferedReader() throws IOException {
        this.unparseableLinesList.clear();
        this.bufferedReader.reset();
    }

    /**
     *
     * @throws IOException
     */
    public void closeBufferedReader() throws IOException {
        this.bufferedReader.close();
    }

    /**
     *
     * @return
     */
    public int getUnparseableSmilesCount() {
        return this.unparseableLinesList.size();
    }

    /**
     *
     * @param aSmilesParser
     * @return
     * @throws IOException
     */
    private IAtomContainer readNextLine(SmilesParser aSmilesParser) throws IOException {
        IAtomContainer tmpAtomContainer = null;
        try {
            String tmpLine;
            String[] tmpSplittedLine;
            if ((tmpLine  = this.bufferedReader.readLine()) != null) {
                tmpSplittedLine = tmpLine.split(Importer.separatorsStringArray[this.separatorIndex], 2);
                try {
                    tmpAtomContainer = aSmilesParser.parseSmiles(tmpSplittedLine[this.smilesStringIndex]);
                    tmpAtomContainer.setProperty("SMILES", tmpSplittedLine[this.smilesStringIndex]);
                    tmpAtomContainer.setProperty("ID", tmpSplittedLine[this.smilesStringIndex - 1]);
                } catch (InvalidSmilesException e) {
                    this.unparseableLinesList.add(tmpLine);
                }
            }
        } catch (IOException anIOException) {   //TODO
            throw anIOException;
        }
        return tmpAtomContainer;
    }

}
