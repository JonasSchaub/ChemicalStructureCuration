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

package de.unijena.cheminf.curation.enums;

/**
 * The enum defines the errors (issues with structures) encountered in the processing of structures.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public enum ErrorCodes {

    /**
     * The IAtomContainer instance could not be cloned.
     */
    CLONE_ERROR,

    /**
     * The IAtomContainer instance of the structure is null.
     */
    ATOM_CONTAINER_NULL_ERROR,

    /**
     * An IAtom instance contained by the atom container of the structure is null.
     */
    ATOM_NULL_ERROR,

    /**
     * An IBond instance contained by the atom container of the structure is null.
     */
    BOND_NULL_ERROR,

    /**
     * The order of an IBond instance is UNSET.
     */
    BOND_ORDER_UNSET_ERROR,

    /**
     * The order of an IBond instance is null.
     */
    BOND_ORDER_NULL_ERROR,

    /**
     * The order on an IBond instance is of an unknown type.
     */
    BOND_ORDER_UNKNOWN_ERROR,

    /**
     * An atom has no atomic number; the atomic number is null.
     */
    ATOMIC_NUMBER_NULL_ERROR,

    /**
     * The IAtomContainer instance of the structure contains no atoms.
     */
    NO_ATOMS_ERROR,

    /**
     * A threshold value is considered as illegal; this might be a fatal error.
     */
    ILLEGAL_THRESHOLD_VALUE_ERROR,

    /**
     * An invalid atomic number was detected.
     */
    INVALID_ATOMIC_NUMBER_ERROR,

    /**
     * The given MassComputationFlavours enum constant is null.
     */
    FLAVOUR_NULL_ERROR,

    /**
     * An expected atom container property is unset.
     */
    MISSING_ATOM_CONTAINER_PROPERTY,

    /**
     * The external ID property is unset.
     */
    UNSET_EXTERNAL_ID_PROPERTY,

    /**
     * An unexpected (probably fatal) exception was encountered. For more information the log / log-file shall be
     * visited.
     */
    UNEXPECTED_EXCEPTION_ERROR

}
