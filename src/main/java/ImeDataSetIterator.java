import org.datavec.image.transform.ImageTransform;
import org.nd4j.linalg.dataset.MultiDataSet;
import org.nd4j.linalg.dataset.api.MultiDataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;

/**
 * @author Bibin
 */

public class ImeDataSetIterator implements MultiDataSetIterator {
    private int batchSize = 0;
    private int batchNum = 0;
    private int numExample = 0;
    private ImeLoader load;
    private MultiDataSetPreProcessor preProcessor;

    public ImeDataSetIterator(int batchSize, String dataSetType) {
        this(batchSize, null, dataSetType);
    }
    public ImeDataSetIterator(int batchSize, ImageTransform imageTransform, String dataSetType) {
        this.batchSize = batchSize;
        load = new ImeLoader(imageTransform, dataSetType);
        numExample = load.totalExamples();
    }


    @Override
    public MultiDataSet next(int i) {
        batchNum += i;
        MultiDataSet mds = load.next(i);
        if (preProcessor != null) {
            preProcessor.preProcess(mds);
        }
        return mds;
    }

    @Override
    public void setPreProcessor(MultiDataSetPreProcessor multiDataSetPreProcessor) {
        this.preProcessor = preProcessor;
    }

    @Override
    public MultiDataSetPreProcessor getPreProcessor() {
        return preProcessor;
    }

    @Override
    public boolean resetSupported() {
        return true;
    }

    @Override
    public boolean asyncSupported() {
        return true;
    }

    @Override
    public void reset() {
        batchNum = 0;
        load.reset();
    }

    @Override
    public boolean hasNext() {
        if(batchNum < numExample){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public MultiDataSet next() {
        return next(batchSize);
    }
}

