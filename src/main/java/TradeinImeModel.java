import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.MultiDataSet;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class TradeinImeModel {

    private static final Logger log = LoggerFactory.getLogger(TradeinImeModel.class);
    private static int batchSize = 15;

    public static void main(String[] args) throws Exception {

        MultiDataSetIterator validateMulIterator = new ImeDataSetIterator(batchSize,"validate");
        File locationToSave = new File("C:\\Users\\lambapi\\Documents\\MyJabberFiles\\bibin.thiruvoth@verizon.com\\peronal\\AIModel\\IME\\trained_ime_model.zip");
        ComputationGraph model = ModelSerializer.restoreComputationGraph(locationToSave);
        System.out.println("=====eval model=====validate==================");
        modelPredict(model, validateMulIterator);
    }


    public static void modelPredict(ComputationGraph model, MultiDataSetIterator iterator) {

        while (iterator.hasNext()) {
            MultiDataSet mds = iterator.next();
            INDArray[]  output = model.output(mds.getFeatures());
            int dataNum = batchSize > output[0].rows() ? output[0].rows() : batchSize;
            for (int dataIndex = 0;  dataIndex < dataNum; dataIndex ++) {
                String peLabel = "";
                INDArray preOutput = null;
                for (int digit = 0; digit < 15; digit ++) {
                    preOutput = output[digit].getRow(dataIndex);
                    peLabel += Nd4j.argMax(preOutput, 1).getInt(0);
                   // int label = Nd4j.argMax(preOutput.getRow(0), 1).getInt(0);
                }
                log.info("label {}",peLabel);
            }
        }
        iterator.reset();
    }
}

