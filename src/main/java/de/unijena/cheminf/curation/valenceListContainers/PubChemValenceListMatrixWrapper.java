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

package de.unijena.cheminf.curation.valenceListContainers;

import java.io.IOException;

/**
 * An extension of the {@link ValenceListMatrixWrapper} that specifically imports the PubChem valence list. This class
 * follows the Singleton pattern: To receive an instance of the class, the method {@link #getInstance()} is to be used.
 * An instance is only generated once during the class loading.
 * <br>
 * The valence list was received from the paper describing the PubChem standardization approaches. See:
 * <a href="https://doi.org/10.1186/s13321-018-0293-8"> "Hähnke, V.D., Kim, S., Bolton, E.E. PubChem chemical structure
 * standardization. J Cheminform 10, 36 (2018). https://doi.org/10.1186/s13321-018-0293-8" </a>.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 * @see #getInstance()
 */
public class PubChemValenceListMatrixWrapper extends ValenceListMatrixWrapper {

    /**
     * String of the path to the PubChem valence list text file.
     */
    private static final String VALENCE_LIST_FILE_PATH = "src/main/resources/de/unijena/cheminf/curation/" +
            "PubChem_Valence_list.txt";

    /**
     * Integer value of the number of lines contained by the PubChem valence list text file; 981 lines of content plus
     * one headline.
     */
    protected static final int NUMBER_OF_LINES_IN_FILE = 982;

    /**
     * Instance of ValenceListContainer; it is initialized in the static block.
     */
    protected static PubChemValenceListMatrixWrapper instance;

    static {
        /* initializes the valence list matrix wrapper instance */
        try {
            PubChemValenceListMatrixWrapper.instance = new PubChemValenceListMatrixWrapper();
        } catch (IOException anIOException) {
            // e.g. the file cannot be found at the expected file path
            // or the file differs from the expected format
            throw new RuntimeException(anIOException);
        }
    }

    /**
     * Private constructor; calls the super constructor passing the file path of the "PubChem_Valence_list.txt" file and
     * the number of lines in the file. For info on the source and a description of the file, see the class description.
     *
     * @throws IOException if a problem occurs loading the data from the valence list text file associated with this
     * class, e.g. the file cannot be found at the expected file path or the file differs from the expected format
     * @see #VALENCE_LIST_FILE_PATH
     */
    private PubChemValenceListMatrixWrapper() throws IOException {
        super(PubChemValenceListMatrixWrapper.VALENCE_LIST_FILE_PATH,
                PubChemValenceListMatrixWrapper.NUMBER_OF_LINES_IN_FILE);
    }

    /**
     * Returns a PubChemValenceListMatrixWrapper instance containing the data loaded from the "PubChem_Valence_list.txt"
     * text file wrapped into two matrices. The file contains a list of valid valences and configurations of atoms with
     * respect to atomic number, charge, number of π bonds, number of σ bonds and maximum number of implicit hydrogens.
     * <p>
     * An instance is only generated once (as the class is loaded).
     *
     * @return ValenceListContainer
     * @see PubChemValenceListMatrixWrapper
     */
    public static PubChemValenceListMatrixWrapper getInstance() {
        if (PubChemValenceListMatrixWrapper.instance == null) {
            throw new IllegalStateException("The PubChemValenceListContainer instance has not been initialized.");
        }
        return PubChemValenceListMatrixWrapper.instance;
    }

}
