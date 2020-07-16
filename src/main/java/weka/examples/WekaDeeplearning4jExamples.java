package weka.examples;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.ResNet50;
import org.nd4j.linalg.api.ndarray.INDArray;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Dl4jMlpClassifier;
import weka.core.Instances;
import weka.core.converters.ImageDirectoryLoader;
import weka.dl4j.iterators.instance.ImageInstanceIterator;
import weka.dl4j.playground.*;
import weka.dl4j.zoo.*;
import weka.dl4j.zoo.keras.EfficientNet;
import weka.dl4j.zoo.keras.ResNet;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Dl4jMlpFilter;

import java.io.File;
import java.util.Random;

public class WekaDeeplearning4jExamples {

    public static void main(String[] args) throws Exception {
        playground();
    }

    public static void zooTest() throws Exception {
        ZooModel zooModel = ResNet50.builder().build();
        ComputationGraph computationGraph = (ComputationGraph) zooModel.initPretrained();

        NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
        INDArray image = loader.asMatrix(new File("dog.jpg"));

        INDArray array = computationGraph.outputSingle(image);

        ModelOutputDecoder decoder = new ModelOutputDecoder(new ClassMap(ClassMap.BuiltInClassMap.IMAGENET));

        TopNPredictions[] predictions = decoder.decodePredictions(array);

        for (TopNPredictions p : predictions) {
            System.out.println(p);
        }
    }


        private static void filter() throws Exception {
        String folderPath = "src/test/resources/nominal/plant-seedlings-small";
        ImageDirectoryLoader loader = new ImageDirectoryLoader();
        loader.setInputDirectory(new File(folderPath));
        Instances inst = loader.getDataSet();
        inst.setClassIndex(1);

        Dl4jMlpFilter filter = new Dl4jMlpFilter();

        ImageInstanceIterator iterator = new ImageInstanceIterator();
        iterator.setImagesLocation(new File(folderPath));

        KerasEfficientNet kerasEfficientNet = new KerasEfficientNet();
        kerasEfficientNet.setVariation(EfficientNet.VARIATION.EFFICIENTNET_B1);
        filter.setZooModelType(kerasEfficientNet);

        filter.setInstanceIterator(iterator);
        filter.setInputFormat(inst);

        Instances filteredInstances = Filter.useFilter(inst, filter);
        System.out.println(filteredInstances);
    }

    private static void train() throws Exception {
        String folderPath = "src/test/resources/nominal/plant-seedlings-small";
        ImageDirectoryLoader loader = new ImageDirectoryLoader();
        loader.setInputDirectory(new File(folderPath));
        Instances inst = loader.getDataSet();
        inst.setClassIndex(1);

        Dl4jMlpClassifier classifier = new Dl4jMlpClassifier();
        classifier.setNumEpochs(3);

        KerasEfficientNet kerasEfficientNet = new KerasEfficientNet();
        kerasEfficientNet.setVariation(EfficientNet.VARIATION.EFFICIENTNET_B1);
        classifier.setZooModel(kerasEfficientNet);

        ImageInstanceIterator iterator = new ImageInstanceIterator();
        iterator.setImagesLocation(new File(folderPath));

        classifier.setInstanceIterator(iterator);

        // Stratify and split the data
        Random rand = new Random(0);
        inst.randomize(rand);
        inst.stratify(5);
        Instances train = inst.trainCV(5, 0);
        Instances test = inst.testCV(5, 0);

        // Build the classifier on the training data
        classifier.buildClassifier(train);

        // Evaluate the model on test data
        Evaluation eval = new Evaluation(test);
        eval.evaluateModel(classifier, test);

        // Output some summary statistics
        System.out.println(eval.toSummaryString());
        System.out.println(eval.toMatrixString());
    }

    public static void playground() throws Exception {
        Dl4jImageModelPlayground explorer = new Dl4jImageModelPlayground();

        KerasResNet kerasEfficientNet = new KerasResNet();
        kerasEfficientNet.setVariation(ResNet.VARIATION.RESNET152);
        explorer.setZooModelType(kerasEfficientNet);

        explorer.init();
        explorer.makePrediction(new File("src/test/resources/images/pufferfish.jpg"));
    }
}
