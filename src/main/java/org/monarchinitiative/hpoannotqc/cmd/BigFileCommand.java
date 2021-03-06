package org.monarchinitiative.hpoannotqc.cmd;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.monarchinitiative.hpoannotqc.bigfile.BigFileWriter;
import org.monarchinitiative.hpoannotqc.exception.HPOException;
import org.monarchinitiative.hpoannotqc.io.V2SmallFileIngestor;
import org.monarchinitiative.hpoannotqc.orphanet.OrphanetDisorder;
import org.monarchinitiative.hpoannotqc.orphanet.OrphanetXML2HpoDiseaseModelParser;
import org.monarchinitiative.hpoannotqc.smallfile.V2SmallFile;
import org.monarchinitiative.phenol.formats.hpo.HpoOntology;
import org.monarchinitiative.phenol.io.obo.hpo.HpoOboParser;

import java.io.*;
import java.util.List;


/**
 * This class coordinates the output of the {@code phenotype_annotation.tab} file or variations thereof with the
 * new an old format. It combines the V2 small files with the Orphanet data (note this has to be downloaded first
 * with the {@link DownloadCommand}).
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class BigFileCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    /** Path to directory where we will write the new "small files". */
    private final String v2smallFileDirectory;
    /** Path to the {@code hp.obo} file. */
    private final String hpOboPath;
    /** Path to the downloaded Orphanet XML file */
    private final String orphanetXMLpath;
    private HpoOntology ontology;
    /** Should usually be phenotype.hpoa, may also include path */
    private final String outputFilePath;
    /** path to the omit-list.txt file, which is located with the small files in the same directory */
    private final String omitPath;

    /**
     * Command to create the V2 bigfile from the various small files
     * @param hpopath path to hp.obo
     * @param dir directory with the small files
     * @param orphaXML path to the Orphanet XML file
     * @param outpath path to the new output file (phenotype.hpoa)
     */
    public BigFileCommand(String hpopath, String dir, String orphaXML, String outpath) {
        hpOboPath=hpopath;
        v2smallFileDirectory =dir;
        orphanetXMLpath=orphaXML;
        outputFilePath=outpath;
        omitPath=String.format("%s%s%s",v2smallFileDirectory,File.separator,"omit-list.txt");
    }

    @Override
    public void execute() {
        try {
            logger.trace("Parsing hp.obo ...");
            HpoOboParser hpoOboParser = new HpoOboParser(new File(hpOboPath));
            this.ontology = hpoOboParser.parse();
        } catch (IOException e) {
            logger.fatal("Unable to parse hp.obo file at " + hpOboPath);
            logger.fatal("Unable to recover, stopping execution");
            return;
        }

        try {
            V2SmallFileIngestor v2ingestor = new V2SmallFileIngestor(v2smallFileDirectory,omitPath,ontology);
            List<V2SmallFile> v2entries = v2ingestor.getV2SmallFileEntries();
            BigFileWriter writer = new BigFileWriter(ontology, v2entries, outputFilePath);

            OrphanetXML2HpoDiseaseModelParser parser = new OrphanetXML2HpoDiseaseModelParser(this.orphanetXMLpath, this.ontology);
            List<OrphanetDisorder> orphanetDisorders = parser.getDisorders();
            debugPrintOrphanetDisorders(orphanetDisorders);
            /// output the V2 version of the big file
            writer.initializeV2filehandle();
            writer.setNumberOfDiseasesForHeader(orphanetDisorders.size());
            writer.setOntologyMetadata(ontology.getMetaInfo());
            writer.outputBigFileV2();
            writer.appendOrphanetV2(orphanetDisorders);
            writer.closeFileHandle();
        } catch (IOException | HPOException e) {
            logger.fatal("[ERROR] Could not output V2 big file",e);
        }
    }



   private void debugPrintOrphanetDisorders(List<OrphanetDisorder> orphanetDisorders){
        int n_annot=0;
        for (OrphanetDisorder od : orphanetDisorders) {
            n_annot += od.getHpoIds().size();
        }
        System.out.println(String.format("We extracted %d orphanet disorders with %d annotations ",orphanetDisorders.size(),n_annot));
   }

}
