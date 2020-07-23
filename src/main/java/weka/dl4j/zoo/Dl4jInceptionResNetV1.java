/*
 * WekaDeeplearning4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WekaDeeplearning4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WekaDeeplearning4j.  If not, see <https://www.gnu.org/licenses/>.
 *
 * InceptionResNetV1.java
 * Copyright (C) 2017-2018 University of Waikato, Hamilton, New Zealand
 */

package weka.dl4j.zoo;

import org.deeplearning4j.nn.conf.CacheMode;
import org.deeplearning4j.nn.graph.ComputationGraph;
import weka.dl4j.Preferences;
import weka.dl4j.PretrainedType;

/**
 * A WEKA version of DeepLearning4j's InceptionResNetV1 ZooModel.
 *
 * @author Steven Lang
 * @author Rhys Compton
 */
public class Dl4jInceptionResNetV1 extends AbstractZooModel {

  private static final long serialVersionUID = -9139462134170258014L;

  public Dl4jInceptionResNetV1() {
    setPretrainedType(PretrainedType.NONE);
  }
  @Override
  public ComputationGraph init(int numLabels, long seed, int[] shape, boolean filterMode) {
    org.deeplearning4j.zoo.model.InceptionResNetV1 net = org.deeplearning4j.zoo.model.InceptionResNetV1
        .builder()
        .cacheMode(CacheMode.NONE)
        .workspaceMode(Preferences.WORKSPACE_MODE)
        .inputShape(shape)
        .numClasses(numLabels)
        .build();

    ComputationGraph defaultNet = net.init();

    return attemptToLoadWeights(net, defaultNet, seed, numLabels, filterMode);
  }

  @Override
  public int[][] getShape() {
    return org.deeplearning4j.zoo.model.InceptionResNetV1.builder().build().metaData()
        .getInputShape();
  }

  /**
   * Get the current variation of the zoo model (e.g., Resnet50 or Resnet101)
   *
   * @return Variation
   */
  @Override
  public Enum getVariation() {
    return null;
  }

  /**
   * Does the model require input images to have the ImageNet preprocessing?
   *
   * @return true if the model input should be rescaled
   */
  @Override
  public boolean requiresImageNetScaling() {
    return false;
  }
}
