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

package de.unijena.cheminf.valenceListContainers;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * TODO
 * TODO: link / reference!
 */
public class PubChemValenceListContainer extends BaseValenceListContainer {

    /*
    TODO: implement tests
    TODO: remove logger?
    TODO: check the doc comments
    TODO: class description
    TODO: add link / reference to the source of the PubChem valence list file
    //
    TODO: implement filter
     */

    /** TODO: unnecessary? / remove?
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(PubChemValenceListContainer.class.getName());

    /**
     * String of the file path of the PubChem valence list text file in this project.
     * TODO: link / reference!
     */
    private static final String VALENCE_LIST_FILE_PATH = "src/main/resources/de/unijena/cheminf/curation/PubChem_Valence_list.txt";

    /**
     * Integer value of the highest atomic number contained in the "PubChem_Valence_list.txt".
     * @see #VALENCE_LIST_FILE_PATH
     */
    protected static final int HIGHEST_ATOMIC_NUMBER_IN_LIST = 112;

    /**
     * Integer value of the number of rows contained by the PubChem valence list text file; 981 lines of content + one
     * headline.
     * @see #VALENCE_LIST_FILE_PATH
     */
    protected static final int NUMBER_OF_ROWS_IN_FILE = 982;

    /**
     * Instance of ValenceListContainer; it is initialized at the first call of {@link PubChemValenceListContainer#getInstance()}.
     */
    protected static PubChemValenceListContainer instance = null;

    /**
     * Private constructor. Loads the text file "PubChem_Valence_list.txt", a list of valid valences and configurations
     * of atoms with respect to atomic number (column 1), charge (column 2), number of π bonds (column 3), number of σ
     * bonds (column 4) and the maximum number of implicit hydrogens (column 5).
     * TODO: link / reference!
     *
     * @throws IOException if a problem occurs loading the data from the valence list text file associated with this
     * class, e.g. the file cannot be found at the expected file path or the file differs from the expected format
     */
    private PubChemValenceListContainer() throws IOException {
        super(
                PubChemValenceListContainer.VALENCE_LIST_FILE_PATH,
                PubChemValenceListContainer.NUMBER_OF_ROWS_IN_FILE,
                PubChemValenceListContainer.HIGHEST_ATOMIC_NUMBER_IN_LIST
        );
    }

    /**
     * Returns an IValenceListContainer instance containing the data loaded from the "PubChem_Valence_list.txt"
     * text file, a list of valid valences and configurations of atoms with respect to atomic number (column 1), charge
     * (column 2), number of π bonds (column 3), number of σ bonds (column 4) and the maximum number of implicit
     * hydrogens (column 5).
     * <p>
     * An instance is only generated once.
     *
     * @return ValenceListContainer
     * @throws IOException if no instance exists so far and a problem occurs generating one by loading the data from
     * the valence list text file associated with this class (e.g. the file cannot be found at the expected file path
     * or the file differs from the expected format)
     */
    public static PubChemValenceListContainer getInstance() throws IOException {
        if (PubChemValenceListContainer.instance == null) {
            PubChemValenceListContainer.instance = new PubChemValenceListContainer();
        }
        return PubChemValenceListContainer.instance;
    }

}
