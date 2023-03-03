/*
 * MIT License
 *
 * Copyright (c) 2023 Samuel Behr, Felix Baensch, Jonas Schaub, Christoph Steinbeck, and Achim Zielesny
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

package de.unijena.cheminf;

import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import java.util.Objects;

/**
 *
 */
public class TestUtils {

    /**
     * Parses a set of given SMILES strings.
     *
     * @param aSmilesStrings Strings containing SMILES codes
     * @return Set of AtomContainers resulting out of parsing the SMILES strings
     * @throws NullPointerException if one of the given strings is null
     * @throws InvalidSmilesException if one of the given SMILES strings is not parsable
     */
    public static IAtomContainerSet parseSmilesStrings(String... aSmilesStrings) throws NullPointerException, InvalidSmilesException {
        for (String tmpString : aSmilesStrings) {
            Objects.requireNonNull(tmpString, "element of aSmilesStrings (instance of String) is null");
        }
        SmilesParser tmpSmilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
        IAtomContainer tmpAtomContainer;
        IAtomContainerSet tmpACSet = new AtomContainerSet();
        for (String tmpSmilesString :
                aSmilesStrings) {
            tmpAtomContainer = tmpSmilesParser.parseSmiles(tmpSmilesString);
            tmpACSet.addAtomContainer(tmpAtomContainer);
        }
        return tmpACSet;
    }

    /**
     * Parses a given SMILES string.
     *
     * @param aSmilesString String containing a SMILES code
     * @return AtomContainer of the parsed SMILES string
     * @throws NullPointerException if the given string is null
     * @throws InvalidSmilesException if the given SMILES string can not be parsed
     */
    public static IAtomContainer parseSmilesString(String aSmilesString) throws NullPointerException, InvalidSmilesException {
        Objects.requireNonNull(aSmilesString, "aSmilesString (instance of String) is null");
        SmilesParser tmpSmilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
        return tmpSmilesParser.parseSmiles(aSmilesString);
    }

}
