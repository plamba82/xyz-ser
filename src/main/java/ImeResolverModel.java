import org.apache.commons.io.IOUtils;
import org.bytedeco.javacpp.opencv_core;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.concurrency.AffinityManager;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.MultiDataSet;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImeResolverModel extends NativeImageLoader {

    private static final Logger log = LoggerFactory.getLogger(ImeResolverModel.class);
    private static int batchSize = 15;
    File modelLocation = new File("C:\\Users\\lambapi\\Documents\\MyJabberFiles\\bibin.thiruvoth@verizon.com\\peronal\\AIModel\\SmartPhones\\trained_ime_model.zip");


    public void predict(InputStream is)throws Exception {

        ComputationGraph model = ModelSerializer.restoreComputationGraph(modelLocation);
        MultiDataSet mds = convertDataSetFromByte(is);
        INDArray[]  output = model.output(mds.getFeatures());
        int dataNum = batchSize > output[0].rows() ? output[0].rows() : batchSize;
        for (int dataIndex = 0;  dataIndex < dataNum; dataIndex ++) {
            String outputLabel = "";
            INDArray preOutput = null;
            for (int digit = 0; digit < 15; digit ++) {
                preOutput = output[digit].getRow(dataIndex);
                outputLabel += Nd4j.argMax(preOutput, 1).getInt(0);
            }
            System.out.println("The Output is -->"+outputLabel);
        }
    }

    public org.nd4j.linalg.dataset.MultiDataSet convertDataSetFromByte(InputStream is) throws Exception {

        INDArray feature = asMatrix(is);
        INDArray[] features = new INDArray[]{feature};
        INDArray[] labels = new INDArray[15];
        Nd4j.getAffinityManager().ensureLocation(feature, AffinityManager.Location.DEVICE);

        return new org.nd4j.linalg.dataset.MultiDataSet(features, labels, null, null);
    }
}

