package org.monarchinitiative.hpoannotqc.bigfile;

import com.google.common.collect.ImmutableList;
import org.junit.BeforeClass;
import org.junit.Test;
import org.monarchinitiative.hpoannotqc.exception.HPOException;
import org.monarchinitiative.hpoannotqc.smallfile.V2SmallFile;
import org.monarchinitiative.hpoannotqc.smallfile.V2SmallFileEntry;
import org.monarchinitiative.phenol.formats.hpo.HpoOntology;
import org.monarchinitiative.phenol.io.obo.hpo.HpoOboParser;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class BigFileWriterTest {



    private static V2SmallFileEntry entry;
    private static HpoOntology ontology;

    @BeforeClass
    public static void init() throws IOException {
        // set up ontology
        ClassLoader classLoader = BigFileWriterTest.class.getClassLoader();
        String hpOboPath =classLoader.getResource("hp.obo").getFile();
        Objects.requireNonNull(hpOboPath);
        HpoOboParser oboparser = new HpoOboParser(new File(hpOboPath));
        ontology = oboparser.parse();
        // Make a typical entry. All other fields are emtpy.
        String diseaseID="OMIM:154700";
        String diseaseName="MARFAN SYNDROME";
        TermId hpoId= TermId.constructWithPrefix("HP:0004872");
        String hpoName="Incisional hernia";
        String evidenceCode="IEA";
        String pub="OMIM:154700";
        String biocuration="HPO:skoehler[2015-07-26]";
        String onsetModifier="HP:0040283";
        V2SmallFileEntry.Builder builder=new V2SmallFileEntry.Builder(diseaseID,diseaseName,hpoId,hpoName,evidenceCode,pub,biocuration).ageOfOnsetId(onsetModifier);
        entry = builder.build();
    }



    /**
     * Test emitting a line of the V2 (2018-?) big file from a V2 small file line.
     */
    @Test
    public void testV2line() throws HPOException {
        String [] v1bigFileFields = {
                "OMIM:154700",//DiseaseID
                "MARFAN SYNDROME", // Name
                "",//Qualifier
                "HP:0004872", // HPO_ID,
                "OMIM:154700",//DB_Reference
                "IEA", // Evidence_Code
                "HP:0040283",//Onset
                "", // Frequency
                "", // Sex
                "",//Modifier
                "P",// Aspect
                "HPO:skoehler[2015-07-26]", // biocuration

        };
        String expected= Arrays.stream(v1bigFileFields).collect(Collectors.joining("\t"));
        List<V2SmallFile> emptyList = ImmutableList.of(); // needed for testing.
        V2BigFile v1b = new V2BigFile(ontology, emptyList);
        String line = v1b.transformEntry2BigFileLineV2(entry);
        assertEquals(expected,line);
    }


    @Test
    public void testV2Header() {
        String expected="DatabaseID\tDiseaseName\tQualifier\tHPO_ID\tReference\tEvidence\tOnset\tFrequency\tSex\tModifier\tAspect\tBiocuration";
        assertEquals(expected,V2BigFile.getHeaderV2());
    }

}