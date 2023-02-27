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
import org.junit.jupiter.api.Test;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.config.Elements;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ChemicalStructureCurationUtilsTest {

    ChemicalStructureCurationUtilsTest() {
    }

    @Test
    public void checkForBondTypesTest1() {
        try {
            Importer tmpImporter = new Importer(0); //file: "COCONUTfirstSMILES.smi"
            IAtomContainerSet tmpAtomContainerSet = tmpImporter.importHoleDataSet();
            //
            boolean[] tmpBondTypesArrayExpected;
            boolean[] tmpBondTypesArray;
            tmpBondTypesArrayExpected = new boolean[]{true, true, true, false, false, false, false};  //for "COCONUTfirstSMILES.smi"
            tmpBondTypesArray = ChemicalStructureCurationUtils.hasBondTypes(tmpAtomContainerSet);
            Assertions.assertArrayEquals(tmpBondTypesArrayExpected, tmpBondTypesArray);
            //
            tmpBondTypesArrayExpected = new boolean[]{true, true, false, false, false, false, false};  //for "COCONUTfirstSMILES.smi" line index 0 - 2
            IAtomContainerSet tmpSubset = new AtomContainerSet();
            for (int i = 0; i < 2; i++) {
                tmpSubset.addAtomContainer(tmpAtomContainerSet.getAtomContainer(i));
            }
            tmpBondTypesArray = ChemicalStructureCurationUtils.hasBondTypes(tmpSubset);
            Assertions.assertArrayEquals(tmpBondTypesArrayExpected, tmpBondTypesArray);
        } catch (Exception anException) {
        }
    }

    @Test
    public void checkForBondTypesTest2() {
        try {
            Importer tmpImporter = new Importer(0); //file: "COCONUTfirstSMILES.smi"
            IAtomContainerSet tmpAtomContainerSet = tmpImporter.importHoleDataSet();
            //
            boolean[] tmpBondTypesArrayExpected;
            boolean[] tmpBondTypesArray;
            tmpBondTypesArrayExpected = new boolean[]{true, true, false, false, false, false, false};   //for "COCONUTfirstSMILES.smi" line index 0
            tmpBondTypesArray = ChemicalStructureCurationUtils.hasBondTypes(tmpAtomContainerSet.getAtomContainer(0));
            Assertions.assertArrayEquals(tmpBondTypesArrayExpected, tmpBondTypesArray);
            //
            tmpBondTypesArrayExpected = new boolean[]{true, true, true, false, false, false, false};    //for "COCONUTfirstSMILES.smi" line index 12
            tmpBondTypesArray = ChemicalStructureCurationUtils.hasBondTypes(tmpAtomContainerSet.getAtomContainer(12));
            Assertions.assertArrayEquals(tmpBondTypesArrayExpected, tmpBondTypesArray);
        } catch (Exception anException) {
        }
    }

    @Test
    public void countByAtomicNumberTest() {
        try {
            Importer tmpImporter = new Importer(2); //file: "COCONUTfirstSMILES.smi"
            IAtomContainerSet tmpAtomContainerSet = tmpImporter.importHoleDataSet();
            //
            List<IAtom> tmpAllAtomsList = new ArrayList<>();
            for (IAtomContainer tmpAtomContainer :
                    tmpAtomContainerSet.atomContainers()) {
                if (tmpAtomContainer == null) {
                    System.out.println("AtomContainer is null");
                } else if (tmpAtomContainer.getAtomCount() == 0) {
                    System.out.println("AtomCount is zero");
                } else {
                    for (IAtom tmpAtom :
                            tmpAtomContainer.atoms()) {
                        tmpAllAtomsList.add(tmpAtom);
                    }
                }
            }
            int[] tmpAtomicNumberFrequencyArray1 = ChemicalStructureCurationUtils.countByAtomicNumber(tmpAllAtomsList);
            System.out.println("Frequency of each atomic number:");
            int tmpValue;
            for (int i = 0; i < 112; i++) {
                if ((tmpValue = tmpAtomicNumberFrequencyArray1[i]) > 0) {
                    System.out.println(Elements.ofNumber(i + 1).symbol() + "\t" + (i + 1) + "\t" + tmpValue);
                }
            }
            //
            List<IAtom> tmpIncorrectValenciesAtomsList = ChemicalStructureCurationUtils.getAtomsWithIncorrectValencies(tmpAtomContainerSet);
            int[] tmpAtomicNumberFrequencyArray2 = ChemicalStructureCurationUtils.countByAtomicNumber(tmpIncorrectValenciesAtomsList);
            System.out.println("Frequency of each atomic number:");
            for (int i = 0; i < 112; i++) {
                if ((tmpValue = tmpAtomicNumberFrequencyArray2[i]) > 0) {
                    System.out.println(Elements.ofNumber(i + 1).symbol() + "\t" + (i + 1) + "\t" + tmpValue);
                }
            }
        } catch (Exception anException) {
        }
    }

    @Test
    public void checkMoleculeTest() {   //TODO: remove
        SmilesParser tmpSmilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
        try {
            IAtomContainer tmpMolecule = tmpSmilesParser.parseSmiles("OC=1C=C(OC)C=2C(O)=C3C(=C(C2C1)C=4C=5C=C(OC)C=C(OC)C5C(O)=C6C4C(O)C(OC6)C)C(O)C(OC3)C");
        } catch (InvalidSmilesException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void analyseDistributionOfIncorrectMoleculesOverDataSet() {
        //TODO: check whether the COCONUT entries with atoms with incorrect valencies cluster in any way (-> identify less trust worthy sources?! / rank them)
    }

}