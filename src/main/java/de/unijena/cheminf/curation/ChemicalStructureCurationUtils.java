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

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.tools.CDKHydrogenAdder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 */
public class ChemicalStructureCurationUtils {

    public static void checkMolecule(IAtomContainer aMolecule) {
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
            System.out.println(tmpIterator + " - " + tmpAtom.getSymbol());
            System.out.println("\ttmpFormalCharge = " + tmpFormalCharge);
            System.out.println("\ttmpImplicitHydrogenCount = " + tmpImplicitHydrogenCount);
            System.out.println("\ttmpBondCount = " + tmpBondCount);
            System.out.println("\ttmpSigmaBondsCount = " + tmpSigmaBondsCount);
            System.out.println("\ttmpPiBondsCount = " + tmpPiBondsCount);
            tmpIterator++;
        }
    }

    public static boolean hasCorrectValencies(IAtomContainer anAtomContainer, ValenceListContainer aValenceListContainer) {     //TODO: switch correct to known?
        for (IAtom tmpAtom :
                anAtomContainer.atoms()) {
            if (!ChemicalStructureCurationUtils.hasCorrectValencies(tmpAtom, aValenceListContainer)) {
                //System.out.println("returning false");
                return false;
            }
        }
        //System.out.println("returning true");
        return true;
    }

    public static boolean hasCorrectValencies(IAtom anAtom, ValenceListContainer aValenceListContainer) {
        int tmpAtomicNumber = anAtom.getAtomicNumber();
        if (tmpAtomicNumber == 0) {
            return false;
        }
        int tmpFormalCharge = anAtom.getFormalCharge();
        int tmpImplicitHydrogensCount = anAtom.getImplicitHydrogenCount();
        int tmpSigmaBondsCount = tmpImplicitHydrogensCount;
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
        //System.out.println("Atom [" + anAtom.getSymbol() + "]: returning false");
        System.out.println("No match found:\treturning false");
        return false;
    }

    public static void addImplicitHydrogens(IAtomContainer anAtomContainer) throws CDKException {
        CDKHydrogenAdder tmpHydrogenAdder = CDKHydrogenAdder.getInstance(anAtomContainer.getBuilder()); //TODO: ?!?
        tmpHydrogenAdder.addImplicitHydrogens(anAtomContainer);
    }

    public static void addImplicitHydrogens(IAtomContainer anAtomContainer, IAtom anAtom) throws CDKException {
        CDKHydrogenAdder tmpHydrogenAdder = CDKHydrogenAdder.getInstance(anAtomContainer.getBuilder()); //TODO: ?!?
        tmpHydrogenAdder.addImplicitHydrogens(anAtomContainer, anAtom);
    }

}
