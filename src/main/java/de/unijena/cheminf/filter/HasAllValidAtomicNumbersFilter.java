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

package de.unijena.cheminf.filter;

import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.Objects;

/**
 * Has all valid atomic numbers filter to filter atom containers with all valid atomic numbers out of a set of atom
 * containers.
 */
public class HasAllValidAtomicNumbersFilter extends Filter {

    /**
     * Boolean value whether to consider the wildcard atomic number zero as a valid atomic number.
     */
    protected final boolean wildcardAtomicNumberIsValid;

    /**
     * Constructor of the HasAllValidAtomicNumbersFilter class. The wildcard atomic number zero may or may not be
     * considered as valid atomic number.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether the wildcard atomic number zero should be considered
     *                                     as a valid atomic number
     */
    public HasAllValidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid) {
        this.wildcardAtomicNumberIsValid = aWildcardAtomicNumberIsValid;
    }

    /**
     * {@inheritDoc}
     * The class field wildcardAtomicNumberIsValid decides whether zero is considered as a valid atomic number. An
     * atomic number being null is considered as an invalid atomic number.
     *
     * @throws NullPointerException if the given IAtomContainer instance or an IAtom instance contained by it is null
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        //
        try {
            return !FilterUtils.hasAllValidAtomicNumbers(anAtomContainer, this.wildcardAtomicNumberIsValid);
        } catch (IllegalArgumentException anIllegalArgumentException) {
            //TODO: log / report the exception
            return true;
        }
    }

    /**
     * Returns whether the wildcard atomic number zero is considered as valid atomic number.
     *
     * @return Boolean value
     */
    public boolean isWildcardAtomicNumberIsValid() {
        return this.wildcardAtomicNumberIsValid;
    }

}
