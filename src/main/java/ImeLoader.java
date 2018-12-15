import org.apache.commons.io.FileUtils;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.transform.ImageTransform;
import org.nd4j.linalg.api.concurrency.AffinityManager;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.MultiDataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Bibin kt
 */
public class ImeLoader extends NativeImageLoader implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(ImeLoader.class);

    private static int height = 60;
    private static int width = 450;
    private static int channels = 1;
    private File fullDir = null;
    private Iterator<File> fileIterator;
    private int numExample = 0;


    public ImeLoader(String dataSetType) {
        this( height, width, channels, null, dataSetType);
    }
    public ImeLoader(ImageTransform imageTransform, String dataSetType)  {
        this( height, width, channels, imageTransform, dataSetType );
    }
    public ImeLoader(int height, int width, int channels, ImageTransform imageTransform, String dataSetType) {
        super(height, width, channels, imageTransform);
        this.height = height;
        this.width = width;
        this.channels = channels;
//        try {
//            this.fullDir = fullDir != null && fullDir.exists() ? fullDir : new ClassPathResource("/IME").getFile();
//        } catch (Exception e) {
//            throw new RuntimeException( e );
//        }
        this.fullDir = new File("C:\\Users\\lambapi\\Documents\\MyJabberFiles\\bibin.thiruvoth@verizon.com\\peronal\\AIModel\\IME", dataSetType);
        load();
    }

    protected void load() {
        try {
            List<File> dataFiles = (List<File>) FileUtils.listFiles(fullDir, new String[]{"png"}, true );
            Collections.shuffle(dataFiles);
            fileIterator = dataFiles.iterator();
            numExample = dataFiles.size();
        } catch (Exception var4) {
            throw new RuntimeException( var4 );
        }
    }

    public MultiDataSet convertDataSet(int num) throws Exception {
        int batchNumCount = 0;

        INDArray[] featuresMask = null;
        INDArray[] labelMask = null;

        List<MultiDataSet> multiDataSets = new ArrayList<>();

        while (batchNumCount != num && fileIterator.hasNext()) {
            File image = fileIterator.next();
            String imageName = image.getName().substring(0,image.getName().lastIndexOf('.'));
            String[] imageNames = imageName.split("");
            INDArray feature = asMatrix(image);
            INDArray[] features = new INDArray[]{feature};
            INDArray[] labels = new INDArray[15];

            Nd4j.getAffinityManager().ensureLocation(feature, AffinityManager.Location.DEVICE);
            if (imageName.length() < 15) {
                imageName = imageName + "0";
                imageNames = imageName.split("");
            }
            for (int i = 0; i < imageNames.length; i ++) {
                int digit = Integer.parseInt(imageNames[i]);
                labels[i] = Nd4j.zeros(1, 10).putScalar(new int[]{0, digit}, 1);
            }
            feature =  feature.muli(1.0/255.0);

            multiDataSets.add(new MultiDataSet(features, labels, featuresMask, labelMask));

            batchNumCount ++;
        }
        MultiDataSet result = MultiDataSet.merge(multiDataSets);
        return result;
    }

    public MultiDataSet next(int batchSize) {
        try {
            MultiDataSet result = convertDataSet( batchSize );
            return result;
        } catch (Exception e) {
            log.error("the next function shows error", e);
        }
        return null;
    }

    public MultiDataSet convertDataSetFromByte(InputStream is) throws Exception {

            INDArray feature = asMatrix(is);
            INDArray[] features = new INDArray[]{feature};
            INDArray[] labels = new INDArray[15];
            Nd4j.getAffinityManager().ensureLocation(feature, AffinityManager.Location.DEVICE);

            return new MultiDataSet(features, labels, null, null);
    }

    public void reset() {
        load();
    }
    public int totalExamples() {
        return numExample;
    }
}

