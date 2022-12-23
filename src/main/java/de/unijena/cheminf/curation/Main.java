package de.unijena.cheminf.curation;

import org.openscience.cdk.config.Elements;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            Importer tmpImporter = new Importer(3);
            IAtomContainerSet tmpAtomContainerSet = tmpImporter.importHoleDataSet();
            System.out.println("UnparseableSmilesCount: " + tmpImporter.getUnparseableSmilesCount());
            tmpImporter.closeBufferedReader();
            //
            //ChemicalStructureCurationPipeline.checkForCorrectValencies(tmpAtomContainerSet);
            //ChemicalStructureCurationPipeline.preprocessAtomContainerSet(tmpAtomContainerSet);
            //ChemicalStructureCurationPipeline.checkForCorrectValencies(tmpAtomContainerSet, tmpImporter.getImportedIdentifiersList());
            //ChemicalStructureCurationUtils.checkForBondTypes(tmpAtomContainerSet);
            //Main.countByAtomicNumberTest(tmpAtomContainerSet);
            //Main.getAllValenciesOfAtomTypeTest(tmpAtomContainerSet);
        } catch (Exception e) {
            System.out.println("An exception occurred!");
            System.out.println(e.toString());
            //throw e;
        } /*catch (InvalidSmilesException e) {
            throw new RuntimeException(e);
        }*/
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

    private static void countByAtomicNumberTest(IAtomContainerSet anAtomContainerSet) {
        //list of all atoms with incorrect valencies of all structures in the COCONUT DB
        List<IAtom> tmpIncorrectValenciesAtomsList = ChemicalStructureCurationUtils.getAtomsWithIncorrectValencies(anAtomContainerSet);
        int[] tmpAtomicNumberFrequencyArray1 = ChemicalStructureCurationUtils.countByAtomicNumber(tmpIncorrectValenciesAtomsList);
        //list of all atoms of all structures in the COCONUT DB
        List<IAtom> tmpAllAtomsList = Main.getListOfAllAtoms(anAtomContainerSet);
        int[] tmpAtomicNumberFrequencyArray2 = ChemicalStructureCurationUtils.countByAtomicNumber(tmpAllAtomsList);
        //
        /*
         List displaying the share that each atom type has of the total amount of atoms with incorrect valencies sorted
         by the frequency of their occurrence
         */
        System.out.println("Percentage of total number of atoms with incorrect valencies:");
        List<Integer> tmpSortedList = new ArrayList<>();
        int tmpMaxValueArrayIndex = 0;
        sortingLoop: {
            for (int i = 0; i < 112; i++) {
                for (int j = 0; j < 112; j++) {
                    if (!tmpSortedList.contains(j)) {
                        tmpMaxValueArrayIndex = j;
                        break;
                    }
                }
                for (int j = 0; j < 112; j++) {
                    if (tmpAtomicNumberFrequencyArray1[j] > tmpAtomicNumberFrequencyArray1[tmpMaxValueArrayIndex] && !tmpSortedList.contains(j)) {
                        if (tmpAtomicNumberFrequencyArray1[j] == 0) {
                            break sortingLoop;
                        }
                        tmpMaxValueArrayIndex = j;
                    }
                }
                if (tmpAtomicNumberFrequencyArray1[tmpMaxValueArrayIndex] == 0) {
                    break sortingLoop;
                }
                tmpSortedList.add(tmpMaxValueArrayIndex);
            }
        }
        int tmpValue;
        for (int tmpIndex :
                tmpSortedList) {
            tmpValue = tmpAtomicNumberFrequencyArray1[tmpIndex];
            float tmpPercentage = (float) tmpValue / tmpIncorrectValenciesAtomsList.size() * 100;
            System.out.printf("\t%s\t%5.1f", Elements.ofNumber(tmpIndex + 1).symbol(), tmpPercentage);
            System.out.print("%\t");
            System.out.printf("%5d / %d\n", tmpValue, tmpIncorrectValenciesAtomsList.size());
        }
        //
        /*
         List of all atom types present in the original data set listed with the frequency of them being associated
         with incorrect valencies sorted by their atomic number
         */
        System.out.println("Frequency of each atomic number:");
        int tmpValue1;
        int tmpValue2;
        float tmpPercentage;
        for (int i = 0; i < 112; i++) {
            tmpValue1 = tmpAtomicNumberFrequencyArray1[i];
            tmpValue2 = tmpAtomicNumberFrequencyArray2[i];
            tmpPercentage = (float) tmpValue1 / tmpValue2 * 100;
            if (tmpValue1 > 0 || tmpValue2 > 0) {
                System.out.printf("\t%s\t%5.1f%5d / %8d\n", Elements.ofNumber(i + 1).symbol(), tmpPercentage);
                System.out.print("%\t");
                System.out.printf("%5d / %8d\n", tmpValue1, tmpValue2);
            }
        }
    }

    private static void getAllValenciesOfAtomTypeTest(IAtomContainerSet anAtomContainerSet) {
        List<IAtom> tmpIncorrectValenciesAtomsList = ChemicalStructureCurationUtils.getAtomsWithIncorrectValencies(anAtomContainerSet);
        System.out.println("Atoms with incorrect valencies: " + tmpIncorrectValenciesAtomsList.size());
        int tmpAtomicNumberToExtract;
        List<int[]> tmpListOfValencies;
        //
        tmpAtomicNumberToExtract = 6;
        tmpListOfValencies = ChemicalStructureCurationUtils.getAllValenciesOfAtomType(tmpIncorrectValenciesAtomsList, tmpAtomicNumberToExtract);
        for (int[] tmpValenceArray :
                tmpListOfValencies) {
            System.out.println(Arrays.toString(tmpValenceArray));
        }
        System.out.println();
        //
        tmpAtomicNumberToExtract = 7;
        tmpListOfValencies = ChemicalStructureCurationUtils.getAllValenciesOfAtomType(tmpIncorrectValenciesAtomsList, tmpAtomicNumberToExtract);
        for (int[] tmpValenceArray :
                tmpListOfValencies) {
            System.out.println(Arrays.toString(tmpValenceArray));
        }

        //List<IAtom> tmpAllAtomsList = Main.getListOfAllAtoms(anAtomContainerSet);
        //tmpListOfValencies = ChemicalStructureCurationUtils.getAllValenciesOfAtomType(tmpAtomicNumberToExtract, tmpIncorrectValenciesAtomsList);
    }

    private static List<IAtom> getListOfAllAtoms(IAtomContainerSet anAtomContainerSet) {
        List<IAtom> tmpAllAtomsList = new ArrayList<>();
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            for (IAtom tmpAtom :
                    tmpAtomContainer.atoms()) {
                tmpAllAtomsList.add(tmpAtom);
            }
        }
        return tmpAllAtomsList;
    }

}
