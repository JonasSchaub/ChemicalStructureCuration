package de.unijena.cheminf.curation;

import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        //ChemicalStructureCurationUtils.checkMoleculeTest();
        /*try {
            new ValenceListContainer();
        } catch (IOException e) {
        }*/
        //
        SmilesParser tmpSmilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
        try {
            //IAtomContainer tmpMolecule = tmpSmilesParser.parseSmiles("OC=1C=C(OC)C=2C(O)=C3C(=C(C2C1)C=4C=5C=C(OC)C=C(OC)C5C(O)=C6C4C(O)C(OC6)C)C(O)C(OC3)C");
            //
            //Main.checkMoleculeTest(tmpMolecule);
            /*ValenceListContainer tmpValenceListContainer = new ValenceListContainer();
            ChemicalStructureCurationUtils.hasCorrectValencies(tmpMolecule, tmpValenceListContainer);
            Main.preprocessingTest(tmpMolecule);
            ChemicalStructureCurationUtils.hasCorrectValencies(tmpMolecule, tmpValenceListContainer);*/
            //
            IAtomContainerSet tmpAtomContainerSet = new AtomContainerSet();
            String[] tmpFilePaths = new String[]{
                    "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUTfirstSMILES.smi",    //with \t and 1
                    "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB_first200kSMILES.txt",    //with \t and 1
                    "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB.smi",    //with \t and 1
                    "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB_canonical_2022_12_16.smi",   //with " " and 0
                    "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB_first_400k_absoluteSMILES_2022_12_16.smi",   //with " " and 0
                    "C:\\Users\\Behr\\Documents\\MORTAR_Test_files\\COCONUT_DB_last_500k_absoluteSMILES_2022_12_16.smi",    //with " " and 0
            };
            File tmpSmilesFile = new File(tmpFilePaths[3]);
            FileReader tmpFileReader = new FileReader(tmpSmilesFile);
            BufferedReader tmpBufferedReader = new BufferedReader(tmpFileReader);
            String tmpLine;
            String tmpSmilesString;
            int tmpUnparseableSmilesCount = 0;
            while ((tmpLine = tmpBufferedReader.readLine()) != null) {
                //tmpSmilesString = tmpLine.split("\t", 2)[1];
                tmpSmilesString = tmpLine.split(" ", 2)[0];
                try {
                    IAtomContainer tmpAtomContainer = tmpSmilesParser.parseSmiles(tmpSmilesString);
                    tmpAtomContainerSet.addAtomContainer(tmpAtomContainer);
                } catch (InvalidSmilesException e) {
                    System.out.println(tmpLine);
                    tmpUnparseableSmilesCount++;
                }
            }
            System.out.println("tmpUnparseableSmilesCount = " + tmpUnparseableSmilesCount);
            tmpFileReader.close();
            tmpBufferedReader.close();
            //
            //ChemicalStructureCurationPipeline.checkForCorrectValencies(tmpAtomContainerSet);
            //ChemicalStructureCurationPipeline.preprocessAtomContainerSet(tmpAtomContainerSet);
            //ChemicalStructureCurationPipeline.checkForCorrectValencies(tmpAtomContainerSet);
            ChemicalStructureCurationUtils.checkForBondTypes(tmpAtomContainerSet);
        } /*catch (InvalidSmilesException e) {
            throw new RuntimeException(e);
        }*/ catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkMoleculeTest(IAtomContainer anAtomContainer) {
        ChemicalStructureCurationUtils.checkMolecule(anAtomContainer);
    }

    private static void preprocessingTest(IAtomContainer anAtomContainer) {
        System.out.println("AtomCount:\t" + anAtomContainer.getAtomCount());
        System.out.println("BondCount:\t" + anAtomContainer.getBondCount());
        int[] tmpAtomsImplicitHydrogensCountsBefore = new int[anAtomContainer.getAtomCount()];
        int tmpIterator = 0;
        for (IAtom tmpAtom :
                anAtomContainer.atoms()) {
            tmpAtomsImplicitHydrogensCountsBefore[tmpIterator] = tmpAtom.getImplicitHydrogenCount();
            tmpIterator++;
        }
        System.out.println(Arrays.toString(tmpAtomsImplicitHydrogensCountsBefore));
        //
        ChemicalStructureCurationPipeline.preprocessAtomContainer(anAtomContainer);
        //
        int[] tmpAtomsImplicitHydrogensCountsAfter = new int[anAtomContainer.getAtomCount()];
        tmpIterator = 0;
        for (IAtom tmpAtom :
                anAtomContainer.atoms()) {
            tmpAtomsImplicitHydrogensCountsAfter[tmpIterator] = tmpAtom.getImplicitHydrogenCount();
            tmpIterator++;
        }
        System.out.println(Arrays.toString(tmpAtomsImplicitHydrogensCountsAfter));
        System.out.println("AtomCount:\t" + anAtomContainer.getAtomCount());
        System.out.println("BondCount:\t" + anAtomContainer.getBondCount());
    }

}
