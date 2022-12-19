/*
 * MIT License
 *
 * Copyright (c) 2022 Samuel Behr, Felix Baensch, Jonas Schaub, Christoph Steinbeck, and Achim Zielesny
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

/**
 *
 */
public class ChemicalStructureCurationUtilsTest {

    public static IAtomContainerSet importedAtomContainerSet;

    ChemicalStructureCurationUtilsTest() {
    }

    @BeforeAll
    public static void importAtomContainerSet() {
        try {
            SmilesParser tmpSmilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
            ChemicalStructureCurationUtilsTest.importedAtomContainerSet = new AtomContainerSet();
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
                    ChemicalStructureCurationUtilsTest.importedAtomContainerSet.addAtomContainer(tmpAtomContainer);
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
    public void checkForBondTypes1Test() {
        boolean[] tmpBondTypesArrayExpected;
        boolean[] tmpBondTypesArray;
        tmpBondTypesArrayExpected = new boolean[]{true, true, true, false, false, false, false};  //for COCONUTfirstSMILES.smi
        tmpBondTypesArray = ChemicalStructureCurationUtils.checkForBondTypes(ChemicalStructureCurationUtilsTest.importedAtomContainerSet);
        Assertions.assertArrayEquals(tmpBondTypesArrayExpected, tmpBondTypesArray);
        //
        tmpBondTypesArrayExpected = new boolean[]{true, true, false, false, false, false, false};  //for COCONUTfirstSMILES.smi line index 0 - 2
        IAtomContainerSet tmpSubset = new AtomContainerSet();
        for (int i = 0; i < 2; i++) {
            tmpSubset.addAtomContainer(ChemicalStructureCurationUtilsTest.importedAtomContainerSet.getAtomContainer(i));
        }
        tmpBondTypesArray = ChemicalStructureCurationUtils.checkForBondTypes(tmpSubset);
        Assertions.assertArrayEquals(tmpBondTypesArrayExpected, tmpBondTypesArray);
    }

    @Test
    public void checkForBondTypes2Test() {
        boolean[] tmpBondTypesArrayExpected;
        boolean[] tmpBondTypesArray;
        tmpBondTypesArrayExpected = new boolean[]{true, true, false, false, false, false, false};   //for COCONUTfirstSMILES.smi line index 0
        tmpBondTypesArray = ChemicalStructureCurationUtils.checkForBondTypes(ChemicalStructureCurationUtilsTest.importedAtomContainerSet.getAtomContainer(0));
        Assertions.assertArrayEquals(tmpBondTypesArrayExpected, tmpBondTypesArray);
        //
        tmpBondTypesArrayExpected = new boolean[]{true, true, true, false, false, false, false};    //for COCONUTfirstSMILES.smi line index 12
        tmpBondTypesArray = ChemicalStructureCurationUtils.checkForBondTypes(ChemicalStructureCurationUtilsTest.importedAtomContainerSet.getAtomContainer(12));
        Assertions.assertArrayEquals(tmpBondTypesArrayExpected, tmpBondTypesArray);
    }

    @Test
    private void checkMoleculeTest() {
        SmilesParser tmpSmilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
        try {
            IAtomContainer tmpMolecule = tmpSmilesParser.parseSmiles("OC=1C=C(OC)C=2C(O)=C3C(=C(C2C1)C=4C=5C=C(OC)C=C(OC)C5C(O)=C6C4C(O)C(OC6)C)C(O)C(OC3)C");
        } catch (InvalidSmilesException e) {
            throw new RuntimeException(e);
        }
    }

}