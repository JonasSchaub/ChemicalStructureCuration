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
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

/**
 *
 */
public class ChemicalStructureCurationUtilsTest {

    ChemicalStructureCurationUtilsTest() {
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

    @Test
    private void test() {   //TODO: remove
        System.out.println("It works!! - 1");
        Assertions.assertEquals(true, true);
    }

    @Test
    private void test2() {  //TODO: remove
        System.out.println("It works!! - 2");
    }

}