import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Random;


public class SmartPhoneModel {
    private static Logger log = LoggerFactory.getLogger(SmartPhoneModel.class);

    public static void main(String[] args) throws Exception {

        int height = 400;
        int width = 400;
        int channels = 1;
        int rngseed = 123;
        Random randNumGen = new Random(rngseed);
        int batchSize = 128;
        int outputNum = 11;



        File testData = new File("C:\\Users\\lambapi\\Documents\\MyJabberFiles\\bibin.thiruvoth@verizon.com\\peronal\\AIModel\\SmartPhones\\testing");

        FileSplit test = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();

        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, labelMaker);

        log.info("LOAD TRAINED MODEL");
        File modelLocation = new File("C:\\Users\\lambapi\\Documents\\MyJabberFiles\\bibin.thiruvoth@verizon.com\\peronal\\AIModel\\SmartPhones\\trained_tradein_model.zip");

        if (modelLocation.exists()) {
            log.info("Saved Model Found!");
        } else {
            log.error("File not found!");
            System.exit(0);
        }

        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(modelLocation);
        recordReader.initialize(test);
        DataSetIterator testIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, outputNum);
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.fit(testIter);
        testIter.setPreProcessor(scaler);

        // Create Eval object with 2 possible classes
        /*Evaluation eval = new Evaluation(2);
        while (testIter.hasNext()) {
            DataSet next = testIter.next();
            INDArray output = model.output(next.getFeatures());
            eval.eval(next.getLabels(), output);
        }
        log.info(eval.stats());*/
        while (testIter.hasNext()) {
            DataSet next = testIter.next();
            INDArray output = model.output(next.getFeatures());
            int label = Nd4j.argMax(output.getRow(0), 1).getInt(0);
            System.out.println("Hey my number is !!!"+label);

        }
    }

}
