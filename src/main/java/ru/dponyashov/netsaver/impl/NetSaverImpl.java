package ru.dponyashov.netsaver.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.dponyashov.netsaver.NetSaver;
import ru.dponyashov.nnet.NewNet;
import ru.dponyashov.nnet.impl.NewNetImpl;
import ru.dponyashov.nnet.layer.Layer;
import ru.dponyashov.nnet.layer.impl.AbstLayer;
import ru.dponyashov.nnet.layer.impl.InnerLayer;
import ru.dponyashov.nnet.layer.impl.OutLayer;
import ru.dponyashov.nnet.neuron.Neuron;
import ru.dponyashov.nnet.neuron.impl.InnerNeuron;
import ru.dponyashov.nnet.neuron.impl.OutNeuron;
import ru.dponyashov.nnet.util.AppLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class NetSaverImpl implements NetSaver {
    private final String FILE_NAME = "netSource.json";

    @Override
    public void save(NewNet source) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();

            NetForSave saveSource = new NetForSave(source);

            objectMapper.writeValue(new File(FILE_NAME), saveSource);
            AppLogger.setLog(String.format("Записали %s", LocalTime.now()));
        } catch (Exception e){
            AppLogger.setLog(String.format("Ошибка записи %s", e.getMessage()));
        }
    }
    @Override
    public NewNet load() {
        try {
            String jsonString = Files.readString(Paths.get(FILE_NAME));
            NetForSave loadNet = new NetForSave();
            AppLogger.setLog(String.format("Прочитали %s", LocalTime.now()));
            return loadNet.getNet(jsonString);
        } catch (IOException e) {
            AppLogger.setLog(String.format("Ошибка чтения файла %s", e.getMessage()));
        }
        return null;
    }

    static class NetForSave{
        public Layer[] innerLayers;
        public Layer outLayer;

        public NetForSave(){}

        public NetForSave(NewNet source) {
            List<Layer> innerList = new ArrayList<>();
            Layer layer = ((NewNetImpl)source).getInputLayer();
            while(layer != null){
                if(layer.getClass().getSimpleName().equals("OutLayer")){
                    outLayer = layer;
                    break;
                }
                innerList.add(layer);
                layer = ((AbstLayer)layer).getNextLayer();
            }
            innerLayers = innerList.toArray(Layer[]::new);
        }
        public NewNet getNet(String jsonString){
            JSONObject jsonNet = new JSONObject(jsonString);

            List<Layer> innerLayers = new ArrayList<>();

            JSONArray jsonInnerLayers = jsonNet.getJSONArray("innerLayers");
            Layer layer = null;
            for(int l = 0; l < jsonInnerLayers.length(); l++){
                JSONArray jsonNeurons = jsonInnerLayers.getJSONObject(l).getJSONArray("neurons");
                Neuron[] neurons = new Neuron[jsonNeurons.length()];
                for(int n = 0; n < jsonNeurons.length(); n++){
                    JSONArray jsonWeights = jsonNeurons.getJSONObject(n).getJSONArray("weights");
                    double[] weights = new double[jsonWeights.length()];
                    for(int w = 0; w < jsonWeights.length(); w++){
                        double weight = jsonWeights.getDouble(w);
                        weights[w] = weight;
                    }
                    double learnSpeed = jsonNeurons.getJSONObject(n).getDouble("learnSpeed");
                    double shift = jsonNeurons.getJSONObject(n).getDouble("shift");

                    neurons[n] = new InnerNeuron(weights, shift, learnSpeed);
                }
                Layer newLayer = new InnerLayer(neurons);
                newLayer.setPreviousLayer(layer);
                if(layer != null){
                    layer.setNextLayer(newLayer);
                }
                innerLayers.add(newLayer);
                layer = newLayer;
            }

            JSONObject jsonOutLayer = jsonNet.getJSONObject("outLayer");
            JSONArray jsonNeurons = jsonOutLayer.getJSONArray("neurons");
            Neuron[] neurons = new Neuron[jsonNeurons.length()];
            for(int n = 0; n < jsonNeurons.length(); n++){
                JSONArray jsonWeights = jsonNeurons.getJSONObject(n).getJSONArray("weights");
                double[] weights = new double[jsonWeights.length()];
                for(int w = 0; w < jsonWeights.length(); w++){
                    double weight = jsonWeights.getDouble(w);
                    weights[w] = weight;
                }
                double learnSpeed = jsonNeurons.getJSONObject(n).getDouble("learnSpeed");
                double shift = jsonNeurons.getJSONObject(n).getDouble("shift");

                neurons[n] = new OutNeuron(weights, shift, learnSpeed);
            }

            Layer outLayer = new OutLayer(neurons);
            if(!innerLayers.isEmpty()) {
                layer = innerLayers.get(innerLayers.size() - 1);
                outLayer.setPreviousLayer(layer);
                layer.setNextLayer(outLayer);
            }
            AppLogger.setLog(String.format("Преобразовали %s", LocalTime.now()));
            return new NewNetImpl(innerLayers.get(0), outLayer);
        }
    }
}
