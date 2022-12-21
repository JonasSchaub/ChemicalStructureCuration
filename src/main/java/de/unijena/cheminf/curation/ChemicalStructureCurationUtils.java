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

import org.openscience.cdk.AtomType;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.tools.CDKHydrogenAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class ChemicalStructureCurationUtils {

    public static void checkMolecule(IAtomContainer aMolecule) {    //TODO: probably remove
        Objects.requireNonNull(aMolecule, "aMolecule (instance of IAtomContainer) is null");
        //
        int tmpAtomCount = aMolecule.getAtomCount();
        int tmpIterator = 0;
        for (IAtom tmpAtom : aMolecule.atoms()) {
            int tmpBondCount = tmpAtom.getBondCount() + tmpAtom.getImplicitHydrogenCount();
            int tmpFormalCharge = tmpAtom.getFormalCharge();
            int tmpImplicitHydrogenCount = tmpAtom.getImplicitHydrogenCount();
            int tmpSigmaBondsCount = 0;
            int tmpPiBondsCount = 0;
            for (IBond tmpBond : tmpAtom.bonds()) {
                switch (tmpBond.getOrder()) {
                    case UNSET -> {}    //TODO
                    case SINGLE -> tmpSigmaBondsCount++;
                    case DOUBLE -> {
                        tmpSigmaBondsCount++;
                        tmpPiBondsCount++;
                    }
                    case TRIPLE -> {
                        tmpSigmaBondsCount++;
                        tmpPiBondsCount += 2;
                    }
                    case QUADRUPLE -> {
                        tmpSigmaBondsCount++;
                        tmpPiBondsCount += 3;
                    }
                    case QUINTUPLE -> {
                        tmpSigmaBondsCount++;
                        tmpPiBondsCount += 4;
                    }
                    case SEXTUPLE -> {
                        tmpSigmaBondsCount++;
                        tmpPiBondsCount += 5;
                    }
                    default -> {}   //TODO
                }
            }
            tmpSigmaBondsCount += tmpAtom.getImplicitHydrogenCount();
            //
            System.out.println(tmpIterator + " - " + tmpAtom.getSymbol());
            System.out.println("\ttmpFormalCharge = " + tmpFormalCharge);
            System.out.println("\ttmpImplicitHydrogenCount = " + tmpImplicitHydrogenCount);
            System.out.println("\ttmpBondCount = " + tmpBondCount);
            System.out.println("\ttmpSigmaBondsCount = " + tmpSigmaBondsCount);
            System.out.println("\ttmpPiBondsCount = " + tmpPiBondsCount);
            tmpIterator++;
        }
    }

    public static boolean[] checkForBondTypes(IAtomContainerSet anAtomContainerSet) {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null");
        //
        boolean[] tmpBondTypesBooleanArray = new boolean[7];
        boolean[] tmpMoleculeBondTypesBooleanArray;
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            if (tmpAtomContainer == null) {
                continue;
            }
            tmpMoleculeBondTypesBooleanArray = ChemicalStructureCurationUtils.checkForBondTypes(tmpAtomContainer);
            for (int i = 0; i < 7; i++) {
                tmpBondTypesBooleanArray[i] = tmpBondTypesBooleanArray[i] || tmpMoleculeBondTypesBooleanArray[i];
            }
        }
        System.out.println("SINGLE - DOUBLE - TRIPLE - QUADRUPLE - QUINTUPLE - SEXTUPLE - UNSET");
        System.out.println(Arrays.toString(tmpBondTypesBooleanArray));
        return tmpBondTypesBooleanArray;
    }

    public static boolean[] checkForBondTypes(IAtomContainer aMolecule) {
        Objects.requireNonNull(aMolecule, "aMolecule (instance of IAtomContainer) is null");
        //
        boolean[] tmpBondTypesBooleanArray = new boolean[7];
        for (IAtom tmpAtom : aMolecule.atoms()) {
            for (IBond tmpBond : tmpAtom.bonds()) {
                switch (tmpBond.getOrder()) {
                    case SINGLE -> {
                        tmpBondTypesBooleanArray[0] = true;
                    }
                    case DOUBLE -> {
                        tmpBondTypesBooleanArray[1] = true;
                    }
                    case TRIPLE -> {
                        tmpBondTypesBooleanArray[2] = true;
                    }
                    case QUADRUPLE -> {
                        tmpBondTypesBooleanArray[3] = true;
                    }
                    case QUINTUPLE -> {
                        tmpBondTypesBooleanArray[4] = true;
                    }
                    case SEXTUPLE -> {
                        tmpBondTypesBooleanArray[5] = true;
                    }
                    case UNSET -> {
                        tmpBondTypesBooleanArray[6] = true;
                    }
                    default -> {}   //TODO
                }
            }
        }
        return tmpBondTypesBooleanArray;
    }

    public static boolean hasCorrectValencies(IAtomContainer anAtomContainer) {     //TODO: switch "correct" to "known"?
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null");
        //
        ValenceListContainer tmpValenceListContainer = new ValenceListContainer();
        return ChemicalStructureCurationUtils.hasCorrectValencies(anAtomContainer, tmpValenceListContainer);
    }

    public static boolean hasCorrectValencies(IAtomContainer anAtomContainer, ValenceListContainer aValenceListContainer) {     //TODO: switch "correct" to "known"?
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null");
        Objects.requireNonNull(aValenceListContainer, "aValenceListContainer (instance of ValenceListContainer) is null");
        //
        for (IAtom tmpAtom :
                anAtomContainer.atoms()) {
            if (!ChemicalStructureCurationUtils.hasCorrectValencies(tmpAtom, aValenceListContainer)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasCorrectValencies(IAtom anAtom) {
        Objects.requireNonNull(anAtom, "anAtom (instance of IAtom) is null");
        //
        ValenceListContainer tmpValenceListContainer = new ValenceListContainer();
        return ChemicalStructureCurationUtils.hasCorrectValencies(anAtom, tmpValenceListContainer);
    }

    public static boolean hasCorrectValencies(IAtom anAtom, ValenceListContainer aValenceListContainer) {
        Objects.requireNonNull(anAtom, "anAtom (instance of IAtom) is null");
        Objects.requireNonNull(aValenceListContainer, "aValenceListContainer (instance of ValenceListContainer) is null");
        //
        int tmpAtomicNumber = anAtom.getAtomicNumber();
        if (tmpAtomicNumber == 0) {
            return false;
        }
        int tmpFormalCharge = anAtom.getFormalCharge();
        int tmpImplicitHydrogensCount = anAtom.getImplicitHydrogenCount();
        int tmpSigmaBondsCount = 0;
        int tmpPiBondsCount = 0;
        for (IBond tmpBond : anAtom.bonds()) {
            switch (tmpBond.getOrder()) {
                case UNSET -> {}    //TODO
                case SINGLE -> tmpSigmaBondsCount++;
                case DOUBLE -> {
                    tmpSigmaBondsCount++;
                    tmpPiBondsCount++;
                }
                case TRIPLE -> {
                    tmpSigmaBondsCount++;
                    tmpPiBondsCount += 2;
                }
                case QUADRUPLE -> {
                    tmpSigmaBondsCount++;
                    tmpPiBondsCount += 3;
                }
                case QUINTUPLE -> {
                    tmpSigmaBondsCount++;
                    tmpPiBondsCount += 4;
                }
                case SEXTUPLE -> {
                    tmpSigmaBondsCount++;
                    tmpPiBondsCount += 5;
                }
                default -> {}   //TODO
            }
        }
        tmpSigmaBondsCount += tmpImplicitHydrogensCount;
        //
        int tmpValenceListPointer = aValenceListContainer.VALENCE_LIST_POINTER_MATRIX[tmpAtomicNumber - 1][0];
        for (int i = 0; i < aValenceListContainer.VALENCE_LIST_POINTER_MATRIX[tmpAtomicNumber - 1][1]; i++) {
            if (anAtom.getFormalCharge() == aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)[1]
                    && tmpPiBondsCount == aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)[2]
                    && tmpSigmaBondsCount == aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)[3]
                    && tmpImplicitHydrogensCount <= aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)[4]) {
                return true;
            }
            /*if (tmpFormalCharge == aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)[1]) {
                if (tmpPiBondsCount == aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)[2]) {
                    if (tmpSigmaBondsCount == aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)[3]) {
                        if (tmpImplicitHydrogensCount <= aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)[4]) {
                            System.out.println("Atom [" + anAtom.getSymbol() + "] matches:\t" + Arrays.toString(aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)));
                            return true;
                        }
                    }
                }
            }*/
            //System.out.println("No match:\t" + Arrays.toString(aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)));
        }
        System.out.printf("Atom [" + anAtom.getSymbol() + "]: " + "[%d, %d, %d, %d, %d ]\n",anAtom.getAtomicNumber(), tmpFormalCharge, tmpPiBondsCount, tmpSigmaBondsCount, tmpImplicitHydrogensCount);
        //System.out.println("No match found:\treturning false");
        return false;
    }

    public static List<IAtom> getAtomsWithIncorrectValencies(IAtomContainerSet anAtomContainerSet) {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null");
        //
        ValenceListContainer tmpValenceListContainer = new ValenceListContainer();
        return ChemicalStructureCurationUtils.getAtomsWithIncorrectValencies(anAtomContainerSet, tmpValenceListContainer);
    }

    public static List<IAtom> getAtomsWithIncorrectValencies(IAtomContainerSet anAtomContainerSet, ValenceListContainer aValenceListContainer) {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null");
        Objects.requireNonNull(aValenceListContainer, "aValenceListContainer (instance of ValenceListContainer) is null");
        //
        ArrayList<IAtom> tmpAtomsWithIncorrectValenciesList = new ArrayList<>();
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            tmpAtomsWithIncorrectValenciesList.addAll(ChemicalStructureCurationUtils.getAtomsWithIncorrectValencies(tmpAtomContainer, aValenceListContainer));
        }
        return tmpAtomsWithIncorrectValenciesList;
    }

    public static List<IAtom> getAtomsWithIncorrectValencies(IAtomContainer anAtomContainer) {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null");
        //
        ValenceListContainer tmpValenceListContainer = new ValenceListContainer();
        return ChemicalStructureCurationUtils.getAtomsWithIncorrectValencies(anAtomContainer, tmpValenceListContainer);
    }

    public static List<IAtom> getAtomsWithIncorrectValencies(IAtomContainer anAtomContainer, ValenceListContainer aValenceListContainer) {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null");
        Objects.requireNonNull(aValenceListContainer, "aValenceListContainer (instance of ValenceListContainer) is null");
        //
        ArrayList<IAtom> tmpAtomsWithIncorrectValenciesList = new ArrayList<>();
        for (IAtom tmpAtom :
                anAtomContainer.atoms()) {
            int tmpAtomicNumber = tmpAtom.getAtomicNumber();
            if (tmpAtomicNumber == 0) {
                //TODO: what to do if the atomic number is not specified / 0
            }
            int tmpImplicitHydrogensCount = tmpAtom.getImplicitHydrogenCount();
            int tmpSigmaBondsCount = 0;
            int tmpPiBondsCount = 0;
            for (IBond tmpBond : tmpAtom.bonds()) {
                switch (tmpBond.getOrder()) {
                    case UNSET -> {}    //TODO
                    case SINGLE -> tmpSigmaBondsCount++;
                    case DOUBLE -> {
                        tmpSigmaBondsCount++;
                        tmpPiBondsCount++;
                    }
                    case TRIPLE -> {
                        tmpSigmaBondsCount++;
                        tmpPiBondsCount += 2;
                    }
                    case QUADRUPLE -> {
                        tmpSigmaBondsCount++;
                        tmpPiBondsCount += 3;
                    }
                    case QUINTUPLE -> {
                        tmpSigmaBondsCount++;
                        tmpPiBondsCount += 4;
                    }
                    case SEXTUPLE -> {
                        tmpSigmaBondsCount++;
                        tmpPiBondsCount += 5;
                    }
                    default -> {
                    }   //TODO
                }
            }
            tmpSigmaBondsCount += tmpImplicitHydrogensCount;
            //
            int tmpValenceListPointer = aValenceListContainer.VALENCE_LIST_POINTER_MATRIX[tmpAtomicNumber - 1][0];
            boolean tmpHasCorrectValency = false;
            for (int i = 0; i < aValenceListContainer.VALENCE_LIST_POINTER_MATRIX[tmpAtomicNumber - 1][1]; i++) {
                if (tmpAtom.getFormalCharge() == aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)[1]
                        && tmpPiBondsCount == aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)[2]
                        && tmpSigmaBondsCount == aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)[3]
                        && tmpImplicitHydrogensCount <= aValenceListContainer.VALENCE_LIST.get(tmpValenceListPointer + i)[4]) {
                    tmpHasCorrectValency = true;
                }
            }
            if (!tmpHasCorrectValency) {
                tmpAtomsWithIncorrectValenciesList.add(tmpAtom);
            }
        }
        return tmpAtomsWithIncorrectValenciesList;
    }

    public static List<int[]> getAllValenciesOfAtomType(int anAtomicNumber, List<IAtom> anAtomList) {
        //TODO: checks
        List<int[]> tmpListOfValencies = new ArrayList<>();
        boolean tmpAlreadyPresent;
        //
        for (IAtom tmpAtom :
                anAtomList) {
            if (tmpAtom.getAtomicNumber() == anAtomicNumber) {
                int tmpFormalCharge = tmpAtom.getFormalCharge();
                int tmpImplicitHydrogensCount = tmpAtom.getImplicitHydrogenCount();
                int tmpSigmaBondsCount = 0;
                int tmpPiBondsCount = 0;
                for (IBond tmpBond : tmpAtom.bonds()) {
                    switch (tmpBond.getOrder()) {
                        case UNSET -> {}    //TODO
                        case SINGLE -> tmpSigmaBondsCount++;
                        case DOUBLE -> {
                            tmpSigmaBondsCount++;
                            tmpPiBondsCount++;
                        }
                        case TRIPLE -> {
                            tmpSigmaBondsCount++;
                            tmpPiBondsCount += 2;
                        }
                        case QUADRUPLE -> {
                            tmpSigmaBondsCount++;
                            tmpPiBondsCount += 3;
                        }
                        case QUINTUPLE -> {
                            tmpSigmaBondsCount++;
                            tmpPiBondsCount += 4;
                        }
                        case SEXTUPLE -> {
                            tmpSigmaBondsCount++;
                            tmpPiBondsCount += 5;
                        }
                        default -> {
                        }   //TODO
                    }
                }
                tmpSigmaBondsCount += tmpImplicitHydrogensCount;
                //
                tmpAlreadyPresent = false;
                for (int[] tmpValence :
                        tmpListOfValencies) {
                    //System.out.println("Something");
                    if (tmpValence[1] == tmpFormalCharge
                            && tmpValence[2] == tmpPiBondsCount
                            && tmpValence[3] == tmpSigmaBondsCount
                            && tmpValence[4] == tmpImplicitHydrogensCount) {
                        tmpAlreadyPresent = true;
                        break;
                    }
                }
                if (!tmpAlreadyPresent) {
                    tmpListOfValencies.add(new int[]{anAtomicNumber, tmpFormalCharge, tmpPiBondsCount, tmpSigmaBondsCount, tmpImplicitHydrogensCount});
                }
            }
        }
        return tmpListOfValencies;
    }

    public static int[] countByAtomicNumber(List<IAtom> anAtomList) {
        //TODO: checks
        int[] tmpAtomicNumberFrequencyArray = new int[112]; //length of valency list?!
        for (int i = 0; i < 112; i++) {
            if (tmpAtomicNumberFrequencyArray[i] != 0) {    //TODO: remove!
                System.out.println("You need to initialize the integers, stupid!");
            }
        }
        for (IAtom tmpAtom :
                anAtomList) {
            if (tmpAtom.getAtomicNumber() == 0) {
                System.out.println("\nAn atomic number was zero!\n");
            }
            tmpAtomicNumberFrequencyArray[tmpAtom.getAtomicNumber() - 1] += 1;
        }
        return tmpAtomicNumberFrequencyArray;
    }

    public static void addImplicitHydrogens(IAtomContainer anAtomContainer) throws CDKException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null");
        //
        CDKHydrogenAdder tmpHydrogenAdder = CDKHydrogenAdder.getInstance(anAtomContainer.getBuilder()); //TODO: ?!?
        tmpHydrogenAdder.addImplicitHydrogens(anAtomContainer);
    }

    public static void addImplicitHydrogens(IAtomContainer anAtomContainer, IAtom anAtom) throws CDKException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null");
        Objects.requireNonNull(anAtom, "anAtom (instance of IAtom) is null");
        //
        CDKHydrogenAdder tmpHydrogenAdder = CDKHydrogenAdder.getInstance(anAtomContainer.getBuilder()); //TODO: ?!?
        tmpHydrogenAdder.addImplicitHydrogens(anAtomContainer, anAtom);
    }

}
